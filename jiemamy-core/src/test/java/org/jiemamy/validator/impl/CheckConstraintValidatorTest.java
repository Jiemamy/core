/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/05/03
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
import static org.hamcrest.Matchers.describedAs;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultCheckConstraintModel;
import org.jiemamy.model.attribute.constraint.DefaultCheckConstraintModelBuilder;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.utils.UUIDUtil;
import org.jiemamy.validator.Problem;

/**
 * {@link CheckConstraintValidator}のテストクラス。
 * 
 * @author daisuke
 */
public class CheckConstraintValidatorTest {
	
	/** テスト対象のバリデータ */
	private CheckConstraintValidator validator;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		validator = new CheckConstraintValidator();
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		validator = null;
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
		
		DefaultColumnModel columnModel = new DefaultColumnModel(UUIDUtil.valueOfOrRandom("b"));
		columnModel.setName("bar");
//		columnModel.setDataType(factory.newDataType(BuiltinDataTypeMold.UNKNOWN));
		
		tableModel1.store(columnModel);
		context.store(tableModel1);
		
		Collection<? extends Problem> result1 = validator.validate(context);
		assertThat(result1.size(), is(0)); // 問題なし
		
		DefaultCheckConstraintModel checkConstraint =
				new DefaultCheckConstraintModelBuilder().setExpression("").build();
		tableModel1.addConstraint(checkConstraint);
		context.store(tableModel1);
		
		Collection<? extends Problem> result2 = validator.validate(context);
		assertThat(result2.size(), is(1)); // 問題1つ
		
		Problem problem1 = result2.iterator().next();
		assertThat(problem1, is(instanceOf(CheckConstraintValidator.EmptyExpressionProblem.class)));
		assertThat(problem1.getMessage(Locale.JAPAN), is("テーブルfooの1番目のチェック制約に制約式がありません"));
		assertThat(problem1.getErrorCode(), is("E0031"));
		
		tableModel1.removeConstraint(checkConstraint);
		
		checkConstraint = new DefaultCheckConstraintModelBuilder().setName("cc").setExpression("").build();
		tableModel1.addConstraint(checkConstraint);
		context.store(tableModel1);
		
		Collection<? extends Problem> result3 = validator.validate(context);
		assertThat(result3.size(), is(1)); // 問題1つ
		
		Problem problem2 = result3.iterator().next();
		assertThat(problem2, is(instanceOf(CheckConstraintValidator.EmptyExpressionProblem.class)));
		assertThat(problem2.getMessage(Locale.JAPAN), is("テーブルfooに設定されたチェック制約ccに制約式がありません"));
		assertThat(problem2.getErrorCode(), is("E0030"));
		
		tableModel1.removeConstraint(checkConstraint);
		
		checkConstraint = new DefaultCheckConstraintModelBuilder().setExpression("bar > 0").apply(checkConstraint);
		tableModel1.addConstraint(checkConstraint);
		context.store(tableModel1);
		
		Collection<? extends Problem> result4 = validator.validate(context);
		assertThat(result4.size(), describedAs(result4.toString(), is(0))); // 問題なし
	}
}
