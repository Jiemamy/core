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

import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.out.SMNamespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.script.JmAroundScript;
import org.jiemamy.serializer.EntityComparator;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.StaxDirector;
import org.jiemamy.serializer.stax2.StaxHandler;
import org.jiemamy.xml.SqlNamespace;
import org.jiemamy.xml.SqlQName;

/**
 * {@link SqlFacet}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SqlFacetStaxHandler extends StaxHandler<SqlFacet> {
	
	private static Logger logger = LoggerFactory.getLogger(SqlFacetStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SqlFacetStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SqlFacet handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(SqlQName.SQLS));
			Validate.notNull(dctx.getContext());
			Validate.isTrue(dctx.getContext().hasFacet(SqlFacet.class));
			
			SqlFacet facet = dctx.getContext().getFacet(SqlFacet.class);
			
			JiemamyCursor cursor = dctx.peek();
			JiemamyCursor diagramsCursor = cursor.childElementCursor();
			while (diagramsCursor.getNext() != null) {
				dctx.push(diagramsCursor);
				JmAroundScript aroundScript = getDirector().direct(dctx);
				if (aroundScript != null) {
					facet.store(aroundScript);
				} else {
					logger.warn("null aroundScript");
				}
				dctx.pop();
			}
			
			return facet;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SqlFacet model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(SqlQName.SQLS);
			
			SMNamespace xsiNs =
					element.getSMOutputElement().getNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi");
			element.addAttribute(xsiNs, "schemaLocation", getSchemaLocationDefinition(SqlNamespace.values()));
			
			sctx.push(element);
			List<JmAroundScript> list = Lists.newArrayList(model.getAroundScripts());
			Collections.sort(list, EntityComparator.INSTANCE);
			for (JmAroundScript aroundScript : list) {
				getDirector().direct(aroundScript, sctx);
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
