/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/13
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
package org.jiemamy.model.parameter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link IntegerConverter}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class IntegerConverterTest {
	
	private IntegerConverter converter;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		converter = new IntegerConverter();
	}
	
	/**
	 * {@link IntegerConverter#toString(Integer)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_toString() throws Exception {
		assertThat(converter.toString(0), is("0"));
		assertThat(converter.toString(1), is("1"));
		assertThat(converter.toString(5), is("5"));
		assertThat(converter.toString(100), is("100"));
		assertThat(converter.toString(-10), is("-10"));
	}
	
	/**
	 * {@link IntegerConverter#valueOf(String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_valueOf() throws Exception {
		assertThat(converter.valueOf("0"), is(0));
		assertThat(converter.valueOf("1"), is(1));
		assertThat(converter.valueOf("5"), is(5));
		assertThat(converter.valueOf("100"), is(100));
		assertThat(converter.valueOf("-10"), is(-10));
	}
}
