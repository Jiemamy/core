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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.Repository;
import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.Entity;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.model.attribute.DefalutColumnModel;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.dbo.TableModel;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class RepositoryTest {
	
	private Repository repository;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		repository = new Repository();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_add_and_remove() throws Exception {
		DefaultTableModel table = new DefaultTableModel();
		
		assertThat(table.getId(), is(nullValue()));
		repository.add(table);
		assertThat(table.getId(), is(not(nullValue())));
		repository.remove(table);
		assertThat(table.getId(), is(nullValue()));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_add_and_remove2() throws Exception {
		DefalutColumnModel column = new DefalutColumnModel();
		DefaultTableModel table = new DefaultTableModel();
		table.addColumn(column);
		
		assertThat(table.getId(), is(nullValue()));
		assertThat(column.getId(), is(nullValue()));
		
		repository.add(table);
		
		assertThat(table.getId(), is(not(nullValue())));
		assertThat(column.getId(), is(not(nullValue())));
		
		repository.remove(table);
		
		assertThat(table.getId(), is(nullValue()));
		assertThat(column.getId(), is(nullValue()));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_double_add() throws Exception {
		Repository anotherRepository = new Repository();
		DefaultTableModel table = new DefaultTableModel();
		repository.add(table);
		
		try {
			anotherRepository.add(table);
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
	public void test04_get() throws Exception {
		UUID id = UUID.randomUUID();
		DefaultEntityRef<TableModel> ref = new DefaultEntityRef<TableModel>(id);
		
		try {
			repository.get(id);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		try {
			repository.get(ref);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		DefaultTableModel table = new DefaultTableModel();
		repository.add(table, id);
		
		Entity entityById = repository.get(id);
		assertThat(entityById, is((Entity) table));
		
		Entity entityByRef = repository.get(ref);
		assertThat(entityByRef, is((Entity) table));
	}
	
}
