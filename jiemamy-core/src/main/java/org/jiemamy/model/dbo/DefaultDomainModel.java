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
package org.jiemamy.model.dbo;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import org.jiemamy.EntityRef;
import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.attribute.constraint.CheckConstraintModel;
import org.jiemamy.model.attribute.constraint.NotNullConstraintModel;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.TypeParameter;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * ドメイン定義モデル。
 * 
 * @author daisuke
 */
public class DefaultDomainModel extends AbstractDatabaseObjectModel implements DomainModel {
	
	/** ドメインとして定義された型記述子 */
	private TypeVariant dataType;
	
	private CheckConstraintModel checkConstraint;
	
	private NotNullConstraintModel notNullConstraint;
	
	Set<TypeParameter<?>> params = Sets.newHashSet();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultDomainModel(UUID id) {
		super(id);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param serial
	 */
	public void addParameter(TypeParameter<?> param) {
		CollectionsUtil.addOrReplace(params, param);
	}
	
	public TypeVariant asType() {
		return new DomainType();
	}
	
	@Override
	public DefaultDomainModel clone() {
		return (DefaultDomainModel) super.clone();
	}
	
	public CheckConstraintModel getCheckConstraint() {
		return checkConstraint;
	}
	
	public TypeVariant getDataType() {
		return dataType;
	}
	
	public NotNullConstraintModel getNotNullConstraint() {
		return notNullConstraint;
	}
	
	/**
	 * チェック制約を設定する
	 * 
	 * @param checkConstraint チェック制約
	 * @since 0.3
	 */
	public void setCheckConstraint(CheckConstraintModel checkConstraint) {
		this.checkConstraint = checkConstraint;
	}
	
	/**
	 * 型記述子を設定する
	 * 
	 * @param dataType 型記述子
	 * @since 0.3
	 */
	public void setDataType(TypeVariant dataType) {
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
	
	public EntityRef<DefaultDomainModel> toReference() {
		return new DefaultEntityRef<DefaultDomainModel>(this);
	}
	
	@Override
	public String toString() {
		return "Domain " + getName();
	}
	

	final class DomainType extends DefaultEntityRef<DomainModel> implements TypeVariant {
		
		public DomainType() {
			super(DefaultDomainModel.this);
		}
		
		public DataTypeCategory getCategory() {
			return dataType.getCategory();
		}
		
		public Set<TypeParameter<?>> getParams() {
			return dataType.getParams();
		}
		
		public String getTypeName() {
			return dataType.getTypeName();
		}
		
	}
}
