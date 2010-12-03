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
package org.jiemamy.model.dbo.index;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.EntityRef;
import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.dbo.AbstractDatabaseObjectModel;

/**
 * インデックスモデル。
 * 
 * @author daisuke
 */
public class DefaultIndexModel extends AbstractDatabaseObjectModel implements IndexModel {
	
	/** ユニークインデックスか否か */
	private boolean unique;
	
	/** インデックスカラムのリスト */
	private List<IndexColumnModel> indexColumns;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultIndexModel(UUID id) {
		super(id);
	}
	
	public List<IndexColumnModel> getIndexColumns() {
		assert indexColumns != null;
		return new ArrayList<IndexColumnModel>(indexColumns);
	}
	
	@Override
	public String getLogicalName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public EntityRef<IndexModel> getReference() {
		return new DefaultEntityRef<IndexModel>(this);
	}
	
	public boolean isUnique() {
		return unique;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
