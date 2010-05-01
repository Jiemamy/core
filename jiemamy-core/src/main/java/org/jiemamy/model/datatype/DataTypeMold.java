/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/03/18
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

/**
 * データ型インスタンスを生成するための鋳型情報を表すインターフェイス。
 * 
 * @param <T> この鋳型から生成されるデータ型の型
 * @since 0.2
 * @author daisuke
 */
public interface DataTypeMold<T extends DataType> {
	
	/**
	 * 型名を取得する。
	 * 
	 * @return　型名
	 * @since 0.2
	 */
	String getName();
	
}
