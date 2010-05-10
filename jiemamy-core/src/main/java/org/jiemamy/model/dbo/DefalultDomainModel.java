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
package org.jiemamy.model.dbo;

import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.constraint.ColumnCheckConstraintModel;
import org.jiemamy.model.attribute.constraint.NotNullConstraintModel;
import org.jiemamy.model.datatype.BuiltinDataType;
import org.jiemamy.model.datatype.DataType;

/**
 * ドメイン定義モデル。
 * 
 * @author daisuke
 */
public class DefalultDomainModel extends AbstractDatabaseObjectModel implements DomainModel {
	
	/** ドメインとして定義された型記述子 */
	private BuiltinDataType dataType;
	
	private ColumnCheckConstraintModel checkConstraint;
	
	private NotNullConstraintModel notNullConstraint;
	

	public ColumnCheckConstraintModel getCheckConstraint() {
		return checkConstraint;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public NotNullConstraintModel getNotNullConstraint() {
		return notNullConstraint;
	}
	
	public EntityRef<DomainModel> getReference() {
		if (getId() == null) {
			throw new EntityLifecycleException();
		}
		return new DefaultEntityRef<DomainModel>(this);
	}
	
	/**
	 * チェック制約を設定する
	 * 
	 * @param check チェック制約
	 * @since 0.3
	 */
	public void setCheckConstraint(ColumnCheckConstraintModel check) {
		checkConstraint = check;
	}
	
	/**
	 * 型記述子を設定する
	 * 
	 * @param dataType 型記述子
	 * @since 0.3
	 */
	public void setDataType(BuiltinDataType dataType) {
		this.dataType = dataType;
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
		return "Domain " + getName();
	}
}
