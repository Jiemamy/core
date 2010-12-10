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

import java.util.List;
import java.util.UUID;

import org.jiemamy.EntityRef;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmPoint;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public class DefaultConnectionModel extends AbstractIdentifiableEntity implements ConnectionModel {
	
	private List<JmPoint> bendpoints;
	
	private JmColor color;
	
	private EntityRef<NodeModel> source;
	
	private EntityRef<NodeModel> target;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultConnectionModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultConnectionModel clone() {
		DefaultConnectionModel clone = (DefaultConnectionModel) super.clone();
		clone.bendpoints = CollectionsUtil.newArrayList(bendpoints);
		return clone;
	}
	
	public List<JmPoint> getBendpoints() {
		return bendpoints;
	}
	
	public JmColor getColor() {
		return color;
	}
	
	public EntityRef<? extends ForeignKeyConstraintModel> getCoreModelRef() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public EntityRef<NodeModel> getSource() {
		return source;
	}
	
	public EntityRef<NodeModel> getTarget() {
		return target;
	}
	
	public boolean isSelfConnection() {
		return getSource().equals(getTarget());
	}
	
	public void setColor(JmColor color) {
		this.color = color;
	}
	
	public void setSource(EntityRef<NodeModel> source) {
		this.source = source;
	}
	
	public void setTarget(EntityRef<NodeModel> target) {
		this.target = target;
	}
	
	public EntityRef<ConnectionModel> toReference() {
		return new DefaultEntityRef<ConnectionModel>(this);
	}
}