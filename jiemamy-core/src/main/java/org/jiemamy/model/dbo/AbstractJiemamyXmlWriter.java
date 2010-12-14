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
package org.jiemamy.model.dbo;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;

import com.bea.xml.stream.EventFactory;
import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.serializer.JiemamyXmlWriter;
import org.jiemamy.xml.CoreQName;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractJiemamyXmlWriter implements JiemamyXmlWriter {
	
	protected static final XMLEventFactory EV_FACTORY = EventFactory.newInstance();
	

	protected Iterator<Attribute> createIdAndClassAtts(UUID id, Object obj) {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(CoreQName.CLASS.getQName(), obj.getClass().getName()));
		result.add(EV_FACTORY.createAttribute(CoreQName.ID.getQName(), id.toString()));
		return result.iterator();
	}
	
	protected Iterator<Namespace> nss() {
		return null;
	}
	
	protected void writeNameLogNameDesc(XMLEventWriter writer, String name, String logicalName, String description)
			throws XMLStreamException {
		writer.add(EV_FACTORY.createStartElement(CoreQName.NAME.getQName(), null, nss()));
		writer.add(EV_FACTORY.createCharacters(name));
		writer.add(EV_FACTORY.createEndElement(CoreQName.NAME.getQName(), nss()));
		
		if (StringUtils.isEmpty(logicalName) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.LOGICAL_NAME.getQName(), null, nss()));
			writer.add(EV_FACTORY.createCharacters(logicalName));
			writer.add(EV_FACTORY.createEndElement(CoreQName.LOGICAL_NAME.getQName(), nss()));
		}
		
		if (StringUtils.isEmpty(description) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.DESCRIPTION.getQName(), null, nss()));
			writer.add(EV_FACTORY.createCharacters(description));
			writer.add(EV_FACTORY.createEndElement(CoreQName.DESCRIPTION.getQName(), nss()));
		}
	}
	
}
