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
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.Entity;
import org.jiemamy.EntityNotFoundException;
import org.jiemamy.EntityRef;
import org.jiemamy.Repository;
import org.jiemamy.RepositoryImpl;
import org.jiemamy.model.dbo.DatabaseObjectModel;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public class DefaultDiagramModel extends AbstractEntity implements DiagramModel {
	
	private String name;
	
	private Level level;
	
	private Mode mode;
	
	private Repository<NodeModel> nodes = new RepositoryImpl<NodeModel>();
	

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
		clone.nodes = nodes.clone();
		return clone;
	}
	
	public void delete(EntityRef<? extends NodeModel> ref) {
		nodes.delete(ref);
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
	
	public NodeModel getNodeFor(EntityRef<? extends DatabaseObjectModel> ref) {
		Validate.notNull(ref);
		for (NodeModel node : nodes.getEntities(NodeModel.class)) {
			if (ref.equals(node.getCoreModelRef())) {
				return node;
			}
		}
		throw new EntityNotFoundException("ref=" + ref);
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return nodes.getEntities(NodeModel.class);
	}
	
	public <T2 extends Entity>T2 resolve(EntityRef<T2> ref) {
		return nodes.resolve(ref);
	}
	
	public Entity resolve(UUID id) {
		return nodes.resolve(id);
	}
	
	/**
	 * 表示レベルを設定する。
	 * 
	 * @param level 表示レベル
	 */
	public void setLevel(Level level) {
		this.level = level;
	}
	
	/**
	 * 表示モードを設定する。
	 * 
	 * @param mode 表示モード
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	/**
	 * ダイアグラム名を設定する。
	 * 
	 * @param name ダイアグラム名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public void store(NodeModel entity) {
		nodes.store(entity);
	}
	
	public EntityRef<DiagramModel> toReference() {
		return new DefaultEntityRef<DiagramModel>(this);
	}
	
}
