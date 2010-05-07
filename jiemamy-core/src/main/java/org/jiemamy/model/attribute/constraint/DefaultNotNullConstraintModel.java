/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import org.jiemamy.model.ValueObject;

/**
 * NOT NULL制約モデル。
 * 
 * @author daisuke
 */
public final class DefaultNotNullConstraintModel extends AbstractConstraintModel implements NotNullConstraintModel, ValueObject {
	
	/**
	 * インスタンスを生成する。
	 */
	public DefaultNotNullConstraintModel() {
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 */
	public DefaultNotNullConstraintModel(String name, String logicalName, String description) {
		super(name, logicalName, description);
	}
	
	public DefaultNotNullConstraintModel changeDescriptionTo(String description) {
		return new DefaultNotNullConstraintModel(getName(), getLogicalName(), description);
	}
	
	public DefaultNotNullConstraintModel changeLogicalNameTo(String logicalName) {
		return new DefaultNotNullConstraintModel(getName(), logicalName, getDescription());
	}
	
	public DefaultNotNullConstraintModel changeNameTo(String name) {
		return new DefaultNotNullConstraintModel(name, getLogicalName(), getDescription());
	}
	
}