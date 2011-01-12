/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import org.jiemamy.dddbase.ValueObject;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * 型記述子。
 * 
 * @author daisuke
 */
public interface TypeVariant extends ValueObject {
	
	/**
	 * 型カテゴリを取得する。
	 * 
	 * @return 型カテゴリ
	 */
	@Deprecated
	DataTypeCategory getCategory();
	
	/**
	 * 型パラメータを取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return 型パラメータ
	 */
	<T>T getParam(TypeParameterKey<T> key);
	
	/**
	 * 全パラメータを取得する。
	 * 
	 * @return 全パラメータ
	 */
	ParameterMap getParams();
	
	/**
	 * 型名の文字列を取得する。
	 * 
	 * @return 型名の文字列
	 */
	@Deprecated
	String getTypeName();
	
	TypeReference getTypeReference();
}
