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
	 * @param container
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JiemamyOutputContainer(SMOutputContainer container) {
		Validate.notNull(container);
		this.container = container;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#_canOutputNewChild()
	 */
	public boolean _canOutputNewChild() throws XMLStreamException {
		return container._canOutputNewChild();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param buffered
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addAndReleaseBuffered(org.codehaus.staxmate.out.SMBufferable)
	 */
	public SMBufferable addAndReleaseBuffered(SMBufferable buffered) throws XMLStreamException {
		return container.addAndReleaseBuffered(buffered);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param buffered
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addBuffered(org.codehaus.staxmate.out.SMBufferable)
	 */
	public SMBufferable addBuffered(SMBufferable buffered) throws XMLStreamException {
		return container.addBuffered(buffered);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param buf
	 * @param offset
	 * @param len
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addCData(char[], int, int)
	 */
	public void addCData(char[] buf, int offset, int len) throws XMLStreamException {
		container.addCData(buf, offset, len);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param text
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addCData(java.lang.String)
	 */
	public void addCData(String text) throws XMLStreamException {
		container.addCData(text);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param buf
	 * @param offset
	 * @param len
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addCharacters(char[], int, int)
	 */
	public void addCharacters(char[] buf, int offset, int len) throws XMLStreamException {
		container.addCharacters(buf, offset, len);
	}
	
	public void addCharacters(String text) throws XMLStreamException {
		if (text != null) {
			container.addCharacters(text);
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param text
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addComment(java.lang.String)
	 */
	public void addComment(String text) throws XMLStreamException {
		if (StringUtils.isEmpty(text) == false) {
			container.addComment(text);
		}
	}
	
	public JiemamyOutputElement addElement(JiemamyQName jQName) throws XMLStreamException {
		QName qName = jQName.getQName();
		SMNamespace ns = container.getNamespace(qName.getNamespaceURI(), qName.getPrefix());
		SMOutputElement element = container.addElement(ns, qName.getLocalPart());
		return new JiemamyOutputElement(element);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ns
	 * @param localName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addElement(org.codehaus.staxmate.out.SMNamespace, java.lang.String)
	 */
	public JiemamyOutputElement addElement(SMNamespace ns, String localName) throws XMLStreamException {
		return new JiemamyOutputElement(container.addElement(ns, localName));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param localName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addElement(java.lang.String)
	 */
	public JiemamyOutputElement addElement(String localName) throws XMLStreamException {
		return new JiemamyOutputElement(container.addElement(localName));
	}
	
	public JiemamyOutputElement addElementAndCharacters(JiemamyQName jQName, Enum<?> e) throws XMLStreamException {
		if (e != null) {
			return addElementAndCharacters(jQName, e.name());
		}
		return null;
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
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ns
	 * @param localName
	 * @param text
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addElementWithCharacters(org.codehaus.staxmate.out.SMNamespace, java.lang.String, java.lang.String)
	 */
	public JiemamyOutputElement addElementWithCharacters(SMNamespace ns, String localName, String text)
			throws XMLStreamException {
		return new JiemamyOutputElement(container.addElementWithCharacters(ns, localName, text));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param name
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addEntityRef(java.lang.String)
	 */
	public void addEntityRef(String name) throws XMLStreamException {
		container.addEntityRef(name);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param target
	 * @param data
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addProcessingInstruction(java.lang.String, java.lang.String)
	 */
	public void addProcessingInstruction(String target, String data) throws XMLStreamException {
		container.addProcessingInstruction(target, data);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addValue(boolean)
	 */
	public void addValue(boolean value) throws XMLStreamException {
		container.addValue(value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addValue(double)
	 */
	public void addValue(double value) throws XMLStreamException {
		container.addValue(value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addValue(int)
	 */
	public void addValue(int value) throws XMLStreamException {
		container.addValue(value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param value
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.out.SMOutputContainer#addValue(long)
	 */
	public void addValue(long value) throws XMLStreamException {
		container.addValue(value);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ns
	 * @param localName
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputContainer#createBufferedElement(org.codehaus.staxmate.out.SMNamespace, java.lang.String)
	 */
	public SMBufferedElement createBufferedElement(SMNamespace ns, String localName) {
		return container.createBufferedElement(ns, localName);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputContainer#createBufferedFragment()
	 */
	public SMBufferedFragment createBufferedFragment() {
		return container.createBufferedFragment();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getContext()
	 */
	public final SMOutputContext getContext() {
		return container.getContext();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param uri
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getNamespace(java.lang.String)
	 */
	public final SMNamespace getNamespace(String uri) {
		return container.getNamespace(uri);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param uri
	 * @param prefPrefix
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getNamespace(java.lang.String, java.lang.String)
	 */
	public final SMNamespace getNamespace(String uri, String prefPrefix) {
		return container.getNamespace(uri, prefPrefix);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getParent()
	 */
	public final JiemamyOutputContainer getParent() {
		return new JiemamyOutputContainer(container.getParent());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getPath()
	 */
	public final String getPath() {
		return container.getPath();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param sb
	 * @see org.codehaus.staxmate.out.SMOutputContainer#getPath(java.lang.StringBuilder)
	 */
	public void getPath(StringBuilder sb) {
		container.getPath(sb);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param indentStr
	 * @param startOffset
	 * @param step
	 * @see org.codehaus.staxmate.out.SMOutputContainer#setIndentation(java.lang.String, int, int)
	 */
	public void setIndentation(String indentStr, int startOffset, int step) {
		container.setIndentation(indentStr, startOffset, step);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return container.toString();
	}
	
}
