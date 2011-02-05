/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.events.Namespace;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.EntityResolver;
import org.jiemamy.dddbase.OnMemoryCompositeEntityResolver;
import org.jiemamy.dddbase.OnMemoryEntityResolver;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.dddbase.OrderedOnMemoryRepository;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.dataset.JmDataSet;
import org.jiemamy.model.domain.JmDomain;
import org.jiemamy.model.index.JmIndex;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.TableNotFoundException;
import org.jiemamy.model.table.TooManyTablesFoundException;
import org.jiemamy.model.view.JmView;
import org.jiemamy.serializer.JiemamySerializer;
import org.jiemamy.serializer.stax2.JiemamyStaxSerializer;
import org.jiemamy.transaction.EventBroker;
import org.jiemamy.transaction.EventBrokerImpl;
import org.jiemamy.transaction.JiemamyTransaction;
import org.jiemamy.transaction.StoredEvent;
import org.jiemamy.utils.LogMarker;
import org.jiemamy.utils.UUIDProvider;
import org.jiemamy.utils.reflect.ClassUtil;
import org.jiemamy.xml.CoreNamespace;
import org.jiemamy.xml.JiemamyNamespace;
import org.jiemamy.xml.JiemamyNamespaceContext;

/**
 * モデル操作の開始から終了までの、一連の操作文脈を表現するクラス。
 * 
 * @version $Id$
 * @since 0.3
 * @author daisuke
 */
