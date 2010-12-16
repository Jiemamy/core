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
package org.jiemamy.model.dataset;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.AbstractJiemamyEntity;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.utils.MutationMonitor;

/**
 * INSERT文用データセット。
 * 
 * @author daisuke
 */
public final class DefaultDataSetModel extends AbstractJiemamyEntity implements DataSetModel {
	
	/** データセット名 */
	private String name;
	
	/** レコード情報 */
	private Map<EntityRef<? extends TableModel>, List<RecordModel>> records = Maps.newHashMap();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultDataSetModel(UUID id) {
		super(id);
	}
	
	public Map<EntityRef<? extends TableModel>, List<RecordModel>> breachEncapsulationOfRecords() {
		return records;
	}
	
	@Override
	public DefaultDataSetModel clone() {
		DefaultDataSetModel clone = (DefaultDataSetModel) super.clone();
		clone.records = Maps.newHashMap();
		for (Entry<EntityRef<? extends TableModel>, List<RecordModel>> e : records.entrySet()) {
			List<RecordModel> cloneValue = Lists.newArrayList();
			for (RecordModel recordModel : e.getValue()) {
				cloneValue.add(recordModel);
			}
			clone.records.put(e.getKey(), cloneValue);
		}
		return clone;
	}
	
	/**
	 * データセット名を取得する。
	 * 
	 * @return データセット名
	 */
	public String getName() {
		return name;
	}
	
	public void getRecord(EntityRef<? extends TableModel> ref) {
		records.get(ref);
	}
	
	/**
	 * レコード情報を取得する。
	 * 
	 * @return レコード情報
	 */
	public Map<EntityRef<? extends TableModel>, List<RecordModel>> getRecords() {
		return MutationMonitor.monitor(Maps.newHashMap(records));
	}
	
	public void putRecord(EntityRef<? extends TableModel> ref, List<RecordModel> record) {
		records.put(ref, record);
	}
	
	public void removeRecord(EntityRef<? extends TableModel> ref) {
		records.remove(ref);
	}
	
	/**
	 * データセット名を設定する。
	 * 
	 * @param name データセット名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public EntityRef<DefaultDataSetModel> toReference() {
		return new DefaultEntityRef<DefaultDataSetModel>(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
