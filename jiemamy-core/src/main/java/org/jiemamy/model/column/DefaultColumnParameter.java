/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.column;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.DatabaseObjectParameter;
import org.jiemamy.model.Key;

/**
 * {@link ColumnParameter}のデフォルト実装クラス。
 * 
 * @param <T> 値の型
 * @version $Id$
 * @author daisuke
 */
public final class DefaultColumnParameter<T> implements ColumnParameter<T> {
	
	/**
	 * 無効パラメータを取得する。
	 * 
	 * @param disabled 無効とする場合は{@code true}、そうでない場合は{@code false}
	 * @return シリアルパラメータ
	 */
	public static DefaultColumnParameter<Boolean> disabled(boolean disabled) {
		return new DefaultColumnParameter<Boolean>(DISABLED, disabled);
	}
	

	private final Key<T> key;
	
	private final T value;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param key キー
	 * @param value 値
	 */
	public DefaultColumnParameter(Key<T> key, T value) {
		Validate.notNull(key);
		this.key = key;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DatabaseObjectParameter<?> other = (DatabaseObjectParameter<?>) obj;
		return getKey().equals(other.getKey());
	}
	
	public Key<T> getKey() {
		return key;
	}
	
	public T getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
}