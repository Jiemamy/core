/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.EntityResolver;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.dddbase.RepositoryException;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.constraint.JmCheckConstraint;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.RawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * ドメイン定義モデル。
 * 
 * @author daisuke
 */
public final class JmDomain extends DbObject {
	
	/** ドメインとして定義された型記述子 */
	private DataType dataType;
	
	private OnMemoryRepository<JmCheckConstraint> checkConstraints = new OnMemoryRepository<JmCheckConstraint>();
	
	private boolean notNull;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public JmDomain() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public JmDomain(UUID id) {
		super(id);
	}
	
	/**
	 * チェック制約を設定する
	 * 
	 * @param checkConstraint チェック制約
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	public void addCheckConstraint(JmCheckConstraint checkConstraint) {
		Validate.notNull(checkConstraint);
		checkConstraints.store(checkConstraint);
	}
	
	/**
	 * ドメインを指す型記述子を取得する。
	 * 
	 * @param resolver {@link JmDomain} を解決する {@code resolver}
	 * @return ドメインを指す型記述子
	 * @since 0.3.1
	 */
	public RawTypeDescriptor asType(EntityResolver resolver) {
		return new DomainType(this, resolver);
	}
	
	@Override
	public JmDomain clone() {
		JmDomain clone = (JmDomain) super.clone();
		clone.checkConstraints = checkConstraints.clone();
		clone.params = params.clone();
		return clone;
	}
	
	/**
	 * チェック制約を取得する。
	 * 
	 * @return　チェック制約. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public Collection<? extends JmCheckConstraint> getCheckConstraints() {
		return checkConstraints.getEntitiesAsSet();
	}
	
	/**
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public DataType getDataType() {
		return dataType;
	}
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 */
	public <T>T getParam(TypeParameterKey<T> key) {
		return params.get(key);
	}
	
	/**
	 * NOT　NULL制約を取得する。
	 * 
	 * @return　NOT　NULL制約. 未設定の場合は{@code null}
	 * @since 0.3
	 */
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
	public EntityRef<? extends JmDomain> toReference() {
		return new EntityRef<JmDomain>(this);
	}
	
	/**
	 * {@link ParameterMap} を取得する。
	 * 
	 * <p>このメソッドは内部で保持している {@link ParameterMap} オブジェクトの参照を返すことにより
	 * 内部表現を暴露していることに注意すること。</p>
	 * 
	 * @return {@link ParameterMap}の内部参照
	 */
	ParameterMap breachEncapsulationOfParams() {
		return params;
	}
	
	
	/**
	 * ドメイン型の{@link RawTypeDescriptor}実装クラス。
	 * 
	 * @version $Id$
	 * @author daisuke
	 */
	public static final class DomainType extends EntityRef<JmDomain> implements RawTypeDescriptor {
		
		private final EntityResolver resolver;
		
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param domain 参照するドメイン
		 * @param resolver 参照のドメインをインスタンスを解決する {@code resolver}
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		DomainType(JmDomain domain, EntityResolver resolver) {
			super(domain);
			Validate.notNull(resolver);
			this.resolver = resolver;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (obj instanceof EntityRef == false) {
				return false;
			}
			return getReferentId().equals(((EntityRef<JmDomain>) obj).getReferentId());
		}
		
		public Collection<String> getAliasTypeNames() {
			return Collections.emptyList();
		}
		
		public RawTypeCategory getCategory() {
			return resolveRef().getDataType().getRawTypeDescriptor().getCategory();
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
			return resolveRef().getDataType().getParam(key);
		}
		
		/**
		 * このモデルが持つ全パラメータを取得する。
		 * 
		 * @return カラムが持つ全パラメータ
		 */
		public ParameterMap getParams() {
			return resolveRef().getDataType().getParams();
		}
		
		public String getTypeName() {
			return resolveRef().getDataType().getRawTypeDescriptor().getTypeName();
		}
		
		/**
		 * 型記述子を取得する。
		 * 
		 * @return 型記述子
		 */
		public RawTypeDescriptor getTypeReference() {
			return resolveRef().getDataType().getRawTypeDescriptor();
		}
		
		@Override
		public int hashCode() {
			return getReferentId().hashCode();
		}
		
		private JmDomain resolveRef() {
			try {
				JmDomain resolved = resolver.resolve(this);
				return resolved;
			} catch (RepositoryException e) {
				throw new IllegalStateException("can not resolve JmDomain Ref " + getReferentId(), e);
			}
		}
	}
}
