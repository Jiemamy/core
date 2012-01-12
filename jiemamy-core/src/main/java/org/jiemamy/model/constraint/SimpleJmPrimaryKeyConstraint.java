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
package org.jiemamy.model.constraint;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.DefaultUUIDEntityRef;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.column.JmColumn;

/**
 * PRIMARY KEY制約モデル。
 * 
 * @author daisuke
 */
public final class SimpleJmPrimaryKeyConstraint extends SimpleJmLocalKeyConstraint implements JmPrimaryKeyConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columns キーカラム
	 * @return {@link SimpleJmPrimaryKeyConstraint}
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public static SimpleJmPrimaryKeyConstraint of(JmColumn... columns) {
		Validate.noNullElements(columns);
		SimpleJmPrimaryKeyConstraint model = new SimpleJmPrimaryKeyConstraint();
		for (JmColumn column : columns) {
			model.addKeyColumn(column.toReference());
		}
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRefs キーカラムへの参照のリスト
	 * @return {@link SimpleJmPrimaryKeyConstraint}
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public static SimpleJmPrimaryKeyConstraint of(List<UUIDEntityRef<? extends JmColumn>> columnRefs) {
		Validate.noNullElements(columnRefs);
		SimpleJmPrimaryKeyConstraint model = new SimpleJmPrimaryKeyConstraint();
		for (UUIDEntityRef<? extends JmColumn> columnRef : columnRefs) {
			model.addKeyColumn(columnRef);
		}
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param columns キーカラム
	 * @return {@link SimpleJmPrimaryKeyConstraint}
	 * @throws IllegalArgumentException 引数{@code columns}に{@code null}を与えた場合
	 */
	public static SimpleJmPrimaryKeyConstraint of(String name, JmColumn... columns) {
		SimpleJmPrimaryKeyConstraint model = of(columns);
		model.setName(name);
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public SimpleJmPrimaryKeyConstraint() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public SimpleJmPrimaryKeyConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmPrimaryKeyConstraint clone() {
		SimpleJmPrimaryKeyConstraint clone = (SimpleJmPrimaryKeyConstraint) super.clone();
		return clone;
	}
	
	@Override
	public UUIDEntityRef<? extends SimpleJmPrimaryKeyConstraint> toReference() {
		return new DefaultUUIDEntityRef<SimpleJmPrimaryKeyConstraint>(this);
	}
	
	@Override
	public String toString() {
		return "PK[" + getKeyColumns() + "]";
	}
}
