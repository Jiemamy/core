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
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.model.AbstractJiemamyElement;
import org.jiemamy.model.entity.TableRef;

/**
 * INSERT文用データセット。
 * 
 * @author daisuke
 */
public class DefaultDataSetModel extends AbstractJiemamyElement implements DataSetModel {
	
	/** データセット名 */
	private String name;
	
	/** レコード情報 */
	private Map<TableRef, List<RecordModel>> records;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultDataSetModel(UUID id) {
		super(id);
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
	
	/**
	 * データセット名を設定する。
	 * 
	 * @param name データセット名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * レコード情報を設定する。
	 * 
	 * @param records レコード情報
	 */
	public void setRecords(Map<TableRef, List<RecordModel>> records) {
		this.records = records;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
