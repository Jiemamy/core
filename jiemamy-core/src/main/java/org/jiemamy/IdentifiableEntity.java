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
package org.jiemamy;

import java.util.Collection;
import java.util.UUID;

/**
 * DDDにおけるENTITYを表すインターフェイス。
 * 
 * <p>ENTITYは、JavaObjectのライフサイクル（new〜GC）を越えうる独自のライフサイクルを持つ。
 * また、ENTITYはIDを持ち、そのIDはENTITYのライフサイクルを通じて不変である。
 * {@link EntityRef}により参照可能なオブジェクトでもある。</p>
 * 
 * @see EntityRef
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public interface IdentifiableEntity extends Entity {
	
	/**
	 * TODO for daisuke
	 * 
	 * @return clone
	 * @since TODO
	 * @see Object#clone()
	 */
	IdentifiableEntity clone();
	
	/**
	 * ENTITY IDの等価性を以て、ENTITYの同一性を比較する。
	 * 
	 * @param obj 比較対象オブジェクト
	 * @return 同じIDを持つ場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.3
	 */
	boolean equals(Object obj);
	
	/**
	* ENTITY IDを取得する。
	* 
	* <p>IDは、ENTITYとしてのライフサイクル開始時に指定または自動生成され、ライフサイクルを通して
	* 一貫していなければならない。ライフサイクルの終了と共にIDは削除される。</p>
	* 
	* @return ENTITY ID
	* @since 0.3
	*/
	UUID getId();
	
	Collection<? extends IdentifiableEntity> getSubEntities();
	
	// boolean isChildEntityRef(EntityRef<?> entityRef);
	
	/**
	 * 参照オブジェクトを返す。
	 * 
	 * @return 参照オブジェクト
	 * @since 0.3
	 */
	EntityRef<? extends IdentifiableEntity> toReference();
}
