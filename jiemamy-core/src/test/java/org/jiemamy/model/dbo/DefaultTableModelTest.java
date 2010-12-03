/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.dbo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.UUID;

import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyCore;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.attribute.Column;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;

/**
 * {@link DefaultTableModel}のテストクラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DefaultTableModelTest {
	
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
		DefaultTableModel live1 = new Table().build(uuid1);
		DefaultTableModel live2 = new Table().build(uuid1);
		DefaultTableModel live3 = new Table().build(uuid2);
		DefaultTableModel dead = new Table().build();
		
		live1.setName("FOO");
		live2.setName("BAR");
		live2.setName("BAZ");
		live2.setName("QUX");
		
		assertThat(live1.equals(live1), is(true));
		assertThat(live1.equals(live2), is(true));
		assertThat(live1.equals(live3), is(false));
		assertThat(live1.equals(dead), is(false));
		
		assertThat(live2.equals(live1), is(true));
		assertThat(live2.equals(live2), is(true));
		assertThat(live2.equals(live3), is(false));
		assertThat(live2.equals(dead), is(false));
		
		assertThat(live3.equals(live1), is(false));
		assertThat(live3.equals(live2), is(false));
		assertThat(live3.equals(live3), is(true));
		assertThat(live3.equals(dead), is(false));
		
		assertThat(dead.equals(live1), is(false));
		assertThat(dead.equals(live2), is(false));
		assertThat(dead.equals(live3), is(false));
		assertThat(dead.equals(dead), is(true));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_getTables() throws Exception {
		JiemamyCore core = new JiemamyContext().getCore();
		// FORMAT-OFF
		TableModel t1 = new Table().whoseNameIs("ONE")
				.with(new Column().whoseNameIs("A").build())
				.with(new Column().whoseNameIs("B").build())
				.build();
		TableModel t2 = new Table().whoseNameIs("TWO")
				.with(new Column().whoseNameIs("C").build())
				.with(new Column().whoseNameIs("D").build()).build();
		// FORMAT-ON
		
		core.add(t1);
		core.add(t2);
		assertThat(DefaultTableModel.findTables(core.getDatabaseObjects()).size(), is(2));
		assertThat(DefaultTableModel.findTables(core.getDatabaseObjects()), hasItem(t1));
		assertThat(DefaultTableModel.findTables(core.getDatabaseObjects()), hasItem(t2));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_findDeclaringTable() throws Exception {
		JiemamyCore core = new JiemamyContext().getCore();
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
		
		core.add(t1);
		core.add(t2);
		
		Collection<TableModel> tables = DefaultTableModel.findTables(core.getDatabaseObjects());
		
		assertThat(DefaultTableModel.findDeclaringTable(tables, a.getReference()), is(t1));
		assertThat(DefaultTableModel.findDeclaringTable(tables, b.getReference()), is(t1));
		assertThat(DefaultTableModel.findDeclaringTable(tables, c.getReference()), is(t2));
		assertThat(DefaultTableModel.findDeclaringTable(tables, d.getReference()), is(t2));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_lifecycle2() throws Exception {
		JiemamyCore core1 = new JiemamyContext().getCore();
		JiemamyCore core2 = new JiemamyContext().getCore();
		
		DefaultTableModel table = new Table().build();
		core1.add(table);
		core1.remove(table);
		core2.add(table);
		core2.remove(table);
		
		core1.add(table);
		try {
			core2.add(table);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
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
		
		table1.addColumn(column);
		table1.removeColumn(column);
		table2.addColumn(column);
		table2.removeColumn(column);
		
		table1.addColumn(column);
		try {
			table2.addColumn(column);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test10_getColumn() throws Exception {
		JiemamyCore core = new JiemamyContext().getCore();
		
		DefaultTableModel table = new Table("HOGE").build();
		DefaultColumnModel foo = new Column("FOO").build();
		DefaultColumnModel foo2 = new Column("FOO").build();
		DefaultColumnModel bar = new Column("BAR").build();
		
		assertThat(table.getColumns().size(), is(0));
		
		table.addColumn(foo);
		table.addColumn(bar);
		core.add(table);
		core.add(foo);
		core.add(bar);
		
		assertThat(table.getColumns().size(), is(2));
		assertThat(table.getColumn("FOO", core), is(foo.getReference()));
		assertThat(table.getColumn("BAR", core), is(bar.getReference()));
		
		table.removeColumn(bar);
		
		assertThat(table.getColumns().size(), is(1));
		assertThat(table.getColumn("FOO", core), is(foo.getReference()));
		
		try {
			table.getColumn("BAR", core);
			fail();
		} catch (ColumnNotFoundException e) {
			// success
		}
		
		table.addColumn(foo2);
		core.add(foo2);
		try {
			table.getColumn("FOO", core);
			fail();
		} catch (TooManyColumnsFoundException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		JiemamyCore core = new JiemamyContext().getCore();
		ColumnModel b;
		ColumnModel c;
		ColumnModel d;
		ColumnModel e;
		ForeignKeyConstraintModel fk12;
		ForeignKeyConstraintModel fk23;
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
				.with(fk12 = DefaultForeignKeyConstraintModel.of(c, b))
				.build();
		TableModel t3 = new Table("THREE")
				.with(e = new Column("E").build())
				.with(new Column("F").build())
				.with(fk23 = DefaultForeignKeyConstraintModel.of(e, d))
				.build();
		
		core.add(t1);
		core.add(t2);
		core.add(t3);
		
		assertThat(DefaultTableModel.findReferencedDatabaseObject(core.getDatabaseObjects(), fk12), is((DatabaseObjectModel) t1));
		assertThat(DefaultTableModel.findReferencedDatabaseObject(core.getDatabaseObjects(), fk23), is((DatabaseObjectModel) t2));
		assertThat(DefaultTableModel.findReferencedKeyConstraint(core.getDatabaseObjects(), fk12), is(pk1));
		assertThat(DefaultTableModel.findReferencedKeyConstraint(core.getDatabaseObjects(), fk23), is(pk2));
		// FORMAT-ON
	}
}
