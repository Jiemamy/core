/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.dbo.index;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * インデックスカラムのモデル。
 * 
 * @author daisuke
 */
public final class DefaultIndexColumnModel implements IndexColumnModel {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column インデックス対象カラム
	 * @return インデックスカラム
	 */
	public static DefaultIndexColumnModel of(ColumnModel column) {
		return new DefaultIndexColumnModel(column.toReference());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column インデックス対象カラム
	 * @param sortOrder カラムソート方式
	 * @return インデックスカラム
	 */
	public static DefaultIndexColumnModel of(ColumnModel column, SortOrder sortOrder) {
		return new DefaultIndexColumnModel(column.toReference(), sortOrder);
	}
	

	/** インデックス対象カラム */
	private final EntityRef<? extends ColumnModel> columnRef;
	
	/** カラムソート方式 */
	private final SortOrder sortOrder;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRef インデックス対象カラム
	 * @throws IllegalArgumentException 引数{@code columnRef}に{@code null}を与えた場合
	 */
	public DefaultIndexColumnModel(EntityRef<? extends ColumnModel> columnRef) {
		this(columnRef, null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRef インデックス対象カラム
	 * @param sortOrder カラムソート方式。無指定の場合は{@code null}
	 * @throws IllegalArgumentException 引数{@code columnRef}に{@code null}を与えた場合
	 */
	public DefaultIndexColumnModel(EntityRef<? extends ColumnModel> columnRef, SortOrder sortOrder) {
		Validate.notNull(columnRef);
		this.columnRef = columnRef;
		this.sortOrder = sortOrder;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if ((obj instanceof DefaultIndexColumnModel) == false) {
			return false;
		}
		DefaultIndexColumnModel other = (DefaultIndexColumnModel) obj;
		if (columnRef.equals(other.columnRef) == false) {
			return false;
		}
		if (sortOrder == null) {
			if (other.sortOrder != null) {
				return false;
			}
		} else if (sortOrder.equals(other.sortOrder) == false) {
			return false;
		}
		return true;
	}
	
	public EntityRef<? extends ColumnModel> getColumnRef() {
		assert columnRef != null;
		return columnRef;
	}
	
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnRef.hashCode();
		result = prime * result + ((sortOrder == null) ? 0 : sortOrder.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
