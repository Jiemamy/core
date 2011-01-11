/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/13
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

import org.jiemamy.model.parameter.ParameterMap;

/**
 * 型記述子のデフォルト実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultTypeVariant implements TypeVariant {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @return 型記述子
	 */
	public static DefaultTypeVariant of(DataTypeCategory category) {
		return new DefaultTypeVariant(category, category.name(), new ParameterMap());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @param params 型パラメータ
	 * @return 型記述子
	 */
	public static DefaultTypeVariant of(DataTypeCategory category, ParameterMap params) {
		return new DefaultTypeVariant(category, category.name(), params);
		
	}
	

	private DataTypeCategory category;
	
	private String typeName;
	
	private ParameterMap params;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @param typeName 型名
	 * @param params 型パラメータ
	 */
	public DefaultTypeVariant(DataTypeCategory category, String typeName, ParameterMap params) {
		Validate.notNull(category);
		Validate.notNull(typeName);
		Validate.notNull(params);
		this.category = category;
		this.typeName = typeName;
		this.params = params.clone();
	}
	
	public DataTypeCategory getCategory() {
		return category;
	}
	
	public <T>T getParam(TypeParameterKey<T> key) {
		return params.get(key);
	}
	
	public ParameterMap getParams() {
		return params.clone();
	}
	
	public String getTypeName() {
		return typeName;
	}
	
}
