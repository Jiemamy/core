/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/01/26
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
package org.jiemamy.model.constraint;

import java.util.List;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;

/**
 * キー制約を表すインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @author daisuke
 */
public interface JmKeyConstraint extends JmConstraint {
	
	JmKeyConstraint clone();
	
	/**
	* キーを構成するカラムのリストを取得する。
	* 
	* <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	* 
	* @return キーを構成するカラムのリスト
	* @since 0.3
	*/
	List<EntityRef<? extends JmColumn>> getKeyColumns();
	
	EntityRef<? extends JmKeyConstraint> toReference();
	
}
