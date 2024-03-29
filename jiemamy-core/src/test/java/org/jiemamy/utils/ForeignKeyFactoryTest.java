/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;

/**
 * {@link ForeignKeyFactory}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ForeignKeyFactoryTest {
	
	/**
	 * {@link ForeignKeyFactory}を用いて、デフォルト設定済みの{@link JmForeignKeyConstraint}を生成する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_create() throws Exception {
		JiemamyContext context = new JiemamyContext();
		// FORMAT-OFF
		JmTable reference = new JmTableBuilder("DEPT")
				.with(new JmColumnBuilder("ID").build())
				.with(new JmColumnBuilder("NAME").build())
				.with(new JmColumnBuilder("LOC").build())
				.build();
		// FORMAT-ON
		reference.add(JmPrimaryKeyConstraint.of(reference.getColumn("ID")));
		context.add(reference);
		
		// FORMAT-OFF
		JmTable declaring = new JmTableBuilder("EMP")
				.with(new JmColumnBuilder("ID").build())
				.with(new JmColumnBuilder("NAME").build())
				.with(new JmColumnBuilder("DEPT_ID").build())
				.build();
		// FORMAT-ON
		declaring.add(JmPrimaryKeyConstraint.of(declaring.getColumn("ID")));
		context.add(declaring);
		
		JmForeignKeyConstraint fk = ForeignKeyFactory.create(context, declaring, reference);
		
		assertThat(Iterables.getOnlyElement(fk.getKeyColumns()).isReferenceOf(declaring.getColumn("DEPT_ID")), is(true));
		assertThat(Iterables.getOnlyElement(fk.getReferenceColumns()).isReferenceOf(reference.getColumn("ID")),
				is(true));
	}
	
	/**
	 * referenceテーブルにキーが存在しない場合はエラー。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_error1() throws Exception {
		JiemamyContext context = new JiemamyContext();
		// FORMAT-OFF
		JmTable reference = new JmTableBuilder("DEPT")
				.with(new JmColumnBuilder("ID").build())
				.with(new JmColumnBuilder("NAME").build())
				.with(new JmColumnBuilder("LOC").build())
				.build();
		// FORMAT-ON
		// NO KEY
		context.add(reference);
		
		// FORMAT-OFF
		JmTable declaring = new JmTableBuilder("EMP")
				.with(new JmColumnBuilder("ID").build())
				.with(new JmColumnBuilder("NAME").build())
				.with(new JmColumnBuilder("DEPT_ID").build())
				.build();
		// FORMAT-ON
		declaring.add(JmPrimaryKeyConstraint.of(declaring.getColumn("ID")));
		context.add(declaring);
		
		try {
			ForeignKeyFactory.create(context, declaring, reference);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	/**
	 * declaringテーブルにカラムが（充分に）存在しない場合はエラー。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_error2() throws Exception {
		JiemamyContext context = new JiemamyContext();
		// FORMAT-OFF
		JmTable reference = new JmTableBuilder("DEPT")
				.with(new JmColumnBuilder("ID").build())
				.with(new JmColumnBuilder("NAME").build())
				.with(new JmColumnBuilder("LOC").build())
				.build();
		// FORMAT-ON
		reference.add(JmPrimaryKeyConstraint.of(reference.getColumn("ID")));
		context.add(reference);
		
		// FORMAT-OFF
		JmTable declaring = new JmTableBuilder("EMP")
				// NO COLUMN
				.build();
		// FORMAT-ON
		context.add(declaring);
		
		try {
			ForeignKeyFactory.create(context, declaring, reference);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}
}
