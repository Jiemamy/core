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
package org.jiemamy.model.datatype;

import org.apache.commons.lang.Validate;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultTypeParameter<T> implements TypeParameter<T> {
	
	public static DefaultTypeParameter<Integer> precision(int precision) {
		return new DefaultTypeParameter<Integer>(PRECISION, precision);
	}
	
	public static DefaultTypeParameter<Integer> scale(int scale) {
		return new DefaultTypeParameter<Integer>(SCALE, scale);
	}
	
	public static DefaultTypeParameter<Boolean> serial(boolean serial) {
		return new DefaultTypeParameter<Boolean>(SERIAL, serial);
	}
	
	public static DefaultTypeParameter<Integer> size(int size) {
		return new DefaultTypeParameter<Integer>(SIZE, size);
	}
	

	private final Key<T> key;
	
	private final T value;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param key
	 * @param value
	 */
	public DefaultTypeParameter(Key<T> key, T value) {
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
		TypeParameter<?> other = (TypeParameter<?>) obj;
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
