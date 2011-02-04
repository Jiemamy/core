/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/02/04
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
package org.jiemamy.model.constraint;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.Column;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.table.Table;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link DefaultForeignKeyConstraintModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultForeignKeyConstraintModelTest {
	
	private JiemamyContext ctx;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx = new JiemamyContext();
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		UUIDUtil.clear();
	}
	
	/**
	 * find系メソッドのテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_find系メソッドのテスト() throws Exception {
		ColumnModel b;
		ColumnModel c;
		ColumnModel d;
		ColumnModel e;
		ForeignKeyConstraintModel fk21;
		ForeignKeyConstraintModel fk32;
		KeyConstraintModel pk1;
		KeyConstraintModel pk2;
		
		// FORMAT-OFF
		TableModel t1 = new Table("ONE")
				.with(new Column("A").build())
				.with(b = new Column("B").build())
				.with(pk1 = DefaultPrimaryKeyConstraintModel.of(b))
				.build();
		TableModel t2 = new Table("TWO")
				.with(c = new Column("C").build())
				.with(d = new Column("D").build())
				.with(pk2 = DefaultPrimaryKeyConstraintModel.of(d))
				.with(fk21 = DefaultForeignKeyConstraintModel.of(c, b))
				.build();
		TableModel t3 = new Table("THREE")
				.with(e = new Column("E").build())
				.with(new Column("F").build())
				.with(fk32 = DefaultForeignKeyConstraintModel.of(e, d))
				.build();
		
		ctx.store(t1);
		ctx.store(t2);
		ctx.store(t3);
		
		assertThat(fk21.findDeclaringTable(ctx.getTables()), is(t2));
		assertThat(fk32.findDeclaringTable(ctx.getTables()), is(t3));
		assertThat(fk21.findReferenceTable(ctx.getTables()), is(t1));
		assertThat(fk32.findReferenceTable(ctx.getTables()), is(t2));
		assertThat(fk21.findReferencedKeyConstraint(ctx.getDatabaseObjects()), is(pk1));
		assertThat(fk32.findReferencedKeyConstraint(ctx.getDatabaseObjects()), is(pk2));
		// FORMAT-ON
	}
}
