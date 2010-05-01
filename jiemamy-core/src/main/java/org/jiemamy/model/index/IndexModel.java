/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.index;

import java.util.List;

import org.jiemamy.model.JiemamyElement;

/**
 * インデックスを表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface IndexModel extends JiemamyElement {
	
	/**
	 * インデックスカラムのリストを取得する。
	 * 
	 * <p>このメソッドは、インスタンスの持つフィールドをそのまま返す。返される{@link List}を直接操作することで、
	 * このオブジェクトのフィールドとして保持される{@link List}を変更することができる。</p>
	 * 
	 * @return インデックスカラムのリスト
	 * @since 0.2
	 */
	List<IndexColumnModel> getIndexColumns();
	
	/**
	 * インデックス名を取得する。
	 * 
	 * @return インデックス名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getName();
	
	/**
	 * ユニークインデックスか否かを取得する。
	 * 
	 * @return ユニークインデックスか否か
	 * @since 0.2
	 */
	boolean isUnique();
}
