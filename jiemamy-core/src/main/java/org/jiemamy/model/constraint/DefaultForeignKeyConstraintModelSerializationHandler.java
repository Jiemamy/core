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

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel.MatchType;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel.ReferentialAction;
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
public final class DefaultForeignKeyConstraintModelSerializationHandler extends
		SerializationHandler<DefaultForeignKeyConstraintModel> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultForeignKeyConstraintModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultForeignKeyConstraintModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultForeignKeyConstraintModel handleDeserialization(DeserializationContext ctx)
			throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(CoreQName.FOREIGN_KEY));
			
			JiemamyCursor cursor = ctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = ctx.getContext().toUUID(idString);
			DefaultForeignKeyConstraintModel fkModel = new DefaultForeignKeyConstraintModel(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			ctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						fkModel.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.LOGICAL_NAME)) {
						fkModel.setLogicalName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DESCRIPTION)) {
						fkModel.setDescription(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DEFERRABILITY)) {
						String text = childCursor.collectDescendantText(false);
						fkModel.setDeferrability(DefaultDeferrabilityModel.valueOf(text));
					} else if (childCursor.isQName(CoreQName.MATCH_TYPE)) {
						String text = childCursor.collectDescendantText(false);
						fkModel.setMatchType(MatchType.valueOf(text));
					} else if (childCursor.isQName(CoreQName.ON_DELETE)) {
						String text = childCursor.collectDescendantText(false);
						fkModel.setOnDelete(ReferentialAction.valueOf(text));
					} else if (childCursor.isQName(CoreQName.ON_UPDATE)) {
						String text = childCursor.collectDescendantText(false);
						fkModel.setOnDelete(ReferentialAction.valueOf(text));
					} else if (childCursor.isQName(CoreQName.KEY_COLUMNS)) {
						JiemamyCursor keyColumnsCursor = childCursor.childElementCursor();
						while (keyColumnsCursor.getNext() != null) {
							String idStr = keyColumnsCursor.getAttrValue(CoreQName.REF);
							UUID refid = ctx.getContext().toUUID(idStr);
							EntityRef<ColumnModel> ref = DefaultEntityRef.of(refid);
							fkModel.addKeyColumn(ref);
						}
					} else if (childCursor.isQName(CoreQName.REF_COLUMNS)) {
						JiemamyCursor refColumnsCursor = childCursor.childElementCursor();
						while (refColumnsCursor.getNext() != null) {
							String idStr = refColumnsCursor.getAttrValue(CoreQName.REF);
							UUID refid = ctx.getContext().toUUID(idStr);
							EntityRef<ColumnModel> ref = DefaultEntityRef.of(refid);
							fkModel.addReferenceColumn(ref);
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			ctx.pop();
			
			return fkModel;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultForeignKeyConstraintModel model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.FOREIGN_KEY);
			element.addAttribute(CoreQName.ID, model.getId());
			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			element.addElementAndCharacters(CoreQName.LOGICAL_NAME, model.getLogicalName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			if (model.getDeferrability() != null) {
				getDirector().direct(model.getDeferrability(), sctx);
			}
			element.addElementAndCharacters(CoreQName.MATCH_TYPE, model.getMatchType());
			element.addElementAndCharacters(CoreQName.ON_DELETE, model.getOnDelete());
			element.addElementAndCharacters(CoreQName.ON_UPDATE, model.getOnUpdate());
			
			JiemamyOutputElement keyColumnsElement = element.addElement(CoreQName.KEY_COLUMNS);
			for (EntityRef<? extends ColumnModel> entityRef : model.getKeyColumns()) {
				keyColumnsElement.addElement(CoreQName.COLUMN_REF).addAttribute(CoreQName.REF,
						entityRef.getReferentId());
			}
			
			JiemamyOutputElement refColumnsElement = element.addElement(CoreQName.REF_COLUMNS);
			for (EntityRef<? extends ColumnModel> entityRef : model.getReferenceColumns()) {
				refColumnsElement.addElement(CoreQName.COLUMN_REF).addAttribute(CoreQName.REF,
						entityRef.getReferentId());
			}
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
