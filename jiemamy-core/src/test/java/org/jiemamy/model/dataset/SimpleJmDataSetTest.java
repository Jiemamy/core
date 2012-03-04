/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.script.ScriptString;

/**
 * {@link SimpleJmDataSet}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmDataSetTest {
	
	/**
	 * 適当な {@link JiemamyContext} のインスタンスを作る。
	 * 
	 * @param tables データセット作製対象のテーブルの集合
	 * @return {@link SimpleJmDataSet}
	 */
	public static SimpleJmDataSet random(Collection<JmTable> tables) {
		SimpleJmDataSet model = new SimpleJmDataSet();
		model.setName(strNullable());
		for (JmTable table : tables) {
			if (bool()) {
				int size = integer(5) + 1;
				List<JmRecord> records = Lists.newArrayListWithCapacity(size);
				for (int i = 0; i < size; i++) {
					records.add(SimpleJmRecordTest.random(table.getColumns()));
				}
				model.putRecord(table.toReference(), records);
			}
		}
		return model;
	}
	
	
	private JmTable hoge;
	
	private JmTable fuga;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		hoge = new JmTable();
		hoge.setName("HOGE");
		hoge.add(new JmColumnBuilder("FOO").type(SimpleDataType.of(RawTypeCategory.INTEGER)).build());
		hoge.add(new JmColumnBuilder("BAR").type(SimpleDataType.of(RawTypeCategory.INTEGER)).build());
		hoge.add(new JmColumnBuilder("BAZ").type(SimpleDataType.of(RawTypeCategory.INTEGER)).build());
		
		fuga = new JmTable();
		fuga.setName("FUGA");
		fuga.add(new JmColumnBuilder("QUX").type(SimpleDataType.of(RawTypeCategory.INTEGER)).build());
		fuga.add(new JmColumnBuilder("QUUX").type(SimpleDataType.of(RawTypeCategory.INTEGER)).build());
		fuga.add(new JmColumnBuilder("CORGE").type(SimpleDataType.of(RawTypeCategory.INTEGER)).build());
		fuga.add(new JmColumnBuilder("GRAULT").type(SimpleDataType.of(RawTypeCategory.INTEGER)).build());
		
	}
	
	/**
	 * データを追加して維持されていることを確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_データを追加して維持されていることを確認() throws Exception {
		SimpleJmDataSet dataSet = new SimpleJmDataSet();
		Map<EntityRef<? extends JmColumn>, ScriptString> map = Maps.newHashMap();
		dataSet.setName("hogehoge");
		
		assertThat(dataSet.getRecords().size(), is(0));
		
		map.put(hoge.getColumn("FOO").toReference(), new ScriptString("1"));
		map.put(hoge.getColumn("BAR").toReference(), new ScriptString("11"));
		map.put(hoge.getColumn("BAZ").toReference(), new ScriptString("111"));
		dataSet.addRecord(hoge.toReference(), new SimpleJmRecord(map));
		
		assertThat(dataSet.getRecords().size(), is(1));
		
		map.put(hoge.getColumn("FOO").toReference(), new ScriptString("2"));
		map.put(hoge.getColumn("BAR").toReference(), new ScriptString("22"));
		map.put(hoge.getColumn("BAZ").toReference(), new ScriptString("222"));
		dataSet.addRecord(hoge.toReference(), new SimpleJmRecord(map));
		
		assertThat(dataSet.getRecords().size(), is(1));
		
		map.put(fuga.getColumn("QUX").toReference(), new ScriptString("3"));
		map.put(fuga.getColumn("QUUX").toReference(), new ScriptString("33"));
//		map.put(fuga.getColumn("CORGE").toReference(), new ScriptString("333"));
		map.put(fuga.getColumn("GRAULT").toReference(), new ScriptString("3333"));
		dataSet.addRecord(fuga.toReference(), new SimpleJmRecord(map));
		
		assertThat(dataSet.getRecords().size(), is(2));
		
		assertThat(dataSet.getRecord(hoge.toReference()).size(), is(2));
		assertThat(dataSet.getRecord(fuga.toReference()).size(), is(1));
	}
}
