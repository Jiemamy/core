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

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.out.SMNamespace;
import org.codehaus.staxmate.out.SMOutputElement;

import org.jiemamy.xml.JiemamyQName;

/**
 * {@link SMOutputElement}をJiemamyで扱いやすくするためのラッパークラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyOutputElement extends JiemamyOutputContainer {
	
	final SMOutputElement element;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param element
	 */
	public JiemamyOutputElement(SMOutputElement element) {
		super(element);
		this.element = element;
	}
	
	public void addAttribute(JiemamyQName jQName, Class<?> clazz) throws XMLStreamException {
		Validate.notNull(clazz);
		addAttribute(jQName, clazz.getName());
	}
	
	public void addAttribute(JiemamyQName jQName, Object obj) throws XMLStreamException {
		Validate.notNull(obj);
		addAttribute(jQName, obj.toString());
	}
	
	public void addAttribute(JiemamyQName jQName, String value) throws XMLStreamException {
		QName qName = jQName.getQName();
		SMNamespace ns = element.getNamespace(qName.getNamespaceURI(), qName.getPrefix());
		element.addAttribute(ns, qName.getLocalPart(), value);
	}
	
	public void addElementAndCharacters(JiemamyQName jQName, Enum<?> e) throws XMLStreamException {
		addElementAndCharacters(jQName, e.name());
	}
	
}
