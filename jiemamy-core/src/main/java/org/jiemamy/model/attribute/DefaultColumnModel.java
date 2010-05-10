/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute;

import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.Repository.InternalKey;
import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.Entity;
import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.constraint.NotNullConstraintModel;
import org.jiemamy.model.datatype.DataType;

/**
 * カラム定義モデル。Artemisにおける{@link ColumnModel}の実装クラス。
 * 
 * @author daisuke
 */
public class DefaultColumnModel implements ColumnModel {
	
	/** ENTITY ID */
	private final UUID id;
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明 */
	private String description;
	
	/** 型記述子 */
	private DataType dataType;
	
	/** デフォルト値 */
	private String defaultValue;
	
	private NotNullConstraintModel notNullConstraint;
	
	private boolean alive;
	

	DefaultColumnModel(UUID id) {
		Validate.notNull(id);
		this.id = id;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof Entity == false) {
			return false;
		}
		return id.equals(((Entity) obj).getId());
	}
	
	public synchronized DataType getDataType() {
		return dataType;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public String getDescription() {
		return description;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getLogicalName() {
		return logicalName;
	}
	
	public String getName() {
		return name;
	}
	
	public NotNullConstraintModel getNotNullConstraint() {
		return notNullConstraint;
	}
	
	public EntityRef<ColumnModel> getReference() {
		return new DefaultEntityRef<ColumnModel>(this);
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	public void initiate(InternalKey key) {
		Validate.notNull(key);
		alive = true;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void kill(InternalKey key) {
		Validate.notNull(key);
		alive = false;
	}
	
	/**
	 * データタイプを設定する
	 * 
	 * @param dataType データタイプ
	 * @since 0.3
	 */
	public synchronized void setDataType(DataType dataType) {
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
	 * somethingを設定する。 TODO for daisuke
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param logicalName the logicalName to set
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * NotNull制約を設定する
	 * 
	 * @param notNullConstraint NotNull制約
	 * @since 0.3
	 */
	public void setNotNullConstraint(NotNullConstraintModel notNullConstraint) {
		this.notNullConstraint = notNullConstraint;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
