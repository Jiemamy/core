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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.model.attribute.ColumnModel;

/**
 * インデックスモデル。
 * 
 * @author daisuke
 */
public final class DefaultIndexModel implements IndexModel {
	
	public static DefaultIndexModel of(ColumnModel... column) {
		List<IndexColumnModel> indexColumns = new ArrayList<IndexColumnModel>();
		for (ColumnModel element : column) {
			indexColumns.add(DefaultIndexColumnModel.of(element));
			
		}
		return new DefaultIndexModel(indexColumns);
	}
	

	/** インデックス名 */
	private final String name;
	
	/** ユニークインデックスか否か */
	private final boolean unique;
	
	/** インデックスカラムのリスト */
	private final List<IndexColumnModel> indexColumns;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param indexColumn インデックスカラムのリスト
	 * @throws IllegalArgumentException 引数{@code indexColumn}が{@code null}の場合
	 */
	public DefaultIndexModel(IndexColumnModel indexColumn) {
		this(null, false, Arrays.asList(indexColumn));
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param indexColumns インデックスカラムのリスト
	 * @throws IllegalArgumentException 引数{@code indexColumns}が{@code null}または空の場合
	 * @throws IllegalArgumentException 引数{@code indexColumns}の要素に{@code null}を含む場合
	 */
	public DefaultIndexModel(List<IndexColumnModel> indexColumns) {
		this(null, false, indexColumns);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name インデックス名
	 * @param unique ユニークインデックスか否か
	 * @param indexColumns インデックスカラムのリスト
	 * @throws IllegalArgumentException 引数{@code indexColumns}が{@code null}または空の場合
	 * @throws IllegalArgumentException 引数{@code indexColumns}の要素に{@code null}を含む場合
	 */
	public DefaultIndexModel(String name, boolean unique, List<IndexColumnModel> indexColumns) {
		Validate.notEmpty(indexColumns);
		Validate.noNullElements(indexColumns);
		this.name = name;
		this.unique = unique;
		this.indexColumns = new ArrayList<IndexColumnModel>(indexColumns);
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
		return new ArrayList<IndexColumnModel>(indexColumns);
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
		result = prime * result + (unique ? 1231 : 1237); // CHECKSTYLE IGNORE THIS LINE
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
