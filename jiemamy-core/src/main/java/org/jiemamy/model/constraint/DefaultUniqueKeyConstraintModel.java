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

import java.util.UUID;

import org.jiemamy.dddbase.DefaultEntityRef;
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
		DefaultUniqueKeyConstraintModel model = new DefaultUniqueKeyConstraintModel(UUID.randomUUID());
		for (ColumnModel columnModel : columns) {
			model.addKeyColumn(columnModel.toReference());
		}
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultUniqueKeyConstraintModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultUniqueKeyConstraintModel clone() {
		DefaultUniqueKeyConstraintModel clone = (DefaultUniqueKeyConstraintModel) super.clone();
		return clone;
	}
	
	@Override
	public EntityRef<? extends DefaultUniqueKeyConstraintModel> toReference() {
		return new DefaultEntityRef<DefaultUniqueKeyConstraintModel>(this);
	}
	
	@Override
	public String toString() {
		return "UK[" + getKeyColumns() + "]";
	}
}
