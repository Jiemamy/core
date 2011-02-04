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
package org.jiemamy.model.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.model.DefaultDatabaseObjectModel;
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.datatype.TypeReference;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * ドメイン定義モデル。
 * 
 * @author daisuke
 */
public final class DefaultDomainModel extends DefaultDatabaseObjectModel implements DomainModel {
	
	/** ドメインとして定義された型記述子 */
	private DataType dataType;
	
	private OnMemoryRepository<CheckConstraintModel> checkConstraints = new OnMemoryRepository<CheckConstraintModel>();
	
	private boolean notNull;
	
	private ParameterMap params = new ParameterMap();
	

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
	 * チェック制約を設定する
	 * 
	 * @param checkConstraint チェック制約
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	public void addCheckConstraint(CheckConstraintModel checkConstraint) {
		Validate.notNull(checkConstraint);
		checkConstraints.store(checkConstraint);
	}
	
	public TypeReference asType() {
		return new DomainType();
	}
	
	@Override
	public DefaultDomainModel clone() {
		DefaultDomainModel clone = (DefaultDomainModel) super.clone();
		clone.checkConstraints = checkConstraints.clone();
		clone.params = params.clone();
		return clone;
	}
	
	public Collection<? extends CheckConstraintModel> getCheckConstraints() {
		return checkConstraints.getEntitiesAsSet();
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public <T>T getParam(TypeParameterKey<T> key) {
		return params.get(key);
	}
	
	public boolean isNotNull() {
		return notNull;
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
	
	/**
	 * 型記述子を設定する。
	 * 
	 * @param dataType 型記述子
	 * @since 0.3
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * NotNull制約を設定する。
	 * 
	 * @param notNull NotNullの場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.3
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	
	@Override
	public EntityRef<? extends DefaultDomainModel> toReference() {
		return new DefaultEntityRef<DefaultDomainModel>(this);
	}
	

	/**
	 * ドメイン型の{@link TypeReference}実装クラス。
	 * 
	 * @version $Id$
	 * @author daisuke
	 */
	public final class DomainType extends DefaultEntityRef<DomainModel> implements TypeReference {
		
		/**
		 * インスタンスを生成する。
		 */
		DomainType() {
			super(DefaultDomainModel.this);
		}
		
		public Collection<String> getAliasTypeNames() {
			return Collections.emptyList();
		}
		
		public DataTypeCategory getCategory() {
			return dataType.getTypeReference().getCategory();
		}
		
		/**
		 * キーに対応するパラメータの値を取得する。
		 * 
		 * @param <T> 値の型
		 * @param key キー
		 * @return パラメータの値
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		public <T>T getParam(TypeParameterKey<T> key) {
			return dataType.getParam(key);
		}
		
		/**
		 * このモデルが持つ全パラメータを取得する。
		 * 
		 * @return カラムが持つ全パラメータ
		 */
		public ParameterMap getParams() {
			return dataType.getParams();
		}
		
		public String getTypeName() {
			return dataType.getTypeReference().getTypeName();
		}
		
		/**
		 * 型記述子を取得する。
		 * 
		 * @return 型記述子
		 */
		public TypeReference getTypeReference() {
			return dataType.getTypeReference();
		}
	}
}
