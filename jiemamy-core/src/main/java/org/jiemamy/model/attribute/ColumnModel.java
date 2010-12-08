/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute;

import org.jiemamy.Entity;
import org.jiemamy.EntityRef;
import org.jiemamy.model.datatype.TypeVariant;

/**
 * リレーショナルデータベースにおける「カラム」を表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface ColumnModel extends Entity {
	
	ColumnModel clone();
	
	/**
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	TypeVariant getDataType();
	
	/**
	 * デフォルト値を取得する。
	 * 
	 * @return デフォルト値. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getDefaultValue();
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getDescription();
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getLogicalName();
	
	/**
	 * 物理名を取得する。
	 * 
	 * @return 物理名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getName();
	
	EntityRef<? extends ColumnModel> toReference();
	
}
