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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DefaultDiagramModel}のシリアライズ処理実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultDiagramModelSerializationWorker extends SerializationWorker<DefaultDiagramModel> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public DefaultDiagramModelSerializationWorker(JiemamyContext context, SerializationDirector director) {
		super(DefaultDiagramModel.class, DiagramQName.DIAGRAM, director);
	}
	
	@Override
	protected DefaultDiagramModel doDeserialize0(StartElement startElement, XMLEventReader reader)
			throws XMLStreamException, SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void doSerialize0(DefaultDiagramModel model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		writer.add(EV_FACTORY.createStartElement(DiagramQName.DIAGRAM.getQName(),
				createIdAndClassAttributes(model.getId(), model), emptyNamespaces()));
		write1Misc(model, writer);
		write2Nodes(model, writer);
		writer.add(EV_FACTORY.createEndElement(DiagramQName.DIAGRAM.getQName(), emptyNamespaces()));
	}
	
	private void write1Misc(DefaultDiagramModel model, XMLEventWriter writer) throws XMLStreamException {
		writeNameLogNameDesc(writer, model.getName(), null, null);
		
		writer.add(EV_FACTORY.createStartElement(DiagramQName.LEVEL.getQName(), emptyAttributes(), emptyNamespaces()));
		writer.add(EV_FACTORY.createCharacters(model.getLevel().toString()));
		writer.add(EV_FACTORY.createEndElement(DiagramQName.LEVEL.getQName(), emptyNamespaces()));
		
		writer.add(EV_FACTORY.createStartElement(DiagramQName.MODE.getQName(), emptyAttributes(), emptyNamespaces()));
		writer.add(EV_FACTORY.createCharacters(model.getMode().toString()));
		writer.add(EV_FACTORY.createEndElement(DiagramQName.MODE.getQName(), emptyNamespaces()));
	}
	
	private void write2Nodes(DefaultDiagramModel model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		for (NodeModel nodeModel : model.getNodes()) {
			getDirector().directSerialization(nodeModel, writer);
		}
	}
}
