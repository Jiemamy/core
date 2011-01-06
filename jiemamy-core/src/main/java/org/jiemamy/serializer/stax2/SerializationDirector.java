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
package org.jiemamy.serializer.stax2;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextSerializationHandler;
import org.jiemamy.JiemamyFacet;
import org.jiemamy.model.column.DefaultColumnModel;
import org.jiemamy.model.column.DefaultColumnModelSerializationHandler;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.DefaultTableModelSerializationHandler;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.JiemamyQName;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class SerializationDirector {
	
	final Map<String, SerializationHandler<?>> handlersS = Maps.newHashMap();
	
	final Map<QName, SerializationHandler<?>> handlersD = Maps.newHashMap();
	
	@Deprecated
	private final DummyHandler dummy;
	
	private final JiemamyContext context;
	

	public SerializationDirector(JiemamyContext context) {
		Validate.notNull(context);
		
		this.context = context;
		
		dummy = new DummyHandler(this); // FIXME これがケツ持ちをしてる
		
		addHandler(JiemamyContext.class, CoreQName.JIEMAMY, new JiemamyContextSerializationHandler(this));
		addHandler(DefaultTableModel.class, CoreQName.TABLE, new DefaultTableModelSerializationHandler(this));
		addHandler(DefaultColumnModel.class, CoreQName.COLUMN, new DefaultColumnModelSerializationHandler(this));
		// TODO ... 色々
		
		for (JiemamyFacet jiemamyFacet : context.getFacets()) {
			jiemamyFacet.prepareSerializationWorkers(this);
		}
	}
	
	public <T>void addHandler(Class<T> clazz, JiemamyQName jQName, SerializationHandler<T> handler) {
		Validate.notNull(clazz);
		Validate.notNull(jQName);
		Validate.notNull(handler);
		handlersS.put(clazz.getName(), handler);
		handlersD.put(jQName.getQName(), handler);
	}
	
	public <T>T direct(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		SerializationHandler<T> handler = findHandler(ctx.getCursor());
		return handler.handle(ctx);
	}
	
	public <T>void direct(T target, SerializationContext sctx) throws SerializationException {
		Validate.notNull(target);
		Validate.notNull(sctx);
		SerializationHandler<T> handler = findHandler(target);
		handler.handle(target, sctx);
	}
	
	public JiemamyContext getContext() {
		return context;
	}
	
	@SuppressWarnings("unchecked")
	private <T>SerializationHandler<T> findHandler(JiemamyCursor cursor) throws SerializationException {
		Validate.notNull(cursor);
		
		try {
			QName qName = cursor.getQName();
			SerializationHandler<T> handler = (SerializationHandler<T>) handlersD.get(qName);
			
			// FIXME ケツ持ち処理start
			if (handler == null) {
				handler = (SerializationHandler<T>) dummy;
			}
			// FIXME ケツ持ち処理end
			
			return handler;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T>SerializationHandler<T> findHandler(T target) {
		Validate.notNull(target);
		SerializationHandler<T> handler = (SerializationHandler<T>) handlersS.get(target.getClass().getName());
		
		// FIXME ケツ持ち処理start
		if (handler == null) {
			handler = (SerializationHandler<T>) dummy;
		}
		// FIXME ケツ持ち処理end
		
		return handler;
	}
}
