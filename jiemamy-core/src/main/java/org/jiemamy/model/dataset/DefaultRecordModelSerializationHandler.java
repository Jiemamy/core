/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;
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
 * {@link DefaultRecordModel}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultRecordModelSerializationHandler extends SerializationHandler<DefaultRecordModel> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultRecordModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultRecordModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultRecordModel handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(CoreQName.RECORD));
			
			JiemamyCursor cursor = ctx.peek();
			
			Map<EntityRef<? extends ColumnModel>, String> values = Maps.newHashMap();
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			ctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					// TODO yamkazu
//					if (childCursor.isQName(CoreQName.CORE)) {
//						String coreIdString = childCursor.getAttrValue(CoreQName.REF);
//						UUID coreId = UUIDUtil.valueOfOrRandom(coreIdString);
//						EntityRef<? extends DatabaseObjectModel> core = DefaultEntityRef.of(coreId);
//						scriptModel.setCoreModelRef(core);
//					} else if (childCursor.isQName(CoreQName.SCRIPT)) {
//						Position position = childCursor.getAttrEnumValue(CoreQName.POSITION, Position.class);
//						String engine = childCursor.getAttrValue(CoreQName.ENGINE);
//						String script = childCursor.collectDescendantText(true);
//						scriptModel.setScript(position, script);
//						scriptModel.setScriptEngineClassName(position, engine);
//					} else {
//						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
//					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			ctx.pop();
			
			return new DefaultRecordModel(values);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultRecordModel model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement recordsElement = parent.addElement(CoreQName.RECORDS);
			recordsElement.addAttribute(CoreQName.CLASS, model.getClass());
			sctx.push(recordsElement);
			
			JiemamyContext context = sctx.getContext();
			
			Iterable<Entry<EntityRef<? extends ColumnModel>, String>> values =
					model.toIterable(sctx.getContext(), sctx.getCurrentTableRef());
			for (Entry<EntityRef<? extends ColumnModel>, String> e : values) {
				EntityRef<? extends ColumnModel> columnRef = e.getKey();
				if (JiemamyContext.isDebug()) {
					recordsElement.addComment(" ColumnName: " + context.resolve(columnRef).getName() + " ");
				}
				JiemamyOutputElement recordElement = recordsElement.addElement(CoreQName.RECORD);
				recordElement.addAttribute(CoreQName.REF, columnRef.getReferentId());
				recordElement.addCharacters(e.getValue());
			}
			
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
