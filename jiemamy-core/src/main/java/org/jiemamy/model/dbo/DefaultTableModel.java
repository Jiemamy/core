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

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.index.IndexModel;

/**
 * テーブルモデル。
 * 
 * @author daisuke
 */
public class DefaultTableModel extends AbstractDatabaseObjectModel implements TableModel {
	
	private final UUID id;
	
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
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DefaultTableModel other = (DefaultTableModel) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	public List<AttributeModel> getAttributes() {
		assert attributes != null;
		return attributes;
	}
	
	public UUID getId() {
		return id;
	}
	
	public List<IndexModel> getIndexes() {
		assert indexes != null;
		return indexes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
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
