/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/05
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
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

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
import org.jiemamy.JiemamyContextTest;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.column.DefaultColumnModel;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.TableModel;

/**
 * {@link JiemamyStaxSerializer}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializerTest {
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyStaxSerializerTest.class);
	
	private JiemamyStaxSerializer serializer;
	
	private static final String LF = "\n";
	

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
	 * 簡単なJiemamyContextのシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_簡単なJiemamyContextのシリアライズ結果を確認() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		ctx.setSchemaName("schema-name");
		ctx.setDescription("");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(ctx, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// DialectClassNameはnullなので出力されない
		// schemaNameが設定通りに出力される
		// descriptionは空文字なので空要素として出力される
		
		String expected = getXml("1.jiemamy");
		
		DetailedDiff diff = new DetailedDiff(new Diff(actual, expected));
		assertThat(diff.getAllDifferences().toString(), diff.similar(), is(true));
	}
	
	/**
	 * Tableを1つ含むJiemamyContextのシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_Tableを1つ含むJiemamyContextのシリアライズ結果を確認() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		ctx.setSchemaName("schema-name");
		ctx.setDescription("");
		
		UUID id = UUID.fromString("d23695f8-76dd-4f8c-b5a2-1e02087ba44d");
		DefaultTableModel t = new DefaultTableModel(id);
		ctx.store(t);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(ctx, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// DialectClassNameはnullなので出力されない
		// schemaNameが設定通りに出力される
		// descriptionは空文字なので空要素として出力される
		
		String expected = getXml("2.jiemamy");
		
		DetailedDiff diff = new DetailedDiff(new Diff(actual, expected));
		assertThat(diff.getAllDifferences().toString(), diff.similar(), is(true));
	}
	
	/**
	 * Columnを1つ含むTableを1つ含むJiemamyContextのシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_Columnを1つ含むTableを1つ含むJiemamyContextのシリアライズ結果を確認() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		ctx.setDialectClassName(null); // null → 要素欠損
		ctx.setSchemaName("schema-name");
		ctx.setDescription(""); // 空文字列 → 空要素
		
		UUID tid = UUID.fromString("d23695f8-76dd-4f8c-b5a2-1e02087ba44d");
		DefaultTableModel t = new DefaultTableModel(tid);
		t.setName("foo");
		
		UUID cid = UUID.fromString("58a57dcc-7745-4718-b03d-0143eaaa8af3");
		DefaultColumnModel c = new DefaultColumnModel(cid);
		c.setName("bar");
		c.setLogicalName("baz");
		c.setDescription("");
		t.store(c);
		ctx.store(t);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(ctx, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// DialectClassNameはnullなので出力されない
		// schemaNameが設定通りに出力される
		// descriptionは空文字なので空要素として出力される
		
		String expected = getXml("3.jiemamy");
		DetailedDiff diff = new DetailedDiff(new Diff(actual, expected));
		assertThat(diff.getAllDifferences().toString(), diff.similar(), is(true));
		
//		logger.info("actual  ={}", actual.replaceAll("[\n\r]", ""));
//		logger.info("expected={}", expected.replaceAll("[\n\r]", ""));
	}
	
	/**
	 * 簡単なJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11_簡単なJiemamyContextのデシリアライズ結果を確認() throws Exception {
		String xml = getXml("1.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		
		assertThat(deserialized.getDialectClassName(), is(nullValue())); // 要素欠損 → null
		assertThat(deserialized.getSchemaName(), is("schema-name"));
		assertThat(deserialized.getDescription(), is("")); // 空要素 → 空文字列
	}
	
	/**
	 * 簡単なJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12_Tableを1つ含むJiemamyContextのデシリアライズ結果を確認() throws Exception {
		String xml = getXml("2.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		
		assertThat(deserialized.getDialectClassName(), is(nullValue())); // 要素欠損 → null
		assertThat(deserialized.getSchemaName(), is("schema-name"));
		assertThat(deserialized.getDescription(), is("")); // 空要素 → 空文字列
		assertThat(deserialized.getTables().size(), is(1));
		TableModel tableModel = deserialized.getTables().iterator().next();
		
		assertThat(tableModel.getId(), is(UUID.fromString("d23695f8-76dd-4f8c-b5a2-1e02087ba44d")));
		assertThat(tableModel.getColumns().size(), is(0));
	}
	
	/**
	 * 簡単なJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test13_Columnを1つ含むTableを1つ含むJiemamyContextのデシリアライズ結果を確認() throws Exception {
		String xml = getXml("3.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getTables().size(), is(1));
		
		TableModel tableModel = deserialized.getTables().iterator().next();
		assertThat(tableModel.getId(), is(UUID.fromString("d23695f8-76dd-4f8c-b5a2-1e02087ba44d")));
		assertThat(tableModel.getName(), is("foo"));
		assertThat(tableModel.getLogicalName(), is(nullValue()));
		assertThat(tableModel.getDescription(), is(nullValue()));
		assertThat(tableModel.getColumns().size(), is(1));
		
		ColumnModel columnModel = tableModel.getColumns().get(0);
		assertThat(columnModel.getId(), is(UUID.fromString("58a57dcc-7745-4718-b03d-0143eaaa8af3")));
		assertThat(columnModel.getName(), is("bar"));
		assertThat(columnModel.getLogicalName(), is("baz"));
		assertThat(columnModel.getDescription(), is(""));
	}
	
	/**
	 * 適当なモデルを一杯作ってみて、それぞれのシリアライズやデシリアライズが異常終了しないことを確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@Ignore("実装が不完全なので通らない")
	public void test99_適当なモデルを一杯作ってみて_それぞれのシリアライズやデシリアライズが異常終了しないことを確認() throws Exception {
		for (int i = 0; i < 100; i++) {
			// 適当なモデルを生成
			JiemamyContext original = JiemamyContextTest.random();
			
			// XMLにシリアライズして、logに出してみる
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(original, baos);
			String first = baos.toString(CharEncoding.UTF_8);
			logger.info("1 = {}", first);
			
			// そのXMLをデシリアライズしてみる
			ByteArrayInputStream bais = new ByteArrayInputStream(first.getBytes());
			JiemamyContext deserialized = serializer.deserialize(bais);
			assertThat(deserialized, is(notNullValue()));
			
			// デシリアライズされた新しいcontextを再びシリアライズして、logに出してみる
			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
			serializer.serialize(deserialized, baos2);
			String second = baos2.toString(CharEncoding.UTF_8);
			logger.info("2 = {}", second);
			
			// 何度やってもXMLは全く同じモノになるハズだ
			DetailedDiff diff = new DetailedDiff(new Diff(first, second));
			assertThat(diff.getAllDifferences().toString(), diff.similar(), is(true));
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
