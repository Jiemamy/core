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

import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmPoint;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.StaxDirector;
import org.jiemamy.serializer.stax2.StaxHandler;
import org.jiemamy.utils.UUIDUtil;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link SimpleJmConnection}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmConnectionStaxHandler extends StaxHandler<SimpleJmConnection> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmConnectionStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmConnectionStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleJmConnection handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(DiagramQName.CONNECTION));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = dctx.getContext().toUUID(idString);
			SimpleJmConnection connection = null;
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(DiagramQName.CORE)) {
						String coreIdString = childCursor.getAttrValue(CoreQName.REF);
						UUID coreId = UUIDUtil.valueOfOrRandom(coreIdString);
						EntityRef<? extends JmForeignKeyConstraint> core = DefaultEntityRef.of(coreId);
						connection = new SimpleJmConnection(id, core);
					} else if (childCursor.isQName(DiagramQName.SOURCE)) {
						String sourceIdString = childCursor.getAttrValue(CoreQName.REF);
						UUID sourceId = UUIDUtil.valueOfOrRandom(sourceIdString);
						EntityRef<? extends JmNode> source = DefaultEntityRef.of(sourceId);
						connection.setSource(source);
					} else if (childCursor.isQName(DiagramQName.TARGET)) {
						String targetIdString = childCursor.getAttrValue(CoreQName.REF);
						UUID targetId = UUIDUtil.valueOfOrRandom(targetIdString);
						EntityRef<? extends JmNode> target = DefaultEntityRef.of(targetId);
						connection.setTarget(target);
					} else if (childCursor.isQName(DiagramQName.BENDPOINTS)) {
						JiemamyCursor bendpointCursor = childCursor.childElementCursor();
						List<JmPoint> points = connection.breachEncapsulationOfBendpoints();
						while (bendpointCursor.getNext() != null) {
							dctx.push(bendpointCursor);
							JmPoint point = getDirector().direct(dctx);
							if (point != null) {
								points.add(point);
							} else {
								logger.warn("null point");
							}
							dctx.pop();
						}
					} else if (childCursor.isQName(DiagramQName.COLOR)) {
						JmColor color = getDirector().direct(dctx);
						connection.setColor(color);
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return connection;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleJmConnection model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement connElement = parent.addElement(DiagramQName.CONNECTION);
			connElement.addAttribute(CoreQName.ID, model.getId());
//			connElement.addAttribute(CoreQName.CLASS, model.getClass());
			sctx.push(connElement);
			
			connElement.addElement(DiagramQName.CORE).addAttribute(CoreQName.REF,
					model.getCoreModelRef().getReferentId());
			
			connElement.addElement(DiagramQName.SOURCE).addAttribute(CoreQName.REF, model.getSource().getReferentId());
			connElement.addElement(DiagramQName.TARGET).addAttribute(CoreQName.REF, model.getTarget().getReferentId());
			
			if (model.getColor() != null) {
				getDirector().direct(model.getColor(), sctx);
			}
			sctx.push(connElement.addElement(DiagramQName.BENDPOINTS));
			for (JmPoint point : model.getBendpoints()) {
				getDirector().direct(point, sctx);
			}
			sctx.pop(); // end of bendpoints
			
			sctx.pop(); // end of connection
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}