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
package org.jiemamy.serializer.stax2.handlers;

import javax.xml.stream.XMLStreamException;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.CoreQName;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultColumnModelSerializationHandler extends SerializationHandler<DefaultColumnModel> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context
	 * @param director
	 */
	public DefaultColumnModelSerializationHandler(JiemamyContext context, SerializationDirector director) {
		super(director);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void handle(DefaultColumnModel model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.COLUMN);
			element.addAttribute(CoreQName.ID, model.getId());
			element.addAttribute(CoreQName.CLASS, model.getClass());
			
			element.addElementAndCharacters(CoreQName.NAME, model.getName());
			element.addElementAndCharacters(CoreQName.LOGICAL_NAME, model.getLogicalName());
			element.addElementAndCharacters(CoreQName.DESCRIPTION, model.getDescription());
			
			TypeVariant dataType = model.getDataType();
			if (dataType != null) {
				getDirector().direct(dataType, sctx);
			}
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public DefaultColumnModel handle(DeserializationContext ctx) throws SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
