/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import org.jiemamy.model.ValueObject;

/**
 * チェック制約モデル。
 * 
 * @author daisuke
 */
public final class DefaultColumnCheckConstraintModel extends AbstractCheckConstraintModel implements ColumnCheckConstraintModel,
		ValueObject {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param expression CHEKC制約定義式
	 * @throws IllegalArgumentException 引数expressionに{@code null}を与えた場合
	 */
	public DefaultColumnCheckConstraintModel(String expression) {
		super(expression);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param expression CHEKC制約定義式
	 * @throws IllegalArgumentException 引数expressionに{@code null}を与えた場合
	 */
	public DefaultColumnCheckConstraintModel(String name, String logicalName, String description, String expression) {
		super(name, logicalName, description, expression);
	}
	
	/**
	 * このインスタンスの説明要素だけを変更した新しいオブジェクトを生成する。
	 * 
	 * @param description 新しい説明
	 * @return 新しいオブジェクト
	 * @since 0.3
	 */
	public DefaultColumnCheckConstraintModel changeDescriptionTo(String description) {
		return new DefaultColumnCheckConstraintModel(getName(), getLogicalName(), description, getExpression());
	}
	
	/**
	 * このインスタンスのCHEKC制約定義式要素だけを変更した新しいオブジェクトを生成する。
	 * 
	 * @param expression 新しいCHEKC制約定義式
	 * @return 新しいオブジェクト
	 * @since 0.3
	 */
	public DefaultColumnCheckConstraintModel changeExpressionTo(String expression) {
		return new DefaultColumnCheckConstraintModel(getName(), getLogicalName(), getDescription(), expression);
	}
	
	/**
	 * このインスタンスの論理名要素だけを変更した新しいオブジェクトを生成する。
	 * 
	 * @param logicalName 新しい論理名
	 * @return 新しいオブジェクト
	 * @since 0.3
	 */
	public DefaultColumnCheckConstraintModel changeLogicalNameTo(String logicalName) {
		return new DefaultColumnCheckConstraintModel(getName(), logicalName, getDescription(), getExpression());
	}
	
	/**
	 * このインスタンスの物理名要素だけを変更した新しいオブジェクトを生成する。
	 * 
	 * @param name 新しい物理名
	 * @return 新しいオブジェクト
	 * @since 0.3
	 */
	public DefaultColumnCheckConstraintModel changeNameTo(String name) {
		return new DefaultColumnCheckConstraintModel(name, getLogicalName(), getDescription(), getExpression());
	}
	
}