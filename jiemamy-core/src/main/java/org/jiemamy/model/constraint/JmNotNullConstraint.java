/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.model.constraint;

import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.column.JmColumn;

/**
 * NOT NULL制約を表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public interface JmNotNullConstraint extends JmValueConstraint {
	
	JmNotNullConstraint clone();
	
	/**
	* 対象カラム参照を取得する。
	* 
	* @return 対象カラム参照、未設定の場合{@code null}
	*/
	UUIDEntityRef<? extends JmColumn> getColumn();
	
	UUIDEntityRef<? extends JmNotNullConstraint> toReference();
}
