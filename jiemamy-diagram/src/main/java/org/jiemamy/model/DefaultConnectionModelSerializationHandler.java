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
package org.jiemamy.model;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.geometory.JmPoint;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DefaultConnectionModel}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultConnectionModelSerializationHandler extends SerializationHandler<DefaultConnectionModel> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultConnectionModelSerializationHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public DefaultConnectionModel handleDeserialization(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void handleSerialization(DefaultConnectionModel model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement connElement = parent.addElement(DiagramQName.CONNECTION);
			connElement.addAttribute(CoreQName.ID, model.getId());
			connElement.addAttribute(CoreQName.CLASS, model.getClass());
			sctx.push(connElement);
			
			connElement.addElement(DiagramQName.CORE).addAttribute(CoreQName.REF,
					model.getCoreModelRef().getReferentId());
			
			if (model.getColor() != null) {
				getDirector().direct(model.getColor(), sctx);
			}
			sctx.push(connElement.addElement(DiagramQName.BENDPOINTS));
			for (JmPoint point : model.getBendpoints()) {
				getDirector().direct(point, sctx);
			}
			sctx.pop(); // end of bendpoints
			
			sctx.pop(); // end of connection
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
