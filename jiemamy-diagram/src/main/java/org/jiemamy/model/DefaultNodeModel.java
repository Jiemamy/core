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
import java.util.Collections;
import java.util.UUID;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmRectangle;

/**
 * {@link NodeModel}のデフォルト実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public abstract class DefaultNodeModel extends AbstractEntity implements NodeModel {
	
	private JmRectangle boundary;
	
	private JmColor color;
	
	OnMemoryRepository<ConnectionModel> sourceConnections = new OnMemoryRepository<ConnectionModel>();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultNodeModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultNodeModel clone() {
		DefaultNodeModel clone = (DefaultNodeModel) super.clone();
		clone.sourceConnections = sourceConnections.clone();
		return clone;
	}
	
	/**
	 * {@link ConnectionModel}を削除する。
	 * 
	 * @param reference 削除する{@link ConnectionModel}への参照
	 */
	public void delete(EntityRef<? extends ConnectionModel> reference) {
		sourceConnections.delete(reference);
	}
	
	public JmRectangle getBoundary() {
		return boundary;
	}
	
	public JmColor getColor() {
		return color;
	}
	
	public Collection<? extends ConnectionModel> getSourceConnections() {
		return sourceConnections.getEntitiesAsSet();
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return getSourceConnections();
	}
	
	public Collection<? extends ConnectionModel> getTargetConnections() {
		return Collections.emptyList(); // FIXME ロジックで算出しなければならない
	}
	
	/**
	 * ノードの位置を設定する。
	 * 
	 * @param boundary 位置
	 */
	public void setBoundary(JmRectangle boundary) {
		this.boundary = boundary;
	}
	
	/**
	 * ノードの色を設定する。
	 * 
	 * @param color 色
	 */
	public void setColor(JmColor color) {
		this.color = color;
	}
	
	/**
	 * 
	 * 
	 * @param connection source connection
	 */
	public void store(ConnectionModel connection) {
		sourceConnections.store(connection);
	}
	
	public EntityRef<? extends DefaultNodeModel> toReference() {
		return new DefaultEntityRef<DefaultNodeModel>(this);
	}
}
