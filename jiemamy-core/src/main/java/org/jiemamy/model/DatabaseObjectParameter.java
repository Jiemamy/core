/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/13
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
package org.jiemamy.model;

import org.jiemamy.model.parameter.Converter;
import org.jiemamy.model.parameter.Converters;
import org.jiemamy.model.parameter.ParameterKey;

/**
 * {@link DatabaseObjectModel}のパラメータを表すインターフェイス。
 * 
 * @param <T> 値の型
 * @version $Id$
 * @author daisuke
 */
public class DatabaseObjectParameter<T> extends ParameterKey<T> {
	
	/** 無効フラグ用のキー */
	public static final DatabaseObjectParameter<Boolean> DISABLED = new DatabaseObjectParameter<Boolean>(
			Converters.BOOLEAN, "disabled");
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param converter コンバータ
	 * @param keyString キー文字列
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DatabaseObjectParameter(Converter<T> converter, String keyString) {
		super(converter, keyString);
	}
}
