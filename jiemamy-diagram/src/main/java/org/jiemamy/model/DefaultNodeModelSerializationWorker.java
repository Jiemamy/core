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
package org.jiemamy.model;

import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DefaultDiagramModel}のシリアライズ処理実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultNodeModelSerializationWorker extends SerializationWorker<DefaultNodeModel> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public DefaultNodeModelSerializationWorker(JiemamyContext context, SerializationDirector director) {
		super(DefaultNodeModel.class, DiagramQName.NODE, director);
	}
	
	@Override
	protected DefaultNodeModel doDeserialize0(StartElement startElement, XMLEventReader reader)
			throws XMLStreamException, SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void doSerialize0(DefaultNodeModel model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		writer.add(EV_FACTORY.createStartElement(DiagramQName.NODE.getQName(),
				createIdAndClassAttributes(model.getId(), model), emptyNamespaces()));
		write1Misc(model, writer);
		write2Connections(model, writer);
		writer.add(EV_FACTORY.createEndElement(DiagramQName.NODE.getQName(), emptyNamespaces()));
	}
	
	private Iterator<Attribute> attsForCore(DefaultNodeModel model) {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(CoreQName.REF.getQName(), model.getCoreModelRef().getReferentId()
			.toString()));
		return result.iterator();
	}
	
	private void write1Misc(DefaultNodeModel model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		writer.add(EV_FACTORY.createStartElement(DiagramQName.CORE.getQName(), attsForCore(model), emptyNamespaces()));
		writer.add(EV_FACTORY.createEndElement(DiagramQName.CORE.getQName(), emptyNamespaces()));
		getDirector().directSerialization(model.getBoundary(), writer);
		if (model.getColor() != null) {
			getDirector().directSerialization(model.getColor(), writer);
		}
	}
	
	private void write2Connections(DefaultNodeModel model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		for (ConnectionModel connectionModel : model.getSourceConnections()) {
			getDirector().directSerialization(connectionModel, writer);
		}
	}
}
