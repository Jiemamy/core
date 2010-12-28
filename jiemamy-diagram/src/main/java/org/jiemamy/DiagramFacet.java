/*
 * Copyright 2010 Jiemamy Project and the others.
 * Created on 2010/12/07
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

import java.util.List;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.model.DiagramModel;
import org.jiemamy.model.dbo.AbstractJiemamyXmlWriter;
import org.jiemamy.serializer.JiemamyXmlWriter;
import org.jiemamy.xml.DiagramNamespace;
import org.jiemamy.xml.DiagramQName;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * ER図表現ファセット。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DiagramFacet implements JiemamyFacet {
	
	/** プロバイダ */
	public static final FacetProvider PROVIDER = new FacetProvider() {
		
		public JiemamyFacet getFacet(JiemamyContext context) {
			return new DiagramFacet(context);
		}
		
		public Class<? extends JiemamyFacet> getFacetType() {
			return DiagramFacet.class;
		}
		
	};
	
	private OnMemoryRepository<DiagramModel> repos = new OnMemoryRepository<DiagramModel>();
	
	private static Logger logger = LoggerFactory.getLogger(DiagramFacet.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	DiagramFacet(JiemamyContext context) {
		Validate.notNull(context);
	}
	
	public List<? extends DiagramModel> getDiagrams() {
		return repos.getEntitiesAsList();
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return DiagramNamespace.values();
	}
	
	public JiemamyXmlWriter getWriter(JiemamyContext context) {
		return new JiemamyXmlWriterImpl(context);
	}
	
	/**
	 * {@link DiagramModel}を保存する。
	 * 
	 * @param diagram ダイアグラム
	 */
	public void store(DiagramModel diagram) {
		repos.store(diagram);
	}
	

	private class JiemamyXmlWriterImpl extends AbstractJiemamyXmlWriter {
		
		private final JiemamyContext context;
		

		public JiemamyXmlWriterImpl(JiemamyContext context) {
			this.context = context;
		}
		
		public void writeTo(XMLEventWriter writer) throws XMLStreamException {
			writer.add(EV_FACTORY.createStartElement(DiagramQName.DIAGRAMS.getQName(), null, null));
			logger.error("EMPTY WRITER");
			for (DiagramModel diagramModel : repos.getEntitiesAsList()) {
				diagramModel.getWriter(context).writeTo(writer);
			}
//			write1Misc(context, writer);
//			write2DatabaseObjects(context, writer);
//			write3DataSets(context, writer);
//			write4Facets(context, writer);
			writer.add(EV_FACTORY.createEndElement(DiagramQName.DIAGRAMS.getQName(), null));
		}
	}
}
