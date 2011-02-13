/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/10
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
package org.jiemamy.model.view;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.xml.stream.XMLOutputFactory;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMOutputFactory;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.column.SimpleJmColumnStaxHandlerTest;
import org.jiemamy.serializer.stax.JiemamyDocument;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax2.AbstractStaxHandlerTest;

/**
 * {@link SimpleJmViewStaxHandler}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmViewStaxHandlerTest extends AbstractStaxHandlerTest {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmColumnStaxHandlerTest.class);
	
	private static final SMOutputFactory F = new SMOutputFactory(XMLOutputFactory.newInstance());
	
	private StaxDirector director;
	
	private SimpleJmViewStaxHandler handler;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		director = mock(StaxDirector.class);
		handler = new SimpleJmViewStaxHandler(director);
	}
	
	/**
	 * 簡単なビューを1つシリアライズして内容を確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_簡単なビューを1つシリアライズして内容を確認する() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SMOutputDocument doc = F.createOutputDocument(baos);
		doc.setIndentation("\n" + StringUtils.repeat("  ", 100), 1, 2); // CHECKSTYLE IGNORE THIS LINE
		
		UUID id = UUID.randomUUID();
		SimpleJmView model = new SimpleJmView(id);
		model.setName("foo");
		model.setDefinition("SELECT * FROM BAR");
		
		SerializationContext sctx = mock(SerializationContext.class);
		when(sctx.peek()).thenReturn(new JiemamyDocument(doc));
		handler.handleSerialization(model, sctx);
		
		doc.closeRootAndWriter();
		
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// FORMAT-OFF
		String expected = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<view xmlns=\"http://jiemamy.org/xml/ns/core\"" +
						" id=\"" + id.toString() + "\">" + LF
					+ "  <name>foo</name>" + LF
					+ "  <definition>SELECT * FROM BAR</definition>" + LF
				+ "</view>";
		// FORMAT-ON
		logger.info("actual={}", actual);
		logger.info("expected={}", expected);
		
		assertThat(actual, is(expected));
	}
	
}
