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
package org.jiemamy.dialect;

import java.util.Collection;
import java.util.List;

import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.RawTypeDescriptor;
import org.jiemamy.validator.Validator;

/**
 * SQL方言インターフェイス。
 * 
 * <p>実装クラスは、引数無しのデフォルトコンストラクタを持たなければならない。
 * また、実装クラスはステートレスである必要がある。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public interface Dialect {
	
	/**
	 * データ型名を全て取得する。
	 * 
	 * <p>必ず要素数が{@code 1}以上のリストを返さなければならない。</p>
	 * 
	 * @return データ型名のリスト
	 * @since 0.3
	 */
	List<RawTypeDescriptor> getAllRawTypeDescriptors();
	
	/**
	 * JDBC接続URLの雛形文字列を取得する。
	 * 
	 * @return JDBC接続URLの雛形文字列
	 * @since 0.2
	 */
	String getConnectionUriTemplate();
	
	/**
	 * このDB用の{@link DatabaseMetadataParser}を返す。
	 * 
	 * @return {@link DatabaseMetadataParser}
	 */
	DatabaseMetadataParser getDatabaseMetadataParser();
	
	/**
	 * SQL方言名を取得する。
	 * 
	 * @return SQL方言名
	 * @since 0.2
	 */
	String getName();
	
	/**
	 * このDB用の{@link SqlEmitter}を返す。
	 * 
	 * @return {@link SqlEmitter}
	 */
	SqlEmitter getSqlEmitter();
	
	/**
	 * 指定した{@link RawTypeDescriptor}に対する {@link TypeParameterSpec}の集合を取得する。
	 * 
	 * <p>例えば、VARCHARに対して、どんなパラメータ仕様がありますか？ → SIZE が REQUIRED です<br/>
	 * のような感じ。</p>
	 * 
	 * @param reference {@link RawTypeDescriptor}
	 * @return {@link RawTypeDescriptor}に対する {@link TypeParameterSpec}の集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	Collection<TypeParameterSpec> getTypeParameterSpecs(RawTypeDescriptor reference);
	
	/**
	 * モデルのバリデータを取得する。
	 * 
	 * @return モデルのバリデータ
	 * @since 0.2
	 */
	Validator getValidator();
	
	/**
	 * 型記述子を、このSQL方言による型記述子表現に正規化する。
	 * 
	 * @param in 入力型記述子
	 * @return 正規化した型記述子、正規化に失敗した場合は {@link RawTypeCategory#UNKNOWN} の型記述子となる。
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	RawTypeDescriptor normalize(RawTypeDescriptor in);
	
	/**
	 * SQL方言IDを返す。
	 * 
	 * @return SQL方言ID
	 * @since 0.2
	 */
	String toString();
}
