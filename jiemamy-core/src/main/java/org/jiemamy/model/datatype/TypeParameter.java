/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/13
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
package org.jiemamy.model.datatype;

import org.jiemamy.ValueObject;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public interface TypeParameter<T> extends ValueObject {
	
	Key<Integer> SIZE = new Key<Integer>("size");
	
	Key<Integer> SCALE = new Key<Integer>("scale");
	
	Key<Integer> PRECISION = new Key<Integer>("precision");
	
	Key<Boolean> SERIAL = new Key<Boolean>("serial");
	

	Key<T> getKey();
	
	T getValue();
	

	public class Key<T> {
		
		private final String keyString;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param keyString
		 */
		public Key(String keyString) {
			this.keyString = keyString;
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
			Key<?> other = (Key<?>) obj;
			return keyString.equals(other.keyString);
		}
		
		@Override
		public int hashCode() {
			return keyString.hashCode();
		}
	}
}
