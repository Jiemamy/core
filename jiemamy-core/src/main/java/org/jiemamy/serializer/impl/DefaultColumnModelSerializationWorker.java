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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.attribute.DefaultColumnModel;
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
public final class DefaultColumnModelSerializationWorker extends SerializationWorker<DefaultColumnModel> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public DefaultColumnModelSerializationWorker(JiemamyContext context, SerializationDirector director) {
		super(DefaultColumnModel.class, CoreQName.COLUMN, director);
	}
	
	@Override
	protected DefaultColumnModel doDeserialize0(StartElement startElement, XMLEventReader reader)
			throws XMLStreamException, SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void doSerialize0(DefaultColumnModel model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		writer.add(EV_FACTORY.createStartElement(CoreQName.COLUMN.getQName(),
				createIdAndClassAttributes(model.getId(), model), emptyNamespaces()));
		write1Misc(model, writer);
		writer.add(EV_FACTORY.createEndElement(CoreQName.COLUMN.getQName(), emptyNamespaces()));
	}
	
	private void write1Misc(DefaultColumnModel model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		writeNameLogNameDesc(writer, model.getName(), model.getLogicalName(), model.getDescription());
		getDirector().directSerialization(model.getDataType(), writer);
	}
}
