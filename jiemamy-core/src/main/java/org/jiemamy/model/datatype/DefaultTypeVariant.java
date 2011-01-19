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

import java.util.UUID;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * 型記述子のデフォルト実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultTypeVariant extends AbstractEntity implements TypeVariant {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @return 型記述子
	 */
	public static DefaultTypeVariant of(DataTypeCategory category) {
		DefaultTypeVariant type = new DefaultTypeVariant(UUID.randomUUID());
		type.setTypeReference(new DefaultTypeReference(category));
		return type;
	}
	

	private ParameterMap params = new ParameterMap();
	
	private TypeReference typeReference;
	

	/**
	 * インスタンスを生成する。
	 * @param id 
	 * 
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultTypeVariant(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultTypeVariant clone() {
		DefaultTypeVariant clone = (DefaultTypeVariant) super.clone();
		clone.params = params.clone();
		return clone;
	}
	
	@Deprecated
	public DataTypeCategory getCategory() {
		return typeReference.getCategory();
	}
	
	public <T>T getParam(TypeParameterKey<T> key) {
		return params.get(key);
	}
	
	public ParameterMap getParams() {
		return params.clone();
	}
	
	@Deprecated
	public String getTypeName() {
		return typeReference.getTypeName();
	}
	
	public TypeReference getTypeReference() {
		return typeReference;
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
	
	public EntityRef<? extends DefaultTypeVariant> toReference() {
		return new DefaultEntityRef<DefaultTypeVariant>(this);
	}
}
