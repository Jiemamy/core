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
import org.jiemamy.serializer.stax.DeserializationContext;
import org.jiemamy.serializer.stax.JiemamyCursor;
import org.jiemamy.serializer.stax.JiemamyOutputContainer;
import org.jiemamy.serializer.stax.JiemamyOutputElement;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax.StaxHandler;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link SimpleJmDiagram}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmDiagramStaxHandler extends StaxHandler<SimpleJmDiagram> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmDiagramStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmDiagramStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleJmDiagram handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(DiagramQName.DIAGRAM));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = dctx.getContext().toUUID(idString);
			SimpleJmDiagram diagram = new SimpleJmDiagram(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(DiagramQName.NAME)) {
						diagram.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(DiagramQName.LEVEL)) {
						String text = childCursor.collectDescendantText(false);
						diagram.setLevel(Level.valueOf(text));
					} else if (childCursor.isQName(DiagramQName.MODE)) {
						String text = childCursor.collectDescendantText(false);
						diagram.setMode(Mode.valueOf(text));
					} else if (childCursor.isQName(DiagramQName.NODES)) {
						JiemamyCursor nodesCursor = childCursor.childElementCursor();
						while (nodesCursor.getNext() != null) {
							dctx.push(nodesCursor);
							JmNode node = getDirector().direct(dctx);
							if (node != null) {
								diagram.store(node);
							} else {
								logger.warn("null node");
							}
							dctx.pop();
						}
					} else if (childCursor.isQName(DiagramQName.CONNECTIONS)) {
						JiemamyCursor connectionsCursor = childCursor.childElementCursor();
						while (connectionsCursor.getNext() != null) {
							dctx.push(connectionsCursor);
							SimpleJmConnection connection = getDirector().direct(dctx);
							if (connection != null) {
								diagram.store(connection);
							} else {
								logger.warn("null connection");
							}
							dctx.pop();
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return diagram;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleJmDiagram model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement diagramElement = parent.addElement(DiagramQName.DIAGRAM);
			sctx.push(diagramElement);
			diagramElement.addAttribute(CoreQName.ID, model.getId());
//			diagramElement.addAttribute(CoreQName.CLASS, model.getClass());
			
			diagramElement.addElementAndCharacters(DiagramQName.NAME, model.getName());
			diagramElement.addElementAndCharacters(DiagramQName.LEVEL, model.getLevel());
			diagramElement.addElementAndCharacters(DiagramQName.MODE, model.getMode());
			
			JiemamyOutputElement nodesElement = diagramElement.addElement(DiagramQName.NODES);
			sctx.push(nodesElement);
			List<? extends JmNode> nodes = Lists.newArrayList(model.getNodes());
			Collections.sort(nodes, EntityComparator.INSTANCE);
			for (JmNode node : nodes) {
				getDirector().direct(node, sctx);
			}
			sctx.pop(); // -- end of nodes
			
			JiemamyOutputElement connectionsElement = diagramElement.addElement(DiagramQName.CONNECTIONS);
			sctx.push(connectionsElement);
			List<? extends JmConnection> connections = Lists.newArrayList(model.getConnections());
			Collections.sort(connections, EntityComparator.INSTANCE);
			for (JmConnection connection : connections) {
				getDirector().direct(connection, sctx);
			}
			sctx.pop(); // -- end of nodes
			
			sctx.pop(); // -- end of diagram
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
