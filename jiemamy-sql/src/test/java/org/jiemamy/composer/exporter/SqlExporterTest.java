/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/07/12
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextTest;
import org.jiemamy.SqlFacet;
import org.jiemamy.composer.Exporter;
import org.jiemamy.dialect.MockDialect;

/**
 * {@link SqlExporter}のテストクラス。
 * 
 * @author daisuke
 */
public class SqlExporterTest {
	
	private static Logger logger = LoggerFactory.getLogger(SqlExporterTest.class);
	
	/** ${WORKSPACE}/org.jiemamy.composer/target/sqlExporterTest1.sql */
	private static final File OUTPUT_FILE = new File("./target/sqlExporterTest1.sql");
	
	/** ${WORKSPACE}/org.jiemamy.composer/target/notExists/sqlExporterTest2.sql */
	private static final File OUTPUT_FILE_IN_NOT_EXISTS_DIR = new File("./target/notExists/sqlExporterTest2.sql");
	
	private static final File NOT_EXISTS_DIR = new File("./target/notExists");
	
	/** テスト対象のエクスポータ */
	private Exporter<SqlExportConfig> exporter = new SqlExporter();
	

	/**
	 * モデルからSQLファイルがエクスポートできることを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_() throws Exception {
		JiemamyContext context = JiemamyContextTest.random(SqlFacet.PROVIDER);
		
		deleteFile(OUTPUT_FILE);
		assertThat(OUTPUT_FILE.exists(), is(false));
		
		context.setDialectClassName(MockDialect.class.getName());
		
		BufferedReader reader = null;
		try {
			DefaultSqlExportConfig config = new DefaultSqlExportConfig();
			config.setOutputFile(OUTPUT_FILE);
			config.setOverwrite(true);
			exporter.exportModel(context, config);
			
			assertThat(OUTPUT_FILE.exists(), is(true));
			
			reader = new BufferedReader(new FileReader(OUTPUT_FILE));
			String line;
			while ((line = reader.readLine()) != null) {
				logger.info(line);
			}
			
			// UNDONE sqlExporterTest1.sqlの内容確認
			
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	/**
	 * モデルからSQLファイルがエクスポートできることを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_() throws Exception {
		JiemamyContext context = JiemamyContextTest.random(SqlFacet.PROVIDER);
		
		FileUtils.deleteDirectory(NOT_EXISTS_DIR);
		assertThat(NOT_EXISTS_DIR.exists(), is(false));
		
		context.setDialectClassName(MockDialect.class.getName());
		
		BufferedReader reader = null;
		try {
			DefaultSqlExportConfig config = new DefaultSqlExportConfig();
			config.setOutputFile(OUTPUT_FILE_IN_NOT_EXISTS_DIR);
			config.setOverwrite(true);
			exporter.exportModel(context, config);
			
			assertThat(OUTPUT_FILE_IN_NOT_EXISTS_DIR.exists(), is(true));
			
			reader = new BufferedReader(new FileReader(OUTPUT_FILE_IN_NOT_EXISTS_DIR));
			String line;
			while ((line = reader.readLine()) != null) {
				logger.info(line);
			}
			
			// UNDONE sqlExporterTest2.sqlの内容確認
			
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	private void deleteFile(File file) {
		if (file.exists() == false) {
			return;
		}
		if (file.delete() == false) {
			fail("Cannot delete file: " + file.getPath());
		}
	}
}