public/*final*/class JiemamyContext implements EntityResolver {
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyContext.class);
	
	private static boolean debug = getVersion().isSnapshot();
	
	private static ServiceLocator serviceLocator = new SimpleServiceLocator();
	
	/** 利用する{@link JiemamySerializer}実装クラスのFQCN */
	private static String serializerName = JiemamyStaxSerializer.class.getName();
	
	static {
		MutationMonitor.setDebug(debug);
	}
	

	/**
	 * {@link JiemamySerializer}を取得する。
	 * 
	 * @return {@link JiemamySerializer}
	 * @throws ServiceNotFoundException シリアライザが見つからなかった場合
	 */
	public static JiemamySerializer findSerializer() {
		try {
			return getServiceLocator().getService(JiemamySerializer.class, serializerName);
		} catch (ClassNotFoundException e) {
			throw new ServiceNotFoundException("", e);
		}
	}
	
	/**
	 * {@link ServiceLocator}を取得する。
	 * 
	 * @return {@link ServiceLocator}
	 */
	public static ServiceLocator getServiceLocator() {
		return serviceLocator;
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
	 * 利用する{@link JiemamySerializer}実装クラスのFQCNを指定する。
	 * 
	 * @param serializerName 利用する{@link JiemamySerializer}実装クラスのFQCN
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void setSerializerName(String serializerName) {
		Validate.notNull(serializerName);
		JiemamyContext.serializerName = serializerName;
	}
	
	/**
	 * 利用する{@link ServiceLocator}を指定する。
	 * 
	 * @param serviceLocator 利用する{@link ServiceLocator}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void setServiceLocator(ServiceLocator serviceLocator) {
		Validate.notNull(serviceLocator);
		JiemamyContext.serviceLocator = serviceLocator;
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
	
	private OnMemoryRepository<DbObject> dbObjects = new OnMemoryRepository<DbObject>();
	
	private OrderedOnMemoryRepository<JmDataSet> dataSets = new OrderedOnMemoryRepository<JmDataSet>();
	
	private JmMetadata metadata = new SimpleJmMetadata();
	
	private final UUIDProvider uuidProvider = new UUIDProvider();
	
	private final EventBroker eventBroker = new EventBrokerImpl();
	

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
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JiemamyContext(FacetProvider... facetProviders) {
		Validate.noNullElements(facetProviders);
		for (FacetProvider facetProvider : facetProviders) {
			facets.put(facetProvider.getFacetType(), facetProvider.getFacet(this));
		}
		logger.debug(LogMarker.LIFECYCLE, "new context created (debug={})", isDebug());
	}
	
	public boolean contains(EntityRef<?> reference) {
		Validate.notNull(reference);
		return contains(reference.getReferentId());
	}
	
	public boolean contains(UUID id) {
		return getCompositeResolver().contains(id);
	}
	
	/**
	 * {@link JmDataSet}を削除する。
	 * 
	 * @param reference 削除する{@link JmDataSet}への参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException このコンテキストが指定したデータセットを管理していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmDataSet deleteDataSet(EntityRef<? extends JmDataSet> reference) {
		Validate.notNull(reference);
		JmDataSet deleted = dataSets.delete(reference);
		logger.info(LogMarker.LIFECYCLE, "dataset deleted: " + deleted);
		eventBroker.fireEvent(new StoredEvent<JmDataSet>(dataSets, deleted, null));
		return deleted;
	}
	
	/**
	 * {@link DbObject}を削除する。
	 * 
	 * @param reference 削除する{@link DbObject}への参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException このコンテキストが指定したデータベースオブジェクトを管理していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DbObject deleteDbObject(EntityRef<? extends DbObject> reference) {
		Validate.notNull(reference);
		DbObject deleted = dbObjects.delete(reference);
		logger.info(LogMarker.LIFECYCLE, "dbObject deleted: " + deleted);
		eventBroker.fireEvent(new StoredEvent<DbObject>(dbObjects, deleted, null));
		return deleted;
	}
	
	/**
	 * このコンテキストが保持するSQL方言IDからSQL方言のインスタンスを探す。
	 * 
	 * @return SQL方言
	 * @throws ClassNotFoundException SQL方言が見つからなかった場合
	 * @throws IllegalStateException {@code metadata}またはそこに設定したSQL方言が{@code null}の場合
	 */
	public Dialect findDialect() throws ClassNotFoundException {
		if (metadata == null || metadata.getDialectClassName() == null) {
			throw new IllegalStateException();
		}
		return getServiceLocator().getService(Dialect.class, metadata.getDialectClassName());
	}
	
	/**
	 * {@code dbObject}<b>が</b>直接依存する{@link DbObject}の集合を返す。
	 * 
	 * @param dbObject 対象{@link DbObject}
	 * @return 直接の依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Set<DbObject> findSubDbObjectsNonRecursive(DbObject dbObject) {
		Validate.notNull(dbObject);
		return dbObject.findSubDbObjectsNonRecursive(this);
	}
	
	/**
	 * {@code dbObject}<b>が</b>依存する全ての{@link DbObject}の集合を返す。
	 * 
	 * @param dbObject 対象{@link DbObject}
	 * @return 全ての依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Set<DbObject> findSubDbObjectsRecursive(DbObject dbObject) {
		Validate.notNull(dbObject);
		Collection<DbObject> subDbObjects = findSubDbObjectsNonRecursive(dbObject);
		Set<DbObject> result = Sets.newHashSet(subDbObjects);
		
		for (DbObject subDbObject : subDbObjects) {
			if (dbObject.equals(subDbObject) == false) {
				result.addAll(findSubDbObjectsRecursive(subDbObject));
			}
		}
		
		return result;
	}
	
	/**
	 * {@code dbObject}<b>に</b>直接依存する{@link DbObject}の集合を返す。
	 * 
	 * @param dbOjbect 対象{@link DbObject}
	 * @return 直接の非依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Set<DbObject> findSuperDbObjectsNonRecursive(DbObject dbOjbect) {
		Validate.notNull(dbOjbect);
		return dbOjbect.findSuperDbObjectsNonRecursive(getDbObjects());
	}
	
	/**
	 * {@code dbObject}<b>に</b>依存する全ての{@link DbObject}の集合を返す。
	 * 
	 * @param dbOjbect 対象{@link DbObject}
	 * @return 全ての非依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Set<DbObject> findSuperDbObjectsRecursive(DbObject dbOjbect) {
		Validate.notNull(dbOjbect);
		return findSuperDbObjectsRecursive(dbOjbect, dbOjbect, new HashSet<DbObject>());
	}
	
	/**
	 * このコンテキストが管理する全ての{@link JmDataSet}の{@link List}を取得する。
	 * 
	 * @return {@link JmDataSet} の{@link List}
	 */
	public List<JmDataSet> getDataSets() {
		return dataSets.getEntitiesAsList();
	}
	
	/**
	 * このコンテキストが管理する全ての{@link DbObject}の{@link Set}を取得する。
	 * 
	 * @return {@link DbObject}の{@link Set}
	 */
	public Set<DbObject> getDbObjects() {
		return dbObjects.getEntitiesAsSet();
	}
	
	/**
	 * このコンテキストが管理する全ての{@link DbObject}のうち、{@code clazz}型を持つものを取得する。
	 * 
	 * @param <T> フィルターする型
	 * @param clazz フィルターする型
	 * @return {@link DbObject}の{@link Set}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T extends DbObject>Set<T> getDbObjects(Class<T> clazz) {
		Validate.notNull(clazz);
		Set<T> result = Sets.newHashSet(Iterables.filter(dbObjects.getEntitiesAsSet(), clazz));
		return result;
	}
	
	/**
	 * このコンテキストが管理する全ての{@link JmDomain} を取得する。
	 * 
	 * <p>Note: Convenience method; equivalent to
	 * {@code getDbObjects(JmDomain.class);}.</p>
	 * 
	 * @return {@link JmDomain}のセット
	 */
	public Set<JmDomain> getDomains() {
		return getDbObjects(JmDomain.class);
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
	 * このコンテキストの{@link JiemamyFacet}を取得する。
	 * 
	 * @param clazz ファセットの型
	 * @param <T> ファセットの型
	 * @return このコンテキストの {@link JiemamyFacet}
	 * @throws IllegalStateException コンテキストが指定したFacetを持っていない場合
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
	
	/**
	 * このコンテキストが管理する全ての{@link JmIndex}を取得する。
	 * 
	 * <p>Note: Convenience method; equivalent to
	 * {@code getDbObjects(JmIndex.class);}.</p>
	 * 
	 * @return {@link JmTable}のセット
	 */
	public Set<JmIndex> getIndexes() {
		return getDbObjects(JmIndex.class);
	}
	
	/**
	 * このコンテキストのメタデータを取得する。
	 * 
	 * @return メタデータ
	 */
	public JmMetadata getMetadata() {
		return metadata.clone();
	}
	
	/**
	 * このコンテキストが利用する {@link Namespace} を表現する {@link NamespaceContext} を取得する。
	 * 
	 * @return {@link NamespaceContext}
	 */
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
	 * このコンテキストが管理する{@link JmTable}の中から、指定したテーブル名を持つものを返す。
	 * 
	 * @param name テーブル名
	 * @return 見つかった{@link JmTable}
	 * @throws TableNotFoundException 指定したテーブル名を持つ{@link JmTable}が見つからなかった場合
	 * @throws TooManyTablesFoundException 指定したテーブル名を持つ{@link JmTable}が複数見つかった場合
	 */
	public JmTable getTable(final String name) {
		Collection<JmTable> c = Collections2.filter(getTables(), new Predicate<JmTable>() {
			
			public boolean apply(JmTable input) {
				return ObjectUtils.equals(input.getName(), name);
			}
		});
		try {
			return Iterables.getOnlyElement(c);
		} catch (NoSuchElementException e) {
			throw new TableNotFoundException("name=" + name);
		} catch (IllegalArgumentException e) {
			throw new TooManyTablesFoundException(c);
		}
	}
	
	/**
	 * このコンテキストが管理する全ての{@link JmTable}を取得する。
	 * 
	 * <p>Note: Convenience method; equivalent to
	 * {@code getDbObjects(JmTable.class);}.</p>
	 * 
	 * @return {@link JmTable}のセット
	 */
	public Set<JmTable> getTables() {
		return getDbObjects(JmTable.class);
	}
	
	/**
	 * 新しいファサードインスタンスを生成し、取得する。
	 * 
	 * @return 新しいファサード
	 * @since 0.2
	 */
	@Experimental
	public JiemamyTransaction getTransaction() {
		return new JiemamyTransaction(this);
	}
	
	/**
	 * このコンテキストが管理する全ての{@link JmView}を取得する。
	 * 
	 * <p>Note: Convenience method; equivalent to
	 * {@code getDbObjects(JmView.class);}.</p>
	 * 
	 * @return {@link JmView}のセット
	 */
	public Set<JmView> getViews() {
		return getDbObjects(JmView.class);
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
	 * {@link EntityRef}から、{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子{@link Entity}も含む。</p>
	 * 
	 * @param <T> {@link Entity}の型
	 * @param reference {@link EntityRef}
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T extends Entity>T resolve(EntityRef<T> reference) {
		Validate.notNull(reference);
		return getCompositeResolver().resolve(reference);
	}
	
	/**
	 * 指定したIDを持つ{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子{@link Entity}も含む。</p>
	 * 
	 * @param id ENTITY ID
	 * @return 見つかった{@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Entity resolve(UUID id) {
		Validate.notNull(id);
		return getCompositeResolver().resolve(id);
	}
	
	/**
	 * このコンテキストのメタデータを設定する。
	 * 
	 * @param metadata メタデータ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setMetadata(JmMetadata metadata) {
		Validate.notNull(metadata);
		this.metadata = metadata.clone();
	}
	
	/**
	 * {@link DbObject}を保存する。
	 * 
	 * @param dbObject 保存したい{@link DbObject}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(DbObject dbObject) {
		Validate.notNull(dbObject);
//		Validate.notNull(dbObject.getName());
		DbObject old = dbObjects.store(dbObject);
		if (old == null) {
			logger.info(LogMarker.LIFECYCLE, "dbObject stored: " + dbObject);
		} else {
			logger.info(LogMarker.LIFECYCLE, "dbObject updated: (old) " + old);
			logger.info(LogMarker.LIFECYCLE, "                         (new) " + dbObject);
		}
		eventBroker.fireEvent(new StoredEvent<DbObject>(dbObjects, old, dbObject));
	}
	
	/**
	 * {@link JmDataSet}を保存する。
	 * 
	 * @param dataSet 保存したい{@link JmDataSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(JmDataSet dataSet) {
		Validate.notNull(dataSet);
		JmDataSet old = dataSets.store(dataSet);
		if (old == null) {
			logger.info(LogMarker.LIFECYCLE, "dataset stored: " + dataSet);
		} else {
			logger.info(LogMarker.LIFECYCLE, "dataset updated: (old) " + old);
			logger.info(LogMarker.LIFECYCLE, "                 (new) " + dataSet);
		}
		eventBroker.fireEvent(new StoredEvent<JmDataSet>(dataSets, old, dataSet));
	}
	
	/**
	 * 指定した2つの位置にあるデータセットの順序を入れ替える。
	 * 
	 * @param index1 the index of the {@link JmDataSet} to be swapped.
	 * @param index2 the index of the other {@link JmDataSet} to be swapped.
	 * @throws IndexOutOfBoundsException if either {@code index1} or {@code index2}
	 *         is out of range (index1 &lt; 0 || index1 &gt;= list.size()
	 *         || index2 &lt; 0 || index2 &gt;= list.size()).
	 */
	public void swapDataSet(int index1, int index2) {
		dataSets.swap(index1, index2);
		eventBroker.fireEvent(new StoredEvent<JmDataSet>(dataSets, null, null));
	}
	
	@Override
	public String toString() {
		return ClassUtil.getShortClassName(getClass()) + "@" + Integer.toHexString(hashCode());
	}
	
	/**
	 * このcontext用の{@link UUIDProvider}を利用して、文字列を{@link UUID}に変換する。
	 * 
	 * @param name 文字列
	 * @return {@link UUID}
	 * @see UUIDProvider
	 */
	public UUID toUUID(String name) {
		return uuidProvider.valueOfOrRandom(name);
	}
	
	private Set<DbObject> findSuperDbObjectsRecursive(DbObject start, DbObject target, Set<DbObject> collector) {
		Collection<DbObject> superDbObjects = findSuperDbObjectsNonRecursive(target);
		collector.addAll(superDbObjects);
		
		for (DbObject superDbObject : superDbObjects) {
			if (superDbObject.equals(target) == false && superDbObject.equals(start) == false) {
				findSuperDbObjectsRecursive(start, superDbObject, collector);
			}
		}
		return collector;
	}
	
	private OnMemoryCompositeEntityResolver getCompositeResolver() {
		Collection<OnMemoryEntityResolver<?>> c = Lists.newArrayList();
		c.add(dbObjects);
		c.add(dataSets);
		for (JiemamyFacet facet : facets.values()) {
			c.add(facet.getResolver());
		}
		return new OnMemoryCompositeEntityResolver(c.toArray(new OnMemoryEntityResolver[c.size()]));
	}
}
