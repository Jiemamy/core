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
import java.util.Set;
import java.util.SortedSet;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyFacet;
import org.jiemamy.model.dataset.DataSetModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.serializer.DatabaseObjectComparator;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * {@link JiemamyContext}のシリアライズ処理実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class JiemamyContextSerializationWorker extends SerializationWorker<JiemamyContext> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public JiemamyContextSerializationWorker(JiemamyContext context, SerializationDirector director) {
		super(JiemamyContext.class, context, director);
	}
	
	@Override
	protected void doWork0(JiemamyContext model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		writer.add(EV_FACTORY.createStartDocument());
		writer.add(EV_FACTORY.createStartElement(CoreQName.JIEMAMY.getQName(), atts(), nss()));
		write1Misc(writer);
		write2DatabaseObjects(writer);
		write3DataSets(writer);
		write4Facets(writer);
		writer.add(EV_FACTORY.createEndElement(CoreQName.JIEMAMY.getQName(), nss()));
		writer.add(EV_FACTORY.createEndDocument());
	}
	
	private Iterator<Attribute> atts() {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(CoreQName.VERSION.getQName(), JiemamyContext.getVersion().toString()));
		return result.iterator();
	}
	
	private Iterator<Namespace> nss() {
		List<Namespace> result = Lists.newArrayList();
		for (JiemamyNamespace jns : getContext().getNamespaces()) {
			Namespace ns = EV_FACTORY.createNamespace(jns.getPrefix(), jns.getNamespaceURI().toString());
			if (StringUtils.isEmpty(ns.getNamespaceURI()) == false) {
				result.add(ns);
			}
		}
		return result.iterator();
	}
	
	private void write1Misc(XMLEventWriter writer) throws XMLStreamException {
		JiemamyContext context = getContext();
		
		if (StringUtils.isEmpty(context.getDialectClassName()) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.DIALECT.getQName(), null, null));
			writer.add(EV_FACTORY.createCharacters(context.getDialectClassName()));
			writer.add(EV_FACTORY.createEndElement(CoreQName.DIALECT.getQName(), null));
		}
		
		if (StringUtils.isEmpty(context.getSchemaName()) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.SCHEMA_NAME.getQName(), null, null));
			writer.add(EV_FACTORY.createCharacters(context.getSchemaName()));
			writer.add(EV_FACTORY.createEndElement(CoreQName.SCHEMA_NAME.getQName(), null));
		}
		
		if (StringUtils.isEmpty(context.getDescription()) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.DESCRIPTION.getQName(), null, null));
			writer.add(EV_FACTORY.createCharacters(context.getDescription()));
			writer.add(EV_FACTORY.createEndElement(CoreQName.DESCRIPTION.getQName(), null));
		}
	}
	
	private void write2DatabaseObjects(XMLEventWriter writer) throws XMLStreamException, SerializationException {
		JiemamyContext context = getContext();
		
		Set<? extends DatabaseObjectModel> databaseObjects = context.getDatabaseObjects();
		if (databaseObjects.size() <= 0) {
			return;
		}
		writer.add(EV_FACTORY.createStartElement(CoreQName.DBOBJECTS.getQName(), null, null));
		SortedSet<DatabaseObjectModel> set = Sets.newTreeSet(new DatabaseObjectComparator());
		set.addAll(databaseObjects);
		for (DatabaseObjectModel dom : set) {
			getDirector().direct(dom, writer);
		}
		writer.add(EV_FACTORY.createEndElement(CoreQName.DBOBJECTS.getQName(), null));
	}
	
	private void write3DataSets(XMLEventWriter writer) throws XMLStreamException, SerializationException {
		JiemamyContext context = getContext();
		
		List<? extends DataSetModel> dataSets = context.getDataSets();
		if (dataSets.size() <= 0) {
			return;
		}
		writer.add(EV_FACTORY.createStartElement(CoreQName.DATASETS.getQName(), null, null));
		for (DataSetModel dsm : dataSets) {
			getDirector().direct(dsm, writer);
		}
		writer.add(EV_FACTORY.createEndElement(CoreQName.DATASETS.getQName(), null));
	}
	
	private void write4Facets(XMLEventWriter writer) throws XMLStreamException, SerializationException {
		JiemamyContext context = getContext();
		
		Set<JiemamyFacet> facets = context.getFacets();
		for (JiemamyFacet facet : facets) {
			getDirector().direct(facet, writer);
		}
	}
}
