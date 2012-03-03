/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.ValueObject;
import org.jiemamy.model.column.JmColumn;

/**
 * インデックスカラムのモデル。
 * 
 * @author daisuke
 */
public final class JmIndexColumn implements ValueObject {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column インデックス対象カラム
	 * @return インデックスカラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static JmIndexColumn of(JmColumn column) {
		Validate.notNull(column);
		return new JmIndexColumn(column.toReference());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column インデックス対象カラム
	 * @param sortOrder カラムソート方式
	 * @return インデックスカラム
	 * @throws IllegalArgumentException 引数{@code column}に{@code null}を与えた場合
	 */
	public static JmIndexColumn of(JmColumn column, SortOrder sortOrder) {
		Validate.notNull(sortOrder);
		return new JmIndexColumn(column.toReference(), sortOrder);
	}
	
	
	/** インデックス対象カラム */
	private final EntityRef<? extends JmColumn> columnRef;
	
	/** カラムソート方式 */
	private final SortOrder sortOrder;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRef インデックス対象カラム
	 * @throws IllegalArgumentException 引数{@code columnRef}に{@code null}を与えた場合
	 */
	public JmIndexColumn(EntityRef<? extends JmColumn> columnRef) {
		this(columnRef, null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRef インデックス対象カラム
	 * @param sortOrder カラムソート方式。無指定の場合は{@code null}
	 * @throws IllegalArgumentException 引数{@code columnRef}に{@code null}を与えた場合
	 */
	public JmIndexColumn(EntityRef<? extends JmColumn> columnRef, SortOrder sortOrder) {
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
		if ((obj instanceof JmIndexColumn) == false) {
			return false;
		}
		JmIndexColumn other = (JmIndexColumn) obj;
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
	
	/**
	 * インデックス対象カラムを取得する。
	 * 
	 * @return インデックス対象カラム
	 * @since 0.3
	 */
	public EntityRef<? extends JmColumn> getColumnRef() {
		assert columnRef != null;
		return columnRef;
	}
	
	/**
	 * カラムソート方式を取得する。
	 * 
	 * @return カラムソート方式. 未設定の場合は{@code null}
	 * @since 0.3
	 */
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
