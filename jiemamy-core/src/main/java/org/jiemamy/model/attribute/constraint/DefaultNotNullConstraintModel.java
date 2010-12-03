/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import org.jiemamy.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * NOT NULL制約モデル。
 * 
 * @author daisuke
 */
public final class DefaultNotNullConstraintModel extends AbstractConstraintModel implements NotNullConstraintModel {
	
	private final EntityRef<ColumnModel> column;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param deferrability 遅延評価可能性
	 * @param column 対象カラム
	 */
	public DefaultNotNullConstraintModel(String name, String logicalName, String description,
			DeferrabilityModel deferrability, EntityRef<ColumnModel> column) {
		super(name, logicalName, description, deferrability);
		this.column = column;
	}
	
	public EntityRef<ColumnModel> getColumn() {
		return column;
	}
	
}
