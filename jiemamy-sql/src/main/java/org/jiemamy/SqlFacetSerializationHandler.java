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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.script.AroundScriptModel;
import org.jiemamy.serializer.EntityComparator;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.SqlQName;

/**
 * {@link SqlFacet}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SqlFacetSerializationHandler extends SerializationHandler<SqlFacet> {
	
	private static Logger logger = LoggerFactory.getLogger(SqlFacetSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public SqlFacetSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public SqlFacet handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(SqlQName.SQLS));
			Validate.notNull(ctx.getContext());
			Validate.isTrue(ctx.getContext().hasFacet(SqlFacet.class));
			
			SqlFacet facet = ctx.getContext().getFacet(SqlFacet.class);
			
			JiemamyCursor cursor = ctx.peek();
			JiemamyCursor diagramsCursor = cursor.childElementCursor();
			while (diagramsCursor.getNext() != null) {
				ctx.push(diagramsCursor);
				AroundScriptModel aroundScriptModel = getDirector().direct(ctx);
				if (aroundScriptModel != null) {
					facet.store(aroundScriptModel);
				} else {
					logger.warn("null aroundScriptModel");
				}
				ctx.pop();
			}
			
			return facet;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SqlFacet model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			sctx.push(parent.addElement(SqlQName.SQLS));
			List<AroundScriptModel> list = Lists.newArrayList(model.getAroundScripts());
			Collections.sort(list, new EntityComparator());
			for (AroundScriptModel aroundScript : list) {
				getDirector().direct(aroundScript, sctx);
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
