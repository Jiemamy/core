/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/27
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
package org.jiemamy.model.params;

import org.jiemamy.dddbase.ValueObject;

/**
 * モデルのパラメータを表すインターフェイス。
 * 
 * @param <T> 値の型
 * @version $Id$
 * @author daisuke
 */
public interface ModelParameter<T> extends ValueObject {
	
	/**
	 * キーを取得する。
	 * 
	 * @return キー
	 */
	Key<T> getKey();
	
	/**
	 * 値を取得する。
	 * 
	 * @return 値
	 */
	T getValue();
	

	/**
	 * 型パラメータのキー。
	 * 
	 * @param <T> 値の型
	 * @author daisuke
	 */
	public class Key<T> {
		
		private final String keyString;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param keyString キー文字列
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
		
		public String getKeyString() {
			return keyString;
		}
		
		@Override
		public int hashCode() {
			return keyString.hashCode();
		}
	}
	
}