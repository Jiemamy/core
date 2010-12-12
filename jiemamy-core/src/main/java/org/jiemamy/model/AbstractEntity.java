/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/10
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
package org.jiemamy.model;

import org.apache.commons.lang.Validate;

import org.jiemamy.Entity;
import org.jiemamy.JiemamyError;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractEntity implements Entity {
	
	final Object id;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public AbstractEntity(Object id) {
		Validate.notNull(id);
		this.id = id;
	}
	
	@Override
	public AbstractEntity clone() {
		try {
			return (AbstractEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new JiemamyError("clone not supported", e);
		}
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof AbstractEntity == false) {
			return false;
		}
		return id.equals(((AbstractEntity) obj).id);
	}
	
	@Override
	public final int hashCode() {
		return id.hashCode();
	}
}
