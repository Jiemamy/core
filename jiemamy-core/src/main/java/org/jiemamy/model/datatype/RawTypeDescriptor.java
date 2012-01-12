/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/11
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

import java.util.Collection;

import org.jiemamy.dddbase.ValueObject;

/**
 * 型記述子インターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @version $Id$
 * @author daisuke
 */
public interface RawTypeDescriptor extends ValueObject {
	
	/**
	 * エイリアス名の集合を取得する。
	 * 
	 * @return エイリアス名の集合
	 */
	Collection<String> getAliasTypeNames();
	
	/**
	 * 型カテゴリを取得する。
	 * 
	 * @return 型カテゴリ
	 */
	RawTypeCategory getCategory();
	
	/**
	 * 型名の文字列を取得する。
	 * 
	 * @return 型名の文字列
	 */
	String getTypeName();
}
