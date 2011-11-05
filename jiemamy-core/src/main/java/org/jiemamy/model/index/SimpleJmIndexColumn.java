/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.column.JmColumn;

/**
 * インデックスカラムのモデル。
 * 
 * @author daisuke
 */
public final class SimpleJmIndexColumn implements JmIndexColumn {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column インデックス対象カラム
	 * @return インデックスカラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static SimpleJmIndexColumn of(JmColumn column) {
		Validate.notNull(column);
		return new SimpleJmIndexColumn(column.toReference());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column インデックス対象カラム
	 * @param sortOrder カラムソート方式
	 * @return インデックスカラム
	 * @throws IllegalArgumentException 引数{@code column}に{@code null}を与えた場合
	 */
	public static SimpleJmIndexColumn of(JmColumn column, SortOrder sortOrder) {
		Validate.notNull(sortOrder);
		return new SimpleJmIndexColumn(column.toReference(), sortOrder);
	}
	

	/** インデックス対象カラム */
	private final UUIDEntityRef<? extends JmColumn> columnRef;
	
	/** カラムソート方式 */
	private final SortOrder sortOrder;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRef インデックス対象カラム
	 * @throws IllegalArgumentException 引数{@code columnRef}に{@code null}を与えた場合
	 */
	public SimpleJmIndexColumn(UUIDEntityRef<? extends JmColumn> columnRef) {
		this(columnRef, null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRef インデックス対象カラム
	 * @param sortOrder カラムソート方式。無指定の場合は{@code null}
	 * @throws IllegalArgumentException 引数{@code columnRef}に{@code null}を与えた場合
	 */
	public SimpleJmIndexColumn(UUIDEntityRef<? extends JmColumn> columnRef, SortOrder sortOrder) {
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
		if ((obj instanceof SimpleJmIndexColumn) == false) {
			return false;
		}
		SimpleJmIndexColumn other = (SimpleJmIndexColumn) obj;
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
	
	public UUIDEntityRef<? extends JmColumn> getColumnRef() {
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
