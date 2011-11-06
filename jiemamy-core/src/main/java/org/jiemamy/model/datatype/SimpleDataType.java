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

import org.jiemamy.model.domain.SimpleJmDomain.DomainType;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * 型記述子のデフォルト実装クラス。
 * 
 * <p>VALUE OBJECTであるが<b>可変オブジェクト</p>であることに注意。</p>
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleDataType implements DataType {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @return 型記述子
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static SimpleDataType of(RawTypeCategory category) {
		Validate.notNull(category);
		return new SimpleDataType(new SimpleRawTypeDescriptor(category));
	}
	
	
	private ParameterMap params = new ParameterMap();
	
	private RawTypeDescriptor rawTypeDescriptor;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param rawTypeDescriptor 型記述子
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleDataType(RawTypeDescriptor rawTypeDescriptor) {
		Validate.notNull(rawTypeDescriptor);
		this.rawTypeDescriptor = rawTypeDescriptor;
	}
	
	@Override
	public SimpleDataType clone() {
		try {
			SimpleDataType clone = (SimpleDataType) super.clone();
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
		SimpleDataType other = (SimpleDataType) obj;
		if (params.equals(other.params) == false) {
			return false;
		}
		if (rawTypeDescriptor.equals(other.rawTypeDescriptor) == false) {
			return false;
		}
		return true;
	}
	
	public <T>T getParam(TypeParameterKey<T> key) {
		T result = params.get(key);
		if (result == null && rawTypeDescriptor instanceof DomainType) {
			DomainType domainType = (DomainType) rawTypeDescriptor;
			result = domainType.getParam(key);
		}
		return result;
	}
	
	public ParameterMap getParams() {
		return params.clone();
	}
	
	public RawTypeDescriptor getRawTypeDescriptor() {
		return rawTypeDescriptor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + params.hashCode();
		result = prime * result + rawTypeDescriptor.hashCode();
		return result;
	}
	
	/**
	 * {@link String}型でパラメータを追加する。
	 * 
	 * <p>このメソッドはタイプセーフを失うので、通常は {@link #putParam(TypeParameterKey, Object)}
	 * を利用すべきである。</p>
	 * 
	 * @param key キー
	 * @param value 値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void putParam(String key, String value) {
		Validate.notNull(key);
		Validate.notNull(value);
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
		Validate.notNull(key);
		Validate.notNull(value);
		params.put(key, value);
	}
	
	/**
	 * パラメータを削除する。
	 * 
	 * @param key キー
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeParam(TypeParameterKey<?> key) {
		Validate.notNull(key);
		params.remove(key);
	}
	
	/**
	 * 型記述子を設定する。
	 * 
	 * @param rawTypeDescriptor 型記述子
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setRawTypeDescriptor(RawTypeDescriptor rawTypeDescriptor) {
		this.rawTypeDescriptor = rawTypeDescriptor;
	}
	
	@Override
	public String toString() {
		return rawTypeDescriptor.toString() + params.toString();
	}
}
