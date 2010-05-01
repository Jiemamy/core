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
package org.jiemamy.model.attribute;

import org.jiemamy.model.Entity;
import org.jiemamy.model.attribute.constraint.ColumnCheckConstraint;
import org.jiemamy.model.attribute.constraint.NotNullConstraint;
import org.jiemamy.model.attribute.constraint.PrimaryKey;
import org.jiemamy.model.attribute.constraint.UniqueKey;
import org.jiemamy.model.datatype.DataType;

/**
 * リレーショナルデータベースにおける「カラム」を表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface ColumnModel extends AttributeModel, Entity {
	
	/**
	 * チェック制約を取得する。
	 * 
	 * @return　チェック制約. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	ColumnCheckConstraint getCheckConstraint();
	
	/**
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	DataType getDataType();
	
	/**
	 * デフォルト値を取得する。
	 * 
	 * @return デフォルト値. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getDefaultValue();
	
	/**
	 * NOT　NULL制約を取得する。
	 * 
	 * @return　NOT　NULL制約. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	NotNullConstraint getNotNullConstraint();
	
	/**
	 * 主キー制約を取得する。
	 * 
	 * @return　主キー制約. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	PrimaryKey getPrimaryKey();
	
	/**
	 * 一意キー制約を取得する。
	 * 
	 * @return　一意キー制約. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	UniqueKey getUniqueKey();
}
