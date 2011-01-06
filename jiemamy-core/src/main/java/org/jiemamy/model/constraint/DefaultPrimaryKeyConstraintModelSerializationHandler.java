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

import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.DefaultEntityRef;
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
import org.jiemamy.utils.UUIDUtil;
import org.jiemamy.xml.CoreQName;

/**
 * {@link DefaultTableModel}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultPrimaryKeyConstraintModelSerializationHandler extends
		SerializationHandler<DefaultPrimaryKeyConstraintModel> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultPrimaryKeyConstraintModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultPrimaryKeyConstraintModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultPrimaryKeyConstraintModel handleDeserialization(DeserializationContext ctx)
			throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.getCursor().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.getCursor().isQName(CoreQName.NOT_NULL));
			
			JiemamyCursor cursor = ctx.getCursor();
			JiemamyCursor childCursor = cursor.childCursor();
			
			String name = null;
			String logicalName = null;
			String description = null;
			DeferrabilityModel deferrability = null;
			List<EntityRef<? extends ColumnModel>> keyColumns = Lists.newArrayList();
			
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
					} else if (childCursor.isQName(CoreQName.COLUMN_REFS)) {
						JiemamyCursor descendantCursor = childCursor.descendantCursor().advance();
						while (descendantCursor.getCurrEvent() != SMEvent.START_ELEMENT
								&& descendantCursor.getCurrEvent() != null) {
							descendantCursor.advance();
						}
						if (descendantCursor.getCurrEvent() != null) {
							String idStr = descendantCursor.getAttrValue(CoreQName.REF);
							UUID id = UUIDUtil.valueOfOrRandom(idStr);
							EntityRef<ColumnModel> ref = DefaultEntityRef.of(id);
							keyColumns.add(ref);
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
			
			return new DefaultPrimaryKeyConstraintModel(name, logicalName, description, keyColumns, deferrability);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultPrimaryKeyConstraintModel model, SerializationContext sctx)
			throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.TABLE);
			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			element.addElementAndCharacters(CoreQName.LOGICAL_NAME, model.getLogicalName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			getDirector().direct(model.getDeferrability(), sctx);
			
			JiemamyOutputElement refsElement = element.addElement(CoreQName.COLUMN_REFS);
			
			for (EntityRef<? extends ColumnModel> entityRef : model.getKeyColumns()) {
				refsElement.addElement(CoreQName.COLUMN_REF).addAttribute(CoreQName.REF, entityRef.getReferentId());
			}
			
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
