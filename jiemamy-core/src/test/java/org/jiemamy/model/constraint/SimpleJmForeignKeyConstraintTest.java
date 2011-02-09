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
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link SimpleJmForeignKeyConstraint}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmForeignKeyConstraintTest {
	
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
		JmColumn b;
		JmColumn c;
		JmColumn d;
		JmColumn e;
		JmForeignKeyConstraint fk21;
		JmForeignKeyConstraint fk32;
		JmKeyConstraint pk1;
		JmKeyConstraint pk2;
		
		// FORMAT-OFF
		JmTable t1 = new JmTableBuilder("ONE")
				.with(new JmColumnBuilder("A").build())
				.with(b = new JmColumnBuilder("B").build())
				.with(pk1 = SimpleJmPrimaryKeyConstraint.of(b))
				.build();
		JmTable t2 = new JmTableBuilder("TWO")
				.with(c = new JmColumnBuilder("C").build())
				.with(d = new JmColumnBuilder("D").build())
				.with(pk2 = SimpleJmPrimaryKeyConstraint.of(d))
				.with(fk21 = SimpleJmForeignKeyConstraint.of(c, b))
				.build();
		JmTable t3 = new JmTableBuilder("THREE")
				.with(e = new JmColumnBuilder("E").build())
				.with(new JmColumnBuilder("F").build())
				.with(fk32 = SimpleJmForeignKeyConstraint.of(e, d))
				.build();
		
		ctx.store(t1);
		ctx.store(t2);
		ctx.store(t3);
		
		assertThat(fk21.findDeclaringTable(ctx.getTables()), is(t2));
		assertThat(fk32.findDeclaringTable(ctx.getTables()), is(t3));
		assertThat(fk21.findReferenceTable(ctx.getTables()), is(t1));
		assertThat(fk32.findReferenceTable(ctx.getTables()), is(t2));
		assertThat(fk21.findReferencedKeyConstraint(ctx.getDbObjects()), is(pk1));
		assertThat(fk32.findReferencedKeyConstraint(ctx.getDbObjects()), is(pk2));
		// FORMAT-ON
	}
}
