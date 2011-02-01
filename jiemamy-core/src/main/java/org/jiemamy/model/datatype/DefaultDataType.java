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
package org.jiemamy.model.datatype;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.parameter.ParameterMap;

/**
 * 型記述子のデフォルト実装クラス。
 * 
 * <p>VALUE OBJECTであるが<b>可変オブジェクト</p>であることに注意。</p>
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultDataType implements DataType {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @return 型記述子
	 */
	public static DefaultDataType of(DataTypeCategory category) {
		return new DefaultDataType(new DefaultTypeReference(category));
	}
	

	private ParameterMap params = new ParameterMap();
	
	private TypeReference typeReference;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param typeReference 型記述子
	 */
	public DefaultDataType(TypeReference typeReference) {
		Validate.notNull(typeReference);
		this.typeReference = typeReference;
	}
	
	@Override
	public DefaultDataType clone() {
		try {
			DefaultDataType clone = (DefaultDataType) super.clone();
			clone.params = params.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error("clone not supported", e);
		}
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
		DefaultDataType other = (DefaultDataType) obj;
		if (!params.equals(other.params)) {
			return false;
		}
		if (typeReference == null) {
			if (other.typeReference != null) {
				return false;
			}
		} else if (!typeReference.equals(other.typeReference)) {
			return false;
		}
		return true;
	}
	
	public <T>T getParam(TypeParameterKey<T> key) {
		return params.get(key);
	}
	
	public ParameterMap getParams() {
		return params.clone();
	}
	
	public TypeReference getTypeReference() {
		return typeReference;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + params.hashCode();
		result = prime * result + ((typeReference == null) ? 0 : typeReference.hashCode());
		return result;
	}
	
	public void putParam(String key, String value) {
		params.put(key, value);
	}
	
	/**
	 * パラメータを追加する。
	 * 
	 * @param key キー
	 * @param value 値
	 * @param <T> 値の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>void putParam(TypeParameterKey<T> key, T value) {
		params.put(key, value);
	}
	
	/**
	 * パラメータを削除する。
	 * 
	 * @param key キー
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeParam(TypeParameterKey<?> key) {
		params.remove(key);
	}
	
	public void setTypeReference(TypeReference typeReference) {
		this.typeReference = typeReference;
	}
	
	@Override
	public String toString() {
		return typeReference.toString() + params.toString();
	}
}
