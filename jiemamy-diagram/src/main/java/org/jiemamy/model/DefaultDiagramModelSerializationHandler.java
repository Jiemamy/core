/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/31
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
package org.jiemamy.model;

import javax.xml.stream.XMLStreamException;

import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DefaultDiagramModel}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultDiagramModelSerializationHandler extends SerializationHandler<DefaultDiagramModel> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultDiagramModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public void handle(DefaultDiagramModel model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement diagramElement = parent.addElement(DiagramQName.DIAGRAM);
			sctx.push(diagramElement);
			diagramElement.addAttribute(CoreQName.ID, model.getId());
			diagramElement.addAttribute(CoreQName.CLASS, model.getClass());
			
			diagramElement.addElementAndCharacters(CoreQName.NAME, model.getName());
			diagramElement.addElementAndCharacters(DiagramQName.LEVEL, model.getLevel());
			diagramElement.addElementAndCharacters(DiagramQName.MODE, model.getMode());
			
			for (NodeModel nodeModel : model.getNodes()) {
				getDirector().direct(nodeModel, sctx);
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public DefaultDiagramModel handle(DeserializationContext ctx) throws SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
}
