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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.Entity;
import org.jiemamy.model.EntityEvent;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.EntityListener;
import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;
import org.jiemamy.utils.CollectionsUtil;

/**
 * テーブルモデル。
 * 
 * @author daisuke
 */
public class DefaultTableModel extends AbstractDatabaseObjectModel implements TableModel {
	
	List<EntityListener> listeners = CollectionsUtil.newArrayList();
	
	List<ColumnModel> columns = CollectionsUtil.newArrayList();
	
	/** 属性のリスト */
	List<AttributeModel> attributes = CollectionsUtil.newArrayList();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultTableModel(UUID id) {
		super(id);
	}
	
	/**
	 * テーブルに属性を追加する。
	 * 
	 * @param attribute 属性
	 */
	public void addAttribute(AttributeModel attribute) {
		attributes.add(attribute);
	}
	
	/**
	 * テーブルにカラムを追加する。
	 * 
	 * @param column カラム
	 * @throws EntityLifecycleException {@code column}のライフサイクルがaliveの場合
	 */
	public void addColumn(ColumnModel column) {
		columns.add(column);
		notifyAdded(column);
	}
	
	public void addListener(EntityListener listener) {
		listeners.add(listener);
	}
	
	public List<AttributeModel> getAttributes() {
		assert attributes != null;
		return new ArrayList<AttributeModel>(attributes);
	}
	
	public Collection<? extends Entity> getChildren() {
		assert columns != null;
		return new ArrayList<ColumnModel>(columns);
	}
	
	public ColumnModel getColumn(final String name) {
		assert columns != null;
		ColumnModel c = CollectionUtils.find(columns, new Predicate<ColumnModel>() {
			
			public boolean evaluate(ColumnModel col) {
				return col.getName().equals(name);
			}
		});
		if (c != null) {
			return c;
		}
		throw new ColumnNotFoundException();
	}
	
	public List<ColumnModel> getColumns() {
		assert columns != null;
		return new ArrayList<ColumnModel>(columns);
	}
	
	public Collection<ForeignKeyConstraintModel> getForeignKeyConstraintModels() {
		return filterAttribute(ForeignKeyConstraintModel.class);
	}
	
	public Collection<KeyConstraintModel> getKeyConstraintModels() {
		return filterAttribute(KeyConstraintModel.class);
	}
	
	public EntityRef<TableModel> getReference() {
		if (getId() == null) {
			throw new EntityLifecycleException();
		}
		return new DefaultEntityRef<TableModel>(this);
	}
	
	/**
	 * テーブルから属性を削除する。
	 * 
	 * @param attribute 属性
	 */
	public void removeAttribute(AttributeModel attribute) {
		attributes.remove(attribute);
	}
	
	/**
	 * テーブルからカラムを削除する。
	 * 
	 * @param column カラム
	 * @throws EntityLifecycleException {@code column}のライフサイクルがaliveではない場合
	 */
	public void removeColumn(ColumnModel column) {
		columns.remove(column);
		notifyRemoved(column);
	}
	
	public void removeListener(EntityListener listener) {
		listeners.remove(listener);
	}
	
	private <T extends AttributeModel>Collection<T> filterAttribute(Class<T> clazz) {
		Collection<T> result = new ArrayList<T>();
		for (AttributeModel attribute : attributes) {
			if (clazz.isAssignableFrom(attribute.getClass())) {
				result.add(clazz.cast(attribute));
			}
		}
		return result;
	}
	
	private void notifyAdded(Entity entity) {
		if (entity.isAlive()) {
			throw new EntityLifecycleException();
		}
		for (EntityListener listener : listeners) {
			listener.entityAdded(new EntityEvent(entity));
		}
	}
	
	private void notifyRemoved(Entity entity) {
		if (entity.isAlive() == false) {
			throw new EntityLifecycleException();
		}
		for (EntityListener listener : listeners) {
			listener.entityRemoved(new EntityEvent(entity));
		}
	}
}
