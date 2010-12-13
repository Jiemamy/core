/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import org.apache.commons.lang.Validate;

/**
 * チェック制約のモデル。
 * 
 * @author daisuke
 */
public final class DefaultCheckConstraintModel extends AbstractConstraintModel implements CheckConstraintModel {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param expression CHEKC制約定義式
	 * @return {@link DefaultCheckConstraintModel}
	 */
	public static DefaultCheckConstraintModel of(String expression) {
		return new DefaultCheckConstraintModel(null, null, null, expression, null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param expression CHEKC制約定義式
	 * @param name 物理名
	 * @return {@link DefaultCheckConstraintModel}
	 */
	public static DefaultCheckConstraintModel of(String expression, String name) {
		return new DefaultCheckConstraintModel(name, null, null, expression, null);
	}
	

	/** CHEKC制約定義式 */
	private final String expression;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param expression CHEKC制約定義式
	 * @param deferrability 遅延評価可能性
	 * @throws IllegalArgumentException 引数{@code expression}に{@code null}を与えた場合
	 */
	public DefaultCheckConstraintModel(String name, String logicalName, String description, String expression,
			DeferrabilityModel deferrability) {
		super(name, logicalName, description, deferrability);
		Validate.notNull(expression);
		this.expression = expression;
	}
	
	public String getExpression() {
		return expression;
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + expression + "]";
	}
}
