/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/12/31
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

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.DefaultUUIDEntityRef;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.script.PlainScriptEngine;
import org.jiemamy.script.ScriptString;
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
 * {@link SimpleJmRecord}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmRecordStaxHandler extends StaxHandler<SimpleJmRecord> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmRecordStaxHandler.class);
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmRecordStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleJmRecord handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.RECORDS));
			
			JiemamyCursor cursor = dctx.peek();
			
			Map<UUIDEntityRef<? extends JmColumn>, ScriptString> values = Maps.newHashMap();
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.RECORD)) {
						String strRef = childCursor.getAttrValue(CoreQName.REF);
						assert strRef != null;
						UUID referenceId = dctx.getContext().toUUID(strRef);
						UUIDEntityRef<? extends JmColumn> columnRef = DefaultUUIDEntityRef.of(referenceId);
						String engine = PlainScriptEngine.class.getName();
						if (childCursor.hasAttr(CoreQName.ENGINE)) {
							engine = childCursor.getAttrValue(CoreQName.ENGINE);
						}
						String value = childCursor.collectDescendantText(false);
						values.put(columnRef, new ScriptString(value, engine));
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
					
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return new SimpleJmRecord(values);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleJmRecord model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement recordsElement = parent.addElement(CoreQName.RECORDS);
//			Simple用のHandlerはクラス属性をシリアライズしない
//			recordsElement.addAttribute(CoreQName.CLASS, model.getClass());
			sctx.push(recordsElement);
			
			JiemamyContext context = sctx.getContext();
			
			Iterable<Entry<UUIDEntityRef<? extends JmColumn>, ScriptString>> values =
					model.toIterable(sctx.getContext(), sctx.getCurrentTableRef());
			for (Entry<UUIDEntityRef<? extends JmColumn>, ScriptString> entry : values) {
				UUIDEntityRef<? extends JmColumn> columnRef = entry.getKey();
				ScriptString value = entry.getValue();
				if (JiemamyContext.isDebug()) {
					try {
						String name = context.resolve(columnRef).getName();
						recordsElement.addComment(" ColumnName: " + name + " ");
					} catch (EntityNotFoundException e3) {
						recordsElement.addComment(" !!! JmColumnBuilder cannot resolved !!! ");
					}
				}
				JiemamyOutputElement recordElement = recordsElement.addElement(CoreQName.RECORD);
				recordElement.addAttribute(CoreQName.REF, columnRef.getReferentId());
				recordElement.addAttribute(CoreQName.ENGINE, value.getScriptEngineClassName());
				recordElement.addCharacters(value.getScript());
			}
			
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
