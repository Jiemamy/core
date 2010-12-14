/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.utils.UUIDUtil;
import org.jiemamy.validator.Problem;

/**
 * {@link EntityNameCollisionValidator}のテストクラス。
 * 
 * @author daisuke
 */
public class EntityNameCollisionValidatorTest {
	
	/** テスト対象のバリデータ */
	private EntityNameCollisionValidator validator;
	
	/** テスト対象の問題 */
	private EntityNameCollisionValidator.EntityNameCollisionProblem problem;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		validator = new EntityNameCollisionValidator();
		problem = new EntityNameCollisionValidator.EntityNameCollisionProblem("foobar", null);
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		validator = null;
		problem = null;
	}
	
	/**
	 * エンティティ名の重複バリデーションが正常に行われる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_エンティティ名の重複バリデーションが正常に行われる() throws Exception {
		JiemamyContext context = new JiemamyContext();
		
		DefaultTableModel tableModel1 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("a"));
		tableModel1.setName("foo");
		context.store(tableModel1);
		
		Collection<? extends Problem> result1 = validator.validate(context);
		assertThat(result1.size(), is(0)); // 問題なし
		
		DefaultTableModel tableModel2 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("b"));
		tableModel2.setName("foo");
		context.store(tableModel2);
		
		Collection<? extends Problem> result2 = validator.validate(context);
		assertThat(result2.size(), is(1)); // 問題1つ
		
		Problem problem = result2.iterator().next();
		assertThat(problem, is(instanceOf(EntityNameCollisionValidator.EntityNameCollisionProblem.class)));
		assertThat(problem.getMessage(Locale.JAPAN), is("エンティティ名 \"foo\" が重複しています"));
		assertThat(problem.getErrorCode(), is("E0070"));
		
		DefaultTableModel tableModel3 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("c"));
		tableModel3.setName("foo");
		context.store(tableModel3);
		
		Collection<? extends Problem> result3 = validator.validate(context);
		assertThat(result3.size(), is(1)); // 3つ重なっていても、問題は1つ
		
		DefaultTableModel tableModel4 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("d"));
		tableModel4.setName("bar");
		context.store(tableModel4);
		
		DefaultTableModel tableModel5 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("e"));
		tableModel5.setName("bar");
		context.store(tableModel5);
		
		Collection<? extends Problem> result4 = validator.validate(context);
		assertThat(result4.size(), is(2)); // 2件の衝突の場合、問題は2つ
	}
	
	/**
	 * 各ロケールのエラーメッセージが適切に構築される。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_各ロケールのエラーメッセージが適切に構築される() throws Exception {
		assertThat(problem.getMessage(Locale.JAPAN), is("エンティティ名 \"foobar\" が重複しています"));
		assertThat(problem.getMessage(Locale.ENGLISH), is("Duplicate entity name foobar"));
		
		Locale backup = Locale.getDefault();
		try {
			Locale.setDefault(Locale.JAPAN);
			assertThat(problem.getMessage(new Locale("xx")), is("エンティティ名 \"foobar\" が重複しています"));
		} finally {
			Locale.setDefault(backup);
		}
	}
	
}
