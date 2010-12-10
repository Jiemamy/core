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
package org.jiemamy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.EntityNotFoundException;
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
 * {@link 2}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContext2Test {
	
	private JiemamyContext ctx;
	
	private JiemamyContext ctx2;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx = new JiemamyContext();
		ctx2 = new JiemamyContext();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_double_add() throws Exception {
		TableModel table = spy(new DefaultTableModel(UUID.randomUUID()));
		
		ctx.add(table);
		ctx.add(table);
		ctx2.add(table);
		ctx2.remove(table.toReference());
		ctx.remove(table.toReference());
		
		try {
			ctx.remove(table.toReference());
			fail();
		} catch (EntityNotFoundException e) {
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
		
		ctx.add(table1);
		ctx.add(table1);
		ctx.add(table2);
		ctx2.add(table1);
		ctx2.add(table2);
		ctx2.remove(table1.toReference());
		ctx.remove(table2.toReference());
		
		try {
			ctx.remove(table1.toReference());
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		try {
			ctx2.remove(table2.toReference());
			fail();
		} catch (EntityNotFoundException e) {
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
			ctx.resolve(id);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		try {
			ctx.resolve(ref);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		TableModel table = new DefaultTableModel(id);
		ctx.add(table);
		
		assertThat(ctx.resolve(id), is((Entity) table));
		assertThat(ctx.resolve(ref), is((Entity) table));
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
		
		ctx.add(t1);
		ctx.add(t2);
		ctx.add(t3);
		
		assertThat(ctx.findSubDatabaseObjectsNonRecursive(t1).size(), is(1));
		assertThat(ctx.findSubDatabaseObjectsNonRecursive(t2).size(), is(1));
		assertThat(ctx.findSubDatabaseObjectsNonRecursive(t3).size(), is(0));
		assertThat(ctx.findSubDatabaseObjectsNonRecursive(t1), hasItem((DatabaseObjectModel) t2));
		assertThat(ctx.findSubDatabaseObjectsNonRecursive(t2), hasItem((DatabaseObjectModel) t3));
		
		assertThat(ctx.findSubDatabaseObjectsRecursive(t1).size(), is(2));
		assertThat(ctx.findSubDatabaseObjectsRecursive(t2).size(), is(1));
		assertThat(ctx.findSubDatabaseObjectsRecursive(t3).size(), is(0));
		assertThat(ctx.findSubDatabaseObjectsRecursive(t1), hasItems((DatabaseObjectModel) t2, (DatabaseObjectModel) t3));
		assertThat(ctx.findSubDatabaseObjectsRecursive(t2), hasItem((DatabaseObjectModel) t3));
		
		assertThat(ctx.findSuperDatabaseObjectsNonRecursive(t1).size(), is(0));
		assertThat(ctx.findSuperDatabaseObjectsNonRecursive(t2).size(), is(1));
		assertThat(ctx.findSuperDatabaseObjectsNonRecursive(t3).size(), is(1));
		assertThat(ctx.findSuperDatabaseObjectsNonRecursive(t2), hasItem((DatabaseObjectModel) t1));
		assertThat(ctx.findSuperDatabaseObjectsNonRecursive(t3), hasItem((DatabaseObjectModel) t2));
		
		assertThat(ctx.findSuperDatabaseObjectsRecursive(t1).size(), is(0));
		assertThat(ctx.findSuperDatabaseObjectsRecursive(t2).size(), is(1));
		assertThat(ctx.findSuperDatabaseObjectsRecursive(t3).size(), is(2));
		assertThat(ctx.findSuperDatabaseObjectsRecursive(t2), hasItem((DatabaseObjectModel) t1));
		assertThat(ctx.findSuperDatabaseObjectsRecursive(t3), hasItems((DatabaseObjectModel) t1, (DatabaseObjectModel) t2));
		// FORMAT-ON
	}
}
