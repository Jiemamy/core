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
public final class DefaultNotNullConstraint extends AbstractConstraintModel implements NotNullConstraint, ValueObject {
	
	/**
	 * インスタンスを生成する。
	 */
	public DefaultNotNullConstraint() {
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 */
	public DefaultNotNullConstraint(String name, String logicalName, String description) {
		super(name, logicalName, description);
	}
	
	public DefaultNotNullConstraint changeDescriptionTo(String description) {
		return new DefaultNotNullConstraint(getName(), getLogicalName(), description);
	}
	
	public DefaultNotNullConstraint changeLogicalNameTo(String logicalName) {
		return new DefaultNotNullConstraint(getName(), logicalName, getDescription());
	}
	
	public DefaultNotNullConstraint changeNameTo(String name) {
		return new DefaultNotNullConstraint(name, getLogicalName(), getDescription());
	}
	
}
