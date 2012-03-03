/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
 * {@link JmColumn}のファクトリクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmColumnBuilder extends AbstractEntityFactory<JmColumn> {
	
	String name;
	
	DataType type;
	
	private String defaultValue;
	
	private boolean notNull;
	
	
	/**
	 * インスタンスを生成する。
	 */
	public JmColumnBuilder() {
		// noop
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name カラム名
	 */
	public JmColumnBuilder(String name) {
		this.name = name;
	}
	
	public JmColumn build(UUID id) {
		JmColumn column = new JmColumn(id);
		column.setName(name);
		column.setDataType(type);
		column.setDefaultValue(defaultValue);
		column.setNotNull(notNull);
		return column;
	}
	
	/**
	 * デフォルト値を設定する。
	 * 
	 * @param defaultValue デフォルト値
	 * @return {@code this}
	 */
	public JmColumnBuilder defaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * カラム名を設定する。
	 * 
	 * @param name カラム名
	 * @return {@code this}
	 */
	public JmColumnBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public JmColumnBuilder notNull() {
		notNull = true;
		return this;
	}
	
	/**
	 * カラムのデータ型を設定する。
	 * 
	 * @param type データ型
	 * @return {@code this}
	 */
	public JmColumnBuilder type(DataType type) {
		this.type = type;
		return this;
	}
}
