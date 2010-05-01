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
package org.jiemamy.model.entity;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.exception.ElementNotFoundException;
import org.jiemamy.exception.TooManyElementsException;
import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ForeignKey;
import org.jiemamy.model.attribute.constraint.PrimaryKey;
import org.jiemamy.model.index.IndexModel;

/**
 * テーブルモデル。
 * 
 * @author daisuke
 */
public class DefaultTableModel extends AbstractEntityModel implements TableModel {
	
	/** 属性のリスト */
	private List<AttributeModel> attributes;
	
	/** インデックスのリスト */
	private List<IndexModel> indexes;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultTableModel(UUID id) {
		super(id);
	}
	
	public <T extends AttributeModel>T findAttribute(Class<T> clazz) {
		return findAttribute(clazz, false);
	}
	
	public <T extends AttributeModel>T findAttribute(Class<T> clazz, boolean columnAttr) {
		List<T> attributes = findAttributes(clazz, columnAttr);
		if (attributes.size() == 0) {
			throw new ElementNotFoundException(this, clazz);
		} else if (attributes.size() > 1) {
			throw new TooManyElementsException(this, clazz, attributes);
		}
		return attributes.get(0);
	}
	
	public <T extends AttributeModel>List<T> findAttributes(Class<T> clazz) {
		return findAttributes(clazz, false);
	}
	
	public <T extends AttributeModel>List<T> findAttributes(Class<T> clazz, boolean columnAttr) {
		/*
		Validate.notNull(clazz);
		
		List<T> result = CollectionsUtil.newArrayList();
		for (AttributeModel attribute : getAttributes()) {
			if (clazz.isAssignableFrom(attribute.getClass())) {
				result.add(clazz.cast(attribute));
			}
			if (columnAttr && attribute instanceof ColumnModel) {
				ColumnModel columnModel = (ColumnModel) attribute;
				UniqueKey uniqueKey = columnModel.getUniqueKey();
				if (uniqueKey != null && clazz.isAssignableFrom(uniqueKey.getClass())) {
					result.add(clazz.cast(uniqueKey));
				}
				PrimaryKey primaryKey = columnModel.getPrimaryKey();
				if (primaryKey != null && clazz.isAssignableFrom(primaryKey.getClass())) {
					result.add(clazz.cast(primaryKey));
				}
				// column chek と not null は attributeじゃないので処理しない
			}
		}
		return result;
		*/
		return null;
	}
	
	public ColumnModel findColumn(String columnName) {
		/*
		Validate.notNull(columnName);
		
		List<ColumnModel> columns = CollectionsUtil.newArrayList();
		for (ColumnModel columnModel : findAttributes(ColumnModel.class)) {
			if (columnName.equals(columnModel.getName())) {
				columns.add(columnModel);
			}
		}
		if (columns.size() == 0) {
			throw new ElementNotFoundException(this, columnName);
		} else if (columns.size() > 1) {
			throw new TooManyElementsException(this, columnName, columns);
		}
		return columns.get(0);
		*/
		return null;
	}
	
	public List<ColumnModel> findColumns() {
		return findAttributes(ColumnModel.class);
	}
	
	public List<ForeignKey> findForeignKeys() {
		return findAttributes(ForeignKey.class);
	}
	
	public PrimaryKey findPrimaryKey() {
		return findAttribute(PrimaryKey.class, true);
	}
	
	public List<AttributeModel> getAttributes() {
		assert attributes != null;
		return attributes;
	}
	
	public List<IndexModel> getIndexes() {
		assert indexes != null;
		return indexes;
	}
	
	/**
	 * 属性のリストを設定する。
	 * 
	 * @param attributes 属性のリスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setAttributes(List<AttributeModel> attributes) {
		Validate.notNull(attributes);
		this.attributes = attributes;
	}
	
	/**
	 * インデックスのリストを設定する。
	 * 
	 * @param indexes インデックスのリスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setIndexes(List<IndexModel> indexes) {
		Validate.notNull(indexes);
		this.indexes = indexes;
	}
}
