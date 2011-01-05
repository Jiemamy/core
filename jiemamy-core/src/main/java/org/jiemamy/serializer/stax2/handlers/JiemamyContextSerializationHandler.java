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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyFacet;
import org.jiemamy.model.dataset.DataSetModel;
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
 * {@link JiemamyContext}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class JiemamyContextSerializationHandler extends SerializationHandler<JiemamyContext> {
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyContextSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public JiemamyContextSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public JiemamyContext handle(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.getCursor().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.getCursor().isQName(CoreQName.JIEMAMY));
			
			JiemamyContext context = getDirector().getContext();
			
			JiemamyCursor cursor = ctx.getCursor();
			JiemamyCursor childCursor = cursor.childCursor();
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.DIALECT)) {
						context.setDialectClassName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.SCHEMA_NAME)) {
						context.setSchemaName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DESCRIPTION)) {
						context.setDescription(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DBOBJECTS)) {
						JiemamyCursor descendantCursor = childCursor.descendantCursor().advance();
						while (descendantCursor.getCurrEvent() != SMEvent.START_ELEMENT
								&& descendantCursor.getCurrEvent() != null) {
							descendantCursor.advance();
						}
						if (descendantCursor.getCurrEvent() != null) {
							DeserializationContext ctx2 = new DeserializationContext(descendantCursor);
							DatabaseObjectModel databaseObject = getDirector().direct(ctx2);
							if (databaseObject != null) {
								context.store(databaseObject);
							} else {
								logger.warn("null databaseObject");
							}
						}
					} else if (childCursor.isQName(CoreQName.DATASETS)) {
						JiemamyCursor descendantCursor = childCursor.descendantCursor().advance();
						if (descendantCursor.getCurrEvent() != null) {
							DeserializationContext ctx2 = new DeserializationContext(descendantCursor);
							DataSetModel dataSetModel = getDirector().direct(ctx2);
							if (dataSetModel != null) {
								context.store(dataSetModel);
							} else {
								logger.warn("null dataSetModel");
							}
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() == SMEvent.TEXT) {
					if (StringUtils.isEmpty(childCursor.getText().trim()) == false) {
						logger.warn("UNKNOWN TEXT: {}", childCursor.getCurrEvent());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			
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
			sctx.push(element);
			
			element.addAttribute(CoreQName.VERSION, JiemamyContext.getVersion().toString());
			
			element.addElementAndCharacters(CoreQName.DIALECT, model.getDialectClassName());
			element.addElementAndCharacters(CoreQName.SCHEMA_NAME, model.getSchemaName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			
			SortedSet<DatabaseObjectModel> set = Sets.newTreeSet(new DatabaseObjectComparator());
			set.addAll(model.getDatabaseObjects());
			sctx.push(element.addElement(CoreQName.DBOBJECTS));
			for (DatabaseObjectModel databaseObject : set) {
				getDirector().direct(databaseObject, sctx);
			}
			sctx.pop(); // end of dbObjects
			
			sctx.push(element.addElement(CoreQName.DATASETS));
			for (DataSetModel dataSet : model.getDataSets()) {
				getDirector().direct(dataSet, sctx);
			}
			sctx.pop(); // end of dataSets
			
			for (JiemamyFacet facet : model.getFacets()) {
				getDirector().direct(facet, sctx);
			}
			
			sctx.pop(); // --- end of jiemamy
			
			parent.addCharacters("\n");
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
}
