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
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.jiemamy.model.attribute.Column;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultForeignKeyConstraintModelBuilder;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModelBuilder;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.dbo.Table;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class GeneralTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test1() throws Exception {
		Repository repository = new Repository();
		
		DefaultColumnModel col1 = new Column().whoseNameIs("KEY").build();
		DefaultColumnModel col2 = new Column().whoseNameIs("VALUE").build();
		
		DefaultTableModel tableModel = new Table().whoseNameIs("T_PROPERTY").build();
		tableModel.addColumn(col1);
		tableModel.addColumn(col2);
		@SuppressWarnings("unchecked")
		List<EntityRef<ColumnModel>> pk = Arrays.asList(col1.getReference());
		tableModel.addConstraint(new DefaultPrimaryKeyConstraintModel(null, null, null, pk, null));
		repository.add(tableModel);
		
		assertThat(tableModel.getColumns().size(), is(2));
		assertThat(tableModel.getConstraints().size(), is(1));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test2() throws Exception {
		Repository repository = new Repository();
		
		ColumnModel pkColumn;
		// FORMAT-OFF
		DefaultTableModel tableModel = new Table().whoseNameIs("T_PROPERTY")
				.with(pkColumn = new Column().whoseNameIs("KEY").build())
				.with(new Column().whoseNameIs("VALUE").build())
				.with(DefaultPrimaryKeyConstraintModel.of(pkColumn))
				.build();
		// FORMAT-ON
		repository.add(tableModel);
		
		assertThat(tableModel.getColumns().size(), is(2));
		assertThat(tableModel.getConstraints().size(), is(1));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test3() throws Exception {
		Repository repository = new Repository();
		
		ColumnModel pkColumn;
		ColumnModel fkColumn1;
		ColumnModel fkColumn2;
		ColumnModel refColumn;
		// FORMAT-OFF
		DefaultTableModel dept = new Table().whoseNameIs("T_DEPT")
				.with(pkColumn = refColumn = new Column().whoseNameIs("ID").build())
				.with(new Column().whoseNameIs("NAME").build())
				.with(new Column().whoseNameIs("LOC").build())
				.with(new DefaultPrimaryKeyConstraintModelBuilder().addKeyColumn(pkColumn).build())
				.build();
		DefaultTableModel emp = new Table().whoseNameIs("T_EMP")
				.with(pkColumn = new Column().whoseNameIs("ID").build())
				.with(new Column().whoseNameIs("NAME").build())
				.with(fkColumn1 = new Column().whoseNameIs("DEPT_ID").build())
				.with(fkColumn2 = new Column().whoseNameIs("MGR_ID").build())
				.with(new DefaultPrimaryKeyConstraintModelBuilder().addKeyColumn(pkColumn).build())
				.with(new DefaultForeignKeyConstraintModelBuilder()
						.addKeyColumn(fkColumn1).addReferenceColumn(refColumn).build())
				.with(new DefaultForeignKeyConstraintModelBuilder()
						.addKeyColumn(fkColumn2).addReferenceColumn(pkColumn).build())
				.build();
		// FORMAT-ON
		repository.add(dept);
		repository.add(emp);
		
		assertThat(dept.getColumns().size(), is(3));
		assertThat(dept.getConstraints().size(), is(1));
		assertThat(emp.getColumns().size(), is(4));
		assertThat(emp.getConstraints().size(), is(3));
	}
}
