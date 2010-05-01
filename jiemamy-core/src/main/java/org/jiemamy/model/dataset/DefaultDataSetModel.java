/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

import org.apache.commons.collections15.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.model.ValueObject;
import org.jiemamy.model.dbo.TableRef;

/**
 * INSERT文用データセット。
 * 
 * @author daisuke
 */
public final class DefaultDataSetModel implements DataSetModel, ValueObject {
	
	/** データセット名 */
	private final String name;
	
	/** レコード情報 */
	private final Map<TableRef, List<RecordModel>> records;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param name データセット名
	 * @param records レコード情報
	 */
	public DefaultDataSetModel(String name, Map<TableRef, List<RecordModel>> records) {
		this.name = name;
		this.records = MapUtils.unmodifiableMap(records);
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
	 * レコード情報を取得する。
	 * 
	 * @return レコード情報
	 */
	public Map<TableRef, List<RecordModel>> getRecords() {
		return records;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
