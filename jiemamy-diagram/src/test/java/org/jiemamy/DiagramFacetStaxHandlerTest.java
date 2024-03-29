/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/06
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
package org.jiemamy;

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
 * {@link DiagramFacetStaxHandler}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DiagramFacetStaxHandlerTest extends AbstractStaxHandlerTest {
	
	private static Logger logger = LoggerFactory.getLogger(DiagramFacetStaxHandlerTest.class);
	
	private StaxDirector director;
	
	private DiagramFacetStaxHandler handler;
	
	private JiemamyContext context;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		context = new JiemamyContext(DiagramFacet.PROVIDER);
		director = new StaxDirector(context);
		handler = new DiagramFacetStaxHandler(director);
	}
	
	/**
	 * {@link DiagramFacet}を単体シリアライズしてみる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SMOutputDocument doc = getDocument(baos);
		
		DiagramFacet model = context.getFacet(DiagramFacet.class);
		
		SerializationContext sctx = mock(SerializationContext.class);
		when(sctx.peek()).thenReturn(new JiemamyDocument(doc));
		handler.handleSerialization(model, sctx);
		
		doc.closeRootAndWriter();
		
		String actual = baos.toString(CharEncoding.UTF_8);
		
		// FORMAT-OFF
		String expected = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<diagram:diagrams " +
						"xmlns:diagram=\"http://jiemamy.org/xml/ns/diagram\" " +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
						"xsi:schemaLocation=\"http://jiemamy.org/xml/ns/diagram " +
							"http://schema.jiemamy.org/xml/" + JiemamyContext.getVersion().toString() + "/jiemamy-diagram.xsd\"/>";
		
		// FORMAT-ON
		logger.info("actual={}", actual);
		logger.info("expected={}", expected);
		
		assertThat(actual, is(expected));
	}
	
	/**
	 * {@link DiagramFacet}を単体デシリアライズしてみる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11() throws Exception {
		// FORMAT-OFF
		String xml = "<?xml version='1.0' encoding='UTF-8'?>" + LF
				+ "<diagram:diagrams xmlns:diagram=\"http://jiemamy.org/xml/ns/diagram\">" + LF
				+ "</diagram:diagrams>";
		// FORMAT-ON
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8));
		
		SMInputCursor cursor = getCursor(bais).advance();
		
		JiemamyContext context = new JiemamyContext(DiagramFacet.PROVIDER);
		DeserializationContext dctx = mock(DeserializationContext.class);
		when(dctx.peek()).thenReturn(new JiemamyCursor(cursor));
		when(dctx.getContext()).thenReturn(context);
		DiagramFacet facet = handler.handleDeserialization(dctx);
		
		assertThat(facet, is(notNullValue()));
		assertThat(facet.getDiagrams().size(), is(0));
	}
}
