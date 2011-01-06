/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/08/29
 *
 * This file is part of Jiemamy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jiemamy;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.namespace.NamespaceContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.dataset.DataSetModel;
import org.jiemamy.model.index.IndexModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.serializer.JiemamySerializer;
import org.jiemamy.serializer.stax2.JiemamyStaxSerializer;
import org.jiemamy.transaction.Command;
import org.jiemamy.transaction.EventBroker;
import org.jiemamy.transaction.EventBrokerImpl;
import org.jiemamy.transaction.JiemamyTransaction;
import org.jiemamy.utils.MutationMonitor;
import org.jiemamy.utils.reflect.ClassUtil;
import org.jiemamy.xml.CoreNamespace;
import org.jiemamy.xml.JiemamyNamespace;
import org.jiemamy.xml.JiemamyNamespaceContext;

/**
 * モデル操作の開始から終了までの、一連の操作文脈を表現するクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContext {
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyContext.class);
	
	private static boolean debug = getVersion().isSnapshot();
	

	/**
	 * {@link JiemamySerializer}を取得する。
	 * 
	 * @return {@link JiemamySerializer}
	 */
	public static JiemamySerializer findSerializer() {
		// THINK シリアライザの差し替えできるように
		try {
			return getServiceLocator().getService(JiemamySerializer.class, JiemamyStaxSerializer.class.getName());
		} catch (ClassNotFoundException e) {
			throw new JiemamyError("", e);
		}
	}
	
	/**
	 * {@link ServiceLocator}を取得する。
	 * 
	 * @return {@link ServiceLocator}
	 */
	public static ServiceLocator getServiceLocator() {
		return new DefaultServiceLocator(); // TODO 差し替えできるように
	}
	
	/**
	 * Jiemamyのバージョンを返す。
	 * 
	 * @return Jiemamyのバージョン
	 */
	public static Version getVersion() {
		return Version.INSTANCE;
	}
	
	/**
	 * デバッグモードであるかどうか調べる。
	 * 
	 * @return デバッグモードである場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isDebug() {
		return debug;
	}
	
	/**
	 * デバッグモードを設定する。
	 * 
	 * @param debug デバッグモードにする場合は{@code true}、そうでない場合は{@code false}
	 */
	protected static void setDebug(boolean debug) {
		JiemamyContext.debug = debug;
	}
	

	private Map<Class<? extends JiemamyFacet>, JiemamyFacet> facets = Maps.newHashMap();
	
	private OnMemoryRepository<DatabaseObjectModel> doms = new OnMemoryRepository<DatabaseObjectModel>();
	
	private OnMemoryRepository<DataSetModel> dsms = new OnMemoryRepository<DataSetModel>();
	
	private String dialectClassName;
	
	private String description;
	
	private String schemaName;
	
