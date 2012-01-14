/*
 * Copyright 2011-2012 Jiemamy Project and the Others.
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
import static org.junit.Assume.assumeThat;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.internal.AssumptionViolatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.composer.importer.SimpleDbImportConfig;
import org.jiemamy.dialect.Dialect;

/**
 * 実データベースに接続して行うインテグレーションテスト用の抽象実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractDatabaseTest {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractDatabaseTest.class);
	
	private Properties props;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		String hostName = InetAddress.getLocalHost().getHostName();
		
		InputStream in = null;
		try {
			in = AbstractDatabaseTest.class.getResourceAsStream(getPropertiesFilePath(hostName));
			if (in == null) {
				if (hostName.equals("phoenix.jiemamy.org")) {
					throw new AssertionError();
				}
				logger.warn("Database Properties ... NOT READY");
				logger.warn("  -- please deploy database properties file");
			} else {
				logger.info("Database Properties ... ready");
				props = new Properties();
				props.load(in);
			}
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * プロパティファイルから読み出した情報を利用して {@link Connection} を返す。
	 * 
	 * @return {@link ConnectException}
	 * @throws ClassNotFoundException ドライバクラスが見つからなかった場合
	 * @throws AssumptionViolatedException プロパティファイルがロードできなかった場合
	 */
	protected Connection getConnection() throws ClassNotFoundException {
		assumeThat(props, is(notNullValue()));
		Class.forName(getDriverClassName());
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getConnectionUri(), getUsername(), getPassword());
		} catch (SQLException e) {
			// ignore
		}
		assumeThat(connection, is(notNullValue()));
		return connection;
	}
	
	/**
	 * プロパティファイルから読み出したDB接続URLを返す。
	 * 
	 * @return DB接続URL
	 * @throws AssumptionViolatedException プロパティファイルがロードできなかった場合
	 */
	protected String getConnectionUri() {
		assumeThat(props, is(notNullValue()));
		return props.getProperty("uri");
	}
	
	/**
	 * プロパティファイルから読み出したドライバのクラス名を返す。
	 * 
	 * @return ドライバのクラス名
	 * @throws AssumptionViolatedException プロパティファイルがロードできなかった場合
	 */
	protected String getDriverClassName() {
		assumeThat(props, is(notNullValue()));
		return props.getProperty("driverClass");
	}
	
	/**
	 * プロパティファイルから読み出したドライバJARのパスを返す。
	 * 
	 * @return ドライバJARのパス
	 * @throws AssumptionViolatedException プロパティファイルがロードできなかった場合
	 */
	protected String getJarPath() {
		assumeThat(props, is(notNullValue()));
		return props.getProperty("driverJar");
	}
	
	/**
	 * プロパティファイルから読み出したパスワード名を返す。
	 * 
	 * @return パスワード
	 * @throws AssumptionViolatedException プロパティファイルがロードできなかった場合
	 */
	protected String getPassword() {
		assumeThat(props, is(notNullValue()));
		return props.getProperty("password");
	}
	
	/**
	 * DB接続情報プロパティファイルへのclasspathパスを返す。
	 * 
	 * @param hostName テスト実行中のホスト名
	 * @return プロパティファイルへのclasspathパス
	 */
	protected abstract String getPropertiesFilePath(String hostName);
	
	/**
	 * プロパティファイルから読み出したユーザ名を返す。
	 * 
	 * @return ユーザ名
	 * @throws AssumptionViolatedException プロパティファイルがロードできなかった場合
	 */
	protected String getUsername() {
		assumeThat(props, is(notNullValue()));
		return props.getProperty("user");
	}
	
	/**
	 * プロパティファイルから読み出した情報を利用して {@link SimpleDbImportConfig} を返す。
	 * 
	 * @param dialect {@link Dialect} 
	 * @param urls ドライバJARのURLの配列
	 * @return {@link SimpleDbImportConfig}
	 * @throws AssumptionViolatedException プロパティファイルがロードできなかった場合
	 */
	protected SimpleDbImportConfig newDatabaseImportConfig(Dialect dialect, URL[] urls) {
		SimpleDbImportConfig config = new SimpleDbImportConfig();
		config.setDriverClassName(getDriverClassName());
		config.setUsername(getUsername());
		config.setPassword(getPassword());
		config.setUri(getConnectionUri());
		config.setDriverJarPaths(urls);
		config.setDialect(dialect);
		return config;
	}
}
