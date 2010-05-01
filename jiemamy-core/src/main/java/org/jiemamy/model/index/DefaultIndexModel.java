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
package org.jiemamy.model.index;

import java.util.List;

import org.apache.commons.collections15.ListUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.model.ValueObject;

/**
 * インデックスモデル。
 * 
 * @author daisuke
 */
public final class DefaultIndexModel implements IndexModel, ValueObject {
	
	/** インデックス名 */
	private final String name;
	
	/** ユニークインデックスか否か */
	private final boolean unique;
	
	/** インデックスカラムのリスト */
	private final List<IndexColumnModel> indexColumns;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param name インデックス名
	 * @param unique ユニークインデックスか否か
	 * @param indexColumns インデックスカラムのリスト
	 */
	public DefaultIndexModel(String name, boolean unique, List<IndexColumnModel> indexColumns) {
		this.name = name;
		this.unique = unique;
		this.indexColumns = ListUtils.unmodifiableList(indexColumns);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DefaultIndexModel)) {
			return false;
		}
		DefaultIndexModel other = (DefaultIndexModel) obj;
		if (indexColumns == null) {
			if (other.indexColumns != null) {
				return false;
			}
		} else if (!indexColumns.equals(other.indexColumns)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (unique != other.unique) {
			return false;
		}
		return true;
	}
	
	public List<IndexColumnModel> getIndexColumns() {
		assert indexColumns != null;
		return indexColumns;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indexColumns == null) ? 0 : indexColumns.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (unique ? 1231 : 1237);
		return result;
	}
	
	public boolean isUnique() {
		return unique;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
