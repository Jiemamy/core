/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/06/09
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

import java.util.ArrayList;
import java.util.List;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;

/**
 * UNIQUE制約モデル。
 * 
 * @author daisuke
 */
public final class DefaultUniqueKeyConstraintModel extends AbstractKeyConstraintModel implements
		UniqueKeyConstraintModel {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columns 対象カラム
	 * @return {@link DefaultUniqueKeyConstraintModel}
	 */
	public static DefaultUniqueKeyConstraintModel of(ColumnModel... columns) {
		List<EntityRef<? extends ColumnModel>> keyColumnRefs = new ArrayList<EntityRef<? extends ColumnModel>>();
		for (ColumnModel columnModel : columns) {
			keyColumnRefs.add(columnModel.toReference());
		}
		return new DefaultUniqueKeyConstraintModel(null, null, null, keyColumnRefs, null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param keyColumns キー制約を構成するカラムのリスト
	 * @param deferrability 遅延評価可能性モデル
	 */
	public DefaultUniqueKeyConstraintModel(String name, String logicalName, String description,
			List<EntityRef<? extends ColumnModel>> keyColumns, DeferrabilityModel deferrability) {
		super(name, logicalName, description, keyColumns, deferrability);
	}
	
}