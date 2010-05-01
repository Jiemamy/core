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
package org.jiemamy.model.index;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.model.AbstractJiemamyElement;

/**
 * インデックスモデル。
 * 
 * @author daisuke
 */
public class DefaultIndexModel extends AbstractJiemamyElement implements IndexModel {
	
	/** インデックス名 */
	private String name;
	
	/** ユニークインデックスか否か */
	private boolean unique;
	
	/** インデックスカラムのリスト */
	private List<IndexColumnModel> indexColumns;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 */
	public DefaultIndexModel(UUID id) {
		super(id);
	}
	
	public List<IndexColumnModel> getIndexColumns() {
		assert indexColumns != null;
		return indexColumns;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isUnique() {
		return unique;
	}
	
	/**
	 * インデックスカラムのリストを設定する。
	 * 
	 * @param columns インデックスカラムのリスト
	 */
	public void setColumns(List<IndexColumnModel> columns) {
		indexColumns = columns;
	}
	
	/**
	 * 
	 * インデックス名を設定する
	 * 
	 * @param name インデックス名
	 * @since 0.3
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * ユニークインデックスか否かを設定する
	 * 
	 * @param unique true：ユニーク
	 * @since 0.3
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
