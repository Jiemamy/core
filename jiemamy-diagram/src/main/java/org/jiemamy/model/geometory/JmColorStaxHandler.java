/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/12/28
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
package org.jiemamy.model.geometory;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;

import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax.DeserializationContext;
import org.jiemamy.serializer.stax.JiemamyCursor;
import org.jiemamy.serializer.stax.JiemamyOutputContainer;
import org.jiemamy.serializer.stax.JiemamyOutputElement;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax.StaxHandler;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link JmColor}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class JmColorStaxHandler extends StaxHandler<JmColor> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmColorStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public JmColor handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().isQName(DiagramQName.COLOR));
			JiemamyCursor cursor = dctx.peek();
			
			int r = cursor.getAttrIntValue(DiagramQName.R);
			int g = cursor.getAttrIntValue(DiagramQName.G);
			int b = cursor.getAttrIntValue(DiagramQName.B);
			return new JmColor(r, g, b);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(JmColor model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		
		try {
			JiemamyOutputElement colorElement = parent.addElement(DiagramQName.COLOR);
			colorElement.addAttribute(DiagramQName.R, model.red);
			colorElement.addAttribute(DiagramQName.G, model.green);
			colorElement.addAttribute(DiagramQName.B, model.blue);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
}
