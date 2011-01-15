/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.model.view;

import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DefaultDatabaseObjectModel;

/**
 * ビューモデル
 * 
 * @author daisuke
 */
public final class DefaultViewModel extends DefaultDatabaseObjectModel implements ViewModel {
	
	/** VIEW定義SELECT文 */
	private String definition;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultViewModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultViewModel clone() {
		return (DefaultViewModel) super.clone();
	}
	
	public String getDefinition() {
		return definition;
	}
	
	public <T>T getParam(ViewParameterKey<T> key) {
		return super.getParam(key);
	}
	
	/**
	 * パラメータを追加する。
	 * 
	 * @param key キー
	 * @param value 値
	 * @param <T> 値の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>void putParam(ViewParameterKey<T> key, T value) {
		super.putParam(key, value);
	}
	
	/**
	 * パラメータを削除する。
	 * 
	 * @param key キー
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeParam(ViewParameterKey<?> key) {
		super.removeParam(key);
	}
	
	/**
	 * VIEW定義SELECT文を設定する。 
	 * @param definition VIEW定義SELECT文
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public EntityRef<DefaultViewModel> toReference() {
		return new DefaultEntityRef<DefaultViewModel>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (JiemamyContext.isDebug()) {
			sb.append(ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE));
		} else {
			sb.append("View ").append(getName());
		}
		return sb.toString();
	}
}
