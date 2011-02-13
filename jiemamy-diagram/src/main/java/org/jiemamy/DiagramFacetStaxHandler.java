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
package org.jiemamy;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.out.SMNamespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.JmDiagram;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax.DeserializationContext;
import org.jiemamy.serializer.stax.JiemamyCursor;
import org.jiemamy.serializer.stax.JiemamyOutputContainer;
import org.jiemamy.serializer.stax.JiemamyOutputElement;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax.StaxHandler;
import org.jiemamy.xml.DiagramNamespace;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DiagramFacet}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DiagramFacetStaxHandler extends StaxHandler<DiagramFacet> {
	
	private static Logger logger = LoggerFactory.getLogger(DiagramFacetStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DiagramFacetStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public DiagramFacet handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(DiagramQName.DIAGRAMS));
			Validate.notNull(dctx.getContext());
			Validate.isTrue(dctx.getContext().hasFacet(DiagramFacet.class));
			
			DiagramFacet facet = dctx.getContext().getFacet(DiagramFacet.class);
			
			JiemamyCursor cursor = dctx.peek();
			JiemamyCursor diagramsCursor = cursor.childElementCursor();
			while (diagramsCursor.getNext() != null) {
				dctx.push(diagramsCursor);
				JmDiagram diagram = getDirector().direct(dctx);
				if (diagram != null) {
					facet.store(diagram);
				} else {
					logger.warn("null diagram");
				}
				dctx.pop();
			}
			
			return facet;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DiagramFacet model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(DiagramQName.DIAGRAMS);
			
			SMNamespace xsiNs =
					element.getSMOutputElement().getNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi");
			element.addAttribute(xsiNs, "schemaLocation", getSchemaLocationDefinition(DiagramNamespace.values()));
			
			sctx.push(element);
			for (JmDiagram diagram : model.getDiagrams()) {
				getDirector().direct(diagram, sctx);
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
