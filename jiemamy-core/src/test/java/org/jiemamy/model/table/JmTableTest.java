/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/05/02
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.jiemamy.utils.RandomUtil.meta;
import static org.jiemamy.utils.RandomUtil.str;
import static org.jiemamy.utils.RandomUtil.strNotEmpty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.ColumnParameterKey;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnTest;
import org.jiemamy.model.constraint.JmCheckConstraintTest;
import org.jiemamy.model.constraint.JmNotNullConstraintTest;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraintTest;
import org.jiemamy.model.constraint.JmUniqueKeyConstraint;
import org.jiemamy.model.constraint.JmUniqueKeyConstraintTest;
import org.jiemamy.model.parameter.Converters;
import org.jiemamy.utils.RandomUtil;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link JmTable}のテストクラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class JmTableTest {
	
	/**
	 * 適当な {@link JmTable} のインスタンスを作る。
	 * 
	 * @return {@link JmTable}
	 */
	public static JmTable random() {
		JmTable model = new JmTable();
		model.setName(meta(3));
		model.setLogicalName(str());
		model.setDescription(str());
		
		// columnをランダムで追加
		int count = integer(5) + 1;
		for (int i = 0; i < count; i++) {
			model.store(JmColumnTest.random());
		}
		
		// PKをランダム追加
		if (bool() && model.getColumns().size() > 0) {
			JmPrimaryKeyConstraint pk = JmPrimaryKeyConstraintTest.random(model);
			if (pk.getKeyColumns().size() > 0) {
				model.store(pk);
			}
		}
		
		// UKをランダム追加
		if (bool() && model.getColumns().size() > 0) {
			JmUniqueKeyConstraint uk = JmUniqueKeyConstraintTest.random(model);
			if (uk.getKeyColumns().size() > 0) {
				model.store(uk);
			}
		}
		
		// CCをランダム追加
		count = integer(5) + 1;
		for (int i = 0; i < count; i++) {
			model.store(JmCheckConstraintTest.random());
		}
		
		// NNをランダム追加
		for (JmColumn column : model.getColumns()) {
			if (bool()) {
				model.store(JmNotNullConstraintTest.random(column));
			}
		}
		
		// TODO FKをランダム追加
		
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
	
	
	private JiemamyContext ctx;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx = new JiemamyContext();
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
	 * オブジェクト生成テスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_create() throws Exception {
		try {
			@SuppressWarnings("unused")
			JmTable model = new JmTable(null);
		} catch (IllegalArgumentException e) {
			// success
		}
		
		UUID id = UUID.randomUUID();
		JmTable t = new JmTable(id);
		t.setName("FOO");
		
		assertThat(t.getId(), is(id));
		assertThat(t.getName(), is("FOO"));
		
		t.setName("BAR");
		
		assertThat(t.getId(), is(id));
		assertThat(t.getName(), is("BAR"));
	}
	
	/**
	 * {@link JmTable#equals(Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_equals() throws Exception {
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		JmTable live1 = new JmTable(uuid1);
		JmTable live2 = new JmTable(uuid1);
		JmTable live3 = new JmTable(uuid2);
		
		assertThat(live1.equals(live1), is(true));
		assertThat(live1.equals(live2), is(true));
		assertThat(live1.equals(live3), is(false));
		
		assertThat(live2.equals(live1), is(true));
		assertThat(live2.equals(live2), is(true));
		assertThat(live2.equals(live3), is(false));
		
		assertThat(live3.equals(live1), is(false));
		assertThat(live3.equals(live2), is(false));
		assertThat(live3.equals(live3), is(true));
	}
	
	/**
	 * {@link JmTable#clone()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_clone() throws Exception {
		JmTable original = new JmTable(UUIDUtil.valueOfOrRandom("a"));
		original.setName("name1");
		
		JmTable clone = original.clone();
		
		// cloneはoriginalと同じステートのはず
		assertThat(clone.getName(), is("name1"));
		assertThat(clone.getColumns().size(), is(0));
		
		// originalを書き換えてもcloneには影響しない
		original.setName("name2");
		assertThat(original.getName(), is("name2"));
		assertThat(clone.getName(), is("name1"));
		
		// cloneを書き換えてもoriginalには影響しない
		clone.setName("name3");
		assertThat(original.getName(), is("name2"));
		assertThat(clone.getName(), is("name3"));
		
		// cloneにcolumnを追加してもoriginalに影響しない
		clone.store(new JmColumn(UUIDUtil.valueOfOrRandom("b")));
		assertThat(clone.getColumns().size(), is(1));
		assertThat(original.getColumns().size(), is(0));
		
		// originalにcolumnを追加してもcloneに影響しない
		original.store(new JmColumn(UUIDUtil.valueOfOrRandom("c")));
		assertThat(original.getColumns().size(), is(1));
		assertThat(clone.getColumns().size(), is(1));
	}
	
	/**
	 * {@link JmTable#getColumn(org.jiemamy.dddbase.EntityRef)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_getColumn() throws Exception {
		JmTable table = new JmTableBuilder("HOGE").build();
		JmColumn foo = new JmColumnBuilder("FOO").build();
		JmColumn foo2 = new JmColumnBuilder("FOO").build();
		JmColumn bar = new JmColumnBuilder("BAR").build();
		
		// 最初はcolumn数0のはず
		assertThat(table.getColumns().size(), is(0));
		
		table.store(foo);
		table.store(bar);
		
		// 2つstoreした
		assertThat(table.getColumns().size(), is(2));
		
		ctx.store(table);
		
		// contextにstoreしても特に変わらず
		assertThat(table.getColumns().size(), is(2));
		
		// 名前でcolumnを取得できる（sameinstanceではなく、IDが同じなだけ）
		assertThat(table.getColumn("FOO"), is(foo));
		assertThat(table.getColumn("BAR"), is(bar));
		
		table.deleteColumn(bar.toReference());
		
		// 消したら1つになる
		assertThat(table.getColumns().size(), is(1));
		
		// FOOの方は同じように取得できる
		assertThat(table.getColumn("FOO"), is(foo));
		
		// BARはColumnNotFoundExceptionとなる
		try {
			table.getColumn("BAR");
			fail();
		} catch (ColumnNotFoundException e) {
			// success
		}
		
		// FOOと別IDだが同名のカラムをstoreした
		table.store(foo2);
		
		// 名前では解決できなくなる
		try {
			table.getColumn("FOO");
			fail();
		} catch (TooManyColumnsFoundException e) {
			// 見つかったカラムは2つ
			assertThat(e.getColumns().size(), is(2));
			// fooとfoo2が見つかったはず
			assertThat(e.getColumns(), hasItems(foo, foo2));
		}
	}
	
	/**
	 * カラムの付け替えが成功すること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_column_lifecycle3() throws Exception {
		JmTable table1 = new JmTableBuilder().build();
		JmTable table2 = new JmTableBuilder().build();
		
		JmColumn column = new JmColumnBuilder().build();
		
		table1.store(column);
		table1.deleteColumn(column.toReference());
		table2.store(column);
		table2.deleteColumn(column.toReference());
		
		table1.store(column);
	}
}
