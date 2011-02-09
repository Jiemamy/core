/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/11/05
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
package org.jiemamy.xml;

import java.net.URI;
import java.net.URISyntaxException;

import org.jiemamy.JiemamyError;

/**
 * XML名前空間定数クラス。
 * 
 * @since 0.2
 * @author daisuke
 */
public enum SqlNamespace implements JiemamyNamespace {
	
	/** Jiemamy sql specifications */
	NS_SQL("sql", "http://jiemamy.org/xml/ns/sql", "http://jiemamy.org/xml/0.3/jiemamy-sql.xsd"),

	/**  */
	NONE("", "", "");
	
	/** 名前空間prefix */
	private final String prefix;
	
	/** 名前空間URI */
	private final URI namespaceURI;
	
	private final String xmlSchemaLocation;
	

	SqlNamespace(String prefix, String namespaceURI, String xmlSchemaLocation) {
		this.prefix = prefix;
		try {
			this.namespaceURI = new URI(namespaceURI);
			this.xmlSchemaLocation = xmlSchemaLocation;
		} catch (URISyntaxException e) {
			throw new JiemamyError("URI記述ミス？", e);
		}
	}
	
	public URI getNamespaceURI() {
		return namespaceURI;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getXmlSchemaLocation() {
		return xmlSchemaLocation;
	}
}
