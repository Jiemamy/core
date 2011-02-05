/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/11/28
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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.column.SimpleJmColumn;
import org.jiemamy.model.constraint.SimpleJmForeignKeyConstraint;
import org.jiemamy.model.constraint.SimpleJmPrimaryKeyConstraint;
import org.jiemamy.model.datatype.SimpleDataTypeTest;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;
import org.jiemamy.model.table.SimpleJmTable;
import org.jiemamy.transaction.JiemamyTransaction;
import org.jiemamy.transaction.SavePoint;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class StoryTest {
	
	private JiemamyContext ctx1;
	
	private JiemamyContext ctx2;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx1 = new JiemamyContext();
		ctx2 = new JiemamyContext();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void story1() throws Exception {
		
		JiemamyTransaction transaction = ctx1.getTransaction();
		
		SavePoint save1 = transaction.save();
		// FORMAT-OFF
		JmColumn pk;
		JmTable dept = new JmTableBuilder("T_DEPT").with(
			pk = new JmColumnBuilder("ID").type(SimpleDataTypeTest.random()).build(),
			new JmColumnBuilder("NAME").type(SimpleDataTypeTest.random()).build(),
			new JmColumnBuilder("LOC").type(SimpleDataTypeTest.random()).build()
		).with(SimpleJmPrimaryKeyConstraint.of(pk)).build();
		//FORMAT-ON
		
		ctx1.store(dept);
		
		assertThat(ctx1.getDbObjects().size(), is(1));
		
		SavePoint save2 = transaction.save();
		transaction.rollback(save1);
		
		assertThat(ctx1.getDbObjects().size(), is(0));
		
		transaction.rollback(save2);
		
		assertThat(ctx1.getDbObjects().size(), is(1));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void story2() throws Exception {
		JmTable table = spy(new SimpleJmTable());
		ctx1.store(table);
		ctx2.store(table);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test1() throws Exception {
		SimpleJmColumn col1 = new JmColumnBuilder().name("KEY").build();
		SimpleJmColumn col2 = new JmColumnBuilder().name("VALUE").build();
		
		SimpleJmTable table = new JmTableBuilder().name("T_PROPERTY").build();
		table.store(col1);
		table.store(col2);
		List<EntityRef<? extends JmColumn>> pk = new ArrayList<EntityRef<? extends JmColumn>>();
		pk.add(col1.toReference());
		table.store(SimpleJmPrimaryKeyConstraint.of(pk));
		ctx1.store(table);
		
		assertThat(table.getColumns().size(), is(2));
		assertThat(table.getConstraints().size(), is(1));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test2() throws Exception {
		JmColumn pkColumn;
		// FORMAT-OFF
		SimpleJmTable table = new JmTableBuilder().name("T_PROPERTY")
				.with(pkColumn = new JmColumnBuilder().name("KEY").build())
				.with(new JmColumnBuilder().name("VALUE").build())
				.with(SimpleJmPrimaryKeyConstraint.of(pkColumn))
				.build();
		// FORMAT-ON
		ctx1.store(table);
		
		assertThat(table.getColumns().size(), is(2));
		assertThat(table.getConstraints().size(), is(1));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test3() throws Exception {
		JmColumn pkColumn;
		JmColumn fkColumn1;
		JmColumn fkColumn2;
		JmColumn refColumn;
		// FORMAT-OFF
		SimpleJmTable dept = new JmTableBuilder().name("T_DEPT")
				.with(pkColumn = refColumn = new JmColumnBuilder().name("ID").build())
				.with(new JmColumnBuilder().name("NAME").build())
				.with(new JmColumnBuilder().name("LOC").build())
				.with( SimpleJmPrimaryKeyConstraint.of(pkColumn))
				.build();
		SimpleJmTable emp = new JmTableBuilder().name("T_EMP")
				.with(pkColumn = new JmColumnBuilder().name("ID").build())
				.with(new JmColumnBuilder().name("NAME").build())
				.with(fkColumn1 = new JmColumnBuilder().name("DEPT_ID").build())
				.with(fkColumn2 = new JmColumnBuilder().name("MGR_ID").build())
				.with( SimpleJmPrimaryKeyConstraint.of(pkColumn))
				.with(SimpleJmForeignKeyConstraint.of(fkColumn1,refColumn))
				.with(SimpleJmForeignKeyConstraint.of(fkColumn2,pkColumn))
				.build();
		// FORMAT-ON
		ctx1.store(dept);
		ctx1.store(emp);
		
		assertThat(dept.getColumns().size(), is(3));
		assertThat(dept.getConstraints().size(), is(1));
		assertThat(emp.getColumns().size(), is(4));
		assertThat(emp.getConstraints().size(), is(3));
	}
}
