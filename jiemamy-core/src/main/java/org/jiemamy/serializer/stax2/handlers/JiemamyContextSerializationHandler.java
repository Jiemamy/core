/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/05
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
package org.jiemamy.serializer.stax2.handlers;

import java.util.SortedSet;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.serializer.DatabaseObjectComparator;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.CoreQName;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContextSerializationHandler extends SerializationHandler<JiemamyContext> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context
	 * @param director
	 */
	public JiemamyContextSerializationHandler(JiemamyContext context, SerializationDirector director) {
		super(director);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JiemamyContext handle(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.getCursor().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.getCursor().isQName(CoreQName.JIEMAMY));
			
			SerializationDirector director = getDirector();
			JiemamyContext context = director.getContext();
			
			JiemamyCursor cursor = ctx.getCursor();
			JiemamyCursor child = cursor.childElementCursor(CoreQName.DIALECT).advance();
			context.setDialectClassName(child.collectDescendantText(false));
			
			context.setSchemaName(cursor.childElementCursor(CoreQName.SCHEMA_NAME).advance()
				.collectDescendantText(false));
			context.setDescription(cursor.childElementCursor(CoreQName.DESCRIPTION).advance()
				.collectDescendantText(false));
			
			return context;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handle(JiemamyContext model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.JIEMAMY);
			
			element.addAttribute(CoreQName.VERSION, JiemamyContext.getVersion().toString());
			
			element.addElementAndCharacters(CoreQName.DIALECT, model.getDialectClassName());
			element.addElementAndCharacters(CoreQName.SCHEMA_NAME, model.getSchemaName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			
			SortedSet<DatabaseObjectModel> set = Sets.newTreeSet(new DatabaseObjectComparator());
			set.addAll(model.getDatabaseObjects());
			sctx.push(element.addElement(CoreQName.DBOBJECTS));
			for (DatabaseObjectModel dom : set) {
				getDirector().direct(dom, sctx);
			}
			sctx.pop();
			
			parent.addCharacters("\n");
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
}
