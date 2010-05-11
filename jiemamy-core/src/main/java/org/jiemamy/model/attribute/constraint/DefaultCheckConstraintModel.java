/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
public class DefaultCheckConstraintModel extends AbstractConstraintModel implements CheckConstraintModel {
	
	/** CHEKC制約定義式 */
	private final String expression;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param expression CHEKC制約定義式
	 * @throws IllegalArgumentException 引数{@code expression}に{@code null}を与えた場合
	 */
	public DefaultCheckConstraintModel(String name, String logicalName, String description, String expression) {
		super(name, logicalName, description);
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
