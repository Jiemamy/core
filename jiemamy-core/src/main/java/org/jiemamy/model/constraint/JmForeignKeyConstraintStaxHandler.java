/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.JmTable;
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
 * {@link JmTable}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class JmForeignKeyConstraintStaxHandler extends StaxHandler<JmForeignKeyConstraint> {
	
	private static Logger logger = LoggerFactory.getLogger(JmForeignKeyConstraintStaxHandler.class);
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmForeignKeyConstraintStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public JmForeignKeyConstraint handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.FOREIGN_KEY));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = dctx.getContext().toUUID(idString);
			JmForeignKeyConstraint fk = new JmForeignKeyConstraint(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						fk.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.LOGICAL_NAME)) {
						fk.setLogicalName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DESCRIPTION)) {
						fk.setDescription(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DEFERRABILITY)) {
						JmDeferrability deferrability = getDirector().direct(dctx);
						fk.setDeferrability(deferrability);
					} else if (childCursor.isQName(CoreQName.MATCH_TYPE)) {
						String text = childCursor.collectDescendantText(false);
						fk.setMatchType(MatchType.valueOf(text));
					} else if (childCursor.isQName(CoreQName.ON_DELETE)) {
						String text = childCursor.collectDescendantText(false);
						fk.setOnDelete(ReferentialAction.valueOf(text));
					} else if (childCursor.isQName(CoreQName.ON_UPDATE)) {
						String text = childCursor.collectDescendantText(false);
						fk.setOnUpdate(ReferentialAction.valueOf(text));
					} else if (childCursor.isQName(CoreQName.KEY_COLUMNS)) {
						JiemamyCursor keyColumnsCursor = childCursor.childElementCursor();
						while (keyColumnsCursor.getNext() != null) {
							String idStr = keyColumnsCursor.getAttrValue(CoreQName.REF);
							UUID referenceId = dctx.getContext().toUUID(idStr);
							EntityRef<JmColumn> columnRef = EntityRef.of(referenceId);
							fk.addKeyColumn(columnRef);
						}
					} else if (childCursor.isQName(CoreQName.REF_COLUMNS)) {
						JiemamyCursor refColumnsCursor = childCursor.childElementCursor();
						while (refColumnsCursor.getNext() != null) {
							String idStr = refColumnsCursor.getAttrValue(CoreQName.REF);
							UUID refid = dctx.getContext().toUUID(idStr);
							EntityRef<JmColumn> columnRef = EntityRef.of(refid);
							fk.addReferenceColumn(columnRef);
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return fk;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(JmForeignKeyConstraint model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.FOREIGN_KEY);
			element.addAttribute(CoreQName.ID, model.getId());
//			Simple用のHandlerはクラス属性をシリアライズしない
//			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			element.addElementAndCharacters(CoreQName.LOGICAL_NAME, model.getLogicalName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			
			JiemamyOutputElement keyColumnsElement = element.addElement(CoreQName.KEY_COLUMNS);
			for (EntityRef<? extends JmColumn> columnRef : model.getKeyColumns()) {
				keyColumnsElement.addElement(CoreQName.COLUMN_REF).addAttribute(CoreQName.REF,
						columnRef.getReferentId());
			}
			
			JiemamyOutputElement refColumnsElement = element.addElement(CoreQName.REF_COLUMNS);
			for (EntityRef<? extends JmColumn> columnRef : model.getReferenceColumns()) {
				refColumnsElement.addElement(CoreQName.COLUMN_REF).addAttribute(CoreQName.REF,
						columnRef.getReferentId());
			}
			
			sctx.push(element);
			if (model.getDeferrability() != null) {
				getDirector().direct(model.getDeferrability(), sctx);
			}
			sctx.pop();
			element.addElementAndCharacters(CoreQName.MATCH_TYPE, model.getMatchType());
			element.addElementAndCharacters(CoreQName.ON_DELETE, model.getOnDelete());
			element.addElementAndCharacters(CoreQName.ON_UPDATE, model.getOnUpdate());
			
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
