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
package org.jiemamy;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.DiagramModel;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DiagramFacet}のシリアライズ処理実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DiagramFacetSerializationWorker extends SerializationWorker<DiagramFacet> {
	
	private static Logger logger = LoggerFactory.getLogger(DiagramFacetSerializationWorker.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public DiagramFacetSerializationWorker(JiemamyContext context, SerializationDirector director) {
		super(DiagramFacet.class, context, director);
	}
	
	@Override
	protected void doWork0(DiagramFacet model, XMLEventWriter writer) throws XMLStreamException, SerializationException {
		writer
			.add(EV_FACTORY.createStartElement(DiagramQName.DIAGRAMS.getQName(), emptyAttributes(), emptyNamespaces()));
		logger.error("EMPTY WRITER");
		for (DiagramModel diagramModel : model.repos.getEntitiesAsList()) {
			getDirector().direct(diagramModel, writer);
		}
//		write1Misc(context, writer);
//		write2DatabaseObjects(context, writer);
//		write3DataSets(context, writer);
//		write4Facets(context, writer);
		writer.add(EV_FACTORY.createEndElement(DiagramQName.DIAGRAMS.getQName(), emptyNamespaces()));
	}
}
