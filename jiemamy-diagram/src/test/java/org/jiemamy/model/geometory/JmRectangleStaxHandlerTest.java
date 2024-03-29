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
package org.jiemamy.model.geometory;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.CharEncoding;
import org.codehaus.staxmate.in.SMInputCursor;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.serializer.stax.DeserializationContext;
import org.jiemamy.serializer.stax.JiemamyCursor;
import org.jiemamy.serializer.stax.JiemamyDocument;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax2.AbstractStaxHandlerTest;

/**
 * {@link JmRectangleStaxHandler}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmRectangleStaxHandlerTest extends AbstractStaxHandlerTest {
	
	private static Logger logger = LoggerFactory.getLogger(JmRectangleStaxHandlerTest.class);
	
	private StaxDirector director;
	
	private JmRectangleStaxHandler handler;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		director = mock(StaxDirector.class);
		handler = new JmRectangleStaxHandler(director);
	}
	
	/**
	 * 簡単なシリアライズ。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_簡単なシリアライズ() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SMOutputDocument doc = getDocument(baos);
		
		JmRectangle model = new JmRectangle(1, 2, 30, -1);
		
		SerializationContext sctx = mock(SerializationContext.class);
		when(sctx.peek()).thenReturn(new JiemamyDocument(doc));
		handler.handleSerialization(model, sctx);
		
		doc.closeRootAndWriter();
		
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// FORMAT-OFF
		String expected = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<diagram:boundary xmlns:diagram=\"http://jiemamy.org/xml/ns/diagram\"" +
						" x=\"1\"" +
						" y=\"2\"" +
						" width=\"30\"" +
						" height=\"-1\"/>";
		// FORMAT-ON
		logger.info("actual={}", actual);
		logger.info("expected={}", expected);
		
		assertThat(actual, is(expected));
	}
	
	/**
	 * 簡単なデシリアライズ。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11_簡単なデシリアライズ() throws Exception {
		// FORMAT-OFF
		String xml = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<diagram:boundary xmlns:diagram=\"http://jiemamy.org/xml/ns/diagram\"" +
						" x=\"1\"" +
						" y=\"2\"" +
						" width=\"30\"" +
						" height=\"-1\"/>";
		// FORMAT-ON
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		
		SMInputCursor cursor = getCursor(bais).advance();
		
		DeserializationContext dctx = mock(DeserializationContext.class);
		when(dctx.peek()).thenReturn(new JiemamyCursor(cursor));
		JmRectangle rectangle = handler.handleDeserialization(dctx);
		
		assertThat(rectangle, is(notNullValue()));
		assertThat(rectangle.x, is(1));
		assertThat(rectangle.y, is(2));
		assertThat(rectangle.width, is(30));
		assertThat(rectangle.height, is(-1));
	}
}
