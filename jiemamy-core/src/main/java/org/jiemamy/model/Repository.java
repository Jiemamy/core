/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/10
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
package org.jiemamy.model;

import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.EntityEvent;
import org.jiemamy.EntityListener;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.utils.CollectionsUtil;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class Repository implements EntityListener {
	
	final InternalKey key = new InternalKey();
	
	private Collection<Entity> entities = CollectionsUtil.newArrayList();
	

	public void add(DatabaseObjectModel dbo) {
		add(dbo, UUID.randomUUID());
	}
	
	public void entityAdded(EntityEvent e) {
		Entity source = (Entity) e.getSource();
		add(source, UUID.randomUUID());
	}
	
	public void entityRemoved(EntityEvent e) {
		Entity source = (Entity) e.getSource();
		remove(source);
	}
	
	public <T extends Entity>T get(EntityRef<T> ref) {
		return (T) get(ref.getReferenceId());
	}
	
	public Entity get(UUID id) {
		for (Entity dbo : entities) {
			if (dbo.getId().equals(id)) {
				return dbo;
			}
		}
		throw new EntityNotFoundException();
	}
	
	void add(Entity entity, UUID id) {
		if (entity.getId() != null) {
			throw new EntityLifecycleException();
		}
		
		entity.setId(id, key);
		if (entity instanceof CompositEntity) {
			CompositEntity compositEntity = (CompositEntity) entity;
			for (Entity child : compositEntity.getChildren()) {
				add(child, UUID.randomUUID());
			}
			compositEntity.addListener(this);
		}
		entities.add(entity);
	}
	
	void remove(Entity entity) {
		Validate.notNull(entity.getId());
		
		boolean removed = entities.remove(entity);
		if (removed == false) {
			throw new IllegalArgumentException();
		}
		
		entity.setId(null, key);
		if (entity instanceof CompositEntity) {
			CompositEntity compositEntity = (CompositEntity) entity;
			compositEntity.removeListener(this);
			for (Entity child : compositEntity.getChildren()) {
				remove(child);
			}
		}
	}
	

	public static class InternalKey {
		
		InternalKey() {
		}
		
	}
	
}
