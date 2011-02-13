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
import org.jiemamy.model.DbObject;
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
import org.jiemamy.utils.UUIDUtil;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.SqlQName;

/**
 * {@link SimpleJmAroundScript}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmAroundScriptStaxHandler extends StaxHandler<SimpleJmAroundScript> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmAroundScriptStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmAroundScriptStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleJmAroundScript handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(SqlQName.AROUND_SCRIPT));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = dctx.getContext().toUUID(idString);
			
			SimpleJmAroundScript aroundScript = new SimpleJmAroundScript(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(SqlQName.CORE)) {
						String coreIdString = childCursor.getAttrValue(CoreQName.REF);
						UUID coreId = UUIDUtil.valueOfOrRandom(coreIdString);
						EntityRef<? extends DbObject> core = DefaultEntityRef.of(coreId);
						aroundScript.setCoreModelRef(core);
					} else if (childCursor.isQName(SqlQName.SCRIPT)) {
						Position position = childCursor.getAttrEnumValue(SqlQName.POSITION, Position.class);
						
						String engine;
						if (childCursor.hasAttr(CoreQName.ENGINE)) {
							engine = childCursor.getAttrValue(CoreQName.ENGINE);
						} else { // eingine属性がない場合はPlainScriptEngineで動作する
							engine = PlainScriptEngine.class.getName();
						}
						
						String script = childCursor.collectDescendantText(true);
						aroundScript.setScript(position, script, engine);
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return aroundScript;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleJmAroundScript model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement aroundScriptElement = parent.addElement(SqlQName.AROUND_SCRIPT);
			aroundScriptElement.addAttribute(CoreQName.ID, model.getId());
//			Simple用のHandlerはクラス属性をシリアライズしない
//			aroundScriptElement.addAttribute(CoreQName.CLASS, model.getClass());
			sctx.push(aroundScriptElement);
			
			aroundScriptElement.addElement(SqlQName.CORE).addAttribute(CoreQName.REF,
					model.getCoreModelRef().getReferentId());
			for (Entry<Position, ScriptString> e : model.getScriptStrings().entrySet()) {
				ScriptString value = e.getValue();
				JiemamyOutputElement scriptElement = aroundScriptElement.addElement(SqlQName.SCRIPT);
				scriptElement.addAttribute(SqlQName.POSITION, e.getKey());
				String scriptEngineClassName = value.getScriptEngineClassName();
				if (scriptEngineClassName.equals(PlainScriptEngine.class.getName()) == false) {
					// PlainScriptEngine以外の場合シリアライズする
					scriptElement.addAttribute(CoreQName.ENGINE, scriptEngineClassName);
				}
				scriptElement.addCharacters(value.getScript());
			}
			
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
