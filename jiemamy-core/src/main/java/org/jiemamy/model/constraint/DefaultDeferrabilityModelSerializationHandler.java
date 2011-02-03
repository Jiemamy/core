/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.CoreQName;

/**
 * {@link DefaultDeferrabilityModelSerializationHandler}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author yamkazu
 */
public class DefaultDeferrabilityModelSerializationHandler extends SerializationHandler<DefaultDeferrabilityModel> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultDeferrabilityModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultDeferrabilityModel handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			Validate.isTrue(ctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(ctx.peek().isQName(CoreQName.DEFERRABILITY));
			
			JiemamyCursor cursor = ctx.peek();
			
			String text = cursor.collectDescendantText(false);
			if (StringUtils.isEmpty(text)) {
				return null;
			} else {
				return DefaultDeferrabilityModel.valueOf(text);
			}
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(DefaultDeferrabilityModel model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.DEFERRABILITY);
//			DefaultのHandlerはクラス属性をシリアライズしない
//			element.addAttribute(CoreQName.CLASS, model.getClass());
			element.addCharacters(model.name());
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
}
