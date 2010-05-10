/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.model.dbo;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jiemamy.EntityEvent;
import org.jiemamy.EntityListener;
import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.Entity;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.index.IndexModel;
import org.jiemamy.utils.CollectionsUtil;

/**
 * テーブルモデル。
 * 
 * @author daisuke
 */
public class DefaultTableModel extends AbstractDatabaseObjectModel implements TableModel {
	
	private List<EntityListener> listeners = CollectionsUtil.newArrayList();
	
	private List<ColumnModel> columns = CollectionsUtil.newArrayList();
	
	/** 属性のリスト */
	private List<AttributeModel> attributes = CollectionsUtil.newArrayList();
	
	/** インデックスのリスト */
	private List<IndexModel> indexes = CollectionsUtil.newArrayList();
	

	DefaultTableModel(UUID id) {
		super(id);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param attribute
	 */
	public void addAttributes(AttributeModel attribute) {
		attributes.add(attribute);
	}
	
	public void addColumn(ColumnModel column) {
		notifyAdded(column);
		columns.add(column);
	}
	
	public void addListener(EntityListener listener) {
		listeners.add(listener);
	}
	
	public List<AttributeModel> getAttributes() {
		assert attributes != null;
		return attributes;
	}
	
	public Collection<? extends Entity> getChildren() {
		return columns;
	}
	
	public List<ColumnModel> getColumns() {
		return columns;
	}
	
	public List<IndexModel> getIndexes() {
		assert indexes != null;
		return indexes;
	}
	
	public EntityRef<TableModel> getReference() {
		if (getId() == null) {
			throw new EntityLifecycleException();
		}
		return new DefaultEntityRef<TableModel>(this);
	}
	
	public void notifyAdded(Entity entity) {
		for (EntityListener listener : listeners) {
			listener.entityAdded(new EntityEvent(entity));
		}
	}
	
	public void notifyRemoved(Entity entity) {
		for (EntityListener listener : listeners) {
			listener.entityRemoved(new EntityEvent(entity));
		}
	}
	
	public void removeColumn(ColumnModel column) {
		columns.remove(column);
		notifyRemoved(column);
	}
	
	public void removeListener(EntityListener listener) {
		listeners.remove(listener);
	}
}
