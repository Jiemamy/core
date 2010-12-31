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
public enum DiagramQName implements JiemamyQName {
	
	/***/
	DIAGRAMS(DiagramNamespace.NS_DIAGRAM, "diagrams"),

	/***/
	DIAGRAM(DiagramNamespace.NS_DIAGRAM, "diagram"),

	/***/
	NODE(DiagramNamespace.NS_DIAGRAM, "node"),

	/***/
	BOUNDARY(DiagramNamespace.NS_DIAGRAM, "boundary"),

	/***/
	HEIGHT(DiagramNamespace.NS_DIAGRAM, "height"),

	/***/
	WIDTH(DiagramNamespace.NS_DIAGRAM, "width"),

	/***/
	X(DiagramNamespace.NS_DIAGRAM, "x"),

	/***/
	Y(DiagramNamespace.NS_DIAGRAM, "y"),

	/***/
	COLOR(DiagramNamespace.NS_DIAGRAM, "color"),

	/***/
	R(DiagramNamespace.NS_DIAGRAM, "red"),

	/***/
	G(DiagramNamespace.NS_DIAGRAM, "green"),

	/***/
	B(DiagramNamespace.NS_DIAGRAM, "blue"),

	/***/
	CORE(DiagramNamespace.NS_DIAGRAM, "core"),

//	/***/
//	DIAGRAM_PRESENTATION(DiagramNamespace.NS_DIAGRAM, "diagramPresentation"),
//
//	/***/
//	NODE_PROFILES(DiagramNamespace.NS_DIAGRAM, "nodeProfiles"),
//
//	/***/
//	NODE_PROFILE(DiagramNamespace.NS_DIAGRAM, "nodeProfile"),
//
//	/***/
//	NODE_OBJECT_REF(DiagramNamespace.NS_DIAGRAM, "nodeObjectRef"),
//
//	/***/
//	STICKY(DiagramNamespace.NS_DIAGRAM, "sticky"),
	
	/***/
	CONTENTS(DiagramNamespace.NS_DIAGRAM, "contents"),

//	/***/
//	CONNECTION_PROFILES(DiagramNamespace.NS_DIAGRAM, "connectionProfiles"),
//
//	/***/
//	CONNECTION_PROFILE(DiagramNamespace.NS_DIAGRAM, "connectionProfile"),
//
//	/***/
//	CONNECTION_OBJECT_REF(DiagramNamespace.NS_DIAGRAM, "connectionObjectRef"),
	
	/***/
	BENDPOINTS(DiagramNamespace.NS_DIAGRAM, "bendpoints"),

	/***/
	BENDPOINT(DiagramNamespace.NS_DIAGRAM, "bendpoint"),

	/***/
	MODE(DiagramNamespace.NS_DIAGRAM, "mode"),

	/***/
	LEVEL(DiagramNamespace.NS_DIAGRAM, "level");
	
	/** XML仕様における完全修飾名 */
	private final QName qName;
	

	DiagramQName(JiemamyNamespace namespace, String localPart) {
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
