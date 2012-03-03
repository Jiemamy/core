/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/02/04
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

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmRectangle;

/**
 * ダイアグラム上のノードを表すインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public interface JmNode extends Entity {
	
	JmNode clone();
	
	/**
	 * ノードのレイアウト情報を取得する。
	 * 
	 * @return ノードのレイアウト情報. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	JmRectangle getBoundary();
	
	/**
	 * ノードの色情報を取得する。
	 * 
	 * @return ノードの色情報. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	JmColor getColor();
	
	EntityRef<? extends JmNode> toReference();
}
