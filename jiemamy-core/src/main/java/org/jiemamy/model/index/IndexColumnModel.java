/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.index;

import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * インデックス対象のカラムを表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface IndexColumnModel {
	
	/**
	 * ソート順を表す列挙型。
	 * 
	 * @since 0.2
	 * @author daisuke
	 */
	public static enum SortOrder {
		
		/** 昇順ソート */
		ASC,

		/** 降順ソート */
		DESC
	}
	

	/**
	 * インデックス対象カラムを取得する。
	 * 
	 * @return インデックス対象カラム. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	EntityRef<ColumnModel> getColumnRef();
	
	/**
	 * カラムソート方式を取得する。
	 * 
	 * @return カラムソート方式. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	SortOrder getSortOrder();
}
