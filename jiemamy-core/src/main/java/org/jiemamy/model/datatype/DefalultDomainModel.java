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
package org.jiemamy.model.datatype;

import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.Entity;
import org.jiemamy.model.attribute.constraint.ColumnCheckConstraint;
import org.jiemamy.model.attribute.constraint.NotNullConstraint;

/**
 * ドメイン定義モデル。
 * 
 * @author daisuke
 */
public class DefalultDomainModel implements DomainModel {
	
	/** ENTITY ID */
	private final UUID id;
	
	/** ドメイン名 */
	private String name = "";
	
	/** 論理名 */
	private String logicalName;
	
	/** ドメインとして定義された型記述子 */
	private BuiltinDataType dataType;
	
	/** 説明文 */
	private String description;
	
	private ColumnCheckConstraint checkConstraint;
	
	private NotNullConstraint notNullConstraint;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefalultDomainModel(UUID id) {
		Validate.notNull(id);
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
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
	
	public ColumnCheckConstraint getCheckConstraint() {
		return checkConstraint;
	}
	
	public DataType getDataType() {
		return dataType;
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
	
	public NotNullConstraint getNotNullConstraint() {
		return notNullConstraint;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	/**
	 * 
	 * チェック制約を設定する
	 * 
	 * @param check チェック制約
	 * @since 0.3
	 */
	public void setCheckConstraint(ColumnCheckConstraint check) {
		checkConstraint = check;
	}
	
	/**
	 * 
	 * 型記述子を設定する
	 * 
	 * @param dataType 型記述子
	 * @since 0.3
	 */
	public void setDataType(BuiltinDataType dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * 
	 * 説明文を設定する
	 * 
	 * @param description 説明文
	 * @since 0.3
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * 論理名を設定する
	 * 
	 * @param logicalName 論理名
	 * @since 0.3
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	/**
	 * 
	 * 物理名を設定する
	 * 
	 * @param name 物理名
	 * @since 0.3
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * NotNull制約を設定する
	 * 
	 * @param notNullConstraint NotNull制約
	 * @since 0.3
	 */
	public void setNotNullConstraint(NotNullConstraint notNullConstraint) {
		this.notNullConstraint = notNullConstraint;
	}
	
	@Override
	public String toString() {
		return "Domain " + getName();
	}
}
