/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/03/10
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

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;

/**
 * {@link JmCheckConstraint}のデフォルト実装クラス。
 * 
 * @author daisuke
 */
public final class SimpleJmCheckConstraint extends SimpleJmValueConstraint implements JmCheckConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param expression CHEKC制約定義式
	 * @return {@link SimpleJmCheckConstraint}
	 */
	public static SimpleJmCheckConstraint of(String expression) {
		SimpleJmCheckConstraint model = new SimpleJmCheckConstraint();
		model.setExpression(expression);
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param expression CHEKC制約定義式
	 * @param name 物理名
	 * @return {@link SimpleJmCheckConstraint}
	 */
	public static SimpleJmCheckConstraint of(String expression, String name) {
		SimpleJmCheckConstraint model = new SimpleJmCheckConstraint();
		model.setName(name);
		model.setExpression(expression);
		return model;
	}
	

	/** CHEKC制約定義式 */
	private String expression;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public SimpleJmCheckConstraint() {
		this(UUID.randomUUID());
	}
	
	/**
	* インスタンスを生成する。
	* 
	* @param id ENTITY ID
	* @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	*/
	public SimpleJmCheckConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmCheckConstraint clone() {
		SimpleJmCheckConstraint clone = (SimpleJmCheckConstraint) super.clone();
		return clone;
	}
	
	public String getExpression() {
		return expression;
	}
	
	/**
	 * CHECK制約定義式を設定する。
	 * 
	 * @param expression CHECK制約定義式
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public EntityRef<? extends SimpleJmCheckConstraint> toReference() {
		return new DefaultEntityRef<SimpleJmCheckConstraint>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(sb.length() - 1, ", exp=" + expression);
		return sb.toString();
	}
}
