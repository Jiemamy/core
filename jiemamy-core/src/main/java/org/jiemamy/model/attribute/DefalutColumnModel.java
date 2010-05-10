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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.Entity;
import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.constraint.ColumnCheckConstraintModel;
import org.jiemamy.model.attribute.constraint.NotNullConstraintModel;
import org.jiemamy.model.attribute.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.UniqueKeyConstraintModel;
import org.jiemamy.model.datatype.DataType;

/**
 * カラム定義モデル。Artemisにおける{@link ColumnModel}の実装クラス。
 * 
 * @author daisuke
 */
public class DefalutColumnModel extends AbstractAttributeModel implements ColumnModel {
	
	/** ENTITY ID */
	private UUID id;
	
	/** 型記述子 */
	private DataType dataType;
	
	/** デフォルト値 */
	private String defaultValue;
	
	private ColumnCheckConstraintModel checkConstraint;
	
	private NotNullConstraintModel notNullConstraint;
	
	private PrimaryKeyConstraintModel primaryKey;
	
	private UniqueKeyConstraintModel uniqueKey;
	

	@Override
	public final boolean equals(Object obj) {
		if (id == null) {
			return false;
		}
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
	
	public ColumnCheckConstraintModel getCheckConstraint() {
		return checkConstraint;
	}
	
	public synchronized DataType getDataType() {
		return dataType;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public UUID getId() {
		return id;
	}
	
	public NotNullConstraintModel getNotNullConstraint() {
		return notNullConstraint;
	}
	
	public PrimaryKeyConstraintModel getPrimaryKey() {
		return primaryKey;
	}
	
	public EntityRef<ColumnModel> getReference() {
		return new DefaultEntityRef<ColumnModel>(this);
	}
	
	public UniqueKeyConstraintModel getUniqueKey() {
		return uniqueKey;
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	/**
	 * チェック制約を設定する
	 * 
	 * @param checkConstraint チェック制約
	 * @since 0.3
	 */
	public void setCheckConstraint(ColumnCheckConstraintModel checkConstraint) {
		this.checkConstraint = checkConstraint;
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
	
	@Override
	public void setDescription(String description) {
		super.setDescription(description);
	}
	
	@Override
	public void setLogicalName(String logicalName) {
		super.setLogicalName(logicalName);
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
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
	
	/**
	 * 主キー制約を設定する
	 * 
	 * @param primaryKey 主キー制約
	 * @since 0.3
	 */
	public void setPrimaryKey(PrimaryKeyConstraintModel primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	/**
	 * ユニークキー制約を設定する
	 * 
	 * @param uniqueKey ユニークキー制約
	 * @since 0.3
	 */
	public void setUniqueKey(UniqueKeyConstraintModel uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	void setId(UUID id) {
		this.id = id;
	}
	
}
