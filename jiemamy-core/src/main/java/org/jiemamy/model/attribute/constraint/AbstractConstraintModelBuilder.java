/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/07
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

import org.jiemamy.model.ValueObjectBuilder;

/**
 * {@link ConstraintModel}のビルダー骨格実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 * @param <T> ビルド対象のインスタンスの型
 * @param <S> このビルダークラスの型
 */
public abstract class AbstractConstraintModelBuilder<T extends ConstraintModel, S extends AbstractConstraintModelBuilder<T, S>>
		extends ValueObjectBuilder<T, S> {
	
	String name;
	
	String logicalName;
	
	String description;
	

	@Override
	public S apply(T vo) {
		return setName(vo.getName()).setLogicalName(vo.getLogicalName()).setDescription(vo.getDescription());
	}
	
	/**
	 * 説明文を設定する。 
	 * @param description 説明文
	 * @return このビルダークラスのインスタンス
	 */
	public S setDescription(String description) {
		this.description = description;
		return getThis();
	}
	
	/**
	 * 論理名を設定する。 
	 * @param logicalName 論理名
	 * @return このビルダークラスのインスタンス
	 */
	public S setLogicalName(String logicalName) {
		this.logicalName = logicalName;
		return getThis();
	}
	
	/**
	 * 物理名を設定する。 
	 * @param name 物理名
	 * @return このビルダークラスのインスタンス
	 */
	public S setName(String name) {
		this.name = name;
		return getThis();
	}
	
}
