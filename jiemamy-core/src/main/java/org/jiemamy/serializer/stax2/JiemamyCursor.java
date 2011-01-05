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

import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.in.SMInputCursor;

import org.jiemamy.xml.JiemamyQName;

/**
 * TODO for daisuke
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
	
	public JiemamyCursor advance() throws XMLStreamException {
		return new JiemamyCursor(cursor.advance());
	}
	
	public JiemamyCursor childElementCursor(JiemamyQName jQName) throws XMLStreamException {
		return new JiemamyCursor(cursor.childElementCursor(jQName.getQName()));
	}
	
	public String collectDescendantText(boolean includeIgnorable) {
		try {
			return cursor.collectDescendantText(includeIgnorable);
		} catch (XMLStreamException e) {
			// TODO
		}
		return null;
	}
	
	public SMEvent getCurrEvent() {
		return cursor.getCurrEvent();
	}
	
	public QName getQName() throws XMLStreamException {
		return cursor.getQName();
	}
	
	public boolean isQName(JiemamyQName jQName) throws XMLStreamException {
		return cursor.getQName().equals(jQName.getQName());
	}
}
