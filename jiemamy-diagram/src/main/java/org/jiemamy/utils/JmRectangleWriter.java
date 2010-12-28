/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/28
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
package org.jiemamy.utils;

import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.dbo.AbstractJiemamyXmlWriter;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.xml.DiagramQName;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmRectangleWriter extends AbstractJiemamyXmlWriter {
	
	private final JmRectangle boundary;
	

	public JmRectangleWriter(JmRectangle boundary) {
		Validate.notNull(boundary);
		this.boundary = boundary;
	}
	
	public void writeTo(XMLEventWriter writer) throws XMLStreamException {
		writer.add(EV_FACTORY.createStartElement(DiagramQName.BOUNDARY.getQName(), atts(), null));
		writer.add(EV_FACTORY.createEndElement(DiagramQName.BOUNDARY.getQName(), null));
	}
	
	private Iterator<Attribute> atts() {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(DiagramQName.X.getQName(), String.valueOf(boundary.x)));
		result.add(EV_FACTORY.createAttribute(DiagramQName.Y.getQName(), String.valueOf(boundary.y)));
		result.add(EV_FACTORY.createAttribute(DiagramQName.WIDTH.getQName(), String.valueOf(boundary.width)));
		result.add(EV_FACTORY.createAttribute(DiagramQName.HEIGHT.getQName(), String.valueOf(boundary.height)));
		return result.iterator();
	}
	
}
