/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
package org.jiemamy.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.validator.Problem.Severity;

/**
 * {@link CompositeValidator}のテストクラス。
 * 
 * @author daisuke
 */
public class CompositeValidatorTest {
	
	private CompositeValidator validator;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		validator = new CompositeValidator();
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
	 * 内蔵した2つのバリデータが両方とも実行される。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_内蔵した2つのバリデータが両方とも実行される() throws Exception {
		validator.getValidators().add(new MockValidator1());
		validator.getValidators().add(new MockValidator2());
		
		Collection<? extends Problem> result = validator.validate(mock(JiemamyContext.class));
		
		assertThat(result.size(), is(4));
		Multimap<Severity, Object> m = ArrayListMultimap.create();
		for (Problem problem : result) {
			m.put(problem.getSeverity(), new Object());
		}
		
		assertThat(m.get(Severity.ERROR).size(), is(2));
		assertThat(m.get(Severity.WARN).size(), is(2));
	}
	
	
	private static class MockProblem1 extends AbstractProblem {
		
		MockProblem1(String string) {
			super(null, "EMOCK1");
		}
	}
	
	private static class MockProblem2 extends AbstractProblem {
		
		MockProblem2(String string) {
			super(null, "WMOCK2");
		}
	}
	
	private static class MockValidator1 implements Validator {
		
		public Collection<? extends Problem> validate(JiemamyContext context) {
			return Arrays.asList(new MockProblem1("one"), new MockProblem1("two"));
		}
		
	}
	
	private static class MockValidator2 implements Validator {
		
		public Collection<? extends Problem> validate(JiemamyContext context) {
			return Arrays.asList(new MockProblem2("one"), new MockProblem2("two"));
		}
		
	}
}
