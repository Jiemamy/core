/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
 * {@link JmRectangle}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class JmRectangleStaxHandler extends StaxHandler<JmRectangle> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmRectangleStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public JmRectangle handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().isQName(DiagramQName.BOUNDARY));
			JiemamyCursor cursor = dctx.peek();
			
			int x = cursor.getAttrIntValue(DiagramQName.X);
			int y = cursor.getAttrIntValue(DiagramQName.Y);
			int width = cursor.getAttrIntValue(DiagramQName.WIDTH);
			int height = cursor.getAttrIntValue(DiagramQName.HEIGHT);
			return new JmRectangle(x, y, width, height);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(JmRectangle model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement boundaryElement = parent.addElement(DiagramQName.BOUNDARY);
			boundaryElement.addAttribute(DiagramQName.X, model.x);
			boundaryElement.addAttribute(DiagramQName.Y, model.y);
			boundaryElement.addAttribute(DiagramQName.WIDTH, model.width);
			boundaryElement.addAttribute(DiagramQName.HEIGHT, model.height);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
}
