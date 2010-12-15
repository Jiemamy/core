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
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;

import org.jiemamy.utils.MutationMonitor;

/**
 * TODO for daisuke
 * 
 * @param <T> 管理するエンティティの型
 * @version $Id$
 * @author daisuke
 */
public final class RepositoryImpl<T extends Entity> implements Repository<T> {
	
	private Map<UUID, Entity> storage = Maps.newLinkedHashMap();
	

	@Override
	@SuppressWarnings("unchecked")
	public RepositoryImpl<T> clone() {
		try {
			RepositoryImpl<T> clone = (RepositoryImpl<T>) super.clone();
			clone.storage = Maps.newLinkedHashMap();
			for (Entry<UUID, Entity> e : storage.entrySet()) {
				clone.storage.put(e.getKey(), e.getValue().clone());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new JiemamyError("clone not supported", e);
		}
	}
	
	public T delete(EntityRef<? extends T> ref) {
		Validate.notNull(ref);
		return delete0(ref);
	}
	
	public <E extends Entity>Set<E> getEntities(Class<E> clazz) {
		Set<E> result = Sets.newHashSet();
		for (Entity entity : storage.values()) {
			if (clazz.isInstance(entity)) {
				result.add(clazz.cast(entity.clone()));
			}
		}
		return MutationMonitor.monitor(result);
	}
	
	public <E extends Entity>List<E> getEntitiesAsList(Class<E> clazz) {
		List<E> result = Lists.newArrayList();
		for (Entity entity : storage.values()) {
			if (clazz.isInstance(entity)) {
				result.add(clazz.cast(entity.clone()));
			}
		}
		return MutationMonitor.monitor(result);
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Entity>E resolve(EntityRef<E> ref) {
		return (E) resolve(ref.getReferentId());
	}
	
	public Entity resolve(UUID id) {
		Entity found = storage.get(id);
		if (found == null) {
			throw new EntityNotFoundException("id=" + id);
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
	
	<E extends Entity>E delete0(EntityRef<E> ref) {
		@SuppressWarnings("unchecked")
		E removed = (E) storage.remove(ref.getReferentId());
		if (removed == null) {
			throw new EntityNotFoundException("ref=" + ref);
		}
		for (Entity entity : removed.getSubEntities()) {
			delete0(entity.toReference());
		}
		return removed;
	}
	
	int managedEntityCount() {
		return storage.size();
	}
	
}
