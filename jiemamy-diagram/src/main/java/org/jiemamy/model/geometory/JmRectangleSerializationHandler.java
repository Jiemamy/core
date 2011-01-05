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

import javax.xml.stream.XMLStreamException;

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.DiagramQName;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmRectangleSerializationHandler extends SerializationHandler<JmRectangle> {
	
	public JmRectangleSerializationHandler(JiemamyContext context, SerializationDirector director) {
//		super(JmRectangle.class, DiagramQName.BOUNDARY, director);
		super(director);
	}
	
	@Override
	public JmRectangle handle(DeserializationContext ctx) throws SerializationException {
		// TODO Auto-generated method stub
//		int x = Integer.parseInt(startElement.getAttributeByName(DiagramQName.X.getQName()).getValue());
//		int y = Integer.parseInt(startElement.getAttributeByName(DiagramQName.Y.getQName()).getValue());
//		int width = Integer.parseInt(startElement.getAttributeByName(DiagramQName.WIDTH.getQName()).getValue());
//		int height = Integer.parseInt(startElement.getAttributeByName(DiagramQName.HEIGHT.getQName()).getValue());
//		return new JmRectangle(x, y, width, height);
		return null;
	}
	
	@Override
	public void handle(JmRectangle model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement boundaryElement = parent.addElement(DiagramQName.BOUNDARY);
			boundaryElement.addAttribute(DiagramQName.X, model.x);
			boundaryElement.addAttribute(DiagramQName.Y, model.y);
			boundaryElement.addAttribute(DiagramQName.WIDTH, model.width);
			boundaryElement.addAttribute(DiagramQName.HEIGHT, model.height);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
}
