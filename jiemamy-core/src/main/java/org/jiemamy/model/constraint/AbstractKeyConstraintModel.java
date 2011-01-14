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
package org.jiemamy.model.constraint;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.column.ColumnModel;

/**
 * 抽象キー制約モデル。
 * 
 * @author daisuke
 */
public abstract class AbstractKeyConstraintModel extends AbstractConstraintModel implements KeyConstraintModel {
	
	/** キー制約を構成するカラムのリスト */
	private List<EntityRef<? extends ColumnModel>> keyColumns = Lists.newArrayList();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public AbstractKeyConstraintModel(UUID id) {
		super(id);
	}
	
	public void addKeyColumn(EntityRef<? extends ColumnModel> keyColumn) {
		keyColumns.add(keyColumn);
	}
	
	@Override
	public AbstractKeyConstraintModel clone() {
		AbstractKeyConstraintModel clone = (AbstractKeyConstraintModel) super.clone();
		clone.keyColumns = CloneUtil.cloneValueArrayList(keyColumns);
		return clone;
	}
	
	public List<EntityRef<? extends ColumnModel>> getKeyColumns() {
		return MutationMonitor.monitor(Lists.newArrayList(keyColumns));
	}
	
	public void removeKeyColumn(EntityRef<? extends ColumnModel> keyColumn) {
		keyColumns.remove(keyColumn);
	}
	
	@Override
	public EntityRef<? extends AbstractConstraintModel> toReference() {
		return new DefaultEntityRef<AbstractConstraintModel>(this);
	}
}
