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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.SMOutputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.codehaus.staxmate.out.SMOutputDocument;

import org.jiemamy.FacetProvider;
import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.JiemamySerializer;
import org.jiemamy.serializer.SerializationException;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializer implements JiemamySerializer {
	
	// FIXME delete this main
	// StAX-mate sample
//	public static void main(String[] args) throws XMLStreamException {
//		SMOutputFactory outFactory = new SMOutputFactory(XMLOutputFactory.newInstance());
//		
//		SMOutputDocument doc = outFactory.createOutputDocument(System.out);
//		doc.setIndentation("\n" + StringUtils.repeat("  ", 100), 1, 2); // CHECKSTYLE IGNORE THIS LINE
//		SMOutputElement jiemamy = doc.addElement("jiemamy");
//		jiemamy.addAttribute("foo", "bar");
//		jiemamy.addElement("foo").addCharacters("foo");
//		SMBufferedFragment createBufferedFragment = jiemamy.createBufferedFragment();
//		jiemamy.addBuffered(createBufferedFragment);
//		jiemamy.addElement("baz").addCharacters("baz");
//		
//		SMOutputElement q = createBufferedFragment.addElement("bar").addElement("qux");
//		SMOutputElement qu = q.addElement("quux");
//		qu.addElement("corge").addCharacters("quux");
//		qu.addElement("grault");
//		qu = q.addElement("quux");
//		qu.addElement("corge").addCharacters("quux");
//		qu.addElement("grault");
//		createBufferedFragment.release();
//		doc.closeRoot();
//	}
	
	private static final String LF = "\n";
	
	// FORMAT-OFF
	private static final String XML = "<!-- generated: [CURRENT TIME]-->" + LF
		+ "<employee id=\"123\">" + LF
		+ "  <name>" + LF
		+ "    <first>Tatu</first>" + LF
		+ "    <last>Saloranta</last>" + LF
		+ "  </name>" + LF
		+ "</employee>" + LF;
	// FORMAT-ON
	
	public static void main(String[] args) throws XMLStreamException, UnsupportedEncodingException {
		ByteArrayInputStream bais = new ByteArrayInputStream(XML.getBytes(CharEncoding.UTF_8));
		
		SMInputFactory inFactory = new SMInputFactory(XMLInputFactory.newInstance());
		SMHierarchicCursor rootC = inFactory.rootElementCursor(bais);
		
		System.out.println(rootC.getCurrEvent());
		
		rootC.advance(); // note: 2.0 only method; can also call ".getNext()"
		
		System.out.println(rootC.getCurrEvent() + " : " + rootC.getQName());
		
		int employeeId = rootC.getAttrIntValue(0);
		System.out.println(rootC.getCurrEvent() + " : " + rootC.getQName());
		
		SMInputCursor nameC = rootC.childElementCursor("name").advance();
		
		System.out.println(nameC.getCurrEvent() + " : " + nameC.getQName());
		
		SMInputCursor leafC = nameC.childElementCursor().advance();
		String first = leafC.collectDescendantText(false);
		leafC.advance();
		String last = leafC.collectDescendantText(false);
		rootC.getStreamReader().closeCompletely();
		
		System.out.println(first);
		System.out.println(last);
		
	}
	
	public JiemamyContext deserialize(InputStream in, FacetProvider... facetProviders) throws SerializationException {
		JiemamyContext context = new JiemamyContext(facetProviders);
		
		SMHierarchicCursor cursor = null;
		SMInputFactory inFactory = new SMInputFactory(XMLInputFactory.newInstance());
		try {
			cursor = inFactory.rootElementCursor(in);
			DeserializationContext ctx = new DeserializationContext(cursor);
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
			
			SerializationContext ctx = new SerializationContext(doc);
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
