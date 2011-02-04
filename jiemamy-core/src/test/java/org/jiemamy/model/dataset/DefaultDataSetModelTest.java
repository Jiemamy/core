/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/15
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

import static org.hamcrest.Matchers.is;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.jiemamy.utils.RandomUtil.strNullable;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.Column;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultDataType;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.script.ScriptString;

/**
 * {@link DefaultDataSetModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultDataSetModelTest {
	
	/**
	 * 適当な {@link JiemamyContext} のインスタンスを作る。
	 * 
	 * @param tables データセット作製対象のテーブルの集合
	 * @return {@link DefaultDataSetModel}
	 */
	public static DefaultDataSetModel random(Collection<TableModel> tables) {
		DefaultDataSetModel model = new DefaultDataSetModel(UUID.randomUUID());
		model.setName(strNullable());
		for (TableModel tableModel : tables) {
			if (bool()) {
				int size = integer(5) + 1;
				List<RecordModel> records = Lists.newArrayListWithCapacity(size);
				for (int i = 0; i < size; i++) {
					records.add(DefaultRecordModelTest.random(tableModel.getColumns()));
				}
				model.putRecord(tableModel.toReference(), records);
			}
		}
		return model;
	}
	

	private DefaultTableModel hoge;
	
	private DefaultTableModel fuga;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		hoge = new DefaultTableModel(UUID.randomUUID());
		hoge.setName("HOGE");
		hoge.store(new Column("FOO").type(DefaultDataType.of(DataTypeCategory.INTEGER)).build());
		hoge.store(new Column("BAR").type(DefaultDataType.of(DataTypeCategory.INTEGER)).build());
		hoge.store(new Column("BAZ").type(DefaultDataType.of(DataTypeCategory.INTEGER)).build());
		
		fuga = new DefaultTableModel(UUID.randomUUID());
		fuga.setName("FUGA");
		fuga.store(new Column("QUX").type(DefaultDataType.of(DataTypeCategory.INTEGER)).build());
		fuga.store(new Column("QUUX").type(DefaultDataType.of(DataTypeCategory.INTEGER)).build());
		fuga.store(new Column("CORGE").type(DefaultDataType.of(DataTypeCategory.INTEGER)).build());
		fuga.store(new Column("GRAULT").type(DefaultDataType.of(DataTypeCategory.INTEGER)).build());
		
	}
	
	/**
	 * データを追加して維持されていることを確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_データを追加して維持されていることを確認() throws Exception {
		DefaultDataSetModel dataSet = new DefaultDataSetModel(UUID.randomUUID());
		Map<EntityRef<? extends ColumnModel>, ScriptString> map = Maps.newHashMap();
		dataSet.setName("hogehoge");
		
		assertThat(dataSet.getRecords().size(), is(0));
		
		map.put(hoge.getColumn("FOO").toReference(), new ScriptString("1"));
		map.put(hoge.getColumn("BAR").toReference(), new ScriptString("11"));
		map.put(hoge.getColumn("BAZ").toReference(), new ScriptString("111"));
		dataSet.addRecord(hoge.toReference(), new DefaultRecordModel(map));
		
		assertThat(dataSet.getRecords().size(), is(1));
		
		map.put(hoge.getColumn("FOO").toReference(), new ScriptString("2"));
		map.put(hoge.getColumn("BAR").toReference(), new ScriptString("22"));
		map.put(hoge.getColumn("BAZ").toReference(), new ScriptString("222"));
		dataSet.addRecord(hoge.toReference(), new DefaultRecordModel(map));
		
		assertThat(dataSet.getRecords().size(), is(1));
		
		map.put(fuga.getColumn("QUX").toReference(), new ScriptString("3"));
		map.put(fuga.getColumn("QUUX").toReference(), new ScriptString("33"));
//		map.put(fuga.getColumn("CORGE").toReference(), new ScriptString("333"));
		map.put(fuga.getColumn("GRAULT").toReference(), new ScriptString("3333"));
		dataSet.addRecord(fuga.toReference(), new DefaultRecordModel(map));
		
		assertThat(dataSet.getRecords().size(), is(2));
		
		assertThat(dataSet.getRecord(hoge.toReference()).size(), is(2));
		assertThat(dataSet.getRecord(fuga.toReference()).size(), is(1));
	}
}
