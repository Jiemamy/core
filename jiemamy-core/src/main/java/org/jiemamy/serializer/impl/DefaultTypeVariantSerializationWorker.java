/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.serializer.impl;

import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.datatype.DefaultTypeVariant;
import org.jiemamy.model.datatype.TypeParameter;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.xml.CoreQName;

/**
 * {@link DefaultColumnModel}のシリアライズ処理実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultTypeVariantSerializationWorker extends SerializationWorker<DefaultTypeVariant> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public DefaultTypeVariantSerializationWorker(JiemamyContext context, SerializationDirector director) {
		super(DefaultTypeVariant.class, CoreQName.DATA_TYPE, context, director);
	}
	
	@Override
	protected boolean canDeserialize(StartElement startElement) {
		return startElement.getName().equals(CoreQName.DATA_TYPE.getQName());
	}
	
	@Override
	protected DefaultTypeVariant doDeserialize0(StartElement startElement, XMLEventReader reader)
			throws XMLStreamException, SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void doSerialize0(DefaultTypeVariant model, XMLEventWriter writer) throws XMLStreamException {
		writer.add(EV_FACTORY.createStartElement(CoreQName.DATA_TYPE.getQName(), emptyAttributes(), emptyNamespaces()));
		
		writer.add(EV_FACTORY.createStartElement(CoreQName.TYPE_CATEGORY.getQName(), emptyAttributes(),
				emptyNamespaces()));
		writer.add(EV_FACTORY.createCharacters(model.getCategory().name()));
		writer.add(EV_FACTORY.createEndElement(CoreQName.TYPE_CATEGORY.getQName(), emptyNamespaces()));
		
		writer.add(EV_FACTORY.createStartElement(CoreQName.TYPE_NAME.getQName(), emptyAttributes(), emptyNamespaces()));
		writer.add(EV_FACTORY.createCharacters(model.getTypeName()));
		writer.add(EV_FACTORY.createEndElement(CoreQName.TYPE_NAME.getQName(), emptyNamespaces()));
		
		writeParams(model, writer);
		
		writer.add(EV_FACTORY.createEndElement(CoreQName.DATA_TYPE.getQName(), emptyNamespaces()));
	}
	
	private Iterator<Attribute> paramAttrs(TypeParameter<?> param) {
		List<Attribute> a = Lists.newArrayList();
		a.add(EV_FACTORY.createAttribute(CoreQName.PARAMETER_KEY.getQName(), param.getKey().getKeyString()));
		a.add(EV_FACTORY.createAttribute(CoreQName.CLASS.getQName(),
				param.getKey().getClass().getTypeParameters()[0].getName()));
		return a.iterator();
	}
	
	private void writeParams(DefaultTypeVariant model, XMLEventWriter writer) throws XMLStreamException {
		if (model.getParams().size() <= 0) {
			return;
		}
		writer
			.add(EV_FACTORY.createStartElement(CoreQName.PARAMETERS.getQName(), emptyAttributes(), emptyNamespaces()));
		for (TypeParameter<?> param : model.getParams()) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.PARAMETER.getQName(), paramAttrs(param),
					emptyNamespaces()));
			writer.add(EV_FACTORY.createCharacters(param.getValue().toString()));
			writer.add(EV_FACTORY.createEndElement(CoreQName.PARAMETER.getQName(), emptyNamespaces()));
		}
		writer.add(EV_FACTORY.createEndElement(CoreQName.PARAMETERS.getQName(), emptyNamespaces()));
	}
}
