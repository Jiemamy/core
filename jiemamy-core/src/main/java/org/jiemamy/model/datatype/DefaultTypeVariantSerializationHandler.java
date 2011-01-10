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

import java.util.Set;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Sets;

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
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.CoreQName;

/**
 * {@link DefaultTypeVariant}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultTypeVariantSerializationHandler extends SerializationHandler<DefaultTypeVariant> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultTypeVariantSerializationHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultTypeVariantSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultTypeVariant handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(CoreQName.DATA_TYPE));
			
			JiemamyCursor cursor = ctx.peek();
			
			DataTypeCategory category = DataTypeCategory.INTEGER;
			String typeName = "INTEGER";
			Set<TypeParameter<?>> params = Sets.newHashSet();
			
			JiemamyCursor childCursor = cursor.childCursor();
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.TYPE_CATEGORY)) {
						String text = childCursor.collectDescendantText(false);
						category = DataTypeCategory.valueOf(text);
					} else if (childCursor.isQName(CoreQName.TYPE_NAME)) {
						typeName = childCursor.collectDescendantText(false);
					} else if (childCursor.isQName(CoreQName.PARAMETERS)) {
						JiemamyCursor descendantCursor = childCursor.descendantCursor().advance();
						while (descendantCursor.getCurrEvent() != SMEvent.START_ELEMENT
								&& descendantCursor.getCurrEvent() != null) {
							descendantCursor.advance();
						}
						if (descendantCursor.getCurrEvent() != null) {
							if (descendantCursor.isQName(CoreQName.PARAMETER)) {
								String key = descendantCursor.getAttrValue(CoreQName.PARAMETER_KEY);
								String v = descendantCursor.collectDescendantText(false);
								// TODO んおーー。イレイジャにやられている
//								TypeParameter<?> param = new DefaultTypeParameter<Object>(new Key<Object>(key), v);
//								params.add(param);
							} else {
								logger.warn("unexpected: " + descendantCursor.getQName());
							}
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
			
			return new DefaultTypeVariant(category, typeName, params);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultTypeVariant model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.DATA_TYPE);
			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.TYPE_CATEGORY, model.getCategory());
			element.addElementAndCharacters(CoreQName.TYPE_NAME, model.getTypeName());
			
			Set<TypeParameter<?>> params = model.getParams();
			if (params.size() > 0) {
				JiemamyOutputElement paramesElement = element.addElement(CoreQName.PARAMETERS);
				for (TypeParameter<?> param : params) {
					JiemamyOutputElement paramElement = paramesElement.addElement(CoreQName.PARAMETER);
					paramElement.addAttribute(CoreQName.PARAMETER_KEY, param.getKey().getKeyString());
					paramElement.addCharacters(param.getValue().toString());
				}
			}
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
