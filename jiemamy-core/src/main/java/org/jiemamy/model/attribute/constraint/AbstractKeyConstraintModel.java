/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/01/28
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

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * 抽象キー制約モデル。
 * 
 * @author daisuke
 */
public abstract class AbstractKeyConstraintModel extends AbstractConstraintModel implements KeyConstraintModel {
	
	/** キー制約を構成するカラムのリスト */
	private List<EntityRef<? extends ColumnModel>> keyColumns;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param keyColumns キー制約を構成するカラムのリスト
	 * @param deferrability 遅延評価可能性
	 * @throws IllegalArgumentException 引数{@code keyColumns}の要素が空の場合
	 */
	public AbstractKeyConstraintModel(String name, String logicalName, String description,
			List<EntityRef<? extends ColumnModel>> keyColumns, DeferrabilityModel deferrability) {
		super(name, logicalName, description, deferrability);
		Validate.notEmpty(keyColumns);
		
		this.keyColumns = new ArrayList<EntityRef<? extends ColumnModel>>(keyColumns);
	}
	
	@Override
	public AbstractKeyConstraintModel clone() {
		AbstractKeyConstraintModel clone = (AbstractKeyConstraintModel) super.clone();
		clone.keyColumns = Lists.newArrayList(keyColumns);
		return clone;
	}
	
	public List<EntityRef<? extends ColumnModel>> getKeyColumns() {
		return keyColumns;
	}
	
}
