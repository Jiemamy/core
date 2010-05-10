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

import java.util.UUID;

import org.jiemamy.model.Repository.InternalCredential;

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
public interface Entity {
	
	/**
	 * ENTITY IDの等価性を以て、ENTITYの同一性を比較する。
	 * 
	 * <p>ENTITYのライフサイクルがdeadの場合は、必ず{@code null}を返す。</p>
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
	* @return ENTITY ID.  ENTITYのライフサイクルがdeadの場合は{@code null}
	* @since 0.3
	*/
	UUID getId();
	
	/**
	 * 参照オブジェクトを返す。
	 * 
	 * @return 参照オブジェクト
	 * @throws EntityLifecycleException ENTITYのライフサイクルがdeadの場合
	 * @since 0.3
	 */
	EntityRef<?> getReference();
	
	/**
	 * ENTITYのライフサイクルが開始したこと、すなわち{@link Repository}の管理下に入ったことを通知する。
	 * 
	 * <p>このメソッドは Jiemamy の内部からのみ呼び出されることを意図したAPIであり、
	 * クライアントから呼び出した場合の挙動は保証しない。</p>
	 * 
	 * @param credential 呼び出し資格オブジェクト
	 */
	void initiate(InternalCredential credential);
	
	/**
	 * ENTITYのライフサイクルが開始しているかどうかを調べる。
	 * 
	 * @return ライフサイクルが開始している場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isAlive();
	
	/**
	 * ENTITYのライフサイクルが終了したこと、すなわち{@link Repository}の管理下から離れたことを通知する。
	 * 
	 * <p>このメソッドは Jiemamy の内部からのみ呼び出されることを意図したAPIであり、
	 * クライアントから呼び出した場合の挙動は保証しない。</p>
	 * 
	 * @param credential 呼び出し資格オブジェクト
	 */
	void kill(InternalCredential credential);
}
