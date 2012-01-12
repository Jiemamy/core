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
package org.jiemamy.model.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * {@link SimpleDataType}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleDataTypeStaxHandler extends StaxHandler<SimpleDataType> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleDataTypeStaxHandler.class);
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleDataTypeStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleDataType handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.DATA_TYPE));
			
			JiemamyCursor cursor = dctx.peek();
			
			RawTypeDescriptor typeDesc = null;
			Map<String, String> params = Maps.newHashMap();
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.TYPE_DESC)) {
						typeDesc = getDirector().direct(dctx);
					} else if (childCursor.isQName(CoreQName.PARAMETERS)) {
						JiemamyCursor parameterCursor = childCursor.childElementCursor();
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
			
			SimpleDataType dataType = new SimpleDataType(typeDesc);
			for (Map.Entry<String, String> entry : params.entrySet()) {
				dataType.putParam(entry.getKey(), entry.getValue());
			}
			return dataType;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleDataType model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.DATA_TYPE);
//			Simple用のHandlerはクラス属性をシリアライズしない
//			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			sctx.push(element);
			getDirector().direct(model.getRawTypeDescriptor(), sctx);
			sctx.pop();
			
			ParameterMap params = model.getParams();
			if (params.size() > 0) {
				JiemamyOutputElement paramesElement = element.addElement(CoreQName.PARAMETERS);
				ArrayList<Entry<String, String>> paramList = Lists.newArrayList(params);
				Collections.sort(paramList, new Comparator<Entry<String, String>>() {
					
					public int compare(Entry<String, String> e1, Entry<String, String> e2) {
						return e1.getKey().compareTo(e2.getKey());
					}
					
				});
				for (Entry<String, String> param : paramList) {
					JiemamyOutputElement paramElement = paramesElement.addElement(CoreQName.PARAMETER);
					paramElement.addAttribute(CoreQName.PARAMETER_KEY, param.getKey());
					paramElement.addCharacters(param.getValue());
				}
			}
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
