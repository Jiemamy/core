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
package org.jiemamy.model.index;

import java.util.List;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DbObject;

/**
 * インデックスを表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public interface JmIndex extends DbObject {
	
	JmIndex clone();
	
	/**
	 * インデックスカラムのリストを取得する。
	 * 
	 * @return インデックスカラムのリスト
	 * @since 0.2
	 */
	List<JmIndexColumn> getIndexColumns();
	
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
	
	EntityRef<? extends JmIndex> toReference();
}
