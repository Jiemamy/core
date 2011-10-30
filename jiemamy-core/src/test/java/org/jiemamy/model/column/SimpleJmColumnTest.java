/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/14
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.jiemamy.utils.RandomUtil.meta;
import static org.jiemamy.utils.RandomUtil.str;
import static org.jiemamy.utils.RandomUtil.strNotEmpty;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleDataTypeTest;
import org.jiemamy.model.parameter.Converters;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;
import org.jiemamy.utils.RandomUtil;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link SimpleJmColumn}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmColumnTest {
	
	/**
	 * 適当な {@link SimpleJmColumn} のインスタンスを作る。
	 * 
	 * @return {@link SimpleJmColumn}
	 */
	public static SimpleJmColumn random() {
		SimpleJmColumn model = new SimpleJmColumn();
		model.setName(meta(3));
		model.setLogicalName(str());
		model.setDescription(str());
		model.setDefaultValue(str());
		model.setDataType(SimpleDataTypeTest.random());
		
		// 適当にパラメータを追加する
		int integer = integer(5);
		for (int i = 0; i < integer; i++) {
			int p = RandomUtil.integer(2);
			if (p == 0) {
				model.putParam(new ColumnParameterKey<Boolean>(Converters.BOOLEAN, strNotEmpty()), bool());
			} else if (p == 1) {
				model.putParam(new ColumnParameterKey<Integer>(Converters.INTEGER, strNotEmpty()), integer(100));
			} else {
				model.putParam(new ColumnParameterKey<String>(Converters.STRING, strNotEmpty()), str());
			}
		}
		
		return model;
	}
	
	
	private SimpleJmColumn column;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		column = new SimpleJmColumn(UUIDUtil.valueOfOrRandom("a"));
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		UUIDUtil.clear();
	}
	
	/**
	 * 作ったインスタンスの挙動チェック。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_作ったインスタンスの挙動チェック() throws Exception {
		DataType type = SimpleDataType.of(RawTypeCategory.INTEGER);
		
		assertThat(column.getName(), is(nullValue()));
		assertThat(column.getLogicalName(), is(nullValue()));
		assertThat(column.getDataType(), is(nullValue()));
		assertThat(column.getDefaultValue(), is(nullValue()));
		assertThat(column.getDescription(), is(nullValue()));
		assertThat(column.getId(), is(UUIDUtil.valueOfOrRandom("a")));
		assertThat(column.getSubEntities().size(), is(0));
		
		column.setName("name");
		column.setLogicalName("logicalName");
		column.setDataType(type);
		column.setDefaultValue("'defaultValue'");
		column.setDescription("description");
		
		assertThat(column.getName(), is("name"));
		assertThat(column.getLogicalName(), is("logicalName"));
		assertThat(column.getDataType(), is(type));
		assertThat(column.getDefaultValue(), is("'defaultValue'"));
		assertThat(column.getDescription(), is("description"));
	}
	
	/**
	 * {@link SimpleJmColumn#toReference()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_toReference() throws Exception {
		EntityRef<? extends JmColumn> columnRef = column.toReference();
		assertThat(columnRef, is(notNullValue()));
		assertThat(columnRef.getReferentId(), is(UUIDUtil.valueOfOrRandom("a")));
	}
	
	/**
	 * {@link JmColumn#findDeclaringTable(Iterable)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_findDeclaringTable() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		JmColumn a;
		JmColumn b;
		JmColumn c;
		JmColumn d;
		
		// FORMAT-OFF
		JmTable t1 = new JmTableBuilder().name("ONE")
				.with(a = new JmColumnBuilder().name("A").build())
				.with(b = new JmColumnBuilder().name("B").build())
				.build();
		JmTable t2 = new JmTableBuilder().name("TWO")
				.with(c = new JmColumnBuilder().name("C").build())
				.with(d = new JmColumnBuilder().name("D").build())
				.build();
		// FORMAT-ON
		
		ctx.store(t1);
		ctx.store(t2);
		
		Collection<JmTable> tables = ctx.getTables();
		
		assertThat(a.findDeclaringTable(tables), is(t1));
		assertThat(b.findDeclaringTable(tables), is(t1));
		assertThat(c.findDeclaringTable(tables), is(t2));
		assertThat(d.findDeclaringTable(tables), is(t2));
	}
	
	/**
	 * {@link SimpleJmColumn#clone()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_clone() throws Exception {
		column.setName("name2");
		SimpleJmColumn clone = column.clone();
		
		// cloneはcolumnとは別のインスタンスだが、同じ値を持つ
		assertThat(clone, is(not(sameInstance(column))));
		assertThat(clone, is(equalTo(column)));
		assertThat(clone.getName(), is("name2"));
		
		column.setName("name3");
		
		// cloneはcolumnの変更に影響されない
		assertThat(column.getName(), is("name3"));
		assertThat(clone.getName(), is("name2"));
	}
	
}
