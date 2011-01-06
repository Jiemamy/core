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
	 * @param element
	 */
	public JiemamyOutputElement(SMOutputElement element) {
		super(element);
		this.element = element;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputElement#_canOutputNewChild()
	 */
	@Override
	public boolean _canOutputNewChild() throws XMLStreamException {
		return element._canOutputNewChild();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param buffered
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addAndReleaseBuffered(org.codehaus.staxmate.out.SMBufferable)
	 */
	@Override
	public SMBufferable addAndReleaseBuffered(SMBufferable buffered) throws XMLStreamException {
		return element.addAndReleaseBuffered(buffered);
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
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ns
	 * @param localName
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputElement#addAttribute(org.codehaus.staxmate.out.SMNamespace, java.lang.String, boolean)
	 */
	public void addAttribute(SMNamespace ns, String localName, boolean value) throws XMLStreamException {
		element.addAttribute(ns, localName, value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ns
	 * @param localName
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputElement#addAttribute(org.codehaus.staxmate.out.SMNamespace, java.lang.String, int)
	 */
	public void addAttribute(SMNamespace ns, String localName, int value) throws XMLStreamException {
		element.addAttribute(ns, localName, value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ns
	 * @param localName
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputElement#addAttribute(org.codehaus.staxmate.out.SMNamespace, java.lang.String, long)
	 */
	public void addAttribute(SMNamespace ns, String localName, long value) throws XMLStreamException {
		element.addAttribute(ns, localName, value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ns
	 * @param localName
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputElement#addAttribute(org.codehaus.staxmate.out.SMNamespace, java.lang.String, java.lang.String)
	 */
	public void addAttribute(SMNamespace ns, String localName, String value) throws XMLStreamException {
		element.addAttribute(ns, localName, value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param localName
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputElement#addAttribute(java.lang.String, java.lang.String)
	 */
	public final void addAttribute(String localName, String value) throws XMLStreamException {
		element.addAttribute(localName, value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputElement#getLocalName()
	 */
	public String getLocalName() {
		return element.getLocalName();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputElement#getNamespace()
	 */
	public SMNamespace getNamespace() {
		return element.getNamespace();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param parent
	 * @param blocked
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputElement#linkParent(org.codehaus.staxmate.out.SMOutputContainer, boolean)
	 */
	public void linkParent(SMOutputContainer parent, boolean blocked) throws XMLStreamException {
		element.linkParent(parent, blocked);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ns
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputElement#predeclareNamespace(org.codehaus.staxmate.out.SMNamespace)
	 */
	public void predeclareNamespace(SMNamespace ns) throws XMLStreamException {
		element.predeclareNamespace(ns);
	}
	
}
