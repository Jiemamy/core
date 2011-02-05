/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/01/21
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
package org.jiemamy.validator.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.table.SimpleJmTable;
import org.jiemamy.utils.UUIDUtil;
import org.jiemamy.validator.Problem;

/**
 * {@link DbObjectNameCollisionValidator}のテストクラス。
 * 
 * @author daisuke
 */
public class DbObjectNameCollisionValidatorTest {
	
	/** テスト対象のバリデータ */
	private DbObjectNameCollisionValidator validator;
	
	/** テスト対象の問題 */
	private DbObjectNameCollisionValidator.DbObjectNameCollisionProblem problem;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		validator = new DbObjectNameCollisionValidator();
		problem = new DbObjectNameCollisionValidator.DbObjectNameCollisionProblem("foobar", null);
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
	 * {@link DbObject}名の重複バリデーションが正常に行われる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_DbObject名の重複バリデーションが正常に行われる() throws Exception {
		JiemamyContext context = new JiemamyContext();
		
		SimpleJmTable table1 = new SimpleJmTable(UUIDUtil.valueOfOrRandom("a"));
		table1.setName("foo");
		context.store(table1);
		
		Collection<? extends Problem> result1 = validator.validate(context);
		assertThat(result1.size(), is(0)); // 問題なし
		
		SimpleJmTable table2 = new SimpleJmTable(UUIDUtil.valueOfOrRandom("b"));
		table2.setName("foo");
		context.store(table2);
		
		Collection<? extends Problem> result2 = validator.validate(context);
		assertThat(result2.size(), is(1)); // 問題1つ
		
		Problem problem = result2.iterator().next();
		assertThat(problem, is(instanceOf(DbObjectNameCollisionValidator.DbObjectNameCollisionProblem.class)));
		assertThat(problem.getMessage(Locale.JAPAN), is("DbObject名 \"foo\" が重複しています。"));
		assertThat(problem.getErrorCode(), is("E0070"));
		
		SimpleJmTable table3 = new SimpleJmTable(UUIDUtil.valueOfOrRandom("c"));
		table3.setName("foo");
		context.store(table3);
		
		Collection<? extends Problem> result3 = validator.validate(context);
		assertThat(result3.size(), is(1)); // 3つ重なっていても、問題は1つ
		
		SimpleJmTable table4 = new SimpleJmTable(UUIDUtil.valueOfOrRandom("d"));
		table4.setName("bar");
		context.store(table4);
		
		SimpleJmTable table5 = new SimpleJmTable(UUIDUtil.valueOfOrRandom("e"));
		table5.setName("bar");
		context.store(table5);
		
		Collection<? extends Problem> result4 = validator.validate(context);
		assertThat(result4.size(), is(2)); // 2件の衝突の場合、問題は2つ
	}
	
	/**
	 * 各ロケールのエラーメッセージが適切に構築される。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@Ignore("enロケールメッセージをまだ作っていない")
	public void test02_各ロケールのエラーメッセージが適切に構築される() throws Exception {
		assertThat(problem.getMessage(Locale.JAPAN), is("DbObject名 \"foobar\" が重複しています。"));
		assertThat(problem.getMessage(Locale.ENGLISH), is("Duplicate dbObject name foobar"));
		
		Locale backup = Locale.getDefault();
		try {
			Locale.setDefault(Locale.JAPAN);
			assertThat(problem.getMessage(new Locale("xx")), is("DbObject名 \"foobar\" が重複しています。"));
		} finally {
			Locale.setDefault(backup);
		}
	}
	
}
