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
package org.jiemamy.model.parameter;

/**
 * 任意のオブジェクト{@code T}と{@link String}を相互変換するコンバータ。
 * 
 * <p>一般に、{@code converter.valueOf(converter.toString(obj)).equals.(obj)} 及び
 * {@code converter.toString(converter.valueOf(str)).equals(str)}が成り立つべきである。</p>
 * 
 * @param <T> 変換対象オブジェクトの型
 * @version $Id$
 * @author daisuke
 */
public interface Converter<T> {
	
	/**
	 * オブジェクトを文字列に変換する。
	 * 
	 * @param obj 変換対象オブジェクト
	 * @return オブジェクトの文字列表現
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	String toString(T obj);
	
	/**
	 * 文字列をオブジェクトに変換する。
	 * 
	 * @param str 変換対象文字列
	 * @return 文字列のオブジェクト表現
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	T valueOf(String str);
	
}
