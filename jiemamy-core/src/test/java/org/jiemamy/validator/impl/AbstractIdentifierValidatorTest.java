/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/09
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.dialect.ReservedWordsChecker;
import org.jiemamy.validator.Problem;

/**
 * {@link AbstractIdentifierValidator}のテストクラス。
 * 
 * @author daisuke
 */
public class AbstractIdentifierValidatorTest {
	
	private AbstractIdentifierValidator validator;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		validator = new IdentifierValidatorMock();
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
	 * バリデータ動作テスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_バリデータ動作テスト() throws Exception {
		// 基本的な正規表現バリデート
		assertThat(validator.collectProblem(null, "ABC", new ArrayList<Problem>(), true), is(0));
		assertThat(validator.collectProblem(null, "012", new ArrayList<Problem>(), true), is(1));
		assertThat(validator.collectProblem(null, "ABC012", new ArrayList<Problem>(), true), is(0));
		assertThat(validator.collectProblem(null, "abc012", new ArrayList<Problem>(), true), is(1));
		assertThat(validator.collectProblem(null, "", new ArrayList<Problem>(), false), is(1));
		assertThat(validator.collectProblem(null, null, new ArrayList<Problem>(), false), is(1)); // nullは無条件に通過
		
		// 予約語バリデート
		assertThat(validator.collectProblem(null, "RESERVED1", new ArrayList<Problem>(), false), is(1));
		assertThat(validator.collectProblem(null, "RESERVED2", new ArrayList<Problem>(), false), is(1));
		assertThat(validator.collectProblem(null, "RESERVED3", new ArrayList<Problem>(), false), is(0));
	}
	

	private static class IdentifierValidatorMock extends AbstractIdentifierValidator {
		
		/**
		 * インスタンスを生成する。
		 */
		public IdentifierValidatorMock() {
			super("^[A-Z][A-Z0-9]*$", new ReservedWordsChecker() {
				
				String[] array = new String[] {
					"RESERVED1",
					"RESERVED2"
				};
				

				public boolean isReserved(String name) {
					return ArrayUtils.contains(array, name);
				}
			});
		}
		
	}
	
}
