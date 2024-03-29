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

import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;

/**
 * UNIQUE制約を表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public class JmUniqueKeyConstraint extends JmLocalKeyConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columns 対象カラム
	 * @return {@link JmUniqueKeyConstraint}
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を含むコレクションを与えた場合
	 */
	public static JmUniqueKeyConstraint of(JmColumn... columns) {
		Validate.noNullElements(columns);
		JmUniqueKeyConstraint model = new JmUniqueKeyConstraint();
		for (JmColumn column : columns) {
			model.addKeyColumn(column.toReference());
		}
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public JmUniqueKeyConstraint() {
		this(UUID.randomUUID());
	}
	
	/**
	* インスタンスを生成する。
	* 
	* @param id ENTITY ID
	* @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	*/
	public JmUniqueKeyConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public JmUniqueKeyConstraint clone() {
		JmUniqueKeyConstraint clone = (JmUniqueKeyConstraint) super.clone();
		return clone;
	}
	
	@Override
	public EntityRef<? extends JmUniqueKeyConstraint> toReference() {
		return new EntityRef<JmUniqueKeyConstraint>(this);
	}
	
	@Override
	public String toString() {
		return "UK[" + getKeyColumns() + "]";
	}
}
