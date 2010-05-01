/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/12/12
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
package org.jiemamy.model.attribute;

import org.jiemamy.model.JiemamyElement;
import org.jiemamy.model.entity.TableModel;

/**
 * リレーショナルデータベースモデルにおける「属性」を表すモデルインターフェイス。
 * 
 * <p>SQLのCREATE TABLE文の括弧内にカンマ区切りで列挙される要素のこと。例えばカラム定義, 主キー定義, 外部キー定義など。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public interface AttributeModel extends JiemamyElement {
	
	/**
	 * この属性が所属するテーブルを取得する。
	 * 
	 * @return この属性が所属するテーブル. どのテーブルにも所属していない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	TableModel findDeclaringTable();
	
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
}
