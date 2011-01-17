/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/17
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
package org.jiemamy.utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.google.common.collect.Iterables;

import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.Column;
import org.jiemamy.model.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.Table;

/**
 * {@link ForeignKeyFactory}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ForeignKeyFactoryTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_create() throws Exception {
		JiemamyContext context = new JiemamyContext();
		// FORMAT-OFF
		DefaultTableModel reference = new Table("DEPT")
				.with(new Column("ID").build())
				.with(new Column("NAME").build())
				.with(new Column("LOC").build())
				.build();
		// FORMAT-ON
		reference.store(DefaultPrimaryKeyConstraintModel.of(reference.getColumn("ID")));
		context.store(reference);
		
		// FORMAT-OFF
		DefaultTableModel declaring = new Table("EMP")
				.with(new Column("ID").build())
				.with(new Column("NAME").build())
				.with(new Column("DEPT_ID").build())
				.build();
		// FORMAT-ON
		declaring.store(DefaultPrimaryKeyConstraintModel.of(declaring.getColumn("ID")));
		context.store(declaring);
		
		DefaultForeignKeyConstraintModel fk = ForeignKeyFactory.create(context, declaring, reference);
		
		assertThat(Iterables.getOnlyElement(fk.getKeyColumns()).isReferenceOf(declaring.getColumn("DEPT_ID")), is(true));
		assertThat(Iterables.getOnlyElement(fk.getReferenceColumns()).isReferenceOf(reference.getColumn("ID")),
				is(true));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_error1() throws Exception {
		JiemamyContext context = new JiemamyContext();
		// FORMAT-OFF
		DefaultTableModel reference = new Table("DEPT")
				.with(new Column("ID").build())
				.with(new Column("NAME").build())
				.with(new Column("LOC").build())
				.build();
		// FORMAT-ON
		// NO KEY
		context.store(reference);
		
		// FORMAT-OFF
		DefaultTableModel declaring = new Table("EMP")
				.with(new Column("ID").build())
				.with(new Column("NAME").build())
				.with(new Column("DEPT_ID").build())
				.build();
		// FORMAT-ON
		declaring.store(DefaultPrimaryKeyConstraintModel.of(declaring.getColumn("ID")));
		context.store(declaring);
		
		try {
			ForeignKeyFactory.create(context, declaring, reference);
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
	public void test03_error2() throws Exception {
		JiemamyContext context = new JiemamyContext();
		// FORMAT-OFF
		DefaultTableModel reference = new Table("DEPT")
				.with(new Column("ID").build())
				.with(new Column("NAME").build())
				.with(new Column("LOC").build())
				.build();
		// FORMAT-ON
		reference.store(DefaultPrimaryKeyConstraintModel.of(reference.getColumn("ID")));
		context.store(reference);
		
		// FORMAT-OFF
		DefaultTableModel declaring = new Table("EMP")
				// NO COLUMN
				.build();
		// FORMAT-ON
		context.store(declaring);
		
		try {
			ForeignKeyFactory.create(context, declaring, reference);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}
}
