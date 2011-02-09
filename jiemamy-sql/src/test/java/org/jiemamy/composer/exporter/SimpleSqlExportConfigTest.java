/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy.composer.exporter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link SimpleSqlExportConfig}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleSqlExportConfigTest {
	
	private SimpleSqlExportConfig config;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		config = new SimpleSqlExportConfig();
	}
	
	/**
	 * 作ったインスタンスのチェック。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_作ったインスタンスのチェック() throws Exception {
		assertThat(config.emitCreateSchemaStatement(), is(false));
		assertThat(config.emitDropStatements(), is(false));
		assertThat(config.getDataSetIndex(), is(0));
		assertThat(config.getOutputFile(), is(nullValue()));
		assertThat(config.isOverwrite(), is(false));
		
		config.setEmitCreateSchema(true);
		config.setEmitDropStatements(true);
		config.setDataSetIndex(100);
		config.setOutputFile(mock(File.class));
		config.setOverwrite(true);
		
		assertThat(config.emitCreateSchemaStatement(), is(true));
		assertThat(config.emitDropStatements(), is(true));
		assertThat(config.getDataSetIndex(), is(100));
		assertThat(config.getOutputFile(), is(notNullValue()));
		assertThat(config.isOverwrite(), is(true));
		
		try {
			config.setOutputFile(null);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}
}
