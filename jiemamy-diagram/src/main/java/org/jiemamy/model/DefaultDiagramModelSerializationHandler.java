/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.serializer.EntityComparator;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
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
	
	private static Logger logger = LoggerFactory.getLogger(DefaultDiagramModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultDiagramModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultDiagramModel handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(DiagramQName.DIAGRAM));
			
			JiemamyCursor cursor = ctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = ctx.getContext().toUUID(idString);
			DefaultDiagramModel diagramModel = new DefaultDiagramModel(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			ctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						diagramModel.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(DiagramQName.LEVEL)) {
						String text = childCursor.collectDescendantText(false);
						diagramModel.setLevel(Level.valueOf(text));
					} else if (childCursor.isQName(DiagramQName.MODE)) {
						String text = childCursor.collectDescendantText(false);
						diagramModel.setMode(Mode.valueOf(text));
					} else if (childCursor.isQName(DiagramQName.NODES)) {
						JiemamyCursor nodesCursor = childCursor.childElementCursor();
						while (nodesCursor.getNext() != null) {
							ctx.push(nodesCursor);
							NodeModel nodeModel = getDirector().direct(ctx);
							if (nodeModel != null) {
								diagramModel.store(nodeModel);
							} else {
								logger.warn("null nodeModel");
							}
							ctx.pop();
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			ctx.pop();
			
			return diagramModel;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultDiagramModel model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement diagramElement = parent.addElement(DiagramQName.DIAGRAM);
			sctx.push(diagramElement);
			diagramElement.addAttribute(CoreQName.ID, model.getId());
			diagramElement.addAttribute(CoreQName.CLASS, model.getClass());
			
			diagramElement.addElementAndCharacters(CoreQName.NAME, model.getName());
			diagramElement.addElementAndCharacters(DiagramQName.LEVEL, model.getLevel());
			diagramElement.addElementAndCharacters(DiagramQName.MODE, model.getMode());
			
			JiemamyOutputElement nodesElement = diagramElement.addElement(DiagramQName.NODES);
			sctx.push(nodesElement);
			List<? extends NodeModel> nodes = Lists.newArrayList(model.getNodes());
			Collections.sort(nodes, new EntityComparator());
			for (NodeModel nodeModel : nodes) {
				getDirector().direct(nodeModel, sctx);
			}
			sctx.pop(); // -- end of nodes
			
			JiemamyOutputElement connectionsElement = diagramElement.addElement(DiagramQName.CONNECTIONS);
			sctx.push(connectionsElement);
			List<? extends ConnectionModel> connections = Lists.newArrayList(model.getConnections());
			Collections.sort(connections, new EntityComparator());
			for (ConnectionModel connectionModel : connections) {
				getDirector().direct(connectionModel, sctx);
			}
			sctx.pop(); // -- end of nodes
			
			sctx.pop(); // -- end of diagram
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
