/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextTest;
import org.jiemamy.SimpleJmMetadata;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.column.SimpleJmColumn;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmForeignKeyConstraint.ReferentialAction;
import org.jiemamy.model.constraint.SimpleJmForeignKeyConstraint;
import org.jiemamy.model.constraint.SimpleJmPrimaryKeyConstraint;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;
import org.jiemamy.model.table.SimpleJmTable;
import org.jiemamy.serializer.stax.JiemamyStaxSerializer;

/**
 * {@link JiemamyStaxSerializer}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializerTest {
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyStaxSerializerTest.class);
	
	private JiemamyStaxSerializer serializer;
	
	
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
		JiemamyContext context = new JiemamyContext();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(context, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		String expected = getXml("core1.jiemamy");
//		logger.debug(actual);
//		logger.debug(expected);
		
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
		JiemamyContext context = new JiemamyContext();
		
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setSchemaName("schema-name");
		meta.setDescription("");
		context.setMetadata(meta);
		
		UUID tid = UUID.fromString("d23695f8-76dd-4f8c-b5a2-1e02087ba44d");
		SimpleJmTable t = new SimpleJmTable(tid);
		t.setName("foo");
		context.store(t);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(context, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// DialectClassNameはnullなので出力されない
		// schemaNameが設定通りに出力される
		// descriptionは空文字なので空要素として出力される
		
		String expected = getXml("core2.jiemamy");
		
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
		JiemamyContext context = new JiemamyContext();
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDialectClassName(null); // null → 要素欠損
		meta.setSchemaName("schema-name");
		meta.setDescription(""); // 空文字列 → 空要素
		context.setMetadata(meta);
		
		UUID tid = UUID.fromString("d23695f8-76dd-4f8c-b5a2-1e02087ba44d");
		SimpleJmTable t = new SimpleJmTable(tid);
		t.setName("foo");
		
		UUID cid = UUID.fromString("58a57dcc-7745-4718-b03d-0143eaaa8af3");
		SimpleJmColumn c = new SimpleJmColumn(cid);
		c.setName("bar");
		c.setLogicalName("baz");
		c.setDescription("");
		c.setDataType(SimpleDataType.of(RawTypeCategory.INTEGER));
		t.store(c);
		context.store(t);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(context, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// DialectClassNameはnullなので出力されない
		// schemaNameが設定通りに出力される
		// descriptionは空文字なので空要素として出力される
		
		String expected = getXml("core3.jiemamy");
//		logger.info("actual  ={}", actual.replaceAll("[\n\r]", ""));
//		logger.info("expected={}", expected.replaceAll("[\n\r]", ""));
		DetailedDiff diff = new DetailedDiff(new Diff(actual, expected));
		assertThat(diff.getAllDifferences().toString(), diff.similar(), is(true));
	}
	
	/**
	 * Tableを2つ含むJiemamyContextのシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_Tableを2つ含むJiemamyContextのシリアライズ結果を確認() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		
		UUID id1 = UUID.fromString("cbe160fd-e229-4ede-ae01-3a0ea44ae5d6");
		SimpleJmTable t1 = new SimpleJmTable(id1);
		t1.setName("FOO");
		ctx.store(t1);
		
		UUID id2 = UUID.fromString("76d03b4d-c959-48e9-bd0e-d6c2f61ec54d");
		SimpleJmTable t2 = new SimpleJmTable(id2);
		t2.setName("BAR");
		ctx.store(t2);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(ctx, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		
		String expected = getXml("core4.jiemamy");
		
//		logger.info("actual  ={}", actual.replaceAll("[\n\r]", ""));
//		logger.info("expected={}", expected.replaceAll("[\n\r]", ""));
		
		DetailedDiff diff = new DetailedDiff(new Diff(actual, expected));
		assertThat(diff.getAllDifferences().toString(), diff.similar(), is(true));
	}
	
	/**
	 * Columnを2つ含むTableを1つ含むJiemamyContextのシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_Columnを2つ含むTableを1つ含むJiemamyContextのシリアライズ結果を確認() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		
		UUID tid = UUID.fromString("0d7bf352-59c4-4683-ae59-2808f30e686e");
		SimpleJmTable t = new SimpleJmTable(tid);
		t.setName("TTTT");
		
		UUID cid1 = UUID.fromString("a4333846-1292-4b82-b68c-454762bf92a1");
		SimpleJmColumn c1 = new SimpleJmColumn(cid1);
		c1.setName("CCCC1");
		c1.setDataType(SimpleDataType.of(RawTypeCategory.INTEGER));
		t.store(c1);
		
		UUID cid2 = UUID.fromString("7774052e-40ee-4796-b990-2411be64fb35");
		SimpleJmColumn c2 = new SimpleJmColumn(cid2);
		c2.setName("CCCC2");
		c2.setDataType(SimpleDataType.of(RawTypeCategory.INTEGER));
		t.store(c2);
		
		ctx.store(t);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(ctx, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		String expected = getXml("core5.jiemamy");
		
		logger.info("actual  ={}", actual.replaceAll("[\n\r]", ""));
		logger.info("expected={}", expected.replaceAll("[\n\r]", ""));
		
		DetailedDiff diff = new DetailedDiff(new Diff(actual, expected));
		assertThat(diff.getAllDifferences().toString(), diff.similar(), is(true));
	}
	
	/**
	 * 簡単なJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11_簡単なJiemamyContextのデシリアライズ結果を確認() throws Exception {
		String xml = getXml("core1.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getMetadata(), is(notNullValue()));
	}
	
	/**
	 * Tableを1つ含むJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12_Tableを1つ含むJiemamyContextのデシリアライズ結果を確認() throws Exception {
		String xml = getXml("core2.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getMetadata(), is(notNullValue()));
		
		assertThat(deserialized.getMetadata().getDialectClassName(), is(nullValue())); // 要素欠損 → null
		assertThat(deserialized.getMetadata().getSchemaName(), is("schema-name"));
		assertThat(deserialized.getMetadata().getDescription(), is("")); // 空要素 → 空文字列
		assertThat(deserialized.getTables().size(), is(1));
		JmTable table = Iterables.get(deserialized.getTables(), 0);
		
		assertThat(table.getId(), is(UUID.fromString("d23695f8-76dd-4f8c-b5a2-1e02087ba44d")));
		assertThat(table.getColumns().size(), is(0));
	}
	
	/**
	 * Columnを1つ含むTableを1つ含むJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test13_Columnを1つ含むTableを1つ含むJiemamyContextのデシリアライズ結果を確認() throws Exception {
		String xml = getXml("core3.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getTables().size(), is(1));
		
		JmTable table = Iterables.get(deserialized.getTables(), 0);
		assertThat(table.getId(), is(UUID.fromString("d23695f8-76dd-4f8c-b5a2-1e02087ba44d")));
		assertThat(table.getName(), is("foo"));
		assertThat(table.getLogicalName(), is(nullValue()));
		assertThat(table.getDescription(), is(nullValue()));
		assertThat(table.getColumns().size(), is(1));
		
		JmColumn column = table.getColumns().get(0);
		assertThat(column.getId(), is(UUID.fromString("58a57dcc-7745-4718-b03d-0143eaaa8af3")));
		assertThat(column.getName(), is("bar"));
		assertThat(column.getLogicalName(), is("baz"));
		assertThat(column.getDescription(), is(""));
	}
	
	/**
	 * Tableを2つ含むJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test14_Tableを2つ含むJiemamyContextのデシリアライズ結果を確認() throws Exception {
		String xml = getXml("core4.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		
		assertThat(deserialized.getTables().size(), is(2));
		for (JmTable table : deserialized.getTables()) {
			if (UUID.fromString("cbe160fd-e229-4ede-ae01-3a0ea44ae5d6").equals(table.getId())) {
				assertThat(table.getName(), is("FOO"));
			} else if (UUID.fromString("76d03b4d-c959-48e9-bd0e-d6c2f61ec54d").equals(table.getId())) {
				assertThat(table.getName(), is("BAR"));
			} else {
				fail("unexpeceted id: " + table.getId());
			}
		}
	}
	
	/**
	 * Columnを1つ含むTableを1つ含むJiemamyContextのデシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test15_Columnを2つ含むTableを1つ含むJiemamyContextのデシリアライズ結果を確認() throws Exception {
		String xml = getXml("core5.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getTables().size(), is(1));
		
		JmTable table = Iterables.get(deserialized.getTables(), 0);
		assertThat(table.getId(), is(UUID.fromString("0d7bf352-59c4-4683-ae59-2808f30e686e")));
		assertThat(table.getName(), is("TTTT"));
		assertThat(table.getColumns().size(), is(2));
		
		JmColumn c1 = table.getColumns().get(0);
		assertThat(c1.getId(), is(UUID.fromString("a4333846-1292-4b82-b68c-454762bf92a1")));
		assertThat(c1.getName(), is("CCCC1"));
		
		JmColumn c2 = table.getColumns().get(1);
		assertThat(c2.getId(), is(UUID.fromString("7774052e-40ee-4796-b990-2411be64fb35")));
		assertThat(c2.getName(), is("CCCC2"));
	}
	
	/**
	 * シンボリックIDが利用できることを確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test16_シンボリックIDが利用できることを確認() throws Exception {
		String xml = getXml("symbolicid.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		JmTable t2 = deserialized.getTable("TABLE_2");
		JmForeignKeyConstraint fk = Iterables.getOnlyElement(t2.getForeignKeyConstraints());
		JmTable t1 = fk.findReferenceTable(deserialized.getTables());
		
		assertThat(t1.getName(), is("TABLE_1"));
	}
	
	/**
	 * CORE-207の再現テスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test17_CORE207() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		
		// FORMAT-OFF
		SimpleJmColumn foocolpk = new JmColumnBuilder("FOO1")
			.type(new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.INTEGER)))
		.build();
		SimpleJmColumn barcolpk = new JmColumnBuilder("BAR1")
			.type(new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.INTEGER)))
		.build();
		SimpleJmColumn barcolfk = new JmColumnBuilder("BAR2")
			.type(new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.INTEGER)))
		.build();
		
		SimpleJmForeignKeyConstraint fk = SimpleJmForeignKeyConstraint.of(barcolfk, foocolpk);
		fk.setOnDelete(ReferentialAction.RESTRICT);
		fk.setOnUpdate(ReferentialAction.CASCADE);
		
		SimpleJmTable foo = new JmTableBuilder("FOO")
			.with(foocolpk)
			.with(SimpleJmPrimaryKeyConstraint.of(foocolpk))
		.build();
		SimpleJmTable bar = new JmTableBuilder("BAR")
			.with(barcolpk)
			.with(barcolfk)
			.with(SimpleJmPrimaryKeyConstraint.of(barcolpk))
			.with(fk)
		.build();
		// FORMAT-ON
		ctx.store(foo);
		ctx.store(bar);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(ctx, baos);
		
		String xml = new String(baos.toByteArray());
		logger.info(xml);
		assertThat(xml, containsString("<onDelete>RESTRICT</onDelete>"));
		assertThat(xml, containsString("<onUpdate>CASCADE</onUpdate>"));
		
		JiemamyContext ctx2 = serializer.deserialize(new ByteArrayInputStream(baos.toByteArray()));
		
		JmTable bar2 = ctx2.getTable("BAR");
		SimpleJmForeignKeyConstraint fk2 =
				Iterables.getOnlyElement(bar2.getConstraints(SimpleJmForeignKeyConstraint.class));
		
		assertThat(fk2.getOnDelete(), is(ReferentialAction.RESTRICT));
		assertThat(fk2.getOnUpdate(), is(ReferentialAction.CASCADE));
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
		for (int i = 0; i < 10; i++) {
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
			JiemamyContext original = JiemamyContextTest.random();
			
			// XMLにシリアライズしてみる(1)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(original, baos);
			String first = baos.toString(CharEncoding.UTF_8);
//			logger.info("1 = {}", first);
			FileUtils.write(file1, first);
			
			// そのXMLをデシリアライズしてみる
			ByteArrayInputStream bais = new ByteArrayInputStream(first.getBytes(CharEncoding.UTF_8));
			JiemamyContext deserialized = serializer.deserialize(bais);
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
