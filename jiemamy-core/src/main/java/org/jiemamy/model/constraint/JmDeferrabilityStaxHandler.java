/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/21
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
package org.jiemamy.model.constraint;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;

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
 * {@link JmDeferrabilityStaxHandler}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author yamkazu
 */
public class JmDeferrabilityStaxHandler extends StaxHandler<JmDeferrability> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmDeferrabilityStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public JmDeferrability handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.DEFERRABILITY));
			
			JiemamyCursor cursor = dctx.peek();
			
			String text = cursor.collectDescendantText(false);
			if (StringUtils.isEmpty(text)) {
				return null;
			} else {
				return JmDeferrability.valueOf(text);
			}
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(JmDeferrability model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.DEFERRABILITY);
//			Simple用のHandlerはクラス属性をシリアライズしない
//			element.addAttribute(CoreQName.CLASS, model.getClass());
			element.addCharacters(model.name());
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
}
