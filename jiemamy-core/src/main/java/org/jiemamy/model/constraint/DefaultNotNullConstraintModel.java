/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;

/**
 * NOT NULL制約モデル。
 * 
 * @author daisuke
 */
public final class DefaultNotNullConstraintModel extends AbstractConstraintModel implements NotNullConstraintModel {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column 対象カラム
	 * @return {@link DefaultNotNullConstraintModel}
	 */
	public static DefaultNotNullConstraintModel of(ColumnModel column) {
		return new DefaultNotNullConstraintModel(null, null, null, null, column.toReference());
	}
	

	private final EntityRef<? extends ColumnModel> column;
	

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
			DeferrabilityModel deferrability, EntityRef<? extends ColumnModel> column) {
		super(name, logicalName, description, deferrability);
		Validate.notNull(column);
		this.column = column;
	}
	
	public EntityRef<? extends ColumnModel> getColumn() {
		return column;
	}
	
}