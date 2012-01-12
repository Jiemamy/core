/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2008/09/17
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
package org.jiemamy.model.view;

import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * リレーショナルデータベースにおける「ビュー」を表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public interface JmView extends DbObject {
	
	JmView clone();
	
	/**
	 * VIEW定義SELECT文を取得する。
	 * 
	 * @return VIEW定義SELECT文. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	String getDefinition();
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	<T>T getParam(ViewParameterKey<T> key);
	
	/**
	 * このモデルが持つ全パラメータを取得する。
	 * 
	 * @return カラムが持つ全パラメータ
	 */
	ParameterMap getParams();
	
	UUIDEntityRef<? extends JmView> toReference();
}
