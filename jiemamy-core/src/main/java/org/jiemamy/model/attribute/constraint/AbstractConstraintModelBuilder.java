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
 * TODO for daisuke
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
	

	/**
	 * インスタンスを生成する。
	 */
	public AbstractConstraintModelBuilder() {
		// noop
	}
	
	/**
	 * コピーコンストラクタ。
	 * 
	 * @param vo コピー元の{@link ConstraintModel}
	 */
	public AbstractConstraintModelBuilder(T vo) {
		super(vo);
		
		this.name = vo.getName();
		this.logicalName = vo.getLogicalName();
		this.description = vo.getDescription();
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param description the description to set
	 * @return このビルダークラスのインスタンス
	 */
	public S setDescription(String description) {
		this.description = description;
		return getThis();
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param logicalName the logicalName to set
	 * @return このビルダークラスのインスタンス
	 */
	public S setLogicalName(String logicalName) {
		this.logicalName = logicalName;
		return getThis();
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param name the name to set
	 * @return このビルダークラスのインスタンス
	 */
	public S setName(String name) {
		this.name = name;
		return getThis();
	}
	
}
