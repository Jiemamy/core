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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.model.ValueObject;
import org.jiemamy.model.attribute.ColumnRef;

/**
 * インデックスカラムのモデル。
 * 
 * @author daisuke
 */
public final class DefaultIndexColumnModel implements IndexColumnModel, ValueObject {
	
	/** インデックス対象カラム */
	private final ColumnRef columnRef;
	
	/** カラムソート方式 */
	private final SortOrder sortOrder;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRef インデックス対象カラム
	 * @param sortOrder カラムソート方式
	 */
	public DefaultIndexColumnModel(ColumnRef columnRef, SortOrder sortOrder) {
		this.columnRef = columnRef;
		this.sortOrder = sortOrder;
	}
	
	public ColumnRef getColumnRef() {
		return columnRef;
	}
	
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
