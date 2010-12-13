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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;

import org.jiemamy.dialect.Dialect;
import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.model.dataset.DataSetModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.transaction.JiemamyTransaction;
import org.jiemamy.utils.reflect.ClassUtil;
import org.jiemamy.xml.CoreNamespace;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * モデル操作の開始から終了までの、一連の操作文脈を表現するクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContext {
	
	private Map<Class<? extends JiemamyFacet>, JiemamyFacet> facets = Maps.newHashMap();
	
	private Repository<DatabaseObjectModel> doms = new RepositoryImpl<DatabaseObjectModel>();
	
	private Repository<DataSetModel> dsms = new RepositoryImpl<DataSetModel>();
	
	private String dialectClassName;
	
	private String description;
	
	private String schemaName;
	

	/**
	 * インスタンスを生成する。
	 */
	public JiemamyContext() {
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
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param reference
	 */
	public void delete(EntityRef<? extends DatabaseObjectModel> reference) {
		doms.delete(reference);
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
		return databaseObject.findSuperDatabaseObjectsNonRecursive(getEntities(DatabaseObjectModel.class));
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
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public List<? extends DataSetModel> getDataSets() {
		return dsms.getEntitiesAsList(DataSetModel.class);
	}
	
	public String getDescription() {
		return description;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public String getDialectClassName() {
		return dialectClassName;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param class1
	 * @return
	 */
	public <T extends Entity>Set<T> getEntities(Class<T> clazz) {
		return doms.getEntities(clazz);
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
	
	public Set<JiemamyFacet> getFacets() {
		return Sets.newHashSet(facets.values());
	}
	
	public JiemamyNamespace[] getNamespaces() {
		List<JiemamyNamespace> namespaces = Lists.newArrayList();
		namespaces.addAll(Arrays.asList(CoreNamespace.values()));
		for (JiemamyFacet facet : getFacets()) {
			namespaces.addAll(Arrays.asList(facet.getNamespaces()));
		}
		return namespaces.toArray(new JiemamyNamespace[namespaces.size()]);
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	
	public ServiceLocator getServiceLocator() {
		return new DefaultServiceLocator(); // TODO
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
		return null;
	}
	
	/**
	 * Jiemamyのバージョンを返す。
	 * 
	 * @return Jiemamyのバージョン
	 */
	public Version getVersion() {
		return Version.INSTANCE;
	}
	
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
	@SuppressWarnings("unchecked")
	public <T extends Entity>T resolve(EntityRef<T> ref) {
		return (T) doms.resolve(ref).clone();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param id
	 * @return 
	 */
	public Entity resolve(UUID id) {
		return doms.resolve(id);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param description 
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
	 * TODO for daisuke
	 * 
	 * @param schemaName
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	public void store(DatabaseObjectModel dom) {
		doms.store(dom);
	}
	
	public void store(DataSetModel dsm) {
		dsms.store(dsm);
	}
	
	@Override
	public String toString() {
		return ClassUtil.getShortClassName(getClass()) + "@" + Integer.toHexString(hashCode());
	}
}
