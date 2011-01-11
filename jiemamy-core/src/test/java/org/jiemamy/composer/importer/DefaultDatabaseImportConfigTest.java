/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.composer.importer;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.dialect.MockDialect;

/**
 * {@link DefaultDatabaseImportConfig}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultDatabaseImportConfigTest {
	
	private DefaultDatabaseImportConfig config;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		config = new DefaultDatabaseImportConfig();
	}
	
	/**
	 * 作ったインスタンスの挙動チェック。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_作ったインスタンスの挙動チェック() throws Exception {
		assertThat(config.getDriverClassName(), is(nullValue()));
		assertThat(config.getPassword(), is(nullValue()));
		assertThat(config.getUsername(), is(nullValue()));
		assertThat(config.getUri(), is(nullValue()));
		assertThat(config.getDriverJarPaths(), is(notNullValue()));
		assertThat(config.getDriverJarPaths().length, is(0));
		assertThat(config.getDialect(), is(nullValue()));
		assertThat(config.getEntityTypes(), is(nullValue()));
		assertThat(config.getSchema(), is(nullValue()));
		assertThat(config.getSelectedEntities(), is(nullValue()));
		assertThat(config.isImportDataSet(), is(false));
		
		config.setDriverClassName("d");
		config.setDriverJarPaths(new URL[] {
			new URL("http://jiemamy.org/")
		});
		config.setPassword("p");
		config.setUsername("user");
		config.setUri("uri");
		config.setDialect(new MockDialect());
		config.setEntityTypes(new String[] {
			"et"
		});
		config.setSchema("sch");
		config.setSelectedEntities(new String[] {
			"V"
		});
		config.setImportDataSet(true);
		
		assertThat(config.getDriverClassName(), is("d"));
		assertThat(config.getPassword(), is("p"));
		assertThat(config.getUsername(), is("user"));
		assertThat(config.getUri(), is("uri"));
		assertThat(config.getDriverJarPaths().length, is(1));
		assertThat(config.getDriverJarPaths()[0], is(new URL("http://jiemamy.org/")));
		assertThat(config.getDialect(), is(instanceOf(MockDialect.class)));
		assertThat(config.getEntityTypes().length, is(1));
		assertThat(config.getEntityTypes()[0], is("et"));
		assertThat(config.getSchema(), is("sch"));
		assertThat(config.getSelectedEntities().length, is(1));
		assertThat(config.getSelectedEntities()[0], is("V"));
		assertThat(config.isImportDataSet(), is(true));
		
		config.setDriverJarPaths(null);
		
		assertThat(config.getDriverJarPaths(), is(notNullValue()));
		assertThat(config.getDriverJarPaths().length, is(0));
	}
}
