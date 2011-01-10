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
package org.jiemamy.model.constraint;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.table.DefaultTableModel;
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
public final class DefaultNotNullConstraintModelSerializationHandler extends
		SerializationHandler<DefaultNotNullConstraintModel> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultNotNullConstraintModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultNotNullConstraintModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultNotNullConstraintModel handleDeserialization(DeserializationContext ctx)
			throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(CoreQName.NOT_NULL));
			
			JiemamyCursor cursor = ctx.peek();
			
			String name = null;
			String logicalName = null;
			String description = null;
			DeferrabilityModel deferrability = null;
			EntityRef<? extends ColumnModel> ref = null;
			
			JiemamyCursor childCursor = cursor.childCursor();
			ctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						name = childCursor.collectDescendantText(false);
					} else if (childCursor.isQName(CoreQName.LOGICAL_NAME)) {
						logicalName = childCursor.collectDescendantText(false);
					} else if (childCursor.isQName(CoreQName.DESCRIPTION)) {
						description = childCursor.collectDescendantText(false);
					} else if (childCursor.isQName(CoreQName.DEFERRABILITY)) {
						String text = childCursor.collectDescendantText(false);
						deferrability = DefaultDeferrabilityModel.valueOf(text);
					} else if (childCursor.isQName(CoreQName.COLUMN_REF)) {
						JiemamyCursor descendantCursor = childCursor.descendantCursor().advance();
						while (descendantCursor.getCurrEvent() != SMEvent.START_ELEMENT
								&& descendantCursor.getCurrEvent() != null) {
							descendantCursor.advance();
						}
						if (descendantCursor.getCurrEvent() != null) {
							ctx.push(descendantCursor);
							ref = getDirector().direct(ctx);
							ctx.pop();
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
			ctx.pop();
			
			return new DefaultNotNullConstraintModel(name, logicalName, description, deferrability, ref);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultNotNullConstraintModel model, SerializationContext sctx)
			throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.TABLE);
			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			element.addElementAndCharacters(CoreQName.LOGICAL_NAME, model.getLogicalName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			getDirector().direct(model.getDeferrability(), sctx);
			
			JiemamyOutputElement colRefElement = element.addElement(CoreQName.COLUMN_REF);
			colRefElement.addAttribute(CoreQName.REF, model.getColumn().getReferentId());
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
