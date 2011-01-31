/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import org.jiemamy.model.parameter.ParameterMap;

/**
 * 型記述子。
 * 
 * @author daisuke
 */
public interface DataType extends Cloneable {
	
	DataType clone();
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	<T>T getParam(TypeParameterKey<T> key);
	
	/**
	 * このモデルが持つ全パラメータを取得する。
	 * 
	 * @return カラムが持つ全パラメータ
	 */
	ParameterMap getParams();
	
	TypeReference getTypeReference();
}
