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

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.xml.DiagramQName;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmColorSerializationWorker extends SerializationWorker<JmColor> {
	
	public JmColorSerializationWorker(JiemamyContext context, SerializationDirector director) {
		super(JmColor.class, context, director);
	}
	
	@Override
	protected void doWork0(JmColor model, XMLEventWriter writer) throws XMLStreamException, SerializationException {
		writer.add(EV_FACTORY.createStartElement(DiagramQName.BOUNDARY.getQName(), atts(model), null));
		writer.add(EV_FACTORY.createEndElement(DiagramQName.BOUNDARY.getQName(), null));
	}
	
	private Iterator<Attribute> atts(JmColor color) {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(DiagramQName.R.getQName(), String.valueOf(color.red)));
		result.add(EV_FACTORY.createAttribute(DiagramQName.G.getQName(), String.valueOf(color.green)));
		result.add(EV_FACTORY.createAttribute(DiagramQName.B.getQName(), String.valueOf(color.blue)));
		return result.iterator();
	}
	
}
