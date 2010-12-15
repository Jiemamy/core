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

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;

import org.jiemamy.utils.EntityUtil;

/**
 * TODO for daisuke
 * 
 * @param <T> 管理するエンティティの型
 * @version $Id$
 * @author daisuke
 */
public class OnMemoryRepository<T extends Entity> implements Repository<T> {
	
	private Set<T> mainStorage = Sets.newLinkedHashSet();
	
	private Map<UUID, Entity> subStorage = Maps.newHashMap();
	

	@Override
	public OnMemoryRepository<T> clone() {
		try {
			@SuppressWarnings("unchecked")
			OnMemoryRepository<T> clone = (OnMemoryRepository<T>) super.clone();
			clone.mainStorage = EntityUtil.cloneEntityLinkedHashSet(mainStorage);
			clone.subStorage = EntityUtil.cloneEntityMap(subStorage);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error("clone not supported", e);
		}
	}
	
	public T delete(EntityRef<? extends T> ref) {
		Validate.notNull(ref);
		T deleted = deleteMain(ref);
		deleteSub(deleted);
		return deleted;
	}
	
	public Set<T> getEntitiesAsSet() {
		return EntityUtil.cloneEntityHashSet(mainStorage);
	}
	
	public List<T> getEntitiesAsList() {
		return EntityUtil.cloneEntityList(mainStorage);
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Entity>E resolve(EntityRef<E> ref) {
		return (E) resolve(ref.getReferentId());
	}
	
	public Entity resolve(final UUID id) {
		Iterable<Entity> all = Iterables.concat(mainStorage, subStorage.values());
		try {
			return Iterables.find(all, new Predicate<Entity>() {
				
				public boolean apply(Entity input) {
					return input.getId().equals(id);
				}
			}).clone();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("id=" + id);
		}
	}
	
	public void store(T entity) {
		Validate.notNull(entity);
		
		try {
			Entity old = resolve(entity.toReference());
			// create copy for check
			Map<UUID, Entity> copy = Maps.newHashMap(subStorage);
			for (Entity sub : old.getSubEntities()) {
				copy.remove(sub.getId());
			}
			// check
			for (Entity sub : entity.getSubEntities()) {
				if (copy.containsKey(sub.getId())) {
					// 取り除いても衝突するならば、取り除かずに例外
					// FORMAT-OFF
					throw new IllegalArgumentException(MessageFormat.format(
							"SubEntity({0})@MainEntity({1}) is collision",
							sub.getId(), entity.getId()));
					// FORMAT-ON
				}
			}
			// delete
			@SuppressWarnings("unchecked")
			EntityRef<? extends T> oldReference = (EntityRef<? extends T>) old.toReference();
			delete(oldReference);
		} catch (EntityNotFoundException e) {
			// ignore
		}
		
		for (Entity sub : entity.getSubEntities()) {
			if (subStorage.containsKey(sub.getId())) {
				// 取り除いても衝突するならば、取り除かずに例外
				// FORMAT-OFF
			throw new IllegalArgumentException(MessageFormat.format(
					"SubEntity({0})@MainEntity({1}) is collision",
					sub.getId(), entity.getId()));
			// FORMAT-ON
			}
		}
		
		@SuppressWarnings("unchecked")
		T clone = (T) entity.clone();
		
		mainStorage.add(clone);
		for (Entity sub : clone.getSubEntities()) {
			subStorage.put(sub.getId(), sub);
		}
	}
	
	T deleteMain(EntityRef<? extends T> ref) {
		Iterator<T> iterator = mainStorage.iterator();
		T removed = null;
		while (iterator.hasNext()) {
			T next = iterator.next();
			if (ref.isReferenceOf(next)) {
				iterator.remove();
				removed = next;
				break;
			}
		}
		if (removed == null) {
			throw new EntityNotFoundException("ref=" + ref);
		}
		return removed;
	}
	
	<E extends Entity>void deleteSub(T main) {
		for (Entity entity : main.getSubEntities()) {
			subStorage.remove(entity.getId());
		}
	}
	
	int managedEntityCount() {
		return mainStorage.size();
	}
}
