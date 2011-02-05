/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/02/03
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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.Collection;

import com.google.common.collect.Iterables;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.validator.Problem;
import org.jiemamy.validator.impl.CyclicForeignReferenceValidator.CyclicForeignReferenceProblem;

/**
 * {@link CyclicForeignReferenceValidator}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class CyclicForeignReferenceValidatorTest {
	
	private static Logger logger = LoggerFactory.getLogger(CyclicForeignReferenceValidatorTest.class);
	
	private static final String[] PATHS_CYCLIC = {
		"/org/jiemamy/validator/cyclic2.jiemamy",
		"/org/jiemamy/validator/cyclic3.jiemamy",
		"/org/jiemamy/validator/cyclic4.jiemamy"
	};
	
	private static final String[] PATHS_PSEUDO_CYCLIC = {
		"/org/jiemamy/validator/selfref.jiemamy",
		"/org/jiemamy/validator/noncyclic4.jiemamy",
		"/org/jiemamy/validator/empdept.jiemamy",
	};
	
	private CyclicForeignReferenceValidator validator;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		validator = new CyclicForeignReferenceValidator();
	}
	
	/**
	 * 循環FKを検出できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_循環FKを検出できる() throws Exception {
		InputStream in = null;
		for (String path : PATHS_CYCLIC) {
			logger.debug(path);
			try {
				in = CyclicForeignReferenceValidatorTest.class.getResourceAsStream(path);
				JiemamyContext deserialized = JiemamyContext.findSerializer().deserialize(in);
				Collection<Problem> problems = validator.validate(deserialized);
				assertThat(problems.size(), is(1));
				Problem problem = Iterables.getOnlyElement(problems);
				assertThat(problem, is(instanceOf(CyclicForeignReferenceProblem.class)));
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
	}
	
	/**
	 * 自己参照や循環でないものは循環FKとして検出しない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_自己参照や循環でないものは循環FKとして検出しない() throws Exception {
		InputStream in = null;
		for (String path : PATHS_PSEUDO_CYCLIC) {
			try {
				in = CyclicForeignReferenceValidatorTest.class.getResourceAsStream(path);
				JiemamyContext deserialized = JiemamyContext.findSerializer().deserialize(in);
				Collection<Problem> problems = validator.validate(deserialized);
				assertThat(problems.isEmpty(), is(true));
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
	}
}
