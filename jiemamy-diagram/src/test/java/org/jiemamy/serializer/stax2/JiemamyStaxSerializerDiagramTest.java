package org.jiemamy.serializer.stax2;

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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.google.common.collect.Iterables;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.DiagramFacet;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextDiagramTest;
import org.jiemamy.model.DatabaseObjectNodeModel;
import org.jiemamy.model.DefaultDatabaseObjectNodeModel;
import org.jiemamy.model.DefaultDiagramModel;
import org.jiemamy.model.DiagramModel;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.TableModel;

/**
 * {@link JiemamyStaxSerializer}のテスト：Diagram版。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializerDiagramTest {
	
	private JiemamyStaxSerializer serializer;
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyStaxSerializerDiagramTest.class);
	

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
	public void test01_簡単なシリアライズ() throws Exception {
		UUID tableId = UUID.fromString("5780585f-5f3d-4f72-a38b-7665042e6c00");
		UUID nodeId = UUID.fromString("c748964d-0ef5-492d-97c4-71e2124cf5d6");
		UUID diagramId = UUID.fromString("a0ed1691-bf18-46e4-bc63-5ee4343588f8");
		
		JiemamyContext context = new JiemamyContext(DiagramFacet.PROVIDER);
		
		DefaultTableModel table = new DefaultTableModel(tableId);
		context.store(table);
		
		DefaultDatabaseObjectNodeModel nnode = new DefaultDatabaseObjectNodeModel(nodeId, table.toReference());
		nnode.setBoundary(new JmRectangle(100, 100));
		DefaultDiagramModel diagram = new DefaultDiagramModel(diagramId);
		diagram.store(nnode);
		context.getFacet(DiagramFacet.class).store(diagram);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(context, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		String expected = getXml("diagram1.jiemamy");
		
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
	public void test11_簡単なJiemamyContextのデシリアライズ結果を確認() throws Exception {
		UUID tableId = UUID.fromString("5780585f-5f3d-4f72-a38b-7665042e6c00");
		UUID nodeId = UUID.fromString("c748964d-0ef5-492d-97c4-71e2124cf5d6");
		UUID diagramId = UUID.fromString("a0ed1691-bf18-46e4-bc63-5ee4343588f8");
		
		String xml = getXml("diagram1.jiemamy");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais, DiagramFacet.PROVIDER);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getTables().size(), is(1));
		assertThat(deserialized.getDataSets().size(), is(0));
		TableModel tableModel = Iterables.get(deserialized.getTables(), 0);
		assertThat(tableModel.getId(), is(tableId));
		
		DiagramFacet facet = deserialized.getFacet(DiagramFacet.class);
		assertThat(facet.getDiagrams().size(), is(1));
		DiagramModel diagramModel = facet.getDiagrams().get(0);
		assertThat(diagramModel.getId(), is(diagramId));
		assertThat(diagramModel.getNodes().size(), is(1));
		DatabaseObjectNodeModel nodeModel = (DatabaseObjectNodeModel) Iterables.get(diagramModel.getNodes(), 0);
		assertThat(nodeModel.getId(), is(nodeId));
		
		assertThat(nodeModel.getCoreModelRef().isReferenceOf(tableModel), is(true));
		assertThat(nodeModel.getBoundary(), is(new JmRectangle(100, 100)));
		assertThat(nodeModel.getColor(), is(nullValue()));
	}
	
	/**
	 * 適当なモデルを一杯作ってみて、それぞれのシリアライズやデシリアライズが異常終了しないことを確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@Ignore("実装が不完全なので通らない - マダマダァ！")
	public void test99_適当なモデルを一杯作ってみて_それぞれのシリアライズやデシリアライズが異常終了しないことを確認() throws Exception {
		for (int i = 0; i < 100; i++) {
			// 適当なモデルを生成
			JiemamyContext original = JiemamyContextDiagramTest.random();
			
			// XMLにシリアライズしてみる(1)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(original, baos);
			String first = baos.toString(CharEncoding.UTF_8);
			logger.info("1 = {}", first);
			
			// そのXMLをデシリアライズしてみる
			ByteArrayInputStream bais = new ByteArrayInputStream(first.getBytes());
			JiemamyContext deserialized = serializer.deserialize(bais, DiagramFacet.PROVIDER);
			assertThat(deserialized, is(notNullValue()));
			
			// デシリアライズされた新しいcontextを再びシリアライズしてみる(2)
			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
			serializer.serialize(deserialized, baos2);
			String second = baos2.toString(CharEncoding.UTF_8);
			logger.info("2 = {}", second);
			
			// (1)と(2)をログに出してみる（比較用の1行ログ）
			logger.info("1 = {}", first.replaceAll("[\r\n]", ""));
			logger.info("2 = {}", second.replaceAll("[\r\n]", ""));
			
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
