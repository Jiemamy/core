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
 * データ型をあらわすインターフェイス。
 * 
 * <p>実質的に{@link TypeReference} + パラメータで成っている。</p>
 * 
 * <p>例えば「INTEGER」や「INTEGER(serial)」「VARCHAR(32)」など、DBのデータ型として
 * 完成した形の型表現を表す。</p>
 * 
 * @author daisuke
 * @see TypeReference
 */
public interface DataType extends Cloneable {
	
	/**
	 * 型記述子のクローンを取得する。
	 * 
	 * <p>この型のサブタイプは、必ずこのメソッドを再定義し、戻り値型を自分自身の型に共変して宣言
	 * すべきである(should)。例えば、{@code FooType extends DataType} という型を宣言したら、
	 * そのメソッドとして {@code FooType clone()} というシグネチャのメソッドを再定義
	 * すべきである(should)。</p>
	 * 
	 * @return clone クローンオブジェクト
	 * @since 1.0.0
	 * @see Object#clone()
	 */
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
	
	/**
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子
	 */
	TypeReference getTypeReference();
}
