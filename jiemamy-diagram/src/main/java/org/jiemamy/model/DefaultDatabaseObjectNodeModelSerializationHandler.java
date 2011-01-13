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
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.utils.UUIDUtil;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DefaultDatabaseObjectNodeModel}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultDatabaseObjectNodeModelSerializationHandler extends
		SerializationHandler<DefaultDatabaseObjectNodeModel> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultDatabaseObjectNodeModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultDatabaseObjectNodeModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultDatabaseObjectNodeModel handleDeserialization(DeserializationContext ctx)
			throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(DiagramQName.NODE));
			
			JiemamyCursor cursor = ctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = ctx.getContext().toUUID(idString);
			EntityRef<? extends DatabaseObjectModel> core = null;
			JmRectangle boundary = null;
			JmColor color = null;
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			ctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(DiagramQName.CORE)) {
						String coreIdString = childCursor.getAttrValue(CoreQName.REF);
						UUID coreId = UUIDUtil.valueOfOrRandom(coreIdString);
						core = DefaultEntityRef.of(coreId);
					} else if (childCursor.isQName(DiagramQName.BOUNDARY)) {
						boundary = getDirector().direct(ctx);
					} else if (childCursor.isQName(DiagramQName.COLOR)) {
						color = getDirector().direct(ctx);
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			ctx.pop();
			
			DefaultDatabaseObjectNodeModel nodeModel = new DefaultDatabaseObjectNodeModel(id, core);
			nodeModel.setBoundary(boundary);
			nodeModel.setColor(color);
			return nodeModel;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultDatabaseObjectNodeModel model, SerializationContext sctx)
			throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement nodeElement = parent.addElement(DiagramQName.NODE);
			nodeElement.addAttribute(CoreQName.ID, model.getId());
			nodeElement.addAttribute(CoreQName.CLASS, model.getClass());
			sctx.push(nodeElement);
			
			nodeElement.addElement(DiagramQName.CORE).addAttribute(CoreQName.REF,
					model.getCoreModelRef().getReferentId());
			
			getDirector().direct(model.getBoundary(), sctx);
			if (model.getColor() != null) {
				getDirector().direct(model.getColor(), sctx);
			}
			for (ConnectionModel connectionModel : model.getSourceConnections()) {
				getDirector().direct(connectionModel, sctx);
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
