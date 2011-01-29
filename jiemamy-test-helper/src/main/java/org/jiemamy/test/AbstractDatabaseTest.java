/*
 * Copyright 2011 Jiemamy Project and the Others.
 * Created on 2011/01/29
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
package org.jiemamy.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractDatabaseTest {
	
	private Properties props;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		InputStream in = null;
		try {
			in = AbstractDatabaseTest.class.getResourceAsStream(getPropertiesFilePath());
			if (in == null) {
				in = AbstractDatabaseTest.class.getResourceAsStream(getPropertiesFilePathForCI());
			}
			assertThat(in, is(notNullValue()));
			props = new Properties();
			props.load(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testabs00_connection() throws Exception {
		Class.forName(getDriverClassName());
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getConnectionUri(), getUsername(), getPassword());
			assertThat(connection, is(notNullValue()));
		} finally {
			DbUtils.closeQuietly(connection);
		}
	}
	
	protected String getConnectionUri() {
		return props.getProperty("uri");
	}
	
	protected String getDriverClassName() {
		return props.getProperty("driverClass");
	}
	
	protected String getJarPath() {
		return props.getProperty("driverJar");
	}
	
	protected String getPassword() {
		return props.getProperty("password");
	}
	
	protected String getPropertiesFilePath() {
		return "/database.properties";
	}
	
	protected String getPropertiesFilePathForCI() {
		return "/database_ci.properties";
	}
	
	protected String getUsername() {
		return props.getProperty("user");
	}
}