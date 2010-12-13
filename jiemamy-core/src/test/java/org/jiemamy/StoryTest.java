/*
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
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.attribute.Column;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultForeignKeyConstraintModelBuilder;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModelBuilder;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.dbo.Table;
import org.jiemamy.model.dbo.TableModel;

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
		// FORMAT-OFF
		ColumnModel pk;
		TableModel dept = new Table("T_DEPT").with(
			pk = new Column("ID").whoseTypeIs(Dummy.TYPE).build(),
			new Column("NAME").whoseTypeIs(Dummy.TYPE).build(),
			new Column("LOC").whoseTypeIs(Dummy.TYPE).build()
		).with(new DefaultPrimaryKeyConstraintModelBuilder().addKeyColumn(pk).build()).build();
		
		ctx1.store(dept);
		
		//FORMAT-ON
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void story2() throws Exception {
		TableModel table = spy(new DefaultTableModel(UUID.randomUUID()));
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
		DefaultColumnModel col1 = new Column().whoseNameIs("KEY").build();
		DefaultColumnModel col2 = new Column().whoseNameIs("VALUE").build();
		
		DefaultTableModel tableModel = new Table().whoseNameIs("T_PROPERTY").build();
		tableModel.store(col1);
		tableModel.store(col2);
		List<EntityRef<? extends ColumnModel>> pk = new ArrayList<EntityRef<? extends ColumnModel>>();
		pk.add(col1.toReference());
		tableModel.addConstraint(new DefaultPrimaryKeyConstraintModel(null, null, null, pk, null));
		ctx1.store(tableModel);
		
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
		ColumnModel pkColumn;
		// FORMAT-OFF
		DefaultTableModel tableModel = new Table().whoseNameIs("T_PROPERTY")
				.with(pkColumn = new Column().whoseNameIs("KEY").build())
				.with(new Column().whoseNameIs("VALUE").build())
				.with(DefaultPrimaryKeyConstraintModel.of(pkColumn))
				.build();
		// FORMAT-ON
		ctx1.store(tableModel);
		
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
		ctx1.store(dept);
		ctx1.store(emp);
		
		assertThat(dept.getColumns().size(), is(3));
		assertThat(dept.getConstraints().size(), is(1));
		assertThat(emp.getColumns().size(), is(4));
		assertThat(emp.getConstraints().size(), is(3));
	}
}
