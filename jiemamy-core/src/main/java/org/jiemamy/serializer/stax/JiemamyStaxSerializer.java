/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
package org.jiemamy.serializer.stax;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.stax2.XMLStreamWriter2;
import org.codehaus.stax2.validation.Validatable;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidationSchemaFactory;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.SMOutputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.out.SMOutputContext;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.FacetProvider;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyFacet;
import org.jiemamy.serializer.JiemamySerializer;
import org.jiemamy.serializer.SerializationException;

/**
 * {@link JiemamySerializer}のStAX（+staxmate）による実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyStaxSerializer implements JiemamySerializer {
	
	private boolean validate = true;
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyStaxSerializer.class);
	
	
	public JiemamyContext deserialize(InputStream in, FacetProvider... facetProviders) throws SerializationException {
		Validate.notNull(in);
		Validate.noNullElements(facetProviders);
		JiemamyContext context = new JiemamyContext(facetProviders);
		
		SMHierarchicCursor cursor = null;
		SMInputFactory inFactory = new SMInputFactory(XMLInputFactory.newInstance());
		try {
			cursor = inFactory.rootElementCursor(in);
			
			Validatable streamReader = cursor.getStreamReader();
			setValidators(streamReader, context.getFacets());
			
			DeserializationContext dctx = new DeserializationContext(context, cursor);
			StaxDirector director = new StaxDirector(context);
			cursor.advance();
			director.direct(dctx);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		} finally {
			if (cursor != null) {
				try {
					cursor.getStreamReader().closeCompletely();
				} catch (XMLStreamException e) {
					// ignore
				}
			}
		}
		return context;
	}
	
	/**
	 * シリアライズ・デシリアライズ時にXMLSchemaバリデーションを行うかどうかを返す。
	 * 
	 * @return バリデーションを行う場合は{@code true}、そうでない場合は{@code false}
	 */
	public boolean isValidate() {
		return validate;
	}
	
	public void serialize(JiemamyContext context, OutputStream out) throws SerializationException {
		Validate.notNull(context);
		Validate.notNull(out);
		SMOutputDocument doc = null;
		SMOutputFactory outFactory = new SMOutputFactory(XMLOutputFactory.newInstance());
		XMLStreamWriter2 writer = null;
		try {
			writer = outFactory.createStax2Writer(out);
			setValidators(writer, context.getFacets());
			
			doc = SMOutputContext.createInstance(writer).createDocument();
			doc.setIndentation("\n" + StringUtils.repeat("  ", 100), 1, 2); // CHECKSTYLE IGNORE THIS LINE
			
			SerializationContext ctx = new SerializationContext(context, doc);
			StaxDirector director = new StaxDirector(context);
			director.direct(context, ctx);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (XMLStreamException e) {
					// ignore
				}
			}
		}
		
	}
	
	/**
	 * シリアライズ・デシリアライズ時にXMLSchemaバリデーションを行うかどうかを設定する。
	 * 
	 * @param validate バリデーションを行う場合は{@code true}、そうでない場合は{@code false}
	 */
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	
	private void setValidators(Validatable validatable, Set<JiemamyFacet> facets) throws XMLStreamException {
		if (validate == false) {
			return;
		}
		XMLValidationSchemaFactory sf =
				XMLValidationSchemaFactory.newInstance(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
		XMLValidationSchema vs = sf.createSchema(JiemamyStaxSerializer.class.getResource("/jiemamy-core.xsd"));
		validatable.validateAgainst(vs);
		for (JiemamyFacet facet : facets) {
			URL schema = facet.getSchema();
			if (schema != null) {
				try {
					XMLValidationSchema fvs = sf.createSchema(schema);
					validatable.validateAgainst(fvs);
				} catch (XMLStreamException e) {
					// schema.jiemamy.org に接続できない時はここに来る。
					// jiemamy-diagram.xsdから、jiemamy-core.xsdを参照しているからなぁ。
					logger.warn(e.getMessage());
				}
			}
		}
	}
}
