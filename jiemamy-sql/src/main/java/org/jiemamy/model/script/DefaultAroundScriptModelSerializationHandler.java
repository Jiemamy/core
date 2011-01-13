/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.script;

import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DatabaseObjectModel;
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
import org.jiemamy.xml.SqlQName;

/**
 * {@link DefaultAroundScriptModel}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultAroundScriptModelSerializationHandler extends SerializationHandler<DefaultAroundScriptModel> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultAroundScriptModelSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DefaultAroundScriptModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultAroundScriptModel handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(SqlQName.AROUND_SCRIPT));
			
			JiemamyCursor cursor = ctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = ctx.getContext().toUUID(idString);
			
			DefaultAroundScriptModel scriptModel = new DefaultAroundScriptModel(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			ctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(SqlQName.CORE)) {
						String coreIdString = childCursor.getAttrValue(CoreQName.REF);
						UUID coreId = UUIDUtil.valueOfOrRandom(coreIdString);
						EntityRef<? extends DatabaseObjectModel> core = DefaultEntityRef.of(coreId);
						scriptModel.setCoreModelRef(core);
					} else if (childCursor.isQName(SqlQName.SCRIPT)) {
						Position position = childCursor.getAttrEnumValue(SqlQName.POSITION, Position.class);
						String engine = childCursor.getAttrValue(SqlQName.ENGINE);
						String script = childCursor.collectDescendantText(true);
						scriptModel.setScript(position, script);
						scriptModel.setScriptEngineClassName(position, engine);
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			ctx.pop();
			
			return scriptModel;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultAroundScriptModel model, SerializationContext sctx)
			throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement aroundScriptElement = parent.addElement(SqlQName.AROUND_SCRIPT);
			aroundScriptElement.addAttribute(CoreQName.ID, model.getId());
			aroundScriptElement.addAttribute(CoreQName.CLASS, model.getClass());
			sctx.push(aroundScriptElement);
			
			aroundScriptElement.addElement(SqlQName.CORE).addAttribute(CoreQName.REF,
					model.getCoreModelRef().getReferentId());
			for (Entry<Position, String> e : model.getScripts().entrySet()) {
				JiemamyOutputElement scriptElement = aroundScriptElement.addElement(SqlQName.SCRIPT);
				scriptElement.addAttribute(SqlQName.POSITION, e.getKey());
				scriptElement.addAttribute(SqlQName.ENGINE, model.getScriptEngineClassName(e.getKey()));
				scriptElement.addCharacters(e.getValue());
			}
			
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
