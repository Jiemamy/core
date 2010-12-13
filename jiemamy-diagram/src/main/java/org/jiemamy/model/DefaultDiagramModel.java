/*
 * Copyright 2010 Jiemamy Project and the others.
 * Created on 2010/12/08
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
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.jiemamy.Entity;
import org.jiemamy.EntityRef;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public class DefaultDiagramModel extends AbstractEntityModel implements DiagramModel {
	
	private String name;
	
	private Level level;
	
	private Mode mode;
	
	private Map<UUID, Entity> entities = Maps.newHashMap();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultDiagramModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultDiagramModel clone() {
		DefaultDiagramModel clone = (DefaultDiagramModel) super.clone();
		clone.entities = Maps.newHashMap();
		for (Entity node : entities.values()) {
			clone.entities.put(node.getId(), node.clone());
		}
		return clone;
	}
	
	public void delete(EntityRef<? extends NodeModel> ref) {
		// TODO Auto-generated method stub
		
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return Lists.newArrayList(entities.values());
	}
	
	public <T2 extends Entity>T2 resolve(EntityRef<T2> ref) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Entity resolve(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void store(NodeModel entity) {
		// TODO Auto-generated method stub
		
	}
	
	public EntityRef<DiagramModel> toReference() {
		return new DefaultEntityRef<DiagramModel>(this);
	}
	
}
