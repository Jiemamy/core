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
package org.jiemamy.serializer;

import java.io.InputStream;
import java.io.OutputStream;

import javanet.staxutils.IndentingXMLEventWriter;
import javanet.staxutils.events.EventFactory;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jiemamy.FacetProvider;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyError;

/**
 * {@link JiemamySerializer}のStAX（イベントモデル）による実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializer implements JiemamySerializer {
	
	/** 共用の{@link EventFactory}インスタンス */
	protected static final XMLEventFactory EV_FACTORY = XMLEventFactory.newInstance();
	

	public JiemamyContext deserialize(InputStream in, FacetProvider... facetProviders) throws SerializationException {
		XMLEventReader reader = null;
		XMLInputFactory inFactory = XMLInputFactory.newInstance();
		try {
			reader = inFactory.createXMLEventReader(in);
			JiemamyContext context = new JiemamyContext(facetProviders);
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					SerializationDirector director = new SerializationDirector(context);
					director.directDeserialization(startElement, reader);
					
				} else {
					// ... FIXME
				}
			}
			return context;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException e) {
					// ignore
				}
			}
		}
	}
	
	public void serialize(JiemamyContext context, OutputStream out) throws SerializationException {
		XMLEventWriter writer = null;
		XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
		try {
			writer = new IndentingXMLEventWriter(outFactory.createXMLEventWriter(out));
			SerializationDirector director = new SerializationDirector(context);
			director.directSerialization(context, writer);
		} catch (FactoryConfigurationError e) {
			throw new JiemamyError("", e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (XMLStreamException e) {
					// ignore
				}
			}
		}
	}
	
	private void readEvents(JiemamyContext context, StartElement event) {
		// TODO Auto-generated method stub
	}
}
