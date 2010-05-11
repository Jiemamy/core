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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.Repository;
import org.jiemamy.model.attribute.Column;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.DefaultColumnModel;

/**
 * TODO for daisuke
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DefaultTableModelTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_create() throws Exception {
		try {
			new DefaultTableModel(null);
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
	 * TODO for daisuke
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
		
		live1.activate();
		live2.activate();
		live3.activate();
		
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
	public void test03_lifecycle() throws Exception {
		Repository repository = new Repository();
		DefaultTableModel t = new Table().build();
		
		assertThat(t.isActive(), is(false));
		
		repository.add(t);
		
		assertThat(t.isActive(), is(true));
		
		repository.remove(t);
		
		assertThat(t.isActive(), is(false));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_column_lifecycle1() throws Exception {
		Repository repository = new Repository();
		
		DefaultColumnModel column = new Column().build();
		DefaultTableModel table = new Table().build();
		
		assertThat(table.isActive(), is(false));
		assertThat(column.isActive(), is(false));
		
		table.addColumn(column);
		
		assertThat(table.isActive(), is(false));
		assertThat(column.isActive(), is(true));
		
		repository.add(table);
		
		assertThat(table.isActive(), is(true));
		assertThat(column.isActive(), is(true));
		
		repository.remove(table);
		
		assertThat(table.isActive(), is(false));
		assertThat(column.isActive(), is(true));
		
		table.removeColumn(column);
		
		assertThat(table.isActive(), is(false));
		assertThat(column.isActive(), is(false));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_column_lifecycle2() throws Exception {
		Repository repository = new Repository();
		
		DefaultTableModel table = new Table().build();
		DefaultColumnModel column = new Column().build();
		
		assertThat(table.isActive(), is(false));
		assertThat(column.isActive(), is(false));
		
		repository.add(table);
		
		assertThat(table.isActive(), is(true));
		assertThat(column.isActive(), is(false));
		
		table.addColumn(column);
		
		assertThat(table.isActive(), is(true));
		assertThat(column.isActive(), is(true));
		
		table.removeColumn(column);
		
		assertThat(table.isActive(), is(true));
		assertThat(column.isActive(), is(false));
		
		repository.remove(table);
		
		assertThat(table.isActive(), is(false));
		assertThat(column.isActive(), is(false));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_lifecycle2() throws Exception {
		Repository repository1 = new Repository();
		Repository repository2 = new Repository();
		
		DefaultTableModel table = new Table().build();
		repository1.add(table);
		repository1.remove(table);
		repository2.add(table);
		repository2.remove(table);
		
		repository1.add(table);
		try {
			repository2.add(table);
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
		DefaultTableModel table = new Table("HOGE").build();
		DefaultColumnModel foo = new Column("FOO").build();
		DefaultColumnModel foo2 = new Column("FOO").build();
		DefaultColumnModel bar = new Column("BAR").build();
		
		assertThat(table.getColumns().size(), is(0));
		
		table.addColumn(foo);
		table.addColumn(bar);
		
		assertThat(table.getColumns().size(), is(2));
		assertThat(table.getColumn("FOO"), is((ColumnModel) foo));
		assertThat(table.getColumn("BAR"), is((ColumnModel) bar));
		
		table.removeColumn(bar);
		
		assertThat(table.getColumns().size(), is(1));
		assertThat(table.getColumn("FOO"), is((ColumnModel) foo));
		
		try {
			table.getColumn("BAR");
			fail();
		} catch (ColumnNotFoundException e) {
			// success
		}
		
		table.addColumn(foo2);
		try {
			table.getColumn("FOO");
			fail();
		} catch (TooManyColumnsFoundException e) {
			// success
		}
	}
}
