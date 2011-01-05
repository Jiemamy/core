package org.jiemamy.serializer;

/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.apache.commons.lang.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.DiagramFacet;
import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DefaultDiagramModel;
import org.jiemamy.model.DefaultNodeModel;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.serializer.stax2.JiemamyStaxSerializer;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializerTest {
	
	private JiemamyStaxSerializer serializer;
	
	private static final String LF = "\n";
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyStaxSerializerTest.class);
	

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
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		JiemamyContext context = new JiemamyContext(DiagramFacet.PROVIDER);
		
		DefaultTableModel t = new DefaultTableModel(UUID.fromString("5780585f-5f3d-4f72-a38b-7665042e6c00"));
		context.store(t);
		
		DefaultNodeModel n =
				new DefaultNodeModel(UUID.fromString("c748964d-0ef5-492d-97c4-71e2124cf5d6"), t.toReference());
		n.setBoundary(new JmRectangle(100, 100));
		DefaultDiagramModel d = new DefaultDiagramModel(UUID.fromString("a0ed1691-bf18-46e4-bc63-5ee4343588f8"));
		d.store(n);
		context.getFacet(DiagramFacet.class).store(d);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.serialize(context, baos);
		String actual = baos.toString(CharEncoding.UTF_8);
		// FORMAT-OFF
		String expected = "<?xml version='1.0' encoding='UTF-8'?>" + LF +
				"<jiemamy xmlns=\"http://jiemamy.org/xml/ns/core\" version=\"0.3.0-SNAPSHOT\">" + LF +
				"  <dbobjects>" + LF +
				"    <table id=\"5780585f-5f3d-4f72-a38b-7665042e6c00\" class=\"org.jiemamy.model.dbo.DefaultTableModel\">" + LF +
				"      <columns/>" + LF +
				"      <constraints/>" + LF +
				"    </table>" + LF +
				"  </dbobjects>" + LF +
				"  <dataSets/>" + LF +
				"  <diagram:diagrams xmlns:diagram=\"http://jiemamy.org/xml/ns/diagram\">" + LF +
				"    <diagram:diagram id=\"a0ed1691-bf18-46e4-bc63-5ee4343588f8\" class=\"org.jiemamy.model.DefaultDiagramModel\">" + LF +
				"      <diagram:level>ATTRTYPE</diagram:level>" + LF +
				"      <diagram:mode>PHYSICAL</diagram:mode>" + LF +
				"      <diagram:node id=\"c748964d-0ef5-492d-97c4-71e2124cf5d6\" class=\"org.jiemamy.model.DefaultNodeModel\">" + LF +
				"        <diagram:core ref=\"5780585f-5f3d-4f72-a38b-7665042e6c00\"/>" + LF +
				"        <diagram:boundary x=\"100\" y=\"100\" width=\"-1\" height=\"-1\"/>" + LF +
				"      </diagram:node>" + LF +
				"    </diagram:diagram>" + LF +
				"  </diagram:diagrams>" + LF +
				"</jiemamy>" + LF;
		// FORMAT-ON
		
		logger.info("actual  ={}", actual.replaceAll("\n", ""));
		logger.info("expected={}", expected.replaceAll("\n", ""));
		
		assertThat(actual, is(expected));
	}
}
