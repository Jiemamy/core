/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/18
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
package org.jiemamy.dialect;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.datatype.TypeParameterKey;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class TypeParameterSpec {
	
	public static TypeParameterSpec of(TypeParameterKey<?> key, Necessity necessity) {
		return new TypeParameterSpec(key, necessity);
	}
	

	private final TypeParameterKey<?> key;
	
	private final Necessity necessity;
	

	public TypeParameterSpec(TypeParameterKey<?> key, Necessity necessity) {
		Validate.notNull(key);
		Validate.notNull(necessity);
		this.key = key;
		this.necessity = necessity;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TypeParameterSpec other = (TypeParameterSpec) obj;
		if (key.equals(other.key) == false) {
			return false;
		}
		return necessity == other.necessity;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + key.hashCode();
		result = prime * result + necessity.hashCode();
		return result;
	}
	

	public enum Necessity {
		
		/***/
		REQUIRED,

		/***/
		OPTIONAL
	}
}
