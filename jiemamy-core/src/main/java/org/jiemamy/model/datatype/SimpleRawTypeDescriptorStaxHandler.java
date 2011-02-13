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
package org.jiemamy.model.datatype;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Maps;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * {@link SimpleDataType}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleRawTypeDescriptorStaxHandler extends StaxHandler<SimpleRawTypeDescriptor> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleRawTypeDescriptorStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleRawTypeDescriptorStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleRawTypeDescriptor handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.TYPE_DESC));
			
			JiemamyCursor cursor = dctx.peek();
			
			RawTypeCategory category = RawTypeCategory.UNKNOWN;
			String typeName = "UNKNOWN";
			Map<String, String> params = Maps.newHashMap();
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.TYPE_CATEGORY)) {
						String text = childCursor.collectDescendantText(false);
						if (StringUtils.isEmpty(text) == false) {
							category = RawTypeCategory.valueOf(text);
						}
					} else if (childCursor.isQName(CoreQName.TYPE_NAME)) {
						typeName = childCursor.collectDescendantText(false);
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
			
			SimpleRawTypeDescriptor typeDesc = new SimpleRawTypeDescriptor(category, typeName);
//			for (Map.Entry<String, String> entry : params.entrySet()) {
//				typeDesc.putParam(entry.getKey(), entry.getValue());
//			}
			return typeDesc;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleRawTypeDescriptor model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.TYPE_DESC);
//			Simple用のHandlerはクラス属性をシリアライズしない
//			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.TYPE_CATEGORY, model.getCategory());
			element.addElementAndCharacters(CoreQName.TYPE_NAME, model.getTypeName());
			
//			ParameterMap params = model.getParams();
//			if (params.size() > 0) {
//				JiemamyOutputElement paramesElement = element.addElement(CoreQName.PARAMETERS);
//				ArrayList<Entry<String, String>> paramList = Lists.newArrayList(params);
//				Collections.sort(paramList, new Comparator<Entry<String, String>>() {
//					
//					public int compare(Entry<String, String> e1, Entry<String, String> e2) {
//						return e1.getKey().compareTo(e2.getKey());
//					}
//					
//				});
//				for (Entry<String, String> param : paramList) {
//					JiemamyOutputElement paramElement = paramesElement.addElement(CoreQName.PARAMETER);
//					paramElement.addAttribute(CoreQName.PARAMETER_KEY, param.getKey());
//					paramElement.addCharacters(param.getValue());
//				}
//			}
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
