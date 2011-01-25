/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.ValueObject;

/**
 * {@link ParameterMap}用のキーオブジェクト。
 * 
 * @param <T> キーに対する値の型
 * @version $Id$
 * @author daisuke
 */
public class ParameterKey<T> implements ValueObject {
	
	/** 無効フラグ用のキー */
	public static final ParameterKey<Boolean> DISABLED = new ParameterKey<Boolean>(Converters.BOOLEAN, "disabled");
	
	/** 値の文字列変換に用いるコンバータ */
	private final Converter<T> converter;
	
	/** キー文字列 */
	private final String keyString;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param converter 値の文字列変換に用いるコンバータ
	 * @param keyString キー文字列
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ParameterKey(Converter<T> converter, String keyString) {
		Validate.notNull(converter);
		Validate.notNull(keyString);
		this.converter = converter;
		this.keyString = keyString;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return keyString.equals(((ParameterKey<?>) obj).keyString);
	}
	
	/**
	 * {@link Converter}を取得する。
	 * 
	 * @return the converter
	 */
	public Converter<T> getConverter() {
		return converter;
	}
	
	/**
	 * キー文字列を取得する。
	 * 
	 * @return キー文字列
	 */
	public String getKeyString() {
		return keyString;
	}
	
	@Override
	public final int hashCode() {
		return keyString.hashCode();
	}
	
	@Override
	public String toString() {
		return "ParameterKey[" + keyString + "]";
	}
}
