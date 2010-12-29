/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/02/09
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
package org.jiemamy.transaction;

import org.jiemamy.dddbase.EntityRef;

/**
 * モデルを編集するためのEDITコマンドのインターフェース。
 * 
 * <p>このインターフェイスの実装は、イミュータブルであることが望ましい。</p>
 * 
 * @since 0.2
 * @author daisuke
 * @author shin1ogawa
 */
public interface Command {
	
	/**
	 * EDITコマンドを実行する。
	 * 
	 * @since 0.2
	 */
	void execute();
	
	/**
	 * 取り消しEDITコマンドを取得する。
	 * 
	 * <p>このメソッドは、常に同じ効果をもたらす取り消しEDITコマンドを返さなければならない。
	 * 例えば、{@link #execute()}を実行前後で変化してはならない。
	 * このインターフェイスの実装は、イミュータブルであることが望ましいのは、以上の理由である。</p>
	 * 
	 * @return 取り消しEDITコマンド
	 * @since 0.2
	 */
	Command getNegateCommand();
	
	/**
	 * 操作対象の{@link EntityRef}を返す。
	 * 
	 * @return 操作対象の{@link EntityRef}
	 * @since 0.2
	 */
	EntityRef<?> getTarget();
	
}
