/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyCursor;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link JmColor}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmColorSerializationHandler extends SerializationHandler<JmColor> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context
	 * @param director
	 */
	public JmColorSerializationHandler(JiemamyContext context, SerializationDirector director) {
//		super(JmColor.class, DiagramQName.COLOR, director);
		super(director);
	}
	
	@Override
	public JmColor handle(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		try {
			ctx.getCursor().advance();
			Validate.isTrue(ctx.getCursor().isQName(DiagramQName.COLOR));
			JiemamyCursor cursor = ctx.getCursor();
			int r = cursor.getAttrIntValue(DiagramQName.R);
			int g = cursor.getAttrIntValue(DiagramQName.G);
			int b = cursor.getAttrIntValue(DiagramQName.B);
			return new JmColor(r, g, b);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handle(JmColor model, SerializationContext sctx) throws SerializationException {
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