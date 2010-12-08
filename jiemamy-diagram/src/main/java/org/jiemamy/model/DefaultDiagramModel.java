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

import org.jiemamy.Entity;
import org.jiemamy.EntityRef;
import org.jiemamy.utils.collection.CollectionsUtil;

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
	
	private Collection<NodeModel> nodes = CollectionsUtil.newArrayList();
	

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
		return (DefaultDiagramModel) super.clone();
	}
	
	public Collection<? extends ConnectionModel> getConnections() {
		Collection<ConnectionModel> result = CollectionsUtil.newArrayList();
		for (NodeModel node : getNodes()) {
			result.addAll(node.getSourceConnections());
		}
		return result;
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
	
	public Collection<? extends NodeModel> getNodes() {
		return CollectionsUtil.newArrayList(nodes);
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return CollectionsUtil.newArrayList(nodes);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param table
	 * @param node
	 * @since TODO
	 */
	public void putNode(NodeModel node) {
		nodes.add(node);
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
	
	public EntityRef<DiagramModel> toReference() {
		return new DefaultEntityRef<DiagramModel>(this);
	}
}
