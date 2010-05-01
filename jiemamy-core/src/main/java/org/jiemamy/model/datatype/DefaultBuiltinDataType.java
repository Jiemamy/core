/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/12/10
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
package org.jiemamy.model.datatype;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * {@link BuiltinDataType}の実装クラス。
 * 
 * @author daisuke
 */
public class DefaultBuiltinDataType implements BuiltinDataType {
	
	private DataTypeCategory category;
	
	private String typeName;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @param typeName 型名
	 */
	public DefaultBuiltinDataType(DataTypeCategory category, String typeName) {
		Validate.notNull(category);
		this.category = category;
		this.typeName = typeName;
	}
	
	public DataTypeCategory getCategory() {
		return category;
	}
	
	public String getTypeName() {
		if (typeName == null) {
			return category.toString();
		}
		return typeName;
	}
	
	/**
	 * 
	 * 型カテゴリーを設定する
	 * 
	 * @param category 型カテゴリー
	 * @since 0.3
	 */
	public void setCategory(DataTypeCategory category) {
		this.category = category;
	}
	
	/**
	 * 
	 * データ型名を設定する
	 * 
	 * @param typeName データ型名
	 * @since 0.3
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@Override
	public String toString() {
		ReflectionToStringBuilder toStringBuilder =
				new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		toStringBuilder.setExcludeFieldNames(new String[] {
			"jiemamy"
		});
		
		return toStringBuilder.toString();
	}
	
}
