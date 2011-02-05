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
package org.jiemamy.model.table;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.jiemamy.dddbase.AbstractEntityFactory;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.constraint.JmConstraint;

/**
 * {@link SimpleJmTable}のファクトリクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmTableBuilder extends AbstractEntityFactory<SimpleJmTable> {
	
	List<JmColumn> columns = Lists.newArrayList();
	
	List<JmConstraint> constraints = Lists.newArrayList();
	
	String name;
	

	/**
	 * インスタンスを生成する。
	 */
	public JmTableBuilder() {
		// noop
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name テーブル名
	 */
	public JmTableBuilder(String name) {
		this.name = name;
	}
	
	/**
	 * ファクトリの状態に基づいて {@link SimpleJmTable}のインスタンスを生成する。
	 * 
	 * @param id  ENTITY ID
	 * @return 新しい {@link SimpleJmTable}のインスタンス
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public SimpleJmTable build(UUID id) {
		SimpleJmTable table = new SimpleJmTable(id);
		table.setName(name);
		for (JmColumn column : columns) {
			table.store(column);
		}
		for (JmConstraint constraint : constraints) {
			table.store(constraint);
		}
		return table;
	}
	
	/**
	 * テーブル名を設定する。
	 * 
	 * @param name テーブル名
	 * @return this
	 */
	public JmTableBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * テーブルに作成するカラムを追加する。
	 * 
	 * @param columns カラム
	 * @return this
	 */
	public JmTableBuilder with(JmColumn... columns) {
		for (JmColumn column : columns) {
			with(column);
		}
		return this;
	}
	
	/**
	 * テーブルに作成するカラムを追加する。
	 * 
	 * @param column カラム
	 * @return this
	 */
	public JmTableBuilder with(JmColumn column) {
		columns.add(column);
		return this;
	}
	
	/**
	 * テーブルに作成する制約を追加する。
	 * 
	 * @param constraint 制約
	 * @return this
	 */
	public JmTableBuilder with(JmConstraint constraint) {
		constraints.add(constraint);
		return this;
	}
}
