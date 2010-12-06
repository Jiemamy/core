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

import java.util.Map;
import java.util.UUID;

import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContext {
	
	Map<UUID, Entity> map = CollectionsUtil.newHashMap();
	
	Map<Class<? extends JiemamyFacet>, JiemamyFacet> facets = CollectionsUtil.newHashMap();
	

	public JiemamyContext() {
		facets.put(JiemamyCore.class, new JiemamyCore(this));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param dbo
	 */
	public void add(DatabaseObjectModel dbo) {
		getCore().add(dbo);
	}
	
	public JiemamyCore getCore() {
		return getFacet(JiemamyCore.class);
	}
	
	public <T extends JiemamyFacet>T getFacet(Class<T> clazz) {
		if (facets == null) {
			throw new IllegalStateException();
		}
		return clazz.cast(facets.get(clazz));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param reference
	 */
	public void remove(EntityRef<?> ref) {
		getCore().remove(ref);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ref
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity>T resolve(EntityRef<T> ref) {
		return (T) resolve(ref.getReferentId());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param id
	 * @return 
	 */
	public Entity resolve(UUID id) {
		Entity resolved = map.get(id);
		if (resolved == null) {
			throw new EntityNotFoundException();
		}
		return resolved;
	}
	
	int managedEntityCount() {
		return map.size();
	}
	
}
