/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import java.util.Collection;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.Column;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.column.ColumnParameterKey;
import org.jiemamy.model.column.DefaultColumnModel;
import org.jiemamy.model.column.DefaultColumnModelTest;
import org.jiemamy.model.constraint.DefaultCheckConstraintModelTest;
import org.jiemamy.model.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultNotNullConstraintModelTest;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModelTest;
import org.jiemamy.model.constraint.DefaultUniqueKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultUniqueKeyConstraintModelTest;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.KeyConstraintModel;
import org.jiemamy.model.parameter.Converters;
import org.jiemamy.utils.RandomUtil;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link DefaultTableModel}のテストクラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DefaultTableModelTest {
	
	/**
	 * 適当な {@link DefaultTableModel} のインスタンスを作る。
	 * 
	 * @return {@link DefaultTableModel}
	 */
	public static DefaultTableModel random() {
		DefaultTableModel model = new DefaultTableModel(UUID.randomUUID());
		model.setName(meta(3));
		model.setLogicalName(str());
		model.setDescription(str());
		
		// columnをランダムで追加
		int count = integer(5) + 1;
		for (int i = 0; i < count; i++) {
			model.store(DefaultColumnModelTest.random());
		}
		
		// PKをランダム追加
		if (bool() && model.getColumns().size() > 0) {
			DefaultPrimaryKeyConstraintModel pk = DefaultPrimaryKeyConstraintModelTest.random(model);
			if (pk.getKeyColumns().size() > 0) {
				model.store(pk);
			}
		}
		
		// UKをランダム追加
		if (bool() && model.getColumns().size() > 0) {
			DefaultUniqueKeyConstraintModel uk = DefaultUniqueKeyConstraintModelTest.random(model);
			if (uk.getKeyColumns().size() > 0) {
				model.store(uk);
			}
		}
		
		// CCをランダム追加
		count = integer(5) + 1;
		for (int i = 0; i < count; i++) {
			model.store(DefaultCheckConstraintModelTest.random());
		}
		
		// NNをランダム追加
		for (ColumnModel columnModel : model.getColumns()) {
			if (bool()) {
				model.store(DefaultNotNullConstraintModelTest.random(columnModel));
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
			DefaultTableModel model = new DefaultTableModel(null);
		} catch (IllegalArgumentException e) {
			// success
		}
		
		UUID id = UUID.randomUUID();
		DefaultTableModel t = new DefaultTableModel(id);
		t.setName("FOO");
		
		assertThat(t.getId(), is(id));
		assertThat(t.getName(), is("FOO"));
		
		t.setName("BAR");
		
		assertThat(t.getId(), is(id));
		assertThat(t.getName(), is("BAR"));
	}
	
	/**
	 * {@link DefaultTableModel#equals(Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_equals() throws Exception {
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		DefaultTableModel live1 = new DefaultTableModel(uuid1);
		DefaultTableModel live2 = new DefaultTableModel(uuid1);
		DefaultTableModel live3 = new DefaultTableModel(uuid2);
		
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
	 * {@link DefaultTableModel#clone()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_clone() throws Exception {
		DefaultTableModel original = new DefaultTableModel(UUIDUtil.valueOfOrRandom("a"));
		original.setName("name1");
		
		DefaultTableModel clone = original.clone();
		
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
		clone.store(new DefaultColumnModel(UUIDUtil.valueOfOrRandom("b")));
		assertThat(clone.getColumns().size(), is(1));
		assertThat(original.getColumns().size(), is(0));
		
		// originalにcolumnを追加してもcloneに影響しない
		original.store(new DefaultColumnModel(UUIDUtil.valueOfOrRandom("c")));
		assertThat(original.getColumns().size(), is(1));
		assertThat(clone.getColumns().size(), is(1));
	}
	
	/**
	 * {@link DefaultTableModel#getColumn(org.jiemamy.dddbase.EntityRef)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_getColumn() throws Exception {
		DefaultTableModel table = new Table("HOGE").build();
		ColumnModel foo = new Column("FOO").build();
		ColumnModel foo2 = new Column("FOO").build();
		ColumnModel bar = new Column("BAR").build();
		
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
		
		table.delete(bar.toReference());
		
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
	 * {@link DefaultTableModel#findDeclaringTable(Collection, ColumnModel)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_findDeclaringTable() throws Exception {
		ColumnModel a;
		ColumnModel b;
		ColumnModel c;
		ColumnModel d;
		
		// FORMAT-OFF
		TableModel t1 = new Table().whoseNameIs("ONE")
				.with(a = new Column().whoseNameIs("A").build())
				.with(b = new Column().whoseNameIs("B").build())
				.build();
		TableModel t2 = new Table().whoseNameIs("TWO")
				.with(c = new Column().whoseNameIs("C").build())
				.with(d = new Column().whoseNameIs("D").build())
				.build();
		// FORMAT-ON
		
		ctx.store(t1);
		ctx.store(t2);
		
		Collection<TableModel> tables = ctx.getTables();
		
		assertThat(DefaultTableModel.findDeclaringTable(tables, a), is(t1));
		assertThat(DefaultTableModel.findDeclaringTable(tables, b), is(t1));
		assertThat(DefaultTableModel.findDeclaringTable(tables, c), is(t2));
		assertThat(DefaultTableModel.findDeclaringTable(tables, d), is(t2));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_column_lifecycle3() throws Exception {
		DefaultTableModel table1 = new Table().build();
		DefaultTableModel table2 = new Table().build();
		
		DefaultColumnModel column = new Column().build();
		
		table1.store(column);
		table1.delete(column.toReference());
		table2.store(column);
		table2.delete(column.toReference());
		
		table1.store(column);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11() throws Exception {
		ColumnModel b;
		ColumnModel c;
		ColumnModel d;
		ColumnModel e;
		ForeignKeyConstraintModel fk21;
		ForeignKeyConstraintModel fk32;
		KeyConstraintModel pk1;
		KeyConstraintModel pk2;
		
		// FORMAT-OFF
		TableModel t1 = new Table("ONE")
				.with(new Column("A").build())
				.with(b = new Column("B").build())
				.with(pk1 = DefaultPrimaryKeyConstraintModel.of(b))
				.build();
		TableModel t2 = new Table("TWO")
				.with(c = new Column("C").build())
				.with(d = new Column("D").build())
				.with(pk2 = DefaultPrimaryKeyConstraintModel.of(d))
				.with(fk21 = DefaultForeignKeyConstraintModel.of(c, b))
				.build();
		TableModel t3 = new Table("THREE")
				.with(e = new Column("E").build())
				.with(new Column("F").build())
				.with(fk32 = DefaultForeignKeyConstraintModel.of(e, d))
				.build();
		
		ctx.store(t1);
		ctx.store(t2);
		ctx.store(t3);
		
		assertThat(fk21.findDeclaringTable(ctx.getTables()), is(t2));
		assertThat(fk32.findDeclaringTable(ctx.getTables()), is(t3));
		assertThat(fk21.findReferenceTable(ctx.getTables()), is(t1));
		assertThat(fk32.findReferenceTable(ctx.getTables()), is(t2));
		assertThat(fk21.findReferencedKeyConstraint(ctx.getDatabaseObjects()), is(pk1));
		assertThat(fk32.findReferencedKeyConstraint(ctx.getDatabaseObjects()), is(pk2));
		// FORMAT-ON
	}
}
