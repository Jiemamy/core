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

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.StaxDirector;
import org.jiemamy.serializer.stax2.StaxHandler;
import org.jiemamy.xml.CoreQName;

/**
 * {@link SimpleJmDataSet}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmDataSetStaxHandler extends StaxHandler<SimpleJmDataSet> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmDataSetStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmDataSetStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleJmDataSet handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.DATASET));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = dctx.getContext().toUUID(idString);
			SimpleJmDataSet dataSet = new SimpleJmDataSet(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						dataSet.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.TABLE_RECORDS)) {
						JiemamyCursor tableRecordsCursor = childCursor.childElementCursor();
						while (tableRecordsCursor.getNext() != null) {
							if (tableRecordsCursor.isQName(CoreQName.TABLE_RECORD) == false) {
								logger.warn("unexpected: " + tableRecordsCursor.getQName());
								continue;
							}
							
							String strRef = tableRecordsCursor.getAttrValue(CoreQName.REF);
							assert strRef != null;
							UUID referenceId = dctx.getContext().toUUID(strRef);
							EntityRef<JmTable> tableRef = DefaultEntityRef.of(referenceId);
							
							JiemamyCursor recordsCursor = tableRecordsCursor.childElementCursor();
							List<JmRecord> records = Lists.newArrayList();
							while (recordsCursor.getNext() != null) {
								if (recordsCursor.isQName(CoreQName.RECORDS) == false) {
									logger.warn("unexpected: " + recordsCursor.getQName());
									continue;
								}
								dctx.push(recordsCursor);
								JmRecord record = getDirector().direct(dctx);
								if (record != null) {
									records.add(record);
								} else {
									logger.warn("null recode");
								}
								dctx.pop();
							}
							dataSet.putRecord(tableRef, records);
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return dataSet;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleJmDataSet model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.DATASET);
			element.addAttribute(CoreQName.ID, model.getId());
//			Simple用のHandlerはクラス属性をシリアライズしない
//			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			
			JiemamyOutputElement tableRecordsElement = element.addElement(CoreQName.TABLE_RECORDS);
			sctx.push(tableRecordsElement);
			
			JiemamyContext context = sctx.getContext();
			
			for (Entry<EntityRef<? extends JmTable>, List<JmRecord>> entry : model.getRecords().entrySet()) {
				EntityRef<? extends JmTable> tableRef = entry.getKey();
				JiemamyOutputElement tableRecordElement = tableRecordsElement.addElement(CoreQName.TABLE_RECORD);
				tableRecordElement.addAttribute(CoreQName.REF, tableRef.getReferentId());
				if (JiemamyContext.isDebug()) {
					try {
						String name = context.resolve(tableRef).getName();
						tableRecordElement.addComment(" TableName: " + name + " ");
					} catch (EntityNotFoundException e) {
						tableRecordElement.addComment(" !!! JmTableBuilder cannot resolved !!! ");
					}
				}
				sctx.push(tableRecordElement);
				sctx.setCurrentTableRef(tableRef);
				for (JmRecord record : entry.getValue()) {
					getDirector().direct(record, sctx);
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
