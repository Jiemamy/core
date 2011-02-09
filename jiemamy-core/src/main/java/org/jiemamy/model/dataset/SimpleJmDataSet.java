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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.dddbase.AbstractOrderedEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.table.JmTable;

/**
 * INSERT文用データセット。
 * 
 * @author daisuke
 */
public final class SimpleJmDataSet extends AbstractOrderedEntity implements JmDataSet {
	
	/** データセット名 */
	private String name;
	
	/** レコード情報 */
	private Map<EntityRef<? extends JmTable>, List<JmRecord>> records = Maps.newHashMap();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public SimpleJmDataSet() {
		this(UUID.randomUUID());
	}
	
	/**
	* インスタンスを生成する。
	* 
	* @param id ENTITY ID
	*/
	public SimpleJmDataSet(UUID id) {
		super(id);
	}
	
	/**
	 * テーブルに対応するレコードを追加する。
	 * 
	 * @param tableRef テーブルの参照
	 * @param record レコード
	 * @throws IllegalArgumentException 引数{@code tableRef}に{@code null}を与えた場合
	 */
	public synchronized void addRecord(EntityRef<? extends JmTable> tableRef, JmRecord record) {
		Validate.notNull(tableRef);
		Validate.notNull(record);
		if (records.containsKey(tableRef) == false) {
			records.put(tableRef, new ArrayList<JmRecord>());
		}
		records.get(tableRef).add(record);
	}
	
	@Override
	public synchronized SimpleJmDataSet clone() {
		SimpleJmDataSet clone = (SimpleJmDataSet) super.clone();
		
		Map<EntityRef<? extends JmTable>, List<JmRecord>> cloneMap = Maps.newHashMapWithExpectedSize(records.size());
		for (Entry<EntityRef<? extends JmTable>, List<JmRecord>> entry : records.entrySet()) {
			List<JmRecord> value = entry.getValue();
			cloneMap.put(entry.getKey(), CloneUtil.cloneValueArrayList(value));
		}
		synchronized (clone) {
			clone.records = cloneMap;
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
	
	/**
	 * テーブルに対応するレコードのリストを取得する。
	 * 
	 * @param tableRef テーブルの参照
	 * @return  レコードのリスト、レコードが存在しない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public synchronized List<JmRecord> getRecord(EntityRef<? extends JmTable> tableRef) {
		Validate.notNull(tableRef);
		return records.get(tableRef);
	}
	
	/**
	 * レコード情報を取得する。
	 * 
	 * @return レコード情報
	 */
	public synchronized Map<EntityRef<? extends JmTable>, List<JmRecord>> getRecords() {
		return MutationMonitor.monitor(Maps.newHashMap(records));
	}
	
	/**
	 * テーブルに対応するレコードのリストを設定する。
	 * 
	 * @param tableRef テーブルの参照
	 * @param records レコードのリスト
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public synchronized void putRecord(EntityRef<? extends JmTable> tableRef, List<JmRecord> records) {
		Validate.notNull(tableRef);
		Validate.noNullElements(records);
		this.records.put(tableRef, Lists.newArrayList(records));
	}
	
	/**
	 * テーブルに対応するレコードのリストを削除する。
	 * 
	 * @param tableRef テーブルの参照
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public synchronized void removeRecord(EntityRef<? extends JmTable> tableRef) {
		Validate.notNull(tableRef);
		records.remove(tableRef);
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
	public EntityRef<? extends SimpleJmDataSet> toReference() {
		return new DefaultEntityRef<SimpleJmDataSet>(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
