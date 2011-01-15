/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import java.util.List;
import java.util.UUID;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;

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
	 * @return {@link DefaultPrimaryKeyConstraintModel}
	 */
	public static DefaultPrimaryKeyConstraintModel of(ColumnModel... columns) {
		DefaultPrimaryKeyConstraintModel model = new DefaultPrimaryKeyConstraintModel(UUID.randomUUID());
		for (ColumnModel columnModel : columns) {
			model.addKeyColumn(columnModel.toReference());
		}
		return model;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param refs
	 * @return
	 */
	public static DefaultPrimaryKeyConstraintModel of(EntityRef<? extends ColumnModel>... refs) {
		DefaultPrimaryKeyConstraintModel model = new DefaultPrimaryKeyConstraintModel(UUID.randomUUID());
		for (EntityRef<? extends ColumnModel> ref : refs) {
			model.addKeyColumn(ref);
		}
		return model;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param pk
	 * @return
	 */
	public static DefaultPrimaryKeyConstraintModel of(List<EntityRef<? extends ColumnModel>> columnRefs) {
		DefaultPrimaryKeyConstraintModel model = new DefaultPrimaryKeyConstraintModel(UUID.randomUUID());
		for (EntityRef<? extends ColumnModel> ref : columnRefs) {
			model.addKeyColumn(ref);
		}
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param columns キーカラム
	 * @return {@link DefaultPrimaryKeyConstraintModel}
	 */
	public static DefaultPrimaryKeyConstraintModel of(String name, ColumnModel... columns) {
		DefaultPrimaryKeyConstraintModel model = of(columns);
		model.setName(name);
		return model;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultPrimaryKeyConstraintModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultPrimaryKeyConstraintModel clone() {
		DefaultPrimaryKeyConstraintModel clone = (DefaultPrimaryKeyConstraintModel) super.clone();
		return clone;
	}
	
	@Override
	public EntityRef<? extends DefaultPrimaryKeyConstraintModel> toReference() {
		return new DefaultEntityRef<DefaultPrimaryKeyConstraintModel>(this);
	}
	
	@Override
	public String toString() {
		return "PK[" + getKeyColumns() + "]";
	}
}
