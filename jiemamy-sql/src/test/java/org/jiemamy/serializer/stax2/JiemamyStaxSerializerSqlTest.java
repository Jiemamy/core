/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/15
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

package org.jiemamy.serializer.stax2;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.UUID;

import com.google.common.collect.Iterables;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextSqlTest;
import org.jiemamy.SqlFacet;
import org.jiemamy.model.script.AroundScriptModel;
import org.jiemamy.model.script.DefaultAroundScriptModel;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.TableModel;

/**
 * {@link JiemamyStaxSerializer}のテスト：jiemamy-sql版。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializerSqlTest {
	
	private JiemamyStaxSerializer serializer;
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyStaxSerializerSqlTest.class);
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		serializer = new JiemamyStaxSerializer();
	}
	
	/**
	 * 簡単なシリアライズ。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@Ignore("sql1.jiemamyをまだ用意してない")
	public void test01_簡単なシリアライズ() throws Exception {
		UUID tableId = UUID.fromString("5780585f-5f3d-4f72-a38b-7665042e6c00");
		UUID scriptId = UUID.fromString("c748964d-0ef5-492d-97c4-71e2124cf5d6");
		
		JiemamyContext context = new JiemamyContext(SqlFacet.PROVIDER);
		
		DefaultTableModel table = new DefaultTableModel(tableId);
		context.store(table);
		
		DefaultAroundScriptModel aroundScript = new DefaultAroundScriptModel(scriptId);
		aroundScript.setCoreModelRef(table.toReference());
		context.getFacet(SqlFacet.class).store(aroundScript);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(context, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		String expected = getXml("sql1.jiemamy");
		
		logger.info("actual  ={}", actual.replaceAll("[\r\n]", ""));
		logger.info("expected={}", expected.replaceAll("[\r\n]", ""));
		
		DetailedDiff diff = new DetailedDiff(new Diff(actual, expected));
		assertThat(diff.getAllDifferences().toString(), diff.similar(), is(true));
	}
	
	/**
	 * 簡単なJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@Ignore("sql1.jiemamyをまだ用意してない")
	public void test11_簡単なJiemamyContextのデシリアライズ結果を確認() throws Exception {
		UUID tableId = UUID.fromString("5780585f-5f3d-4f72-a38b-7665042e6c00");
		UUID scriptId = UUID.fromString("a0ed1691-bf18-46e4-bc63-5ee4343588f8");
		
		String xml = getXml("sql1.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais, SqlFacet.PROVIDER);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getTables().size(), is(1));
		assertThat(deserialized.getDataSets().size(), is(0));
		TableModel tableModel = Iterables.get(deserialized.getTables(), 0);
		assertThat(tableModel.getId(), is(tableId));
		
		SqlFacet facet = deserialized.getFacet(SqlFacet.class);
		assertThat(facet.getAroundScripts().size(), is(1));
		AroundScriptModel aroundScript = Iterables.get(facet.getAroundScripts(), 0);
		assertThat(aroundScript.getId(), is(scriptId));
		assertThat(aroundScript.getCoreModelRef().isReferenceOf(tableModel), is(true));
	}
	
	/**
	 * 適当なモデルを一杯作ってみて、それぞれのシリアライズやデシリアライズが異常終了しないことを確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test99_適当なモデルを一杯作ってみて_それぞれのシリアライズやデシリアライズが異常終了しないことを確認() throws Exception {
		File dir = new File("target/test99");
		FileUtils.deleteDirectory(dir);
		for (int i = 0; i < 30; i++) {
			logger.info("test99 - " + i);
			File file1 = new File(dir, String.format("file%03d-1.txt", i));
			if (file1.exists()) {
				file1.delete();
			}
			File file2 = new File(dir, String.format("file%03d-2.txt", i));
			if (file2.exists()) {
				file2.delete();
			}
			
			// 適当なモデルを生成
			JiemamyContext original = JiemamyContextSqlTest.random();
			
			// XMLにシリアライズしてみる(1)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(original, baos);
			String first = baos.toString(CharEncoding.UTF_8);
//			logger.info("1 = {}", first);
			FileUtils.write(file1, first);
			
			// そのXMLをデシリアライズしてみる
			ByteArrayInputStream bais = new ByteArrayInputStream(first.getBytes());
			JiemamyContext deserialized = serializer.deserialize(bais, SqlFacet.PROVIDER);
			assertThat(deserialized, is(notNullValue()));
			
			// デシリアライズされた新しいcontextを再びシリアライズしてみる(2)
			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
			serializer.serialize(deserialized, baos2);
			String second = baos2.toString(CharEncoding.UTF_8);
//			logger.info("2 = {}", second);
			FileUtils.write(file2, second);
			
			// 何度やってもXMLは全く同じモノになるハズだ
			DetailedDiff diff = new DetailedDiff(new Diff(first, second));
			// FORMAT-OFF
			String failLog = MessageFormat.format("\n1 = {0}\n2 = {1}\ndiff = {2}",
					first.replaceAll("[\r\n]", ""),
					second.replaceAll("[\r\n]", ""),
					diff.getAllDifferences().toString());
			// FORMAT-ON
			assertThat(failLog, diff.similar(), is(true));
		}
	}
	
	private String getXml(String name) {
		InputStream in = null;
		String result = null;
		try {
			in = getClass().getResourceAsStream("/org/jiemamy/serializer/" + name);
			result = IOUtils.toString(in);
		} catch (IOException e) {
		} finally {
			IOUtils.closeQuietly(in);
		}
		return result;
	}
}
