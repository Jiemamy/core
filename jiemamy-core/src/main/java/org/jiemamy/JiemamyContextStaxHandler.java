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
package org.jiemamy;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.out.SMNamespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.DbObject;
import org.jiemamy.model.dataset.JmDataSet;
import org.jiemamy.serializer.DbObjectComparator;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax.DeserializationContext;
import org.jiemamy.serializer.stax.JiemamyCursor;
import org.jiemamy.serializer.stax.JiemamyOutputContainer;
import org.jiemamy.serializer.stax.JiemamyOutputElement;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax.StaxHandler;
import org.jiemamy.xml.CoreNamespace;
import org.jiemamy.xml.CoreQName;

/**
 * {@link JiemamyContext}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class JiemamyContextStaxHandler extends StaxHandler<JiemamyContext> {
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyContextStaxHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JiemamyContextStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public JiemamyContext handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.JIEMAMY));
			
			JiemamyContext context = dctx.getContext();
			
			JiemamyCursor cursor = dctx.peek();
			
			String versionString = cursor.getAttrValue(CoreQName.VERSION);
			verifyVersionCompatibility(versionString);
			
			JiemamyCursor childCursor = cursor.childElementCursor();
			dctx.push(childCursor);
			do {
				childCursor.advance();
				if (childCursor.getCurrEvent() == SMEvent.START_ELEMENT) {
					if (childCursor.isQName(CoreQName.META)) {
						JmMetadata meta = getDirector().direct(dctx);
						if (meta != null) {
							context.setMetadata(meta);
						} else {
							logger.warn("null meta");
						}
					} else if (childCursor.isQName(CoreQName.DBOBJECTS)) {
						JiemamyCursor dbObjectsCursor = childCursor.childElementCursor();
						while (dbObjectsCursor.getNext() != null) {
							dctx.push(dbObjectsCursor);
							DbObject dbObject = getDirector().direct(dctx);
							if (dbObject != null) {
								context.store(dbObject);
							} else {
								logger.warn("null dbObject");
							}
							dctx.pop();
						}
					} else if (childCursor.isQName(CoreQName.DATASETS)) {
						JiemamyCursor dataSetsCursor = childCursor.childElementCursor();
						while (dataSetsCursor.getNext() != null) {
							dctx.push(dataSetsCursor);
							JmDataSet dataSet = getDirector().direct(dctx);
							if (dataSet != null) {
								context.store(dataSet);
							} else {
								logger.warn("null dataSet");
							}
							dctx.pop();
						}
					} else {
						getDirector().direct(dctx);
//						logger.warn("UNKNOWN ELEMENT: {}", childCursor.getQName().toString());
					}
				} else if (childCursor.getCurrEvent() != null) {
					logger.warn("UNKNOWN EVENT: {}", childCursor.getCurrEvent());
				}
			} while (childCursor.getCurrEvent() != null);
			dctx.pop();
			
			return context;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(JiemamyContext model, SerializationContext sctx) throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.JIEMAMY);
			sctx.push(element);
			
			SMNamespace xsiNs =
					element.getSMOutputElement().getNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi");
			element.addAttribute(CoreQName.VERSION, JiemamyContext.getVersion().toStringSpec());
			element.addAttribute(xsiNs, "schemaLocation", getSchemaLocationDefinition(CoreNamespace.values()));
			
			JmMetadata meta = model.getMetadata();
			if (meta != null) {
				getDirector().direct(meta, sctx);
			}
			
			List<DbObject> dbObjects = Lists.newArrayList(model.getDbObjects());
			Collections.sort(dbObjects, DbObjectComparator.INSTANCE);
			sctx.push(element.addElement(CoreQName.DBOBJECTS));
			for (DbObject dbObject : dbObjects) {
				getDirector().direct(dbObject, sctx);
			}
			sctx.pop(); // end of dbObjects
			
			sctx.push(element.addElement(CoreQName.DATASETS));
			for (JmDataSet dataSet : model.getDataSets()) {
				getDirector().direct(dataSet, sctx);
			}
			sctx.pop(); // end of dataSets
			
			for (JiemamyFacet facet : model.getFacets()) {
				getDirector().direct(facet, sctx);
			}
			
			sctx.pop(); // --- end of jiemamy
			
			parent.addCharacters("\n");
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	private void verifyVersionCompatibility(String versionString) throws SerializationException {
		Version parsed = Version.parse(versionString);
		Version myVersion = JiemamyContext.getVersion();
		if (myVersion.canDeserialize(parsed) == false) {
			// FORMAT-OFF
			String message = MessageFormat.format(
					"Jiemamy v{0} cannot deserialize v{1} data.",
					myVersion.toString(), parsed.toString());
			// FORMAT-ON
			throw new SerializationException(message);
		}
	}
}
