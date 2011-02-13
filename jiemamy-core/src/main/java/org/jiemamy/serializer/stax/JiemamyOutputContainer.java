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
package org.jiemamy.serializer.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.out.SMBufferable;
import org.codehaus.staxmate.out.SMBufferedElement;
import org.codehaus.staxmate.out.SMBufferedFragment;
import org.codehaus.staxmate.out.SMNamespace;
import org.codehaus.staxmate.out.SMOutputContainer;
import org.codehaus.staxmate.out.SMOutputContext;
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
	 * @param container DOCME
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JiemamyOutputContainer(SMOutputContainer container) {
		Validate.notNull(container);
		this.container = container;
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#_canOutputNewChild()
	 */
	public boolean _canOutputNewChild() throws XMLStreamException { // CHECKSTYLE IGNORE THIS LINE
		return container._canOutputNewChild();
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param buffered DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addAndReleaseBuffered(org.codehaus.staxmate.out.SMBufferable)
	 */
	public SMBufferable addAndReleaseBuffered(SMBufferable buffered) throws XMLStreamException {
		return container.addAndReleaseBuffered(buffered);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param buffered DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addBuffered(org.codehaus.staxmate.out.SMBufferable)
	 */
	public SMBufferable addBuffered(SMBufferable buffered) throws XMLStreamException {
		return container.addBuffered(buffered);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param buf DOCME
	 * @param offset DOCME
	 * @param len DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addCData(char[], int, int)
	 */
	public void addCData(char[] buf, int offset, int len) throws XMLStreamException {
		container.addCData(buf, offset, len);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param text DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addCData(java.lang.String)
	 */
	public void addCData(String text) throws XMLStreamException {
		container.addCData(text);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param buf DOCME
	 * @param offset DOCME
	 * @param len DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addCharacters(char[], int, int)
	 */
	public void addCharacters(char[] buf, int offset, int len) throws XMLStreamException {
		container.addCharacters(buf, offset, len);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param text DOCME
	 * @throws XMLStreamException DOCME
	 */
	public void addCharacters(String text) throws XMLStreamException {
		if (text != null) {
			container.addCharacters(text);
		}
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param text DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addComment(java.lang.String)
	 */
	public void addComment(String text) throws XMLStreamException {
		if (StringUtils.isEmpty(text) == false) {
			container.addComment(text);
		}
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param jQName DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 */
	public JiemamyOutputElement addElement(JiemamyQName jQName) throws XMLStreamException {
		QName qName = jQName.getQName();
		SMNamespace ns = container.getNamespace(qName.getNamespaceURI(), qName.getPrefix());
		SMOutputElement element = container.addElement(ns, qName.getLocalPart());
		return new JiemamyOutputElement(element);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param ns DOCME
	 * @param localName DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addElement(org.codehaus.staxmate.out.SMNamespace, java.lang.String)
	 */
	public JiemamyOutputElement addElement(SMNamespace ns, String localName) throws XMLStreamException {
		return new JiemamyOutputElement(container.addElement(ns, localName));
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param localName DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addElement(java.lang.String)
	 */
	public JiemamyOutputElement addElement(String localName) throws XMLStreamException {
		return new JiemamyOutputElement(container.addElement(localName));
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param jQName DOCME
	 * @param e DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 */
	public JiemamyOutputElement addElementAndCharacters(JiemamyQName jQName, Enum<?> e) throws XMLStreamException {
		if (e != null) {
			return addElementAndCharacters(jQName, e.name());
		}
		return null;
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param jQName DOCME
	 * @param text DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 */
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
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param ns DOCME
	 * @param localName DOCME
	 * @param text DOCME
	 * @return DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#
	 * addElementWithCharacters(org.codehaus.staxmate.out.SMNamespace, java.lang.String, java.lang.String)
	 */
	public JiemamyOutputElement addElementWithCharacters(SMNamespace ns, String localName, String text)
			throws XMLStreamException {
		return new JiemamyOutputElement(container.addElementWithCharacters(ns, localName, text));
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param name DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addEntityRef(java.lang.String)
	 */
	public void addEntityRef(String name) throws XMLStreamException {
		container.addEntityRef(name);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param target DOCME
	 * @param data DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addProcessingInstruction(java.lang.String, java.lang.String)
	 */
	public void addProcessingInstruction(String target, String data) throws XMLStreamException {
		container.addProcessingInstruction(target, data);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addValue(boolean)
	 */
	public void addValue(boolean value) throws XMLStreamException {
		container.addValue(value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addValue(double)
	 */
	public void addValue(double value) throws XMLStreamException {
		container.addValue(value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addValue(int)
	 */
	public void addValue(int value) throws XMLStreamException {
		container.addValue(value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param value DOCME
	 * @throws XMLStreamException DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addValue(long)
	 */
	public void addValue(long value) throws XMLStreamException {
		container.addValue(value);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param ns DOCME
	 * @param localName DOCME
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#
	 * createBufferedElement(org.codehaus.staxmate.out.SMNamespace, java.lang.String)
	 */
	public SMBufferedElement createBufferedElement(SMNamespace ns, String localName) {
		return container.createBufferedElement(ns, localName);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#createBufferedFragment()
	 */
	public SMBufferedFragment createBufferedFragment() {
		return container.createBufferedFragment();
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getContext()
	 */
	public final SMOutputContext getContext() {
		return container.getContext();
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param uri DOCME
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getNamespace(java.lang.String)
	 */
	public final SMNamespace getNamespace(String uri) {
		return container.getNamespace(uri);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param uri DOCME
	 * @param prefPrefix DOCME
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getNamespace(java.lang.String, java.lang.String)
	 */
	public final SMNamespace getNamespace(String uri, String prefPrefix) {
		return container.getNamespace(uri, prefPrefix);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getParent()
	 */
	public final JiemamyOutputContainer getParent() {
		return new JiemamyOutputContainer(container.getParent());
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getPath()
	 */
	public final String getPath() {
		return container.getPath();
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param sb DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getPath(java.lang.StringBuilder)
	 */
	public void getPath(StringBuilder sb) {
		container.getPath(sb);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @param indentStr DOCME
	 * @param startOffset DOCME
	 * @param step DOCME
	 * @see org.codehaus.staxmate.out.SMOutputContainer#setIndentation(java.lang.String, int, int)
	 */
	public void setIndentation(String indentStr, int startOffset, int step) {
		container.setIndentation(indentStr, startOffset, step);
	}
	
	/**
	 * DOCME for daisuke
	 * 
	 * @return DOCME
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return container.toString();
	}
	
}
