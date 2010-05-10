/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import java.util.ArrayList;
import java.util.List;

import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * PRIMARY KEY制約モデル。
 * 
 * @author daisuke
 */
public final class DefaultPrimaryKeyConstraintModel extends AbstractKeyConstraintModel implements
		PrimaryKeyConstraintModel {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param columns キーカラム
	 * @return 新しい {@link DefaultPrimaryKeyConstraintModel}
	 */
	public static DefaultPrimaryKeyConstraintModel of(ColumnModel... columns) {
		List<EntityRef<ColumnModel>> keyColumnRefs = new ArrayList<EntityRef<ColumnModel>>();
		for (ColumnModel columnModel : columns) {
			keyColumnRefs.add(columnModel.getReference());
		}
		return new DefaultPrimaryKeyConstraintModel(null, null, null, keyColumnRefs, null);
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
	public DefaultPrimaryKeyConstraintModel(String name, String logicalName, String description,
			List<EntityRef<ColumnModel>> keyColumns, DeferrabilityModel deferrability) {
		super(name, logicalName, description, keyColumns, deferrability);
	}
}
