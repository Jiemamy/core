/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
	SQLS(SqlNamespace.NS_SQL, "sqls"),

	/***/
	AROUND_SCRIPT(SqlNamespace.NS_SQL, "aroundScript"),

	/***/
	CORE(SqlNamespace.NS_SQL, "core"),

	/***/
	SCRIPT(SqlNamespace.NS_SQL, "script"),

	/***/
	POSITION(SqlNamespace.NONE, "position");
	
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
