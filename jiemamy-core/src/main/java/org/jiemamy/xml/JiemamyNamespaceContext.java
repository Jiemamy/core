/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/03
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

import java.util.Arrays;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import org.apache.commons.lang.Validate;

/**
 * Jiemamy用の{@link NamespaceContext}実装クラス。
 * 
 * @author daisuke
 */
public class JiemamyNamespaceContext implements NamespaceContext {
	
	private JiemamyNamespace[] namespaces;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param namespaces 名前空間の配列
	 * @throws IllegalArgumentException 引数namespaceが{@code null}または空の場合
	 */
	public JiemamyNamespaceContext(JiemamyNamespace[] namespaces) {
		Validate.notEmpty(namespaces);
		this.namespaces = namespaces.clone();
	}
	
	public String getNamespaceURI(String prefix) {
		Validate.notNull(prefix);
		for (JiemamyNamespace ns : namespaces) {
			if (prefix.equals(ns.getPrefix())) {
				return ns.getNamespaceURI().toString();
			}
		}
		return null;
	}
	
	public String getPrefix(String namespaceURI) {
		Validate.notNull(namespaceURI);
		for (JiemamyNamespace ns : namespaces) {
			if (namespaceURI.equals(ns.getNamespaceURI().toString())) {
				return ns.getPrefix();
			}
		}
		return null;
	}
	
	public Iterator<?> getPrefixes(String namespaceURI) {
		Validate.notNull(namespaceURI);
		String p = getPrefix(namespaceURI);
		if (p != null) {
			return Arrays.asList(p).iterator();
		}
		return null;
	}
}
