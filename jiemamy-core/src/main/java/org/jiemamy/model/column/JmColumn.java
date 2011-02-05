/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.model.column;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OrderedEntity;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.parameter.ParameterMap;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.TableNotFoundException;
import org.jiemamy.model.table.TooManyTablesFoundException;

/**
 * リレーショナルデータベースにおける「カラム」を表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public interface JmColumn extends OrderedEntity {
	
	JmColumn clone();
	
	/**
	 * テーブルの集合の中からこのカラムが属するテーブルを返す。
	 * 
	 * @param tables 候補となるテーブルの集合
	 * @return このカラムが属するテーブル
	 * @throws TableNotFoundException 該当するテーブルが見つからなかった場合
	 * @throws TooManyTablesFoundException 該当するテーブルが複数見つかった場合
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	JmTable findDeclaringTable(Iterable<? extends JmTable> tables);
	
	/**
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	DataType getDataType();
	
	/**
	 * デフォルト値を取得する。
	 * 
	 * @return デフォルト値. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getDefaultValue();
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getDescription();
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getLogicalName();
	
	/**
	 * 物理名を取得する。
	 * 
	 * @return 物理名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getName();
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	<T>T getParam(ColumnParameterKey<T> key);
	
	/**
	 * このモデルが持つ全パラメータを取得する。
	 * 
	 * @return カラムが持つ全パラメータ
	 */
	ParameterMap getParams();
	
	EntityRef<? extends JmColumn> toReference();
	
}
