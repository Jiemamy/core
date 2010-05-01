/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/01
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

/**
 * DDDにおけるVALUE OBJECTを表すインターフェイス。
 * 
 * <p>このインターフェイスの実装クラスは、不変(immutable)オブジェクトであるべきである。</p>
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public interface ValueObject {
	
	/**
	 * プロパティ全ての同一性を以て、等価性を判断する。
	 * 
	 * @param obj 比較対象オブジェクト
	 * @return 等価の場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.3
	 */
	boolean equals(Object obj);
}
