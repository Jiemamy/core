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
package org.jiemamy;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.DiagramModel;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DiagramFacet}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DiagramFacetSerializationHandler extends SerializationHandler<DiagramFacet> {
	
	private static Logger logger = LoggerFactory.getLogger(DiagramFacetSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DiagramFacetSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DiagramFacet handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.getCursor().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.getCursor().isQName(DiagramQName.DIAGRAMS));
			Validate.isTrue(getDirector().getContext().hasFacet(DiagramFacet.class));
			
			DiagramFacet facet = getDirector().getContext().getFacet(DiagramFacet.class);
			
			JiemamyCursor cursor = ctx.getCursor();
			JiemamyCursor childCursor = cursor.childCursor();
			do {
				childCursor.advance();
				while (childCursor.getCurrEvent() != SMEvent.START_ELEMENT && childCursor.getCurrEvent() != null) {
					childCursor.advance();
				}
				if (childCursor.getCurrEvent() != null) {
					DeserializationContext ctx2 = new DeserializationContext(childCursor);
					DiagramModel diagramModel = getDirector().direct(ctx2);
					if (diagramModel != null) {
						facet.store(diagramModel);
					} else {
						logger.warn("null diagramModel");
					}
				}
			} while (childCursor.getCurrEvent() != null);
			
			return facet;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DiagramFacet model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			sctx.push(parent.addElement(DiagramQName.DIAGRAMS));
			for (DiagramModel diagramModel : model.repos.getEntitiesAsList()) {
				getDirector().direct(diagramModel, sctx);
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
