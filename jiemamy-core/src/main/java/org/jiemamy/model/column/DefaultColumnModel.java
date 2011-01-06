/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.column;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.ModelParameter.Key;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.utils.MutationMonitor;

/**
 * カラム定義モデル。Artemisにおける{@link ColumnModel}の実装クラス。
 * 
 * @author daisuke
 */
public final class DefaultColumnModel extends AbstractEntity implements ColumnModel {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明文 */
	private String description;
	
	/** 型記述子 */
	private TypeVariant dataType;
	
	/** デフォルト値 */
	private String defaultValue;
	
	private Set<ColumnParameter<?>> params = Sets.newHashSet();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultColumnModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultColumnModel clone() {
		return (DefaultColumnModel) super.clone();
	}
	
	public TypeVariant getDataType() {
		return dataType;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLogicalName() {
		return logicalName;
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public <T>ColumnParameter<T> getParam(Key<T> key) {
		for (ColumnParameter<?> param : params) {
			if (param.getKey().equals(key)) {
				return (ColumnParameter<T>) param;
			}
		}
		return null;
	}
	
	public Set<ColumnParameter<?>> getParams() {
		return MutationMonitor.monitor(Sets.newHashSet(params));
	}
	
	public <T>void putParam(Key<T> key, T value) {
		params.add(new DefaultColumnParameter<T>(key, value));
	}
	
	public <T>void removeParam(Key<T> key) {
		params.remove(new DefaultColumnParameter<T>(key, null));
	}
	
	/**
	 * データタイプを設定する
	 * 
	 * @param dataType データタイプ
	 * @since 0.3
	 */
	public void setDataType(TypeVariant dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * デフォルト値を設定する
	 * 
	 * @param defaultValue デフォルト値
	 * @since 0.3
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * 説明文を設定する。
	 * @param description 説明文
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 論理名を設定する。
	 * @param logicalName 論理名
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	/**
	 * 物理名を設定する。 
	 * @param name 物理名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public EntityRef<DefaultColumnModel> toReference() {
		return new DefaultEntityRef<DefaultColumnModel>(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
