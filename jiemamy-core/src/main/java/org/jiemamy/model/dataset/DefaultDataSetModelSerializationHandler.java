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
package org.jiemamy.model.dataset;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.table.TableModel;
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
 * {@link DefaultDataSetModel}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultDataSetModelSerializationHandler extends SerializationHandler<DefaultDataSetModel> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultDataSetModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultDataSetModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultDataSetModel handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(CoreQName.DATASET));
			
			JiemamyCursor cursor = ctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = ctx.getContext().toUUID(idString);
			DefaultDataSetModel tableModel = new DefaultDataSetModel(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			ctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						tableModel.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.TABLE_RECORDS)) {
						JiemamyCursor tableRecordsCursor = childCursor.childElementCursor();
						while (tableRecordsCursor.getNext() != null) {
							ctx.push(tableRecordsCursor);
							// FIXME
//							ColumnModel columnModel = getDirector().direct(ctx);
//							if (columnModel != null) {
//								tableModel.store(columnModel);
//							} else {
//								logger.warn("null columnModel");
//							}
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
	public void handleSerialization(DefaultDataSetModel model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.DATASET);
			element.addAttribute(CoreQName.ID, model.getId());
			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			
			JiemamyOutputElement tableRecordsElement = element.addElement(CoreQName.TABLE_RECORDS);
			sctx.push(tableRecordsElement);
			
			JiemamyContext context = sctx.getContext();
			
			for (Entry<EntityRef<? extends TableModel>, List<RecordModel>> entry : model.getRecords().entrySet()) {
				EntityRef<? extends TableModel> tableRef = entry.getKey();
				JiemamyOutputElement tableRecordElement = tableRecordsElement.addElement(CoreQName.TABLE_RECORD);
				tableRecordElement.addAttribute(CoreQName.REF, tableRef.getReferentId());
				if (JiemamyContext.isDebug()) {
					tableRecordElement.addComment(" TableName: " + context.resolve(tableRef).getName() + " ");
				}
				sctx.push(tableRecordElement);
				sctx.setCurrentTableRef(tableRef);
				for (RecordModel recordModel : entry.getValue()) {
					getDirector().direct(recordModel, sctx);
				}
				sctx.setCurrentTableRef(null);
				sctx.pop();
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
