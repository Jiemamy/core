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

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.attribute.Column;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.dbo.Table;
import org.jiemamy.model.dbo.TableModel;

/**
 * {@link Repository}のテストクラス。
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
		DefaultTableModel table = new Table().build();
		
		assertThat(table.isActive(), is(false));
		repository.add(table);
		assertThat(table.isActive(), is(true));
		repository.remove(table);
		assertThat(table.isActive(), is(false));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_double_add() throws Exception {
		DefaultTableModel table = new Table().build();
		repository.add(table);
		
		try {
			repository.add(table);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
		assertThat(table.isActive(), is(true));
		
		Repository anotherRepository = new Repository();
		try {
			anotherRepository.add(table);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
		assertThat(table.isActive(), is(true));
		
		try {
			anotherRepository.remove(table);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		assertThat(table.isActive(), is(true));
		
		repository.remove(table);
		
		assertThat(table.isActive(), is(false));
		
		try {
			repository.remove(table);
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
			repository.resolve(id);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		try {
			repository.resolve(ref);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		DefaultTableModel table = new Table().build(id);
		repository.add(table);
		
		Entity entityById = repository.resolve(id);
		assertThat(entityById, is((Entity) table));
		
		Entity entityByRef = repository.resolve(ref);
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
		
		repository.add(t1);
		repository.add(t2);
		repository.add(t3);
		
		assertThat(repository.findSubDatabaseObjectsNonRecursive(t1).size(), is(1));
		assertThat(repository.findSubDatabaseObjectsNonRecursive(t2).size(), is(1));
		assertThat(repository.findSubDatabaseObjectsNonRecursive(t3).size(), is(0));
		assertThat(repository.findSubDatabaseObjectsNonRecursive(t1), hasItem((DatabaseObjectModel) t2));
		assertThat(repository.findSubDatabaseObjectsNonRecursive(t2), hasItem((DatabaseObjectModel) t3));
		
		assertThat(repository.findSubDatabaseObjectsRecursive(t1).size(), is(2));
		assertThat(repository.findSubDatabaseObjectsRecursive(t2).size(), is(1));
		assertThat(repository.findSubDatabaseObjectsRecursive(t3).size(), is(0));
		assertThat(repository.findSubDatabaseObjectsRecursive(t1), hasItems((DatabaseObjectModel) t2, (DatabaseObjectModel) t3));
		assertThat(repository.findSubDatabaseObjectsRecursive(t2), hasItem((DatabaseObjectModel) t3));
		
		assertThat(repository.findSuperDatabaseObjectsNonRecursive(t1).size(), is(0));
		assertThat(repository.findSuperDatabaseObjectsNonRecursive(t2).size(), is(1));
		assertThat(repository.findSuperDatabaseObjectsNonRecursive(t3).size(), is(1));
		assertThat(repository.findSuperDatabaseObjectsNonRecursive(t2), hasItem((DatabaseObjectModel) t1));
		assertThat(repository.findSuperDatabaseObjectsNonRecursive(t3), hasItem((DatabaseObjectModel) t2));
		
		assertThat(repository.findSuperDatabaseObjectsRecursive(t1).size(), is(0));
		assertThat(repository.findSuperDatabaseObjectsRecursive(t2).size(), is(1));
		assertThat(repository.findSuperDatabaseObjectsRecursive(t3).size(), is(2));
		assertThat(repository.findSuperDatabaseObjectsRecursive(t2), hasItem((DatabaseObjectModel) t1));
		assertThat(repository.findSuperDatabaseObjectsRecursive(t3), hasItems((DatabaseObjectModel) t1, (DatabaseObjectModel) t2));
		// FORMAT-ON
	}
}
