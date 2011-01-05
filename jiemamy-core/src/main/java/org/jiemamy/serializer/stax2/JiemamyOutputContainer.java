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
package org.jiemamy.serializer.stax2;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.out.SMNamespace;
import org.codehaus.staxmate.out.SMOutputContainer;
import org.codehaus.staxmate.out.SMOutputElement;

import org.jiemamy.xml.JiemamyQName;

/**
 * {@link SMOutputContainer}をJiemamyで扱いやすくするためのラッパークラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyOutputContainer {
	
	final SMOutputContainer container;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param container
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JiemamyOutputContainer(SMOutputContainer container) {
		Validate.notNull(container);
		this.container = container;
	}
	
	public void addCharacters(String text) throws XMLStreamException {
		container.addCharacters(text);
	}
	
	public JiemamyOutputElement addElement(JiemamyQName jQName) throws XMLStreamException {
		QName qName = jQName.getQName();
		SMNamespace ns = container.getNamespace(qName.getNamespaceURI(), qName.getPrefix());
		SMOutputElement element = container.addElement(ns, qName.getLocalPart());
		return new JiemamyOutputElement(element);
	}
	
	public JiemamyOutputElement addElementAndCharacters(JiemamyQName jQName, String text) throws XMLStreamException {
		if (text == null) {
			return null;
		}
		JiemamyOutputElement e = addElement(jQName);
		if (StringUtils.isEmpty(text) == false) {
			e.addCharacters(text);
		}
		return e;
	}
}
