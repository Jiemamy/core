/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/20
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

import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;

/**
 * {@link JmNotNullConstraint}のデフォルト実装クラス。
 * 
 * @author daisuke
 */
public class JmNotNullConstraint extends JmValueConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column 対象カラム
	 * @return {@link JmNotNullConstraint}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static JmNotNullConstraint of(JmColumn column) {
		Validate.notNull(column);
		JmNotNullConstraint model = new JmNotNullConstraint();
		model.setColumn(column.toReference());
		return model;
	}
	
	
	/** 対象カラム参照 */
	private EntityRef<? extends JmColumn> column;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public JmNotNullConstraint() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public JmNotNullConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public JmNotNullConstraint clone() {
		JmNotNullConstraint clone = (JmNotNullConstraint) super.clone();
		return clone;
	}
	
	/**
	 * 対象カラム参照を取得する。
	 * 
	 * @return 対象カラム参照、未設定の場合{@code null}
	 */
	public EntityRef<? extends JmColumn> getColumn() {
		return column;
	}
	
	/**
	 * 対象カラム参照を設定する。
	 * 
	 * @param column 対象カラム参照
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setColumn(EntityRef<? extends JmColumn> column) {
		Validate.notNull(column);
		this.column = column;
	}
	
	@Override
	public EntityRef<? extends JmNotNullConstraint> toReference() {
		return new EntityRef<JmNotNullConstraint>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(sb.length() - 1, ", target=" + column);
		return sb.toString();
	}
}
