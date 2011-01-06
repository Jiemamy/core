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

/**
 * {@link SerializationHandler}をテストする際の骨格実装テストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractSerializationHandlerTest {
	
	protected static final String LF = "\n";
	
	private static final SMOutputFactory OUT_FACTORY = new SMOutputFactory(XMLOutputFactory.newInstance());
	
	private static final SMInputFactory IN_FACTORY = new SMInputFactory(XMLInputFactory.newInstance());
	

	protected SMHierarchicCursor getCursor(InputStream in) throws XMLStreamException {
		SMHierarchicCursor cursor = IN_FACTORY.rootElementCursor(in);
		return cursor;
	}
	
	protected SMOutputDocument getDocument(OutputStream out) throws XMLStreamException {
		SMOutputDocument doc = OUT_FACTORY.createOutputDocument(out);
		doc.setIndentation("\n" + StringUtils.repeat("  ", 100), 1, 2); // CHECKSTYLE IGNORE THIS LINE
		return doc;
	}
}
