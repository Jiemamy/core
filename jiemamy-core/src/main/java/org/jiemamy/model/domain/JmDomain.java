/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/09/17
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
import java.util.UUID;

import org.jiemamy.dddbase.EntityResolver;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.constraint.JmCheckConstraint;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.datatype.RawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * ドメインを表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public interface JmDomain extends DbObject {
	
	/**
	 * ドメインを指す型記述子を取得する。
	 * 
	 * @return ドメインを指す型記述子
	 * @since 0.3
	 * @deprecated use {@link #asType(EntityResolver)}
	 */
	@Deprecated
	RawTypeDescriptor asType();
	
	/**
	 * ドメインを指す型記述子を取得する。
	 * 
	 * @param resolver {@link JmDomain} を解決する {@code resolver}
	 * @return ドメインを指す型記述子
	 * @since 0.3.1
	 */
	RawTypeDescriptor asType(EntityResolver<UUID> resolver);
	
	JmDomain clone();
	
	/**
	 * チェック制約を取得する。
	 * 
	 * @return　チェック制約. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	Collection<? extends JmCheckConstraint> getCheckConstraints();
	
	/**
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	DataType getDataType();
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 */
	<T>T getParam(TypeParameterKey<T> key);
	
	/**
	 * このモデルが持つ全パラメータを取得する。
	 * 
	 * @return カラムが持つ全パラメータ
	 */
	ParameterMap getParams();
	
	/**
	 * NOT　NULL制約を取得する。
	 * 
	 * @return　NOT　NULL制約. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	boolean isNotNull();
	
	UUIDEntityRef<? extends JmDomain> toReference();
}
