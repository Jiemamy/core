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
package org.jiemamy.model.geometory;

import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.xml.DiagramQName;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmRectangleSerializationWorker extends SerializationWorker<JmRectangle> {
	
	public JmRectangleSerializationWorker(JiemamyContext context, SerializationDirector director) {
		super(JmRectangle.class, DiagramQName.BOUNDARY, context, director);
	}
	
	@Override
	protected JmRectangle doDeserialize0(StartElement startElement, XMLEventReader reader) {
		int x = Integer.parseInt(startElement.getAttributeByName(DiagramQName.X.getQName()).getValue());
		int y = Integer.parseInt(startElement.getAttributeByName(DiagramQName.Y.getQName()).getValue());
		int width = Integer.parseInt(startElement.getAttributeByName(DiagramQName.WIDTH.getQName()).getValue());
		int height = Integer.parseInt(startElement.getAttributeByName(DiagramQName.HEIGHT.getQName()).getValue());
		return new JmRectangle(x, y, width, height);
	}
	
	@Override
	protected void doSerialize0(JmRectangle model, XMLEventWriter writer) throws XMLStreamException {
		writer.add(EV_FACTORY.createStartElement(DiagramQName.BOUNDARY.getQName(), atts(model), null));
		writer.add(EV_FACTORY.createEndElement(DiagramQName.BOUNDARY.getQName(), null));
	}
	
	private Iterator<Attribute> atts(JmRectangle boundary) {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(DiagramQName.X.getQName(), String.valueOf(boundary.x)));
		result.add(EV_FACTORY.createAttribute(DiagramQName.Y.getQName(), String.valueOf(boundary.y)));
		result.add(EV_FACTORY.createAttribute(DiagramQName.WIDTH.getQName(), String.valueOf(boundary.width)));
		result.add(EV_FACTORY.createAttribute(DiagramQName.HEIGHT.getQName(), String.valueOf(boundary.height)));
		return result.iterator();
	}
	
}
