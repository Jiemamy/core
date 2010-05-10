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
package org.jiemamy;

import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.CompositEntity;
import org.jiemamy.model.Entity;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.model.EntityRef;
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
		entityAdded(new EntityEvent(dbo));
	}
	
	public void entityAdded(EntityEvent e) {
		Entity source = (Entity) e.getSource();
		add(source);
	}
	
	public void entityRemoved(EntityEvent e) {
		Entity source = (Entity) e.getSource();
		remove(source);
	}
	
	public <T extends Entity>T get(EntityRef<T> ref) {
		@SuppressWarnings("unchecked")
		T result = (T) get(ref.getReferenceId());
		return result;
	}
	
	public Entity get(UUID id) {
		for (Entity dbo : entities) {
			if (dbo.getId().equals(id)) {
				return dbo;
			}
		}
		throw new EntityNotFoundException();
	}
	
	public void remove(DatabaseObjectModel dbo) {
		entityRemoved(new EntityEvent(dbo));
	}
	
	void add(Entity entity) {
		if (entity.isAlive()) {
			throw new EntityLifecycleException();
		}
		
		entity.initiate(key);
		if (entity instanceof CompositEntity) {
			CompositEntity compositEntity = (CompositEntity) entity;
			for (Entity child : compositEntity.getChildren()) {
				add(child);
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
		
		entity.kill(key);
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
