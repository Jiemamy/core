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

import org.apache.commons.lang.Validate;

import org.jiemamy.dialect.Dialect;
import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.model.dataset.DataSetModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.transaction.JiemamyTransaction;
import org.jiemamy.utils.collection.CollectionsUtil;
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
	
	Map<UUID, IdentifiableEntity> map = CollectionsUtil.newHashMap();
	
	Map<Class<? extends JiemamyFacet>, JiemamyFacet> facets = CollectionsUtil.newHashMap();
	
	private String dialectClassName;
	

	/**
	 * インスタンスを生成する。
	 */
	public JiemamyContext() {
	}
	
	public JiemamyContext(FacetProvider... facetProviders) {
		for (FacetProvider facetProvider : facetProviders) {
			facets.put(facetProvider.getFacetType(), facetProvider.getFacet(this));
		}
	}
	
	/**
	 * このコンテキストの {@link JiemamyFacet} にDBオブジェクトを追加する。
	 * 
	 * @param entity {@link IdentifiableEntity}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void add(IdentifiableEntity entity) {
		Validate.notNull(entity);
		
		if (map.containsKey(entity.getId())) {
			remove(entity.toReference());
		}
		
		for (IdentifiableEntity sub : entity.getSubEntities()) {
			if (map.containsKey(sub.getId())) {
				throw new IllegalArgumentException(sub.toString());
			}
		}
		
		IdentifiableEntity e = entity.clone();
		
		map.put(entity.getId(), e);
		for (IdentifiableEntity sub : e.getSubEntities()) {
			map.put(sub.getId(), sub);
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
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
		return databaseObject.findSubDatabaseObjectsNonRecursive(getDatabaseObjects(), this);
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
		Set<DatabaseObjectModel> result = CollectionsUtil.newHashSet(subModels);
		
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
		Collection<DatabaseObjectModel> result = CollectionsUtil.newArrayList();
		result.addAll(superModels);
		
		for (DatabaseObjectModel superModel : superModels) {
			if (databaseObject.equals(superModel) == false) {
				result.addAll(findSuperDatabaseObjectsRecursive(superModel));
			}
		}
		
		return result;
	}
	
	/**
	 * somethingを取得する。 TODO for daisuke
	 * 
	 * @return the databaseObjects
	 */
	public Set<DatabaseObjectModel> getDatabaseObjects() {
		return getEntities(DatabaseObjectModel.class);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public List<? extends DataSetModel> getDataSets() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public String getDialectClassName() {
		return dialectClassName;
	}
	
	public <T extends IdentifiableEntity>Set<T> getEntities(Class<T> clazz) {
		Validate.notNull(clazz);
		Set<T> s = CollectionsUtil.newHashSet();
		for (IdentifiableEntity e : map.values()) {
			if (clazz.isInstance(e)) {
				s.add(clazz.cast(e));
			}
		}
		return s;
	}
	
	/**
	 * このコンテキストの {@link JiemamyFacet} を取得する。
	 * 
	 * @param clazz ファセットの型
	 * @param <T> ファセットの型
	 * @return このコンテキストの {@link JiemamyFacet}
	 */
	public <T extends JiemamyFacet>T getFacet(Class<T> clazz) {
		if (facets == null) {
			throw new IllegalStateException();
		}
		return clazz.cast(facets.get(clazz));
	}
	
	public Set<JiemamyFacet> getFacets() {
		return CollectionsUtil.newHashSet(facets.values());
	}
	
	public JiemamyNamespace[] getNamespaces() {
		List<JiemamyNamespace> namespaces = CollectionsUtil.newArrayList();
		namespaces.addAll(Arrays.asList(CoreNamespace.values()));
		for (JiemamyFacet facet : getFacets()) {
			namespaces.addAll(Arrays.asList(facet.getNamespaces()));
		}
		return namespaces.toArray(new JiemamyNamespace[namespaces.size()]);
	}
	
	public ServiceLocator getServiceLocator() {
		return new DefaultServiceLocator();
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
	
	public Version getVersion() {
		return Version.INSTANCE;
	}
	
	/**
	 * このコンテキストのから{@link IdentifiableEntity}を削除する。
	 * 
	 * @param ref エンティティ参照
	 */
	public void remove(EntityRef<? extends IdentifiableEntity> ref) {
		Validate.notNull(ref);
		IdentifiableEntity removed = map.remove(ref.getReferentId());
		if (removed == null) {
			throw new EntityNotFoundException();
		}
		for (IdentifiableEntity entity : removed.getSubEntities()) {
			remove(entity.toReference());
		}
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
	public <T extends IdentifiableEntity>T resolve(EntityRef<T> ref) {
		return (T) resolve(ref.getReferentId());
	}
	
	/**
	 * エンティティIDから、実体を引き当てる。
	 * 
	 * @param id エンティティID
	 * @return 実体
	 * @throws EntityNotFoundException IDで示すエンティティが見つからなかった場合
	 */
	public IdentifiableEntity resolve(UUID id) {
		IdentifiableEntity resolved = map.get(id);
		if (resolved == null) {
			throw new EntityNotFoundException();
		}
		return resolved;
	}
	
	public void setDialectClassName(String dialectClassName) {
		this.dialectClassName = dialectClassName;
	}
	
	@Override
	public String toString() {
		return ClassUtil.getShortClassName(getClass()) + "@" + Integer.toHexString(hashCode());
	}
	
	int managedEntityCount() {
		return map.size();
	}
	
}
