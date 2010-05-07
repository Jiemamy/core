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
 */
public abstract class AbstractConstraintModelBuilder<T extends ConstraintModel> implements ValueObjectBuilder<T> {
	
	String name;
	
	String logicalName;
	
	String description;
	

	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param logicalName the logicalName to set
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
