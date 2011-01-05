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
import java.util.UUID;

import org.apache.commons.lang.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.dbo.TableModel;

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
		logger.info(actual);
		
		// インデントは2であること
		// DialectClassNameはnullなので出力されない
		// schemaNameが設定通りに出力される
		// descriptionは空文字なので空要素として出力される
		
		// FORMAT-OFF
		String expected = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<jiemamy xmlns=\"http://jiemamy.org/xml/ns/core\" version=\"0.3.0-SNAPSHOT\">" + LF
				+ "  <schemaName>schema-name</schemaName>" + LF
				+ "  <description/>" + LF
				+ "  <dbobjects/>" + LF
				+ "  <dataSets/>" + LF
				+ "</jiemamy>" + LF;
		// FORMAT-ON
		assertThat(actual, is(expected));
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
		
		UUID id = UUID.randomUUID();
		DefaultTableModel t = new DefaultTableModel(id);
		ctx.store(t);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(ctx, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// インデントは2であること
		// DialectClassNameはnullなので出力されない
		// schemaNameが設定通りに出力される
		// descriptionは空文字なので空要素として出力される
		
		// FORMAT-OFF
		String expected = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<jiemamy xmlns=\"http://jiemamy.org/xml/ns/core\" version=\"0.3.0-SNAPSHOT\">" + LF
				+ "  <schemaName>schema-name</schemaName>" + LF
				+ "  <description/>" + LF
				+ "  <dbobjects>" + LF
				+ "    <table" +
							" id=\"" + id.toString() + "\"" +
							" class=\"" + t.getClass().getName() + "\">" + LF
				+ "      <columns/>" + LF
				+ "      <constraints/>" + LF
				+ "    </table>" + LF
				+ "  </dbobjects>" + LF
				+ "  <dataSets/>" + LF
				+ "</jiemamy>" + LF;
		// FORMAT-ON
		logger.info("actual={}", actual);
		logger.info("expected={}", expected);
		
		assertThat(actual, is(expected));
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
		
		UUID tid = UUID.randomUUID();
		DefaultTableModel t = new DefaultTableModel(tid);
		t.setName("foo");
		
		UUID cid = UUID.randomUUID();
		DefaultColumnModel c = new DefaultColumnModel(cid);
		c.setName("bar");
		c.setLogicalName("baz");
		c.setDescription("");
		t.store(c);
		ctx.store(t);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(ctx, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// インデントは2であること
		// DialectClassNameはnullなので出力されない
		// schemaNameが設定通りに出力される
		// descriptionは空文字なので空要素として出力される
		
		// FORMAT-OFF
		String expected = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<jiemamy xmlns=\"http://jiemamy.org/xml/ns/core\" version=\"0.3.0-SNAPSHOT\">" + LF
//				+ "  <dialect>com.example.Dialect</dialect>" + LF // nullなので要素欠損
				+ "  <schemaName>schema-name</schemaName>" + LF
				+ "  <description/>" + LF // 空文字列なので空要素
				+ "  <dbobjects>" + LF
				+ "    <table" +
							" id=\"" + tid.toString() + "\"" +
							" class=\"" + t.getClass().getName() + "\">" + LF
				+ "      <name>foo</name>" + LF
				+ "      <columns>" + LF
				+ "        <column" +
								" id=\"" + cid.toString() + "\"" +
								" class=\"" + c.getClass().getName() + "\">" + LF
				+ "          <name>bar</name>" + LF
				+ "          <logicalName>baz</logicalName>" + LF
				+ "          <description/>" + LF
				+ "        </column>" + LF
				+ "      </columns>" + LF
				+ "      <constraints/>" + LF
				+ "    </table>" + LF
				+ "  </dbobjects>" + LF
				+ "  <dataSets/>" + LF
				+ "</jiemamy>" + LF;
		// FORMAT-ON
		logger.info("actual={}", actual);
		logger.info("expected={}", expected);
		
		assertThat(actual, is(expected));
	}
	
	/**
	 * 簡単なJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11_簡単なJiemamyContextのデシリアライズ結果を確認() throws Exception {
		// FORMAT-OFF
		String xml = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<jiemamy xmlns=\"http://jiemamy.org/xml/ns/core\" version=\"0.3.0-SNAPSHOT\">" + LF
//				+ "  <dialect>com.example.Dialect</dialect>" + LF // 要素欠損
				+ "  <schemaName>schema-name</schemaName>" + LF
				+ "  <description/>" + LF // 空要素
				+ "  <dbobjects/>" + LF
				+ "  <dataSets/>" + LF
				+ "</jiemamy>" + LF;
		// FORMAT-ON
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
		// FORMAT-OFF
		String xml = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<jiemamy xmlns=\"http://jiemamy.org/xml/ns/core\" version=\"0.3.0-SNAPSHOT\">" + LF
				+ "  <schemaName>schema-name</schemaName>" + LF
				+ "  <description/>" + LF
				+ "  <dbobjects>" + LF
				+ "    <table" +
							" id=\"94f8923f-9dfe-406b-ab35-d9a7c0b88149\"" +
							" class=\"org.jiemamy.model.dbo.DefaultTableModel\">" + LF
				+ "      <columns/>" + LF
				+ "      <constraints/>" + LF
				+ "    </table>" + LF
				+ "  </dbobjects>" + LF
				+ "  <dataSets/>" + LF
				+ "</jiemamy>" + LF;
		// FORMAT-ON
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		
		assertThat(deserialized.getDialectClassName(), is(nullValue())); // 要素欠損 → null
		assertThat(deserialized.getSchemaName(), is("schema-name"));
		assertThat(deserialized.getDescription(), is("")); // 空要素 → 空文字列
		assertThat(deserialized.getTables().size(), is(1));
		TableModel tableModel = deserialized.getTables().iterator().next();
		
		assertThat(tableModel.getId(), is(UUID.fromString("94f8923f-9dfe-406b-ab35-d9a7c0b88149")));
		assertThat(tableModel.getColumns().size(), is(0));
	}
}
