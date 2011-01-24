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
package org.jiemamy.model.index;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.DefaultDatabaseObjectModel;

/**
 * インデックスモデル。
 * 
 * @author daisuke
 */
public final class DefaultIndexModel extends DefaultDatabaseObjectModel implements IndexModel {
	
	/** ユニークインデックスか否か */
	private boolean unique;
	
	/** インデックスカラムのリスト */
	private List<IndexColumnModel> indexColumns = Lists.newArrayList();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultIndexModel(UUID id) {
		super(id);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param indexColumn
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void addIndexColumn(IndexColumnModel indexColumn) {
		indexColumns.add(indexColumn);
	}
	
	public List<IndexColumnModel> breachEncapsulationOfIndexColumns() {
		return indexColumns;
	}
	
	@Override
	public DefaultIndexModel clone() {
		DefaultIndexModel clone = (DefaultIndexModel) super.clone();
		clone.indexColumns = CloneUtil.cloneValueArrayList(indexColumns);
		return clone;
	}
	
	public List<IndexColumnModel> getIndexColumns() {
		assert indexColumns != null;
		return MutationMonitor.monitor(Lists.newArrayList(indexColumns));
	}
	
	public boolean isUnique() {
		return unique;
	}
	
	public void reomveIndexColumn(IndexColumnModel indexColumn) {
		indexColumns.remove(indexColumn);
	}
	
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	@Override
	public EntityRef<? extends DefaultIndexModel> toReference() {
		return new DefaultEntityRef<DefaultIndexModel>(this);
	}
}
