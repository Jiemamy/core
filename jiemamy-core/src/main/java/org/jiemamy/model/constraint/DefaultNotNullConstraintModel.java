/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;

/**
 * {@link NotNullConstraintModel}のデフォルト実装クラス。
 * 
 * @author daisuke
 */
public final class DefaultNotNullConstraintModel extends AbstractConstraintModel implements NotNullConstraintModel {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param column 対象カラム
	 * @return {@link DefaultNotNullConstraintModel}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static DefaultNotNullConstraintModel of(ColumnModel column) {
		Validate.notNull(column);
		DefaultNotNullConstraintModel model = new DefaultNotNullConstraintModel(UUID.randomUUID());
		model.setColumn(column.toReference());
		return model;
	}
	

	private EntityRef<? extends ColumnModel> column;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultNotNullConstraintModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultNotNullConstraintModel clone() {
		DefaultNotNullConstraintModel clone = (DefaultNotNullConstraintModel) super.clone();
		return clone;
	}
	
	public EntityRef<? extends ColumnModel> getColumnRef() {
		return column;
	}
	
	public void setColumn(EntityRef<? extends ColumnModel> column) {
		this.column = column;
	}
	
	@Override
	public EntityRef<? extends DefaultNotNullConstraintModel> toReference() {
		return new DefaultEntityRef<DefaultNotNullConstraintModel>(this);
	}
	
	@Override
	public String toString() {
		return "NN[" + column + "]";
	}
}
