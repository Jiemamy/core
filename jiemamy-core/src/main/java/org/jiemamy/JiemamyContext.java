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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.dialect.Dialect;
import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.utils.collection.CollectionsUtil;
import org.jiemamy.utils.reflect.ClassUtil;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * モデル操作の開始から終了までの、一連の操作文脈を表現するクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContext {
	
	Map<UUID, Entity> map = CollectionsUtil.newHashMap();
	
	Map<Class<? extends JiemamyFacet>, JiemamyFacet> facets = CollectionsUtil.newHashMap();
	
	private String dialectClassName;
	

	/**
	 * インスタンスを生成する。
	 */
	public JiemamyContext() {
		facets.put(CoreFacet.class, new CoreFacet(this));
	}
	
	public JiemamyContext(Class<? extends JiemamyFacet>... facets) {
		this();
		for (Class<? extends JiemamyFacet> facetClass : facets) {
			try {
				Constructor<? extends JiemamyFacet> constructor = facetClass.getConstructor(JiemamyContext.class);
				this.facets.put(facetClass, constructor.newInstance(this));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * このコンテキストの {@link JiemamyFacet} にDBオブジェクトを追加する。
	 * 
	 * @param entity {@link Entity}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void add(Entity entity) {
		Validate.notNull(entity);
		for (Entity sub : entity.getSubEntities()) {
			if (map.containsKey(sub.getId())) {
				throw new IllegalArgumentException();
			}
		}
		
		Entity e = entity.clone();
		
		map.put(entity.getId(), e);
		for (Entity sub : e.getSubEntities()) {
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
	 * このコンテキストの {@link CoreFacet} を取得する。
	 * 
	 * @return このコンテキストの {@link CoreFacet}
	 */
	public CoreFacet getCore() {
		return getFacet(CoreFacet.class);
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
		for (JiemamyFacet facet : getFacets()) {
			namespaces.addAll(Arrays.asList(facet.getNamespaces()));
		}
		return namespaces.toArray(new JiemamyNamespace[namespaces.size()]);
	}
	
	public ServiceLocator getServiceLocator() {
		return new DefaultServiceLocator();
	}
	
	public Version getVersion() {
		return Version.INSTANCE;
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
		return (T) resolve(ref.getReferentId());
	}
	
	/**
	 * エンティティIDから、実体を引き当てる。
	 * 
	 * @param id エンティティID
	 * @return 実体
	 * @throws EntityNotFoundException IDで示すエンティティが見つからなかった場合
	 */
	public Entity resolve(UUID id) {
		Entity resolved = map.get(id);
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
	
	/**
	 * このコンテキストの {@link CoreFacet} からDBオブジェクトを削除する。
	 * 
	 * @param ref エンティティ参照
	 */
	void remove(EntityRef<? extends Entity> ref) {
		Validate.notNull(ref);
		Entity removed = map.remove(ref.getReferentId());
		if (removed == null) {
			throw new EntityNotFoundException();
		}
		for (Entity entity : removed.getSubEntities()) {
			remove(entity.toReference());
		}
	}
	
}
