/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;

import com.google.common.collect.Iterables;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextTest;
import org.jiemamy.SimpleJmMetadata;
import org.jiemamy.SqlFacet;
import org.jiemamy.composer.Exporter;
import org.jiemamy.dialect.MockDialect;
import org.jiemamy.model.parameter.ParameterKey;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.serializer.JiemamySerializer;

/**
 * {@link SqlExporter}のテストクラス。
 * 
 * @author daisuke
 */
public class SqlExporterTest {
	
	private static Logger logger = LoggerFactory.getLogger(SqlExporterTest.class);
	
	/** ${WORKSPACE}/org.jiemamy.composer/target/sqlExporterTest1.sql */
	private static final File OUTPUT_FILE = new File("./target/testresult/sqlExporterTest1.sql");
	
	/** ${WORKSPACE}/org.jiemamy.composer/target/notExists/sqlExporterTest2.sql */
	private static final File OUTPUT_FILE_IN_NOT_EXISTS_DIR = new File(
			"./target/testresult/notExists/sqlExporterTest2.sql");
	
	/** ${WORKSPACE}/org.jiemamy.composer/target/notExists/sqlExporterTest3.sql */
	private static final File OUTPUT_FILE_ALREADY_EXISTS = new File(
			"./target/testresult/notExists/sqlExporterTest3.sql");
	
	private static final File NOT_EXISTS_DIR = new File("./target/testresult/notExists");
	
	/** テスト対象のエクスポータ */
	private Exporter<SqlExportConfig> exporter = new SqlExporter();
	

	/**
	 * CORE-197検証コード。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_core197() throws Exception {
		JiemamySerializer serializer = JiemamyContext.findSerializer();
		InputStream in = null;
		try {
			in = SqlExporterTest.class.getResourceAsStream("/datafile/core-197.jiemamy");
			JiemamyContext context = serializer.deserialize(in, SqlFacet.PROVIDER);
			
			JmTable table = Iterables.getOnlyElement(context.getTables());
			assertThat(table.getParam(ParameterKey.DISABLED), is(true));
			
			File output = new File("./target/testresult/sqlExporterTest_core197.sql");
			
			SimpleSqlExportConfig config = new SimpleSqlExportConfig();
			config.setOutputFile(output);
			config.setOverwrite(true);
			
			boolean exportModel = exporter.exportModel(context, config);
			assertThat(exportModel, is(true));
			
			String content = FileUtils.readFileToString(output);
			logger.info(content);
			assertThat(content, is(""));
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * モデルからSQLファイルがエクスポートできることを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_モデルからSQLファイルがエクスポートできる() throws Exception {
		JiemamyContext context = JiemamyContextTest.random(SqlFacet.PROVIDER);
		
		deleteFile(OUTPUT_FILE);
		assertThat(OUTPUT_FILE.exists(), is(false));
		
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDialectClassName(MockDialect.class.getName());
		context.setMetadata(meta);
		
		BufferedReader reader = null;
		try {
			SimpleSqlExportConfig config = new SimpleSqlExportConfig();
			config.setOutputFile(OUTPUT_FILE);
			config.setOverwrite(true);
			exporter.exportModel(context, config);
			
			assertThat(OUTPUT_FILE.exists(), is(true));
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	/**
	 * 出力先の親ディレクトリが存在しなくてもモデルからSQLファイルがエクスポートできることを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_出力先の親ディレクトリが存在しなくてもモデルからSQLファイルがエクスポートできる() throws Exception {
		JiemamyContext context = JiemamyContextTest.random(SqlFacet.PROVIDER);
		
		FileUtils.deleteDirectory(NOT_EXISTS_DIR);
		assertThat(NOT_EXISTS_DIR.exists(), is(false));
		
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDialectClassName(MockDialect.class.getName());
		context.setMetadata(meta);
		
		BufferedReader reader = null;
		try {
			SimpleSqlExportConfig config = new SimpleSqlExportConfig();
			config.setOutputFile(OUTPUT_FILE_IN_NOT_EXISTS_DIR);
			config.setOverwrite(true);
			exporter.exportModel(context, config);
			
			assertThat(OUTPUT_FILE_IN_NOT_EXISTS_DIR.exists(), is(true));
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	/**
	 * Overwrite(false)の際、出力先ファイルが存在すると何もしない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_() throws Exception {
		JiemamyContext context = JiemamyContextTest.random(SqlFacet.PROVIDER);
		
		FileUtils.writeStringToFile(OUTPUT_FILE_ALREADY_EXISTS, "hoge");
		assertThat(OUTPUT_FILE_ALREADY_EXISTS.exists(), is(true));
		
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDialectClassName(MockDialect.class.getName());
		context.setMetadata(meta);
		
		BufferedReader reader = null;
		try {
			SimpleSqlExportConfig config = new SimpleSqlExportConfig();
			config.setOutputFile(OUTPUT_FILE_ALREADY_EXISTS);
			config.setOverwrite(false);
			boolean exportModel = exporter.exportModel(context, config);
			
			assertThat(exportModel, is(false));
			assertThat(OUTPUT_FILE_ALREADY_EXISTS.exists(), is(true));
			assertThat(FileUtils.readFileToString(OUTPUT_FILE_ALREADY_EXISTS), is("hoge"));
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	/**
	 * Overwrite(true)の際、出力先ファイルが存在すると上書きとなる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_() throws Exception {
		JiemamyContext context = JiemamyContextTest.random(SqlFacet.PROVIDER);
		
		FileUtils.writeStringToFile(OUTPUT_FILE_ALREADY_EXISTS, "hoge");
		assertThat(OUTPUT_FILE_ALREADY_EXISTS.exists(), is(true));
		
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDialectClassName(MockDialect.class.getName());
		context.setMetadata(meta);
		
		BufferedReader reader = null;
		try {
			SimpleSqlExportConfig config = new SimpleSqlExportConfig();
			config.setOutputFile(OUTPUT_FILE_ALREADY_EXISTS);
			config.setOverwrite(true);
			boolean exportModel = exporter.exportModel(context, config);
			
			assertThat(exportModel, is(true));
			assertThat(OUTPUT_FILE_ALREADY_EXISTS.exists(), is(true));
			assertThat(FileUtils.readFileToString(OUTPUT_FILE_ALREADY_EXISTS), is(not("hoge")));
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	/**
	 * CORE-200検証コード。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_core200() throws Exception {
		JiemamySerializer serializer = JiemamyContext.findSerializer();
		InputStream in = null;
		try {
			in = SqlExporterTest.class.getResourceAsStream("/datafile/core-200.jiemamy");
			JiemamyContext context = serializer.deserialize(in, SqlFacet.PROVIDER);
			
			File output = new File("./target/testresult/sqlExporterTest_core200.sql");
			
			SimpleSqlExportConfig config = new SimpleSqlExportConfig();
			config.setOutputFile(output);
			config.setOverwrite(true);
			
			config.setDataSetIndex(-1);
			assertThat(exporter.exportModel(context, config), is(true));
			config.setDataSetIndex(0);
			assertThat(exporter.exportModel(context, config), is(true));
			config.setDataSetIndex(1);
			assertThat(exporter.exportModel(context, config), is(true));
			try {
				config.setDataSetIndex(2);
				exporter.exportModel(context, config);
				fail();
			} catch (IllegalArgumentException e) {
				// success;
			}
		} finally {
			IOUtils.closeQuietly(in);
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
