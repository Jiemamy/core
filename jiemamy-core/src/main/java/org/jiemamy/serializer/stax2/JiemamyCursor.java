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

import java.io.IOException;
import java.io.Writer;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.in.ElementInfoFactory;
import org.codehaus.staxmate.in.SMElementInfo;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.in.SMFilter;
import org.codehaus.staxmate.in.SMInputCursor;
import org.codehaus.staxmate.in.SMInputCursor.Tracking;

import org.jiemamy.xml.JiemamyQName;

/**
 * {@link SMInputCursor}をJiemamyで扱いやすくするためのラッパークラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyCursor {
	
	final SMInputCursor cursor;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param cursor
	 */
	public JiemamyCursor(SMInputCursor cursor) {
		this.cursor = cursor;
	}
	
	public final JiemamyCursor advance() throws XMLStreamException {
		cursor.advance();
		return this;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#asEvent()
	 */
	public XMLEvent asEvent() throws XMLStreamException {
		return cursor.asEvent();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#childCursor()
	 */
	public final JiemamyCursor childCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.childCursor());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param f
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#childCursor(org.codehaus.staxmate.in.SMFilter)
	 */
	public JiemamyCursor childCursor(SMFilter f) throws XMLStreamException {
		return new JiemamyCursor(cursor.childCursor(f));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#childElementCursor()
	 */
	public final JiemamyCursor childElementCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.childElementCursor());
	}
	
	public final JiemamyCursor childElementCursor(JiemamyQName jQName) throws XMLStreamException {
		return new JiemamyCursor(cursor.childElementCursor(jQName.getQName()));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param elemName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#childElementCursor(javax.xml.namespace.QName)
	 */
	public final JiemamyCursor childElementCursor(QName elemName) throws XMLStreamException {
		return new JiemamyCursor(cursor.childElementCursor(elemName));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param elemLocalName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#childElementCursor(java.lang.String)
	 */
	public final JiemamyCursor childElementCursor(String elemLocalName) throws XMLStreamException {
		return new JiemamyCursor(cursor.childElementCursor(elemLocalName));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#childMixedCursor()
	 */
	public final JiemamyCursor childMixedCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.childMixedCursor());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#collectDescendantText()
	 */
	public final String collectDescendantText() throws XMLStreamException {
		return cursor.collectDescendantText();
	}
	
	public String collectDescendantText(boolean includeIgnorable) {
		try {
			return cursor.collectDescendantText(includeIgnorable);
		} catch (XMLStreamException e) {
			// TODO
		}
		return null;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param msg
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#constructStreamException(java.lang.String)
	 */
	public XMLStreamException constructStreamException(String msg) {
		return cursor.constructStreamException(msg);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantCursor()
	 */
	public final JiemamyCursor descendantCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantCursor());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param f
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantCursor(org.codehaus.staxmate.in.SMFilter)
	 */
	public JiemamyCursor descendantCursor(SMFilter f) throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantCursor(f));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantElementCursor()
	 */
	public final JiemamyCursor descendantElementCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantElementCursor());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param elemName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantElementCursor(javax.xml.namespace.QName)
	 */
	public final JiemamyCursor descendantElementCursor(QName elemName) throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantElementCursor(elemName));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param elemLocalName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantElementCursor(java.lang.String)
	 */
	public final JiemamyCursor descendantElementCursor(String elemLocalName) throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantElementCursor(elemLocalName));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantMixedCursor()
	 */
	public final JiemamyCursor descendantMixedCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantMixedCursor());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return cursor.equals(obj);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param uri
	 * @param localName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#findAttrIndex(java.lang.String, java.lang.String)
	 */
	public int findAttrIndex(String uri, String localName) throws XMLStreamException {
		return cursor.findAttrIndex(uri, localName);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrBooleanValue(int)
	 */
	public boolean getAttrBooleanValue(int index) throws XMLStreamException {
		return cursor.getAttrBooleanValue(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @param defValue
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrBooleanValue(int, boolean)
	 */
	public boolean getAttrBooleanValue(int index, boolean defValue) throws XMLStreamException {
		return cursor.getAttrBooleanValue(index, defValue);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrCount()
	 */
	public int getAttrCount() throws XMLStreamException {
		return cursor.getAttrCount();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrDoubleValue(int)
	 */
	public double getAttrDoubleValue(int index) throws XMLStreamException {
		return cursor.getAttrDoubleValue(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @param defValue
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrDoubleValue(int, double)
	 */
	public double getAttrDoubleValue(int index, double defValue) throws XMLStreamException {
		return cursor.getAttrDoubleValue(index, defValue);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param <T>
	 * @param index
	 * @param enumType
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrEnumValue(int, java.lang.Class)
	 */
	public <T extends Enum<T>>T getAttrEnumValue(int index, Class<T> enumType) throws XMLStreamException {
		return cursor.getAttrEnumValue(index, enumType);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrIntValue(int)
	 */
	public int getAttrIntValue(int index) throws XMLStreamException {
		return cursor.getAttrIntValue(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @param defValue
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrIntValue(int, int)
	 */
	public int getAttrIntValue(int index, int defValue) throws XMLStreamException {
		return cursor.getAttrIntValue(index, defValue);
	}
	
	public int getAttrIntValue(JiemamyQName jQName) throws XMLStreamException {
		int index = cursor.findAttrIndex(jQName.getQName().getNamespaceURI(), jQName.getQName().getLocalPart());
		return cursor.getAttrIntValue(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrLocalName(int)
	 */
	public String getAttrLocalName(int index) throws XMLStreamException {
		return cursor.getAttrLocalName(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrLongValue(int)
	 */
	public long getAttrLongValue(int index) throws XMLStreamException {
		return cursor.getAttrLongValue(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @param defValue
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrLongValue(int, long)
	 */
	public long getAttrLongValue(int index, long defValue) throws XMLStreamException {
		return cursor.getAttrLongValue(index, defValue);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrName(int)
	 */
	public QName getAttrName(int index) throws XMLStreamException {
		return cursor.getAttrName(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrNsUri(int)
	 */
	public String getAttrNsUri(int index) throws XMLStreamException {
		return cursor.getAttrNsUri(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrPrefix(int)
	 */
	public String getAttrPrefix(int index) throws XMLStreamException {
		return cursor.getAttrPrefix(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param index
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrValue(int)
	 */
	public String getAttrValue(int index) throws XMLStreamException {
		return cursor.getAttrValue(index);
	}
	
	public String getAttrValue(JiemamyQName jQName) throws XMLStreamException {
		int index = cursor.findAttrIndex(jQName.getQName().getNamespaceURI(), jQName.getQName().getLocalPart());
		return cursor.getAttrValue(index);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param localName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrValue(java.lang.String)
	 */
	public String getAttrValue(String localName) throws XMLStreamException {
		return cursor.getAttrValue(localName);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrValue(java.lang.String, java.lang.String)
	 */
	public String getAttrValue(String namespaceURI, String localName) throws XMLStreamException {
		return cursor.getAttrValue(namespaceURI, localName);
	}
	
	public SMEvent getCurrEvent() {
		return cursor.getCurrEvent();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getCurrEventCode()
	 */
	public int getCurrEventCode() {
		return cursor.getCurrEventCode();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getCursorLocation()
	 */
	public Location getCursorLocation() throws XMLStreamException {
		return cursor.getCursorLocation();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getData()
	 */
	public Object getData() {
		return cursor.getData();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemBooleanValue()
	 */
	public boolean getElemBooleanValue() throws XMLStreamException {
		return cursor.getElemBooleanValue();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param defValue
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemBooleanValue(boolean)
	 */
	public boolean getElemBooleanValue(boolean defValue) throws XMLStreamException {
		return cursor.getElemBooleanValue(defValue);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemDoubleValue()
	 */
	public double getElemDoubleValue() throws XMLStreamException {
		return cursor.getElemDoubleValue();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param defValue
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemDoubleValue(double)
	 */
	public double getElemDoubleValue(double defValue) throws XMLStreamException {
		return cursor.getElemDoubleValue(defValue);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElementCount()
	 */
	public int getElementCount() {
		return cursor.getElementCount();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElementInfoFactory()
	 */
	public final ElementInfoFactory getElementInfoFactory() {
		return cursor.getElementInfoFactory();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElementTracking()
	 */
	public final Tracking getElementTracking() {
		return cursor.getElementTracking();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param <T>
	 * @param enumType
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemEnumValue(java.lang.Class)
	 */
	public <T extends Enum<T>>T getElemEnumValue(Class<T> enumType) throws XMLStreamException {
		return cursor.getElemEnumValue(enumType);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemIntValue()
	 */
	public int getElemIntValue() throws XMLStreamException {
		return cursor.getElemIntValue();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param defValue
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemIntValue(int)
	 */
	public int getElemIntValue(int defValue) throws XMLStreamException {
		return cursor.getElemIntValue(defValue);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemLongValue()
	 */
	public long getElemLongValue() throws XMLStreamException {
		return cursor.getElemLongValue();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param defValue
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemLongValue(long)
	 */
	public long getElemLongValue(long defValue) throws XMLStreamException {
		return cursor.getElemLongValue(defValue);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemStringValue()
	 */
	public String getElemStringValue() throws XMLStreamException {
		return cursor.getElemStringValue();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getLocalName()
	 */
	public String getLocalName() throws XMLStreamException {
		return cursor.getLocalName();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getNext()
	 */
	public SMEvent getNext() throws XMLStreamException {
		return cursor.getNext();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getNodeCount()
	 */
	public int getNodeCount() {
		return cursor.getNodeCount();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getNsUri()
	 */
	public String getNsUri() throws XMLStreamException {
		return cursor.getNsUri();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getParentCount()
	 */
	public int getParentCount() {
		return cursor.getParentCount();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getParentTrackedElement()
	 */
	public SMElementInfo getParentTrackedElement() {
		return cursor.getParentTrackedElement();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getPathDesc()
	 */
	public String getPathDesc() {
		return cursor.getPathDesc();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getPrefix()
	 */
	public String getPrefix() throws XMLStreamException {
		return cursor.getPrefix();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getPrefixedName()
	 */
	public String getPrefixedName() throws XMLStreamException {
		return cursor.getPrefixedName();
	}
	
	public QName getQName() throws XMLStreamException {
		return cursor.getQName();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getStreamLocation()
	 */
	public Location getStreamLocation() {
		return cursor.getStreamLocation();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getStreamReader()
	 */
	public final XMLStreamReader2 getStreamReader() {
		return cursor.getStreamReader();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#getText()
	 */
	public String getText() throws XMLStreamException {
		return cursor.getText();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#getTrackedElement()
	 */
	public SMElementInfo getTrackedElement() {
		return cursor.getTrackedElement();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return cursor.hashCode();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param expName
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#hasLocalName(java.lang.String)
	 */
	public boolean hasLocalName(String expName) throws XMLStreamException {
		return cursor.hasLocalName(expName);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param qname
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#hasName(javax.xml.namespace.QName)
	 */
	public boolean hasName(QName qname) throws XMLStreamException {
		return cursor.hasName(qname);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param expNsURI
	 * @param expLN
	 * @return
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#hasName(java.lang.String, java.lang.String)
	 */
	public boolean hasName(String expNsURI, String expLN) throws XMLStreamException {
		return cursor.hasName(expNsURI, expLN);
	}
	
	public boolean isQName(JiemamyQName jQName) throws XMLStreamException {
		return cursor.getQName().equals(jQName.getQName());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#isRootCursor()
	 */
	public final boolean isRootCursor() {
		return cursor.isRootCursor();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param w
	 * @param includeIgnorable
	 * @throws IOException
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#processDescendantText(java.io.Writer, boolean)
	 */
	public void processDescendantText(Writer w, boolean includeIgnorable) throws IOException, XMLStreamException {
		cursor.processDescendantText(w, includeIgnorable);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#readerAccessible()
	 */
	public final boolean readerAccessible() {
		return cursor.readerAccessible();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param o
	 * @see org.codehaus.staxmate.in.SMInputCursor#setData(java.lang.Object)
	 */
	public void setData(Object o) {
		cursor.setData(o);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param f
	 * @see org.codehaus.staxmate.in.SMInputCursor#setElementInfoFactory(org.codehaus.staxmate.in.ElementInfoFactory)
	 */
	public final void setElementInfoFactory(ElementInfoFactory f) {
		cursor.setElementInfoFactory(f);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param tracking
	 * @see org.codehaus.staxmate.in.SMInputCursor#setElementTracking(org.codehaus.staxmate.in.SMInputCursor.Tracking)
	 */
	public final void setElementTracking(Tracking tracking) {
		cursor.setElementTracking(tracking);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param f
	 * @see org.codehaus.staxmate.in.SMInputCursor#setFilter(org.codehaus.staxmate.in.SMFilter)
	 */
	public final void setFilter(SMFilter f) {
		cursor.setFilter(f);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param msg
	 * @throws XMLStreamException
	 * @see org.codehaus.staxmate.in.SMInputCursor#throwStreamException(java.lang.String)
	 */
	public void throwStreamException(String msg) throws XMLStreamException {
		cursor.throwStreamException(msg);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @see org.codehaus.staxmate.in.SMInputCursor#toString()
	 */
	@Override
	public String toString() {
		return cursor.toString();
	}
}
