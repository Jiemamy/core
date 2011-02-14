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

import org.jiemamy.DiagramFacet;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextDiagramTest;
import org.jiemamy.model.DbObjectNode;
import org.jiemamy.model.JmConnection;
import org.jiemamy.model.JmDiagram;
import org.jiemamy.model.JmNode;
import org.jiemamy.model.SimpleDbObjectNode;
import org.jiemamy.model.SimpleJmDiagram;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.SimpleJmTable;
import org.jiemamy.serializer.stax.JiemamyStaxSerializer;

/**
 * {@link JiemamyStaxSerializer}のテスト：jiemamy-diagram版。
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
		
		SimpleJmTable table = new SimpleJmTable(tableId);
		table.setName("foo");
		context.store(table);
		
		SimpleDbObjectNode nnode = new SimpleDbObjectNode(nodeId, table.toReference());
		nnode.setBoundary(new JmRectangle(100, 100));
		SimpleJmDiagram diagram = new SimpleJmDiagram(diagramId);
		diagram.store(nnode);
		context.getFacet(DiagramFacet.class).store(diagram);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(context, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		String expected = getXml("diagram1.jiemamy.xml");
		
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
		
		String xml = getXml("diagram1.jiemamy.xml");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais, DiagramFacet.PROVIDER);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getTables().size(), is(1));
		assertThat(deserialized.getDataSets().size(), is(0));
		JmTable table = Iterables.get(deserialized.getTables(), 0);
		assertThat(table.getId(), is(tableId));
		assertThat(table.getName(), is("foo"));
		
		DiagramFacet facet = deserialized.getFacet(DiagramFacet.class);
		assertThat(facet.getDiagrams().size(), is(1));
		JmDiagram diagram = facet.getDiagrams().get(0);
		assertThat(diagram.getId(), is(diagramId));
		assertThat(diagram.getNodes().size(), is(1));
		DbObjectNode node = (DbObjectNode) Iterables.get(diagram.getNodes(), 0);
		assertThat(node.getId(), is(nodeId));
		
		assertThat(node.getCoreModelRef().isReferenceOf(table), is(true));
		assertThat(node.getBoundary(), is(new JmRectangle(100, 100)));
		assertThat(node.getColor(), is(nullValue()));
	}
	
	/**
	 * デシリアライズ結果を確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12_デシリアライズ結果を確認() throws Exception {
		UUID diagramId = UUID.fromString("4026c70b-53f2-4ada-aebf-99a2b7d3a467");
		UUID deptTableId = UUID.fromString("733dc4cf-c845-4d7a-bb96-c96e2fb1bf06");
		UUID empTableId = UUID.fromString("1199b4f1-66cc-4e80-9d86-a00139439caf");
		UUID deptNodeId = UUID.fromString("e68577a0-7ce9-4fb1-8411-af5b0dda5a52");
		UUID empNodeId = UUID.fromString("52b1a1e8-737d-4df2-bc67-22aa8b74f562");
		UUID connectionId = UUID.fromString("345125d6-1160-4c5d-a20d-654d6f37e4eb");
		
		String xml = getXml("diagram2.jiemamy.xml");
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais, DiagramFacet.PROVIDER);
		
		assertThat(deserialized, is(notNullValue()));
		assertThat(deserialized.getTables().size(), is(2));
		assertThat(deserialized.getDataSets().size(), is(0));
//		JmTable table = Iterables.get(deserialized.getTables(), 0);
//		assertThat(table.getId(), is(tableId));
		
		DiagramFacet facet = deserialized.getFacet(DiagramFacet.class);
		assertThat(facet.getDiagrams().size(), is(1));
		JmDiagram diagram = facet.getDiagrams().get(0);
		assertThat(diagram.getId(), is(diagramId));
		assertThat(diagram.getNodes().size(), is(2));
		for (JmNode node : diagram.getNodes()) {
			if (node.getId().equals(deptNodeId)) {
				DbObjectNode deptNode = (DbObjectNode) node; // node for DEPT
				assertThat(deptNode.getCoreModelRef().getReferentId(), is(deptTableId));
			} else if (node.getId().equals(empNodeId)) {
				DbObjectNode empNode = (DbObjectNode) node; // node for EMP
				assertThat(empNode.getCoreModelRef().getReferentId(), is(empTableId));
			} else {
				fail();
			}
		}
		assertThat(diagram.getConnections().size(), is(1));
		JmConnection connection = Iterables.get(diagram.getConnections(), 0);
		assertThat(connection.getId(), is(connectionId));
		assertThat(connection.getBendpoints().size(), is(3));
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
			JiemamyContext original = JiemamyContextDiagramTest.random();
			
			// XMLにシリアライズしてみる(1)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(original, baos);
			String first = baos.toString(CharEncoding.UTF_8);
//			logger.info("1 = {}", first);
			FileUtils.write(file1, first);
			
			// そのXMLをデシリアライズしてみる
			ByteArrayInputStream bais = new ByteArrayInputStream(first.getBytes());
			JiemamyContext deserialized = serializer.deserialize(bais, DiagramFacet.PROVIDER);
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
