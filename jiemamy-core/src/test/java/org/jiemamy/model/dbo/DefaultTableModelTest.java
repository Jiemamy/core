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

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.InternalKeyReference;
import org.jiemamy.Repository;
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
		DefaultTableModel t = new Table().whoseNameIs("FOO").build();
		assertThat(t.getName(), is("FOO"));
		t.setName("BAR");
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
		
		live1.initiate(InternalKeyReference.KEY);
		live2.initiate(InternalKeyReference.KEY);
		live3.initiate(InternalKeyReference.KEY);
		
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
		DefaultTableModel live = new Table().build();
		DefaultTableModel dead = new Table().build();
		
		live.initiate(InternalKeyReference.KEY);
		
		assertThat(live.getReference().getReferenceId(), is(live.getId()));
		assertThat(dead.getReference().getReferenceId(), is(dead.getId()));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_column_lifecycle1() throws Exception {
		DefaultColumnModel col1 = new Column().build();
		DefaultTableModel table = new Table().build();
		table.addColumn(col1);
		
		Repository repository = new Repository();
		
		assertThat(table.isAlive(), is(false));
		assertThat(col1.isAlive(), is(false));
		
		repository.add(table);
		
		assertThat(table.isAlive(), is(true));
		assertThat(col1.isAlive(), is(true));
		
		repository.remove(table);
		
		assertThat(table.isAlive(), is(false));
		assertThat(col1.isAlive(), is(false));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_column_lifecycle2() throws Exception {
		DefaultTableModel table = new Table().build();
		
		Repository repository = new Repository();
		repository.add(table);
		
		DefaultColumnModel col = new Column().build();
		assertThat(col.isAlive(), is(false));
		table.addColumn(col);
		assertThat(col.isAlive(), is(true));
		table.removeColumn(col);
		assertThat(col.isAlive(), is(false));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_getColumn() throws Exception {
		DefaultColumnModel foo = new Column().build();
		foo.setName("FOO");
		DefaultColumnModel bar = new Column().build();
		bar.setName("BAR");
		DefaultTableModel table = new Table().build();
		table.addColumn(foo);
		table.addColumn(bar);
		
		assertThat(table.getColumns().size(), is(2));
		assertThat(table.getColumn("FOO"), is((ColumnModel) foo));
		assertThat(table.getColumn("BAR"), is((ColumnModel) bar));
	}
}
