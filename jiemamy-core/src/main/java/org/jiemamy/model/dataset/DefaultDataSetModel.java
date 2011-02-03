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
package org.jiemamy.model.dataset;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.table.TableModel;

/**
 * INSERT文用データセット。
 * 
 * @author daisuke
 */
public final class DefaultDataSetModel extends AbstractEntity implements DataSetModel {
	
	/** データセット名 */
	private String name;
	
	/** レコード情報 */
	private Map<EntityRef<? extends TableModel>, List<RecordModel>> records = Maps.newHashMap();
	
	private int index = -1;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultDataSetModel(UUID id) {
		super(id);
	}
	
	@Override
	public synchronized DefaultDataSetModel clone() {
		DefaultDataSetModel clone = (DefaultDataSetModel) super.clone();
		
		Map<EntityRef<? extends TableModel>, List<RecordModel>> cloneMap =
				Maps.newHashMapWithExpectedSize(records.size());
		for (Entry<EntityRef<? extends TableModel>, List<RecordModel>> entry : records.entrySet()) {
			List<RecordModel> value = entry.getValue();
			cloneMap.put(entry.getKey(), CloneUtil.cloneValueArrayList(value));
		}
		clone.records = cloneMap;
		
		return clone;
	}
	
	public int getIndex() {
		return index;
	}
	
	/**
	 * データセット名を取得する。
	 * 
	 * @return データセット名
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * テーブルに対応するレコードのリストを取得する。
	 * 
	 * @param ref テーブルの参照
	 * @return  レコードのリスト、レコードが存在しない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public synchronized List<RecordModel> getRecord(EntityRef<? extends TableModel> ref) {
		Validate.notNull(ref);
		return records.get(ref);
	}
	
	/**
	 * レコード情報を取得する。
	 * 
	 * @return レコード情報
	 */
	public synchronized Map<EntityRef<? extends TableModel>, List<RecordModel>> getRecords() {
		return MutationMonitor.monitor(Maps.newHashMap(records));
	}
	
	/**
	 * テーブルに対応するレコードのリストを設定する。
	 * 
	 * @param ref テーブルの参照
	 * @param record レコードのリスト
	 * @throws IllegalArgumentException 引数{@code ref}に{@code null}を与えた場合
	 */
	public synchronized void putRecord(EntityRef<? extends TableModel> ref, List<RecordModel> record) {
		Validate.notNull(ref);
		records.put(ref, Lists.newArrayList(record));
	}
	
	/**
	 * テーブルに対応するレコードのリストを削除する。
	 * 
	 * @param ref テーブルの参照
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public synchronized void removeRecord(EntityRef<? extends TableModel> ref) {
		Validate.notNull(ref);
		records.remove(ref);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * データセット名を設定する。
	 * 
	 * @param name データセット名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public EntityRef<? extends DefaultDataSetModel> toReference() {
		return new DefaultEntityRef<DefaultDataSetModel>(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
