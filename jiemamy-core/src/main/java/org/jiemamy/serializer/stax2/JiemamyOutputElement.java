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
import org.codehaus.staxmate.out.SMBufferable;
import org.codehaus.staxmate.out.SMNamespace;
import org.codehaus.staxmate.out.SMOutputContainer;
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
	 * @param element DOCME
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JiemamyOutputElement(SMOutputElement element) {
		super(element);
		this.element = element;
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#_canOutputNewChild()
	 */
	@Override
	public boolean _canOutputNewChild() throws XMLStreamException { // CHECKSTYLE IGNORE THIS LINE
		return element._canOutputNewChild();
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param buffered DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addAndReleaseBuffered(org.codehaus.staxmate.out.SMBufferable)
	 */
	@Override
	public SMBufferable addAndReleaseBuffered(SMBufferable buffered) throws XMLStreamException {
		return element.addAndReleaseBuffered(buffered);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param jQName DOCME
	 * @param clazz DOCME
	 * @throws XMLStreamException DOCME
	 */
	public void addAttribute(JiemamyQName jQName, Class<?> clazz) throws XMLStreamException {
		Validate.notNull(clazz);
		addAttribute(jQName, clazz.getName());
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param jQName DOCME
	 * @param obj DOCME
	 * @throws XMLStreamException DOCME
	 */
	public void addAttribute(JiemamyQName jQName, Object obj) throws XMLStreamException {
		Validate.notNull(obj);
		addAttribute(jQName, obj.toString());
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param jQName DOCME
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 */
	public void addAttribute(JiemamyQName jQName, String value) throws XMLStreamException {
		QName qName = jQName.getQName();
		SMNamespace ns = element.getNamespace(qName.getNamespaceURI(), qName.getPrefix());
		element.addAttribute(ns, qName.getLocalPart(), value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param ns DOCME
	 * @param localName DOCME
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#
	 * addAttribute(org.codehaus.staxmate.out.SMNamespace, java.lang.String, boolean)
	 */
	public void addAttribute(SMNamespace ns, String localName, boolean value) throws XMLStreamException {
		element.addAttribute(ns, localName, value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param ns DOCME
	 * @param localName DOCME
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#addAttribute(org.codehaus.staxmate.out.SMNamespace, java.lang.String, int)
	 */
	public void addAttribute(SMNamespace ns, String localName, int value) throws XMLStreamException {
		element.addAttribute(ns, localName, value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param ns DOCME
	 * @param localName DOCME
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#
	 * addAttribute(org.codehaus.staxmate.out.SMNamespace, java.lang.String, long)
	 */
	public void addAttribute(SMNamespace ns, String localName, long value) throws XMLStreamException {
		element.addAttribute(ns, localName, value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param ns DOCME
	 * @param localName DOCME
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#
	 * addAttribute(org.codehaus.staxmate.out.SMNamespace, java.lang.String, java.lang.String)
	 */
	public void addAttribute(SMNamespace ns, String localName, String value) throws XMLStreamException {
		element.addAttribute(ns, localName, value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param localName DOCME
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#addAttribute(java.lang.String, java.lang.String)
	 */
	public final void addAttribute(String localName, String value) throws XMLStreamException {
		element.addAttribute(localName, value);
	}
	
	/**
	 * somethingを取得する。 TODO for daisuke
	 * @return the element
	 */
	public SMOutputElement getSMOutputElement() {
		return element;
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#getLocalName()
	 */
	public String getLocalName() {
		return element.getLocalName();
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#getNamespace()
	 */
	public SMNamespace getNamespace() {
		return element.getNamespace();
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param parent DOCME
	 * @param blocked DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#linkParent(org.codehaus.staxmate.out.SMOutputContainer, boolean)
	 */
	public void linkParent(SMOutputContainer parent, boolean blocked) throws XMLStreamException {
		element.linkParent(parent, blocked);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param ns DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputElement#predeclareNamespace(org.codehaus.staxmate.out.SMNamespace)
	 */
	public void predeclareNamespace(SMNamespace ns) throws XMLStreamException {
		element.predeclareNamespace(ns);
	}
	
}
