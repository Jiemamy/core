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
package org.jiemamy.model.column;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.parameter.ParameterMap;
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
 * {@link SimpleJmColumn}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmColumnStaxHandler extends StaxHandler<SimpleJmColumn> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmColumnStaxHandler.class);
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmColumnStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleJmColumn handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.COLUMN));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idString = cursor.getAttrValue(CoreQName.ID);
			UUID id = dctx.getContext().toUUID(idString);
			SimpleJmColumn column = new SimpleJmColumn(id);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.NAME)) {
						column.setName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.LOGICAL_NAME)) {
						column.setLogicalName(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DESCRIPTION)) {
						column.setDescription(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DEFAULT_VALUE)) {
						column.setDefaultValue(childCursor.collectDescendantText(false));
					} else if (childCursor.isQName(CoreQName.DATA_TYPE)) {
						DataType type = getDirector().direct(dctx);
						if (type != null) {
							column.setDataType(type);
						} else {
							logger.warn("null type");
						}
					} else if (childCursor.isQName(CoreQName.PARAMETERS)) {
						JiemamyCursor parameterCursor = childCursor.childElementCursor();
						ParameterMap params = column.breachEncapsulationOfParams();
						while (parameterCursor.getNext() != null) {
							if (parameterCursor.isQName(CoreQName.PARAMETER) == false) {
								logger.warn("unexpected: " + parameterCursor.getQName());
								continue;
							}
							params.put(parameterCursor.getAttrValue(CoreQName.PARAMETER_KEY),
									parameterCursor.collectDescendantText(false));
						}
					} else {
						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return column;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleJmColumn model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.COLUMN);
			element.addAttribute(CoreQName.ID, model.getId());
//			Simple用のHandlerはクラス属性をシリアライズしない
//			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			element.addElementAndCharacters(CoreQName.LOGICAL_NAME, model.getLogicalName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			
			sctx.push(element);
			DataType dataType = model.getDataType();
			if (dataType != null) {
				getDirector().direct(dataType, sctx);
			}
			
			element.addElementAndCharacters(CoreQName.DEFAULT_VALUE, model.getDefaultValue());
			
			ParameterMap params = model.getParams();
			if (params.size() > 0) {
				JiemamyOutputElement paramesElement = element.addElement(CoreQName.PARAMETERS);
				ArrayList<Entry<String, String>> paramList = Lists.newArrayList(params);
				Collections.sort(paramList, new Comparator<Entry<String, String>>() {
					
					public int compare(Entry<String, String> e1, Entry<String, String> e2) {
						return e1.getKey().compareTo(e2.getKey());
					}
					
				});
				for (Entry<String, String> entry : paramList) {
					JiemamyOutputElement paramElement = paramesElement.addElement(CoreQName.PARAMETER);
					paramElement.addAttribute(CoreQName.PARAMETER_KEY, entry.getKey());
					paramElement.addCharacters(entry.getValue());
				}
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
}
