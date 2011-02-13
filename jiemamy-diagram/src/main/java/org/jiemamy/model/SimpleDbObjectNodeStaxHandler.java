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

import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax.DeserializationContext;
import org.jiemamy.serializer.stax.JiemamyCursor;
import org.jiemamy.serializer.stax.JiemamyOutputContainer;
import org.jiemamy.serializer.stax.JiemamyOutputElement;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax.StaxHandler;
import org.jiemamy.utils.UUIDUtil;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link SimpleDbObjectNode}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleDbObjectNodeStaxHandler extends StaxHandler<SimpleDbObjectNode> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleDbObjectNodeStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleDbObjectNodeStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleDbObjectNode handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(DiagramQName.NODE));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = dctx.getContext().toUUID(idString);
			EntityRef<? extends DbObject> core = null;
			JmRectangle boundary = null;
			JmColor color = null;
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(DiagramQName.CORE)) {
						String coreIdString = childCursor.getAttrValue(CoreQName.REF);
						UUID coreId = UUIDUtil.valueOfOrRandom(coreIdString);
						core = DefaultEntityRef.of(coreId);
					} else if (childCursor.isQName(DiagramQName.BOUNDARY)) {
						boundary = getDirector().direct(dctx);
					} else if (childCursor.isQName(DiagramQName.COLOR)) {
						color = getDirector().direct(dctx);
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			SimpleDbObjectNode node = new SimpleDbObjectNode(id, core);
			node.setBoundary(boundary);
			node.setColor(color);
			return node;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleDbObjectNode model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement nodeElement = parent.addElement(DiagramQName.NODE);
			nodeElement.addAttribute(CoreQName.ID, model.getId());
//			nodeElement.addAttribute(CoreQName.CLASS, model.getClass());
			sctx.push(nodeElement);
			
			nodeElement.addElement(DiagramQName.CORE).addAttribute(CoreQName.REF,
					model.getCoreModelRef().getReferentId());
			
			if (model.getColor() != null) {
				getDirector().direct(model.getColor(), sctx);
			}
			
			getDirector().direct(model.getBoundary(), sctx);
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
