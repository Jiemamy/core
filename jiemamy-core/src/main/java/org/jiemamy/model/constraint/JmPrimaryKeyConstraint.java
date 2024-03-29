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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.ColumnNotFoundException;
import org.jiemamy.model.table.JmTableBuilder;

/**
 * 主キー制約を表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public class JmPrimaryKeyConstraint extends JmLocalKeyConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columns キーカラム
	 * @return {@link JmPrimaryKeyConstraint}
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public static JmPrimaryKeyConstraint of(JmColumn... columns) {
		Validate.noNullElements(columns);
		JmPrimaryKeyConstraint model = new JmPrimaryKeyConstraint();
		for (JmColumn column : columns) {
			model.addKeyColumn(column.toReference());
		}
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnRefs キーカラムへの参照のリスト
	 * @return {@link JmPrimaryKeyConstraint}
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public static JmPrimaryKeyConstraint of(List<EntityRef<? extends JmColumn>> columnRefs) {
		Validate.noNullElements(columnRefs);
		JmPrimaryKeyConstraint model = new JmPrimaryKeyConstraint();
		for (EntityRef<? extends JmColumn> columnRef : columnRefs) {
			model.addKeyColumn(columnRef);
		}
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param columns キーカラム
	 * @return {@link JmPrimaryKeyConstraint}
	 * @throws IllegalArgumentException 引数{@code columns}に{@code null}を与えた場合
	 */
	public static JmPrimaryKeyConstraint of(String name, JmColumn... columns) {
		JmPrimaryKeyConstraint model = of(columns);
		model.setName(name);
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public JmPrimaryKeyConstraint() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>構築中のビルダからカラムを検索し、
	 * 
	 * @param builder 構築中のビルダ
	 * @param names カラム名
	 * @throws ColumnNotFoundException 引数に指定したカラムの何れかがビルダ内から見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmPrimaryKeyConstraint(JmTableBuilder builder, String... names) {
		this(UUID.randomUUID());
		Validate.notNull(builder);
		Validate.noNullElements(names);
		for (JmColumn column : builder.getColumns()) {
			if (ArrayUtils.contains(names, column.getName())) {
				addKeyColumn(column.toReference());
			}
		}
		if (names.length != getKeyColumns().size()) {
			throw new ColumnNotFoundException("some column is not found: " + Arrays.toString(names));
		}
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public JmPrimaryKeyConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public JmPrimaryKeyConstraint clone() {
		JmPrimaryKeyConstraint clone = (JmPrimaryKeyConstraint) super.clone();
		return clone;
	}
	
	@Override
	public EntityRef<? extends JmPrimaryKeyConstraint> toReference() {
		return new EntityRef<JmPrimaryKeyConstraint>(this);
	}
	
	@Override
	public String toString() {
		return "PK[" + getKeyColumns() + "]";
	}
}
