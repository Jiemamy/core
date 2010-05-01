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

import java.util.UUID;


/**
 * チェック制約の骨格実装クラス。
 * 
 * @author daisuke
 */
public abstract class AbstractCheckConstraint extends AbstractConstraintModel implements CheckConstraint {
	
	/** CHEKC制約定義式 */
	private String expression;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractCheckConstraint(UUID id) {
		super(id);
	}
	
	public String getExpression() {
		return expression;
	}
	
	/**
	 * 
	 * CHECK制約定義式を設定する
	 * 
	 * @param expression CHECK制約定義式約
	 * @since 0.3
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + expression + "]";
	}
}
