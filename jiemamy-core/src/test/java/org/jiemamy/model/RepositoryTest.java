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
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;
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
	public void test05_getTables() throws Exception {
		// FORMAT-OFF
		TableModel t1 =
				new Table().whoseNameIs("ONE").with(new Column().whoseNameIs("A").build()).with(
						new Column().whoseNameIs("B").build()).build();
		TableModel t2 =
				new Table().whoseNameIs("TWO").with(new Column().whoseNameIs("C").build()).with(
						new Column().whoseNameIs("D").build()).build();
		// FORMAT-ON
		
		repository.add(t1);
		repository.add(t2);
		assertThat(repository.findTables().size(), is(2));
		assertThat(repository.findTables(), hasItem(t1));
		assertThat(repository.findTables(), hasItem(t2));
	}
	
	/**
	 * TODO for daisuke
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
		TableModel t1 =
				new Table().whoseNameIs("ONE").with(a = new Column().whoseNameIs("A").build()).with(
						b = new Column().whoseNameIs("B").build()).build();
		TableModel t2 =
				new Table().whoseNameIs("TWO").with(c = new Column().whoseNameIs("C").build()).with(
						d = new Column().whoseNameIs("D").build()).build();
		// FORMAT-ON
		
		repository.add(t1);
		repository.add(t2);
		
		assertThat(repository.findDeclaringTable(a), is(t1));
		assertThat(repository.findDeclaringTable(b), is(t1));
		assertThat(repository.findDeclaringTable(c), is(t2));
		assertThat(repository.findDeclaringTable(d), is(t2));
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
		ForeignKeyConstraintModel fk12;
		ForeignKeyConstraintModel fk23;
		KeyConstraintModel pk1;
		KeyConstraintModel pk2;
		
		// FORMAT-OFF
		TableModel t1 =
				new Table("ONE").with(new Column("A").build()).with(b = new Column("B").build()).with(
						pk1 = DefaultPrimaryKeyConstraintModel.of(b)).build();
		TableModel t2 =
				new Table("TWO").with(c = new Column("C").build()).with(d = new Column("D").build()).with(
						pk2 = DefaultPrimaryKeyConstraintModel.of(d)).with(
						fk12 = DefaultForeignKeyConstraintModel.of(c, b)).build();
		TableModel t3 =
				new Table("THREE").with(e = new Column("E").build()).with(new Column("F").build()).with(
						fk23 = DefaultForeignKeyConstraintModel.of(e, d)).build();
		// FORMAT-ON
		
		repository.add(t1);
		repository.add(t2);
		repository.add(t3);
		
		assertThat(repository.findReferencedEntity(fk12), is(t1));
		assertThat(repository.findReferencedEntity(fk23), is(t2));
		assertThat(repository.findReferencedKeyConstraint(fk12), is(pk1));
		assertThat(repository.findReferencedKeyConstraint(fk23), is(pk2));
		
		assertThat(repository.findSubEntitiesNonRecursive(t1).size(), is(1));
		assertThat(repository.findSubEntitiesNonRecursive(t2).size(), is(1));
		assertThat(repository.findSubEntitiesNonRecursive(t3).size(), is(0));
		assertThat(repository.findSubEntitiesNonRecursive(t1), hasItem((DatabaseObjectModel) t2));
		assertThat(repository.findSubEntitiesNonRecursive(t2), hasItem((DatabaseObjectModel) t3));
		
		// FORMAT-OFF
		assertThat(repository.findSubEntitiesRecursive(t1).size(), is(2));
		assertThat(repository.findSubEntitiesRecursive(t2).size(), is(1));
		assertThat(repository.findSubEntitiesRecursive(t3).size(), is(0));
		assertThat(repository.findSubEntitiesRecursive(t1),
				hasItems((DatabaseObjectModel) t2, (DatabaseObjectModel) t3));
		assertThat(repository.findSubEntitiesRecursive(t2), hasItem((DatabaseObjectModel) t3));
		
		assertThat(repository.findSuperEntitiesNonRecursive(t1).size(), is(0));
		assertThat(repository.findSuperEntitiesNonRecursive(t2).size(), is(1));
		assertThat(repository.findSuperEntitiesNonRecursive(t3).size(), is(1));
		assertThat(repository.findSuperEntitiesNonRecursive(t2), hasItem((DatabaseObjectModel) t1));
		assertThat(repository.findSuperEntitiesNonRecursive(t3), hasItem((DatabaseObjectModel) t2));
		
		assertThat(repository.findSuperEntitiesRecursive(t1).size(), is(0));
		assertThat(repository.findSuperEntitiesRecursive(t2).size(), is(1));
		assertThat(repository.findSuperEntitiesRecursive(t3).size(), is(2));
		assertThat(repository.findSuperEntitiesRecursive(t2), hasItem((DatabaseObjectModel) t1));
		assertThat(repository.findSuperEntitiesRecursive(t3), hasItems((DatabaseObjectModel) t1,
				(DatabaseObjectModel) t2));
		// FORMAT-ON
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test08_addnull() throws Exception {
		repository.add(null);
	}
}