//	private final UUIDProvider uuidProvider = new UUIDProvider();
	
	private EventBroker eventBroker = new EventBrokerImpl();
	

	/**
	 * インスタンスを生成する。
	 */
	public JiemamyContext() {
		this(new FacetProvider[0]);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param facetProviders このコンテキストで利用するファセットの{@link FacetProvider}
	 */
	public JiemamyContext(FacetProvider... facetProviders) {
		for (FacetProvider facetProvider : facetProviders) {
			facets.put(facetProvider.getFacetType(), facetProvider.getFacet(this));
		}
		logger.debug("new context created (debug={})", isDebug());
	}
	
	/**
	 * {@link DatabaseObjectModel}を削除する。
	 * 
	 * @param reference 削除する{@link DatabaseObjectModel}への参照
	 * TODO {@link #deleteDataSet(EntityRef)}と対象となるよう、リネーム
	 */
	public void delete(final EntityRef<? extends DatabaseObjectModel> reference) {
		doms.delete(reference);
		eventBroker.fireCommandProcessed(new Command() {
			
			public void execute() {
				
			}
			
			public Command getNegateCommand() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public EntityRef<? extends DatabaseObjectModel> getTarget() {
				return reference;
			}
		});
	}
	
	public void deleteDataSet(EntityRef<? extends DataSetModel> reference) {
		dsms.delete(reference);
	}
	
	/**
	 * このコンテキストが保持するSQL方言IDからSQL方言のインスタンスを探す。
	 * 
	 * @return SQL方言
	 * @throws ClassNotFoundException SQL方言が見つからなかった場合
	 */
	public Dialect findDialect() throws ClassNotFoundException {
		return getServiceLocator().getService(Dialect.class, dialectClassName);
	}
	
	/**
	 * 直接の依存モデルの集合を返す。
	 * 
	 * @param databaseObject 対象{@link DatabaseObjectModel}
	 * @return 直接の依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSubDatabaseObjectsNonRecursive(DatabaseObjectModel databaseObject) {
		Validate.notNull(databaseObject);
		return databaseObject.findSubDatabaseObjectsNonRecursive(this);
	}
	
	/**
	 * 全ての依存モデルの集合を返す。
	 * 
	 * @param standard 基準モデル
	 * @return 全ての依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSubDatabaseObjectsRecursive(DatabaseObjectModel standard) {
		Validate.notNull(standard);
		Collection<DatabaseObjectModel> subModels = findSubDatabaseObjectsNonRecursive(standard);
		Set<DatabaseObjectModel> result = Sets.newHashSet(subModels);
		
		for (DatabaseObjectModel subModel : subModels) {
			if (standard.equals(subModel) == false) {
				result.addAll(findSubDatabaseObjectsRecursive(subModel));
			}
		}
		
		return result;
	}
	
	/**
	 * 参照先親モデルを返す。
	 * 
	 * @param databaseObject 対象{@link DatabaseObjectModel}
	 * @return 親モデルのSet
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSuperDatabaseObjectsNonRecursive(DatabaseObjectModel databaseObject) {
		Validate.notNull(databaseObject);
		return databaseObject.findSuperDatabaseObjectsNonRecursive(getDatabaseObjects());
	}
	
	/**
	 * 全ての参照先 祖先モデルを返す。
	 * 
	 * @param databaseObject 対象エンティティ
	 * @return 祖先モデルのSet
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSuperDatabaseObjectsRecursive(DatabaseObjectModel databaseObject) {
		Validate.notNull(databaseObject);
		Collection<DatabaseObjectModel> superModels = findSuperDatabaseObjectsNonRecursive(databaseObject);
		Collection<DatabaseObjectModel> result = Lists.newArrayList();
		result.addAll(superModels);
		
		for (DatabaseObjectModel superModel : superModels) {
			if (databaseObject.equals(superModel) == false) {
				result.addAll(findSuperDatabaseObjectsRecursive(superModel));
			}
		}
		
		return result;
	}
	
	/**
	 * {@link DatabaseObjectModel} の{@link List}を取得する。
	 * 
	 * @return データセットの{@link List}
	 */
	public Set<DatabaseObjectModel> getDatabaseObjects() {
		return doms.getEntitiesAsSet();
	}
	
	/**
	 * {@link DataSetModel} の{@link List}を取得する。
	 * 
	 * @return データセットの{@link List}
	 */
	public List<DataSetModel> getDataSets() {
		return dsms.getEntitiesAsList();
	}
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * SQL方言IDを取得する。
	 * 
	 * @return SQL方言ID
	 */
	public String getDialectClassName() {
		return dialectClassName;
	}
	
	/**
	 * {@link EventBroker}を取得する。
	 * 
	 * @return {@link EventBroker}
	 */
	public EventBroker getEventBroker() {
		return eventBroker;
	}
	
	/**
	 * このコンテキストの {@link JiemamyFacet} を取得する。
	 * 
	 * @param clazz ファセットの型
	 * @param <T> ファセットの型
	 * @return このコンテキストの {@link JiemamyFacet}
	 */
	public <T extends JiemamyFacet>T getFacet(Class<T> clazz) {
		if (hasFacet(clazz) == false) {
			throw new IllegalStateException();
		}
		return clazz.cast(facets.get(clazz));
	}
	
	/**
	 * ファセットの{@link Set}を取得する。
	 * 
	 * @return ファセットの{@link Set}
	 */
	public Set<JiemamyFacet> getFacets() {
		return MutationMonitor.monitor(Sets.newHashSet(facets.values()));
	}
	
	public Set<IndexModel> getIndexes() {
		return getDatabaseObjects(IndexModel.class);
	}
	
	public NamespaceContext getNamespaceContext() {
		return new JiemamyNamespaceContext(getNamespaces());
	}
	
	/**
	 * 利用する全ての名前空間を取得する。
	 * 
	 * @return 利用する全ての名前空間
	 */
	public JiemamyNamespace[] getNamespaces() {
		List<JiemamyNamespace> namespaces = Lists.newArrayList();
		namespaces.addAll(Arrays.asList(CoreNamespace.values()));
		for (JiemamyFacet facet : getFacets()) {
			namespaces.addAll(Arrays.asList(facet.getNamespaces()));
		}
		return namespaces.toArray(new JiemamyNamespace[namespaces.size()]);
	}
	
	/**
	 * スキーマ名を取得する。
	 * 
	 * @return スキーマ名
	 */
	public String getSchemaName() {
		return schemaName;
	}
	
	/**
	 * このコンテキストが管理する {@link TableModel} の中から、指定したテーブル名を持つものを返す。
	 * 
	 * @param name テーブル名
	 * @return 見つかった  {@link TableModel}
	 * @throws TableNotFoundException 指定したテーブル名を持つ {@link TableModel} が見つからなかった場合
	 */
	public TableModel getTable(String name) {
		for (TableModel table : getTables()) {
			if (name.equals(table.getName())) {
				return table;
			}
		}
		throw new TableNotFoundException("name=" + name);
	}
	
	/**
	 * このコンテキストが管理する全ての {@link TableModel} を取得する。
	 * 
	 * @return {@link TableModel}のセット
	 */
	public Set<TableModel> getTables() {
		return getDatabaseObjects(TableModel.class);
	}
	
	/**
	 * 新しいファサードインスタンスを生成し、取得する。
	 * 
	 * @param <T> 取得するファサードの型
	 * @param clazz 取得するファサードの型
	 * @return 新しいファサード
	 * @since 0.2
	 */
	public <T extends JiemamyTransaction>T getTransaction(Class<T> clazz) {
		return null; // FIXME
	}
	
	/**
	 * 指定したファセットを持つかどうか調べる。
	 * 
	 * @param <T> ファセットの型
	 * @param clazz ファセットの型
	 * @return ファセットを持つ場合は{@code true}、そうでない場合は{@code false}
	 */
	public <T extends JiemamyFacet>boolean hasFacet(Class<T> clazz) {
		return facets.get(clazz) != null;
	}
	
	/**
	 * エンティティ参照から、実体を引き当てる。
	 * 
	 * @param <T> エンティティの型
	 * @param ref エンティティ参照
	 * @return 実体
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 */
	public <T extends Entity>T resolve(EntityRef<T> ref) {
		return doms.resolve(ref);
	}
	
	/**
	 * コンテキストが管理する {@link Entity} の中から、指定したIDを持つ実体（{@link Entity}）を探して返す。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子エンティティも含む。</p>
	 * 
	 * @param id ENTITY ID
	 * @return 見つかった{@link Entity}
	 * @throws EntityNotFoundException このコンテキストが指定した実体を管理していない場合
	 * @since 1.0.0
	 */
	public Entity resolve(UUID id) {
		return doms.resolve(id);
	}
	
	/**
	 * 説明文を設定する。
	 * 
	 * @param description 説明文 
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * SQL方言IDを設定する。
	 * 
	 * @param dialectClassName SQL方言ID
	 */
	public void setDialectClassName(String dialectClassName) {
		this.dialectClassName = dialectClassName;
	}
	
	/**
	 * スキーマ名を設定する。
	 * 
	 * @param schemaName スキーマ名
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	/**
	 * {@link DatabaseObjectModel}を保存する。
	 * 
	 * @param dom {@link DatabaseObjectModel}
	 */
	public void store(final DatabaseObjectModel dom) {
		doms.store(dom);
		eventBroker.fireCommandProcessed(new Command() {
			
			public void execute() {
				
			}
			
			public Command getNegateCommand() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public EntityRef<? extends DatabaseObjectModel> getTarget() {
				return dom.toReference();
			}
		});
	}
	
	/**
	 * {@link DataSetModel}を保存する。
	 * 
	 * @param dsm {@link DataSetModel}
	 */
	public void store(final DataSetModel dsm) {
		dsms.store(dsm);
		eventBroker.fireCommandProcessed(new Command() {
			
			public void execute() {
				
			}
			
			public Command getNegateCommand() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public EntityRef<? extends DataSetModel> getTarget() {
				return dsm.toReference();
			}
		});
	}
	
	@Override
	public String toString() {
		return ClassUtil.getShortClassName(getClass()) + "@" + Integer.toHexString(hashCode());
	}
	
	<T extends DatabaseObjectModel>Set<T> getDatabaseObjects(Class<T> clazz) {
		Set<T> result = Sets.newHashSet();
		for (DatabaseObjectModel dom : doms.getEntitiesAsSet()) {
			if (clazz.isInstance(dom)) {
				result.add(clazz.cast(dom));
			}
		}
		return result;
	}
}
