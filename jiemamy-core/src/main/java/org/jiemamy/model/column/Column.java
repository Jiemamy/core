/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.model.column;

import java.util.UUID;

import org.jiemamy.dddbase.AbstractEntityFactory;
import org.jiemamy.model.datatype.DataType;

/**
 * {@link DefaultColumnModel}のファクトリクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class Column extends AbstractEntityFactory<DefaultColumnModel> {
	
	String name;
	
	DataType type;
	

	/**
	 * インスタンスを生成する。
	 */
	public Column() {
		// noop
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name カラム名
	 */
	public Column(String name) {
		this.name = name;
	}
	
	public DefaultColumnModel build(UUID id) {
		DefaultColumnModel columnModel = new DefaultColumnModel(id);
		columnModel.setName(name);
		columnModel.setDataType(type);
		return columnModel;
	}
	
	/**
	 * カラム名を設定する。
	 * 
	 * @param name カラム名
	 * @return this
	 */
	public Column whoseNameIs(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * カラムのデータ型を設定する。
	 * 
	 * @param type データ型
	 * @return this
	 */
	public Column whoseTypeIs(DataType type) {
		this.type = type;
		return this;
	}
	
}
