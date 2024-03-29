/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2008/09/17
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
package org.jiemamy.model.dataset;

import java.util.List;
import java.util.Map;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OrderedEntity;
import org.jiemamy.model.table.JmTable;

/**
 * INSERT用データセットを表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public interface JmDataSet extends OrderedEntity, Entity {
	
	JmDataSet clone();
	
	/**
	 * データセット名を取得する。
	 * 
	 * @return データセット名. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	String getName();
	
	/**
	 * レコード情報を取得する。
	 * 
	 * <p>返される{@link Map}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return レコード情報
	 * @since 0.3
	 */
	Map<EntityRef<? extends JmTable>, List<JmRecord>> getRecords();
	
	EntityRef<? extends JmDataSet> toReference();
}
