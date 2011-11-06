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

import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.DefaultUUIDEntityRef;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.SimpleJmTable;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax.DeserializationContext;
import org.jiemamy.serializer.stax.JiemamyCursor;
import org.jiemamy.serializer.stax.JiemamyOutputContainer;
import org.jiemamy.serializer.stax.JiemamyOutputElement;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax.StaxHandler;
import org.jiemamy.xml.CoreQName;

/**
 * {@link SimpleJmTable}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmPrimaryKeyConstraintStaxHandler extends StaxHandler<SimpleJmPrimaryKeyConstraint> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmPrimaryKeyConstraintStaxHandler.class);
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmPrimaryKeyConstraintStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleJmPrimaryKeyConstraint handleDeserialization(DeserializationContext dctx)
			throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.PRIMARY_KEY));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = dctx.getContext().toUUID(idString);
			SimpleJmPrimaryKeyConstraint pk = new SimpleJmPrimaryKeyConstraint(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						pk.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.LOGICAL_NAME)) {
						pk.setLogicalName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DESCRIPTION)) {
						pk.setDescription(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DEFERRABILITY)) {
						SimpleJmDeferrability deferrability = getDirector().direct(dctx);
						pk.setDeferrability(deferrability);
					} else if (childCursor.isQName(CoreQName.KEY_COLUMNS)) {
						JiemamyCursor keyColumnsCursor = childCursor.childElementCursor();
						while (keyColumnsCursor.getNext() != null) {
							String idStr = keyColumnsCursor.getAttrValue(CoreQName.REF);
							UUID refid = dctx.getContext().toUUID(idStr);
							UUIDEntityRef<JmColumn> columnRef = DefaultUUIDEntityRef.of(refid);
							pk.addKeyColumn(columnRef);
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return pk;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleJmPrimaryKeyConstraint model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.PRIMARY_KEY);
			element.addAttribute(CoreQName.ID, model.getId());
//			Simple用のHandlerはクラス属性をシリアライズしない
//			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			element.addElementAndCharacters(CoreQName.LOGICAL_NAME, model.getLogicalName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			
			JiemamyOutputElement keyColumnsElement = element.addElement(CoreQName.KEY_COLUMNS);
			for (UUIDEntityRef<? extends JmColumn> columnRef : model.getKeyColumns()) {
				keyColumnsElement.addElement(CoreQName.COLUMN_REF).addAttribute(CoreQName.REF,
						columnRef.getReferentId());
			}
			
			sctx.push(element);
			if (model.getDeferrability() != null) {
				getDirector().direct(model.getDeferrability(), sctx);
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
