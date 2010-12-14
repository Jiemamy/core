/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/15
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

import javax.xml.namespace.QName;

/**
 * Jiemamy VIEW仕様におけるXMLノード定数を保持する列挙型。
 * 
 * @since 0.2
 * @author daisuke
 */
public enum SqlQName implements JiemamyQName {
	
	/***/
	DIAGRAM_PRESENTATIONS(SqlNamespace.NS_SQL, "diagramPresentations"),

	/***/
	DIAGRAM_PRESENTATION(SqlNamespace.NS_SQL, "diagramPresentation"),

	/***/
	NODE_PROFILES(SqlNamespace.NS_SQL, "nodeProfiles"),

	/***/
	NODE_PROFILE(SqlNamespace.NS_SQL, "nodeProfile"),

	/***/
	NODE_OBJECT_REF(SqlNamespace.NS_SQL, "nodeObjectRef"),

	/***/
	STICKY(SqlNamespace.NS_SQL, "sticky"),

	/***/
	CONTENTS(SqlNamespace.NS_SQL, "contents"),

	/***/
	BOUNDARY(SqlNamespace.NS_SQL, "boundary"),

	/***/
	CONNECTION_PROFILES(SqlNamespace.NS_SQL, "connectionProfiles"),

	/***/
	CONNECTION_PROFILE(SqlNamespace.NS_SQL, "connectionProfile"),

	/***/
	CONNECTION_OBJECT_REF(SqlNamespace.NS_SQL, "connectionObjectRef"),

	/***/
	BENDPOINTS(SqlNamespace.NS_SQL, "bendpoints"),

	/***/
	BENDPOINT(SqlNamespace.NS_SQL, "bendpoint"),

	/***/
	MODE(SqlNamespace.NS_SQL, "mode"),

	/***/
	LEVEL(SqlNamespace.NS_SQL, "level");
	
	/** XML仕様における完全修飾名 */
	private final QName qName;
	

	SqlQName(JiemamyNamespace namespace, String localPart) {
		qName = new QName(namespace.getNamespaceURI().toString(), localPart, namespace.getPrefix());
	}
	
	public QName getQName() {
		return qName;
	}
	
	public String getQNameString() {
		String prefix = isEmpty(qName.getPrefix()) ? "" : qName.getPrefix() + ":";
		return prefix + qName.getLocalPart();
	}
	
	private boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
}
