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

import org.jiemamy.EntityRef;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public class DefaultNodeModel extends AbstractIdentifiableEntity implements NodeModel {
	
	private Collection<ConnectionModel> sourceConnections = CollectionsUtil.newArrayList();
	
	private Collection<ConnectionModel> targetConnections = CollectionsUtil.newArrayList();
	
	private JmRectangle boundary;
	
	private JmColor color;
	
	private final EntityRef<? extends DatabaseObjectModel> coreModelRef;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultNodeModel(UUID id, EntityRef<? extends DatabaseObjectModel> coreModelRef) {
		super(id);
		this.coreModelRef = coreModelRef;
	}
	
	public Collection<ConnectionModel> breachEncapslationOfSourceConnections() {
		return sourceConnections;
	}
	
	public Collection<ConnectionModel> breachEncapslationOfTargetConnections() {
		return targetConnections;
	}
	
	@Override
	public DefaultNodeModel clone() {
		DefaultNodeModel clone = (DefaultNodeModel) super.clone();
		clone.sourceConnections = CollectionsUtil.newArrayList();
		for (ConnectionModel connection : sourceConnections) {
			clone.sourceConnections.add(connection.clone());
		}
		clone.targetConnections = CollectionsUtil.newArrayList();
		for (ConnectionModel connection : targetConnections) {
			clone.targetConnections.add(connection.clone());
		}
		return clone;
	}
	
	public JmRectangle getBoundary() {
		return boundary;
	}
	
	public JmColor getColor() {
		return color;
	}
	
	public EntityRef<? extends DatabaseObjectModel> getCoreModelRef() {
		return coreModelRef;
	}
	
	public Collection<? extends ConnectionModel> getSourceConnections() {
		return CollectionsUtil.newArrayList(sourceConnections);
	}
	
	public Collection<? extends ConnectionModel> getTargetConnections() {
		return CollectionsUtil.newArrayList(targetConnections);
	}
	
	public void setBoundary(JmRectangle boundary) {
		this.boundary = boundary;
	}
	
	public void setColor(JmColor color) {
		this.color = color;
	}
	
	public EntityRef<DefaultNodeModel> toReference() {
		return new DefaultEntityRef<DefaultNodeModel>(this);
	}
	
}