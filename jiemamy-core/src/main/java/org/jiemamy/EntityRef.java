/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy;

import java.util.UUID;

/**
 * {@link IdentifiableEntity}に対する参照オブジェクトインターフェイス。
 * 
 * <p>このインターフェイスの実装は、イミュータブルでなければならない(must)。</p>
 * 
 * @param <T> 実体のモデル型
 * @see IdentifiableEntity
 * @since 0.3
 * @author daisuke
 */
public interface EntityRef<T extends IdentifiableEntity> extends ValueObject {
	
	/**
	 * 参照先要素の同一性を調べる。
	 * 
	 * @param obj 比較対象
	 * @return 同じIDの要素を参照している場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.3
	 */
	boolean equals(Object obj);
	
	/**
	 * 実体を特定する記述子としてのモデルIDを取得する。
	 * 
	 * @return 実体を特定する記述子としてのモデルID
	 * @since 0.3
	 */
	UUID getReferentId();
	
	/**
	 * この参照オブジェクトが引数{@code target}の参照かどうか調べる。
	 * 
	 * @param target 対象エンティティ
	 * @return この参照オブジェクトが引数{@code target}の参照の場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isReferenceOf(IdentifiableEntity target);
}
