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

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.SMOutputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.out.SMOutputDocument;

import org.jiemamy.FacetProvider;
import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.JiemamySerializer;
import org.jiemamy.serializer.SerializationException;

/**
 * {@link JiemamySerializer}のStAX（+staxmate）による実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializer implements JiemamySerializer {
	
	public JiemamyContext deserialize(InputStream in, FacetProvider... facetProviders) throws SerializationException {
		JiemamyContext context = new JiemamyContext(facetProviders);
		
		SMHierarchicCursor cursor = null;
		SMInputFactory inFactory = new SMInputFactory(XMLInputFactory.newInstance());
		try {
			cursor = inFactory.rootElementCursor(in);
			DeserializationContext ctx = new DeserializationContext(context, cursor);
			SerializationDirector director = new SerializationDirector(context);
			cursor.advance();
			director.direct(ctx);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		} finally {
			if (cursor != null) {
				try {
					cursor.getStreamReader().closeCompletely();
				} catch (XMLStreamException e) {
					// ignore
				}
			}
		}
		return context;
	}
	
	public void serialize(JiemamyContext context, OutputStream out) throws SerializationException {
		SMOutputDocument doc = null;
		SMOutputFactory outFactory = new SMOutputFactory(XMLOutputFactory.newInstance());
		try {
			doc = outFactory.createOutputDocument(out);
			doc.setIndentation("\n" + StringUtils.repeat("  ", 100), 1, 2); // CHECKSTYLE IGNORE THIS LINE
			
			SerializationContext ctx = new SerializationContext(context, doc);
			SerializationDirector director = new SerializationDirector(context);
			director.direct(context, ctx);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		} finally {
			if (doc != null) {
				try {
					doc.closeRootAndWriter();
				} catch (XMLStreamException e) {
					// ignore
				}
			}
		}
		
	}
	
}
