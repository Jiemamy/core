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
package org.jiemamy.model.table;

import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.parameter.ParameterMap;
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
 * {@link DefaultTableModel}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultTableModelSerializationHandler extends SerializationHandler<DefaultTableModel> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultTableModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultTableModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultTableModel handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(CoreQName.TABLE));
			
			JiemamyCursor cursor = ctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = ctx.getContext().toUUID(idString);
			DefaultTableModel tableModel = new DefaultTableModel(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			ctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						tableModel.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.LOGICAL_NAME)) {
						tableModel.setLogicalName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DESCRIPTION)) {
						tableModel.setDescription(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.COLUMNS)) {
						JiemamyCursor columnsCursor = childCursor.childElementCursor();
						while (columnsCursor.getNext() != null) {
							ctx.push(columnsCursor);
							ColumnModel columnModel = getDirector().direct(ctx);
							if (columnModel != null) {
								tableModel.store(columnModel);
							} else {
								logger.warn("null columnModel");
							}
							ctx.pop();
						}
					} else if (childCursor.isQName(CoreQName.CONSTRAINTS)) {
						JiemamyCursor constraintsCursor = childCursor.childElementCursor();
						while (constraintsCursor.getNext() != null) {
							ctx.push(constraintsCursor);
							ConstraintModel constraintModel = getDirector().direct(ctx);
							if (constraintModel != null) {
								tableModel.store(constraintModel);
							} else {
								logger.warn("null constraintModel");
							}
							ctx.pop();
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			ctx.pop();
			
			return tableModel;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultTableModel model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.TABLE);
			element.addAttribute(CoreQName.ID, model.getId());
			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			element.addElementAndCharacters(CoreQName.LOGICAL_NAME, model.getLogicalName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			
			sctx.push(element.addElement(CoreQName.COLUMNS));
			for (ColumnModel column : model.getColumns()) {
				getDirector().direct(column, sctx);
			}
			sctx.pop();
			
			sctx.push(element.addElement(CoreQName.CONSTRAINTS));
			for (ConstraintModel constraint : model.getConstraints()) {
				getDirector().direct(constraint, sctx);
			}
			sctx.pop();
			
			ParameterMap params = model.getParams();
			if (params.size() > 0) {
				JiemamyOutputElement paramesElement = element.addElement(CoreQName.PARAMETERS);
				for (Entry<String, String> entry : params) {
					JiemamyOutputElement paramElement = paramesElement.addElement(CoreQName.PARAMETER);
					paramElement.addAttribute(CoreQName.PARAMETER_KEY, entry.getKey());
					paramElement.addCharacters(entry.getValue());
				}
			}
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
