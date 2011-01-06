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
package org.jiemamy.model.domain;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.model.DefaultDatabaseObjectModel;
import org.jiemamy.model.Key;
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.TypeParameter;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.utils.MutationMonitor;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * ドメイン定義モデル。
 * 
 * @author daisuke
 */
public final class DefaultDomainModel extends DefaultDatabaseObjectModel implements DomainModel {
	
	/** ドメインとして定義された型記述子 */
	private TypeVariant dataType;
	
	private Collection<CheckConstraintModel> checkConstraints = Lists.newArrayList();
	
	private boolean notNull;
	
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
	 * チェック制約を設定する
	 * 
	 * @param checkConstraint チェック制約
	 * @since 0.3
	 */
	public void addCheckConstraint(CheckConstraintModel checkConstraint) {
		checkConstraints.add(checkConstraint);
	}
	
	/**
	 * パラメータを追加する。
	 * 
	 * @param param 追加するパラメータ
	 */
	public void addParameter(TypeParameter<?> param) {
		CollectionsUtil.addOrReplace(params, param);
	}
	
	public TypeVariant asType() {
		return new DomainType();
	}
	
	public Collection<? extends CheckConstraintModel> breachEncapsulationOfCheckConstraints() {
		return checkConstraints;
	}
	
	@Override
	public DefaultDomainModel clone() {
		DefaultDomainModel clone = (DefaultDomainModel) super.clone();
		clone.checkConstraints = CloneUtil.cloneValueArrayList(checkConstraints);
		return clone;
	}
	
	public Collection<? extends CheckConstraintModel> getCheckConstraints() {
		return MutationMonitor.monitor(Lists.newArrayList(checkConstraints));
	}
	
	public TypeVariant getDataType() {
		return dataType;
	}
	
	public boolean isNotNull() {
		return notNull;
	}
	
	/**
	 * 型記述子を設定する。
	 * 
	 * @param dataType 型記述子
	 * @since 0.3
	 */
	public void setDataType(TypeVariant dataType) {
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
	
	public EntityRef<DefaultDomainModel> toReference() {
		return new DefaultEntityRef<DefaultDomainModel>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (JiemamyContext.isDebug()) {
			sb.append(ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE));
		} else {
			sb.append("Domain ").append(getName());
		}
		return sb.toString();
	}
	

	final class DomainType extends DefaultEntityRef<DomainModel> implements TypeVariant {
		
		public DomainType() {
			super(DefaultDomainModel.this);
		}
		
		public DataTypeCategory getCategory() {
			return dataType.getCategory();
		}
		
		public <T>TypeParameter<T> getParam(Key<T> key) {
			return dataType.getParam(key);
		}
		
		public Set<TypeParameter<?>> getParams() {
			return dataType.getParams();
		}
		
		public String getTypeName() {
			return dataType.getTypeName();
		}
	}
}
