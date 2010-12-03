/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/10
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
package org.jiemamy.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.Entity;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyCore;
import org.jiemamy.model.attribute.Column;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.dbo.Table;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link JiemamyCore}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyCoreTest {
	
	private JiemamyCore core;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		core = new JiemamyContext().getCore();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_double_add() throws Exception {
		TableModel table = mock(TableModel.class);
		core.add(table);
		
		try {
			core.add(table);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
		
		JiemamyCore anotherRepository = new JiemamyContext().getCore();
		try {
			anotherRepository.add(table);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
		
		try {
			anotherRepository.remove(table);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		core.remove(table);
		
		try {
			core.remove(table);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_double_add() throws Exception {
		
		TableModel table1 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("a"));
		TableModel table2 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("a"));
		
		core.add(table1);
		
		try {
			core.add(table1);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
		
		try {
			core.add(table2);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
		
		JiemamyCore anotherCore = new JiemamyContext().getCore();
		try {
			anotherCore.add(table1);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
		
		anotherCore.add(table2);
		
		try {
			anotherCore.remove(table1);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			core.remove(table2);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		core.remove(table1);
		anotherCore.remove(table2);
		
		try {
			core.remove(table1);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			anotherCore.remove(table2);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_get() throws Exception {
		UUID id = UUID.randomUUID();
		DefaultEntityRef<TableModel> ref = new DefaultEntityRef<TableModel>(id);
		
		try {
			core.resolve(id);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		try {
			core.resolve(ref);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		DefaultTableModel table = new Table().build(id);
		core.add(table);
		
		Entity entityById = core.resolve(id);
		assertThat(entityById, is((Entity) table));
		
		Entity entityByRef = core.resolve(ref);
		assertThat(entityByRef, is((Entity) table));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_query() throws Exception {
		ColumnModel b;
		ColumnModel c;
		ColumnModel d;
		ColumnModel e;
		
		// FORMAT-OFF
		TableModel t1 = new Table("ONE")
				.with(new Column("A").build())
				.with(b = new Column("B").build())
				.with(DefaultPrimaryKeyConstraintModel.of(b))
				.build();
		TableModel t2 = new Table("TWO")
				.with(c = new Column("C").build())
				.with(d = new Column("D").build())
				.with(DefaultPrimaryKeyConstraintModel.of(d))
				.with(DefaultForeignKeyConstraintModel.of(c, b))
				.build();
		TableModel t3 = new Table("THREE")
				.with(e = new Column("E").build())
				.with(new Column("F").build())
				.with(DefaultForeignKeyConstraintModel.of(e, d))
				.build();
		
		core.add(t1);
		core.add(t2);
		core.add(t3);
		
		assertThat(core.findSubDatabaseObjectsNonRecursive(t1).size(), is(1));
		assertThat(core.findSubDatabaseObjectsNonRecursive(t2).size(), is(1));
		assertThat(core.findSubDatabaseObjectsNonRecursive(t3).size(), is(0));
		assertThat(core.findSubDatabaseObjectsNonRecursive(t1), hasItem((DatabaseObjectModel) t2));
		assertThat(core.findSubDatabaseObjectsNonRecursive(t2), hasItem((DatabaseObjectModel) t3));
		
		assertThat(core.findSubDatabaseObjectsRecursive(t1).size(), is(2));
		assertThat(core.findSubDatabaseObjectsRecursive(t2).size(), is(1));
		assertThat(core.findSubDatabaseObjectsRecursive(t3).size(), is(0));
		assertThat(core.findSubDatabaseObjectsRecursive(t1), hasItems((DatabaseObjectModel) t2, (DatabaseObjectModel) t3));
		assertThat(core.findSubDatabaseObjectsRecursive(t2), hasItem((DatabaseObjectModel) t3));
		
		assertThat(core.findSuperDatabaseObjectsNonRecursive(t1).size(), is(0));
		assertThat(core.findSuperDatabaseObjectsNonRecursive(t2).size(), is(1));
		assertThat(core.findSuperDatabaseObjectsNonRecursive(t3).size(), is(1));
		assertThat(core.findSuperDatabaseObjectsNonRecursive(t2), hasItem((DatabaseObjectModel) t1));
		assertThat(core.findSuperDatabaseObjectsNonRecursive(t3), hasItem((DatabaseObjectModel) t2));
		
		assertThat(core.findSuperDatabaseObjectsRecursive(t1).size(), is(0));
		assertThat(core.findSuperDatabaseObjectsRecursive(t2).size(), is(1));
		assertThat(core.findSuperDatabaseObjectsRecursive(t3).size(), is(2));
		assertThat(core.findSuperDatabaseObjectsRecursive(t2), hasItem((DatabaseObjectModel) t1));
		assertThat(core.findSuperDatabaseObjectsRecursive(t3), hasItems((DatabaseObjectModel) t1, (DatabaseObjectModel) t2));
		// FORMAT-ON
	}
}
