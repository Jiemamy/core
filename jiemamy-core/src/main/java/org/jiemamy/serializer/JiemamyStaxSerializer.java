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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;

import com.bea.xml.stream.EventFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyError;
import org.jiemamy.model.dataset.DataSetModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializer implements JiemamySerializer {
	
	protected static final XMLEventFactory EV_FACTORY = EventFactory.newInstance();
	

	public JiemamyContext deserialize(InputStream in) throws SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void serialize(JiemamyContext context, OutputStream out) throws SerializationException {
		XMLEventWriter writer = null;
		try {
			XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
			writer = new IndentingXMLEventWriter(outFactory.createXMLEventWriter(out));
			
			writer.add(EV_FACTORY.createStartElement(CoreQName.JIEMAMY.getQName(), atts(), nss(context)));
			write1Misc(context, writer);
			write2DatabaseObjects(context, writer);
			write3DataSets(context, writer);
			writer.add(EV_FACTORY.createEndElement(CoreQName.JIEMAMY.getQName(), nss(context)));
			
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
	
	private Iterator<Attribute> atts() {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(CoreQName.VERSION.getQName(), JiemamyContext.getVersion().toString()));
		return result.iterator();
	}
	
	private Iterator<Namespace> nss(JiemamyContext context) {
		List<Namespace> result = Lists.newArrayList();
		for (JiemamyNamespace jns : context.getNamespaces()) {
			Namespace ns = EV_FACTORY.createNamespace(jns.getPrefix(), jns.getNamespaceURI().toString());
			result.add(ns);
		}
		return result.iterator();
	}
	
	private void write1Misc(JiemamyContext context, XMLEventWriter writer) throws XMLStreamException {
		if (StringUtils.isEmpty(context.getDialectClassName()) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.DIALECT.getQName(), null, null));
			writer.add(EV_FACTORY.createCharacters(context.getDialectClassName()));
			writer.add(EV_FACTORY.createEndElement(CoreQName.DIALECT.getQName(), null));
		}
		
		if (StringUtils.isEmpty(context.getSchemaName()) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.SCHEMA_NAME.getQName(), null, null));
			writer.add(EV_FACTORY.createCharacters(context.getSchemaName()));
			writer.add(EV_FACTORY.createEndElement(CoreQName.SCHEMA_NAME.getQName(), null));
		}
		
		if (StringUtils.isEmpty(context.getDescription()) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.DESCRIPTION.getQName(), null, null));
			writer.add(EV_FACTORY.createCharacters(context.getDescription()));
			writer.add(EV_FACTORY.createEndElement(CoreQName.DESCRIPTION.getQName(), null));
		}
	}
	
	private void write2DatabaseObjects(JiemamyContext context, XMLEventWriter writer) throws XMLStreamException {
		Set<? extends DatabaseObjectModel> databaseObjects = context.getDatabaseObjects();
		if (databaseObjects.size() <= 0) {
			return;
		}
		writer.add(EV_FACTORY.createStartElement(CoreQName.DBOBJECTS.getQName(), null, null));
		SortedSet<DatabaseObjectModel> set = Sets.newTreeSet(new DatabaseObjectComparator());
		set.addAll(databaseObjects);
		for (DatabaseObjectModel dom : set) {
			dom.getWriter(context).writeTo(writer);
		}
		writer.add(EV_FACTORY.createEndElement(CoreQName.DBOBJECTS.getQName(), null));
	}
	
	private void write3DataSets(JiemamyContext context, XMLEventWriter writer) throws XMLStreamException {
		List<? extends DataSetModel> dataSets = context.getDataSets();
		if (dataSets.size() <= 0) {
			return;
		}
		writer.add(EV_FACTORY.createStartElement(CoreQName.DATASETS.getQName(), null, null));
		for (DataSetModel dsm : dataSets) {
			dsm.getWriter(context).writeTo(writer);
		}
		writer.add(EV_FACTORY.createEndElement(CoreQName.DATASETS.getQName(), null));
	}
}
