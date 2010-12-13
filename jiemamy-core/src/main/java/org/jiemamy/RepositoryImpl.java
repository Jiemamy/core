/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/13
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.EntityNotFoundException;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
final class RepositoryImpl<T extends Entity> implements Repository<T> {
	
	private Map<UUID, Entity> storage = Maps.newLinkedHashMap();
	

	public void delete(EntityRef<? extends T> ref) {
		Validate.notNull(ref);
		delete0(ref);
	}
	
	public <T2 extends Entity>Set<T2> getEntities(Class<T2> clazz) {
		Set<T2> result = Sets.newHashSet();
		for (Entity entity : storage.values()) {
			if (clazz.isInstance(entity)) {
				result.add(clazz.cast(entity));
			}
		}
		return result;
	}
	
	public <T2 extends Entity>List<T2> getEntitiesAsList(Class<T2> clazz) {
		List<T2> result = Lists.newArrayList();
		for (Entity entity : storage.values()) {
			if (clazz.isInstance(entity)) {
				result.add(clazz.cast(entity));
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T2 extends Entity>T2 resolve(EntityRef<T2> ref) {
		return (T2) resolve(ref.getReferentId());
	}
	
	public Entity resolve(UUID id) {
		Entity found = storage.get(id);
		if (found == null) {
			throw new EntityNotFoundException();
		}
		return found.clone();
	}
	
	public void store(T entity) {
		Validate.notNull(entity);
		
		if (storage.containsKey(entity.getId())) {
			delete0(entity.toReference());
		}
		
		for (Entity sub : entity.getSubEntities()) {
			if (storage.containsKey(sub.getId())) {
				throw new IllegalArgumentException(sub.toString());
			}
		}
		
		Entity e = entity.clone();
		
		storage.put(entity.getId(), e);
		for (Entity sub : e.getSubEntities()) {
			storage.put(sub.getId(), sub);
		}
	}
	
	void delete0(EntityRef<? extends Entity> ref) {
		Entity removed = storage.remove(ref.getReferentId());
		if (removed == null) {
			throw new EntityNotFoundException();
		}
		for (Entity entity : removed.getSubEntities()) {
			delete0(entity.toReference());
		}
	}
	
	int managedEntityCount() {
		return storage.size();
	}
	
}
