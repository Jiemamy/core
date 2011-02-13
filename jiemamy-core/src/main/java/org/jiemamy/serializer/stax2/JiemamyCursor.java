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
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.Validate;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.typed.TypedXMLStreamException;
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
	 * @param cursor ラップ対象のカーソル
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JiemamyCursor(SMInputCursor cursor) {
		Validate.notNull(cursor);
		this.cursor = cursor;
	}
	
	/**
	 * Method that does what {@link #getNext()} does, but instead of
	 * returning resulting event type, returns <b>this</b> cursor.
	 * The main purpose of this method is to allow for chaining
	 * calls. This is especially useful right after constructing
	 * a root level cursor, and needing to advance it to point to
	 * the root element. As such, a common idiom is:
	 *<pre>
	 * SMInputCursor positionedRoot = smFactory.rootElementCursor(file).advance();
	 *</pre>
	 * which both constructs the root element cursor, and positions it
	 * over the root element. Can be similarly used with other kinds of
	 * cursors as well, of course
	 * 
	 * @return this
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#advance()
	 */
	public final JiemamyCursor advance() throws XMLStreamException {
		cursor.advance();
		return this;
	}
	
	/**
	 * Method for accessing information regarding event this
	 * cursor points to, as an instance of {@link XMLEvent}.
	 * Depending on underlying input source this may be constructed
	 * from scratch (for example, if the input cursor was not
	 *  constructed from
	 * {@link javax.xml.stream.XMLEventReader}), or accessed
	 * from the underlying event reader.
	 *<p>
	 * Calling this method does not advance the underlying stream
	 * or cursor itself.
	 *<p>
	 * Note, too, that it is ok to call this method at any time:
	 * if the cursor is not in valid state for accessing information
	 * null will be returned.
	 *
	 * @return Information about currently pointed-to input stream
	 *   event, if we are pointing to one; null otherwise
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#asEvent()
	 */
	public XMLEvent asEvent() throws XMLStreamException {
		return cursor.asEvent();
	}
	
	/**
	 * Method that will create a new nested cursor for iterating
	 * over all (immediate) child nodes of the start element this cursor
	 * currently points to.
	 * If cursor does not point to a start element,
	 * it will throw {@link IllegalStateException}; if it does not support
	 * concept of child cursors, it will throw
	 * {@link UnsupportedOperationException}
	 * 
	 * @return child cursor
	 * @throws IllegalStateException If cursor can not be created due
	 *   to the state cursor is in.
	 * @throws UnsupportedOperationException If cursor does not allow
	 *   creation of child cursors.
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#childCursor()
	 */
	public final JiemamyCursor childCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.childCursor());
	}
	
	/**
	 * Method that will create a new nested cursor for iterating
	 * over all (immediate) child nodes of the start element this cursor
	 * currently points to that are passed by the specified filter.
	 * If cursor does not point to a start element,
	 * it will throw {@link IllegalStateException}; if it does not support
	 * concept of child cursors, it will throw
	 * {@link UnsupportedOperationException}
	 *
	 * @param f Filter child cursor is to use for filtering out
	 *    'unwanted' nodes; may be null if no filtering is to be done
	 * @return filtered child cursor
	 * @throws IllegalStateException If cursor can not be created due
	 *   to the state cursor is in.
	 * @throws UnsupportedOperationException If cursor does not allow
	 *   creation of child cursors.
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#childCursor(org.codehaus.staxmate.in.SMFilter)
	 */
	public JiemamyCursor childCursor(SMFilter f) throws XMLStreamException {
		return new JiemamyCursor(cursor.childCursor(f));
	}
	
	/**
	 * Convenience method; equivalent to 
	 *<code>childCursor(SMFilterFactory.getElementOnlyFilter());</code>
	 * 
	 * @return element only filtered child cursor
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#childElementCursor()
	 */
	public final JiemamyCursor childElementCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.childElementCursor());
	}
	
	/**
	 * Convenience method; equivalent to 
	 *<code>childCursor(SMFilterFactory.getElementOnlyFilter(elemName));</code>
	 * Will only return START_ELEMENT and END_ELEMENT events, whose element
	 * name matches given qname.
	 * 
	 * @param elemName element name
	 * @return element name filtered child cursor
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#childElementCursor(javax.xml.namespace.QName)
	 */
	public final JiemamyCursor childElementCursor(JiemamyQName elemName) throws XMLStreamException {
		return new JiemamyCursor(cursor.childElementCursor(elemName.getQName()));
	}
	
	/**
	 * Convenience method; equivalent to 
	 *<code>childCursor(SMFilterFactory.getElementOnlyFilter(elemName));</code>
	 * Will only return START_ELEMENT and END_ELEMENT events, whose element's
	 * local name matches given local name, and that does not belong to
	 * a namespace.
	 * 
	 * @param elemLocalName element local name
	 * @return element local name filtered child cursor
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#childElementCursor(java.lang.String)
	 */
	public final JiemamyCursor childElementCursor(String elemLocalName) throws XMLStreamException {
		return new JiemamyCursor(cursor.childElementCursor(elemLocalName));
	}
	
	/**
	 * Convenience method; equivalent to 
	 *<code>childCursor(SMFilterFactory.getMixedFilter());</code>
	 * 
	 * @return mixed filtered child cursor
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#childMixedCursor()
	 */
	public final JiemamyCursor childMixedCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.childMixedCursor());
	}
	
	/**
	 * Same as calling {@link #collectDescendantText(boolean)} with 'false':
	 * that is, do not include ignorable white space (as determined by DTD
	 * or Schema) in the result text.
	 *<p>
	 * Note: it is not common to have have ignorable white space; it usually
	 * results from indentation, but its detection requires DTD/schema-aware
	 * processing</p>
	 * 
	 * @return text
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#collectDescendantText()
	 */
	public final String collectDescendantText() throws XMLStreamException {
		return cursor.collectDescendantText();
	}
	
	/**
	 * Method that can collect all text contained within START_ELEMENT
	 * currently pointed by this cursor. Collection is done recursively
	 * through all descendant text (CHARACTER, CDATA; optionally SPACE) nodes,
	 * ignoring nodes of other types. After collecting text, cursor
	 * will be positioned at the END_ELEMENT matching initial START_ELEMENT
	 * and thus needs to be advanced to access the next sibling event.
	 *
	 * @param includeIgnorable Whether text for events of type SPACE should
	 *   be ignored in the results or not. If false, SPACE events will be
	 *   skipped; if true, white space will be included in results.
	 * @return text
	 */
	public String collectDescendantText(boolean includeIgnorable) {
		try {
			return cursor.collectDescendantText(includeIgnorable);
		} catch (XMLStreamException e) {
			// ignore
		}
		return null;
	}
	
	/**
	 * Method for constructing stream exception with given message,
	 * and location that matches that of the underlying stream
	 *<b>regardless of whether this cursor is valid</b> (that is,
	 * will indicate location of the stream which may differ from
	 * where this cursor was last valid)
	 * 
	 * @param msg message
	 * @return exception
	 * @see org.codehaus.staxmate.in.SMInputCursor#constructStreamException(java.lang.String)
	 */
	public XMLStreamException constructStreamException(String msg) {
		return cursor.constructStreamException(msg);
	}
	
	/**
	 * Method that will create a new nested cursor for iterating
	 * over all the descendant (children and grandchildren) nodes of
	 * the start element this cursor currently points to.
	 * If cursor does not point to a start element,
	 * it will throw {@link IllegalStateException}; if it does not support
	 * concept of descendant cursors, it will throw
	 * {@link UnsupportedOperationException}
	 * @return descendant cursor
	 *
	 * @throws IllegalStateException If cursor can not be created due
	 *   to the state cursor is in (or for some cursors, if they never
	 *   allow creating such cursors)
	 * @throws UnsupportedOperationException If cursor does not allow
	 *   creation of descendant cursors.
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantCursor()
	 */
	public final JiemamyCursor descendantCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantCursor());
	}
	
	/**
	 * Method that will create a new nested cursor for iterating
	 * over all the descendant (children and grandchildren) nodes of
	 * the start element this cursor currently points to
	 * that are accepted by the specified filter.
	 * If cursor does not point to a start element,
	 * it will throw {@link IllegalStateException}; if it does not support
	 * concept of descendant cursors, it will throw
	 * {@link UnsupportedOperationException}
	 *
	 *
	 * @param f Filter child cursor is to use for filtering out
	 *    'unwanted' nodes; may be null if no filtering is to be done
	 * @return filtered descendant cursor
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @throws IllegalStateException If cursor can not be created due
	 *   to the state cursor is in (or for some cursors, if they never
	 *   allow creating such cursors)
	 * @throws UnsupportedOperationException If cursor does not allow
	 *   creation of descendant cursors.
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantCursor(org.codehaus.staxmate.in.SMFilter)
	 */
	public JiemamyCursor descendantCursor(SMFilter f) throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantCursor(f));
	}
	
	/**
	 * Convenience method; equivalent to 
	 *<code>descendantCursor(SMFilterFactory.getElementOnlyFilter());</code>
	 * 
	 * @return element only filtered descendant cursor
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantElementCursor()
	 */
	public final JiemamyCursor descendantElementCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantElementCursor());
	}
	
	/**
	 * Convenience method; equivalent to 
	 *<code>descendantCursor(SMFilterFactory.getElementOnlyFilter(elemName));</code>
	 * Will only return START_ELEMENT and END_ELEMENT events, whose element
	 * name matches given qname.
	 * 
	 * @param elemName element name
	 * @return element name filtered descendant cursor
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantElementCursor(javax.xml.namespace.QName)
	 */
	public final JiemamyCursor descendantElementCursor(QName elemName) throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantElementCursor(elemName));
	}
	
	/**
	 * Convenience method; equivalent to 
	 *<code>descendantCursor(SMFilterFactory.getElementOnlyFilter(elemLocalName));</code>.
	 * Will only return START_ELEMENT and END_ELEMENT events, whose element
	 * local name matches given local name, and that do not belong to a
	 * namespace
	 * 
	 * @param elemLocalName element local name
	 * @return element local name filtered descendant cursor 
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantElementCursor(java.lang.String)
	 */
	public final JiemamyCursor descendantElementCursor(String elemLocalName) throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantElementCursor(elemLocalName));
	}
	
	/**
	 * Convenience method; equivalent to 
	 *<code>descendantCursor(SMFilterFactory.getMixedFilter());</code>
	 * 
	 * @return mixed filtered descendant cursor
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#descendantMixedCursor()
	 */
	public final JiemamyCursor descendantMixedCursor() throws XMLStreamException {
		return new JiemamyCursor(cursor.descendantMixedCursor());
	}
	
	/**
	 * Method that can be called when this cursor points to START_ELEMENT,
	 * and which will return index of specified attribute, if it
	 * exists for this element. If not, -1 is returned to denote "not found".
	 * 
	 * @param uri uri string
	 * @param localName local name
	 * @return index
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT)
	 * @see org.codehaus.staxmate.in.SMInputCursor#findAttrIndex(java.lang.String, java.lang.String)
	 */
	public int findAttrIndex(String uri, String localName) throws XMLStreamException {
		return cursor.findAttrIndex(uri, localName);
	}
	
	/**
	 * Method for accessing value of specified attribute as boolean.
	 * Method will only succeed if the attribute value is a valid
	 * boolean, as specified by XML Schema specification (and hence
	 * is accessible via Stax2 Typed Access API).
	 *
	 * @param index Index of attribute to access
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of boolean
	 * @throws IllegalArgumentException If given attribute index is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrBooleanValue(int)
	 */
	public boolean getAttrBooleanValue(int index) throws XMLStreamException {
		return cursor.getAttrBooleanValue(index);
	}
	
	/**
	 * Method for accessing value of specified attribute as boolean.
	 * If attribute value is not a valid boolean
	 * (as specified by XML Schema specification), will instead
	 * return specified "default value".
	 *
	 * @param index Index of attribute to access
	 * @param defValue Value to return if attribute value exists but
	 *   is not a valid boolean value
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of boolean.
	 * @throws IllegalArgumentException If given attribute index
	 *   is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrBooleanValue(int, boolean)
	 */
	public boolean getAttrBooleanValue(int index, boolean defValue) throws XMLStreamException {
		return cursor.getAttrBooleanValue(index, defValue);
	}
	
	/**
	 * Method that can be called when this cursor points to START_ELEMENT,
	 * and which will return number of attributes with values for the
	 * start element. This includes both explicit attribute values and
	 * possible implied default values (when DTD support is enabled
	 * by the underlying stream reader).
	 * 
	 * @return count
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrCount()
	 */
	public int getAttrCount() throws XMLStreamException {
		return cursor.getAttrCount();
	}
	
	/**
	 * Method for accessing value of specified attribute as double.
	 * Method will only succeed if the attribute value is a valid
	 * double, as specified by XML Schema specification (and hence
	 * is accessible via Stax2 Typed Access API).
	 *
	 * @param index Index of attribute to access
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of double.
	 * @throws IllegalArgumentException If given attribute index
	 *   is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrDoubleValue(int)
	 */
	public double getAttrDoubleValue(int index) throws XMLStreamException {
		return cursor.getAttrDoubleValue(index);
	}
	
	/**
	 * Method for accessing value of specified attribute as double.
	 * If attribute value is not a valid double
	 * (as specified by XML Schema specification), will instead
	 * return specified "default value".
	 *
	 * @param index Index of attribute to access
	 * @param defValue Value to return if attribute value exists but
	 *   is not a valid double value
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of double.
	 * @throws IllegalArgumentException If given attribute index
	 *   is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrDoubleValue(int, double)
	 */
	public double getAttrDoubleValue(int index, double defValue) throws XMLStreamException {
		return cursor.getAttrDoubleValue(index, defValue);
	}
	
	/**
	 * Method for accessing value of specified attribute as an
	 * Enum value of specified type, if content non-empty.
	 * If it is empty, will return null. And if non-empty value
	 * is not equal to name() of one of Enum values, will throw
	 * a {@link TypedXMLStreamException} to indicate the problem.
	 * 
	 * @param <T> type of enum
	 * @param index Index of attribute to access
	 * @param enumType enum
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of double.
	 * @throws IllegalArgumentException If given attribute index
	 *   is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrEnumValue(int, java.lang.Class)
	 */
	public <T extends Enum<T>>T getAttrEnumValue(int index, Class<T> enumType) throws XMLStreamException {
		return cursor.getAttrEnumValue(index, enumType);
	}
	
	/**
	 * DOCME
	 * 
	 * @param <T> type of enum
	 * @param jQName Jiemamy QName
	 * @param enumType enum
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of double.
	 */
	public <T extends Enum<T>>T getAttrEnumValue(JiemamyQName jQName, Class<T> enumType) throws XMLStreamException {
		int index = cursor.findAttrIndex(jQName.getQName().getNamespaceURI(), jQName.getQName().getLocalPart());
		return cursor.getAttrEnumValue(index, enumType);
	}
	
	/**
	 * Method for accessing value of specified attribute as integer.
	 * Method will only succeed if the attribute value is a valid
	 * integer, as specified by XML Schema specification (and hence
	 * is accessible via Stax2 Typed Access API).
	 *
	 * @param index Index of attribute to access
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of integer.
	 * @throws IllegalArgumentException If given attribute index
	 *   is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrIntValue(int)
	 */
	public int getAttrIntValue(int index) throws XMLStreamException {
		return cursor.getAttrIntValue(index);
	}
	
	/**
	 * Method for accessing value of specified attribute as integer.
	 * If attribute value is not a valid integer
	 * (as specified by XML Schema specification), will instead
	 * return specified "default value".
	 *
	 * @param index Index of attribute to access
	 * @param defValue Value to return if attribute value exists but
	 *   is not a valid integer value
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of integer.
	 * @throws IllegalArgumentException If given attribute index
	 *   is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrIntValue(int, int)
	 */
	public int getAttrIntValue(int index, int defValue) throws XMLStreamException {
		return cursor.getAttrIntValue(index, defValue);
	}
	
	/**
	 * DOCME
	 * 
	 * @param jQName Jiemamy QName
	 * @return value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT)
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of integer.
	 */
	public int getAttrIntValue(JiemamyQName jQName) throws XMLStreamException {
		int index = cursor.findAttrIndex(jQName.getQName().getNamespaceURI(), jQName.getQName().getLocalPart());
		return cursor.getAttrIntValue(index);
	}
	
	/**
	 * Method that can be called when this cursor points to START_ELEMENT,
	 * and returns local name
	 * of the attribute at specified index.
	 * Index has to be between [0, {@link #getAttrCount}[; otherwise
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param index Index of the attribute
	 * @return local name
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT),
	 *   or if invalid attribute 
	 * @throws IllegalArgumentException if attribute index is invalid
	 *   (less than 0 or greater than the last valid index
	 *   [getAttributeCount()-1])
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrLocalName(int)
	 */
	public String getAttrLocalName(int index) throws XMLStreamException {
		return cursor.getAttrLocalName(index);
	}
	
	/**
	 * Method for accessing value of specified attribute as long.
	 * Method will only succeed if the attribute value is a valid
	 * long, as specified by XML Schema specification (and hence
	 * is accessible via Stax2 Typed Access API).
	 *
	 * @param index Index of attribute to access
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of long.
	 * @throws IllegalArgumentException If given attribute index
	 *   is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrLongValue(int)
	 */
	public long getAttrLongValue(int index) throws XMLStreamException {
		return cursor.getAttrLongValue(index);
	}
	
	/**
	 * Method for accessing value of specified attribute as long.
	 * If attribute value is not a valid long
	 * (as specified by XML Schema specification), will instead
	 * return specified "default value".
	 *
	 * @param index Index of attribute to access
	 * @param defValue Value to return if attribute value exists but
	 *   is not a valid long value
	 * @return value
	 * @throws XMLStreamException If specified attribute can not be
	 *   accessed (due to cursor state), or if attribute value
	 *   is not a valid textual representation of long.
	 * @throws IllegalArgumentException If given attribute index
	 *   is invalid
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrLongValue(int, long)
	 */
	public long getAttrLongValue(int index, long defValue) throws XMLStreamException {
		return cursor.getAttrLongValue(index, defValue);
	}
	
	/**
	 * Method that can be called when this cursor points to START_ELEMENT,
	 * and returns fully qualified name
	 * of the attribute at specified index.
	 * Index has to be between [0, {@link #getAttrCount}[; otherwise
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param index Index of the attribute
	 * @return attribute name
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT),
	 *   or if invalid attribute 
	 * @throws IllegalArgumentException if attribute index is invalid
	 *   (less than 0 or greater than the last valid index
	 *   [getAttributeCount()-1])
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrName(int)
	 */
	public QName getAttrName(int index) throws XMLStreamException {
		return cursor.getAttrName(index);
	}
	
	/**
	 * Method that can be called when this cursor points to START_ELEMENT,
	 * and returns namespace URI
	 * of the attribute at specified index (non-empty String if it has
	 * one, and empty String if attribute does not belong to a namespace)
	 * Index has to be between [0, {@link #getAttrCount}[; otherwise
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param index Index of the attribute
	 * @return attribute namespace URI
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT),
	 *   or if invalid attribute 
	 * @throws IllegalArgumentException if attribute index is invalid
	 *   (less than 0 or greater than the last valid index
	 *   [getAttributeCount()-1])
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrNsUri(int)
	 */
	public String getAttrNsUri(int index) throws XMLStreamException {
		return cursor.getAttrNsUri(index);
	}
	
	/**
	 * Method that can be called when this cursor points to START_ELEMENT,
	 * and returns namespace prefix
	 * of the attribute at specified index (if it has any), or
	 * empty String if attribute has no prefix (does not belong to
	 * a namespace).
	 * Index has to be between [0, {@link #getAttrCount}[; otherwise
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param index Index of the attribute
	 * @return attribute prefix
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT),
	 *   or if invalid attribute 
	 * @throws IllegalArgumentException if attribute index is invalid
	 *   (less than 0 or greater than the last valid index
	 *   [getAttributeCount()-1])
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrPrefix(int)
	 */
	public String getAttrPrefix(int index) throws XMLStreamException {
		return cursor.getAttrPrefix(index);
	}
	
	/**
	 * Method that can be called when this cursor points to START_ELEMENT,
	 * and returns unmodified textual value
	 * of the attribute at specified index (non-empty String if it has
	 * one, and empty String if attribute does not belong to a namespace)
	 * Index has to be between [0, {@link #getAttrCount}[; otherwise
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param index Index of the attribute
	 * @return value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT),
	 *   or if invalid attribute 
	 * @throws IllegalArgumentException if attribute index is invalid
	 *   (less than 0 or greater than the last valid index
	 *   [getAttributeCount()-1])
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrValue(int)
	 */
	public String getAttrValue(int index) throws XMLStreamException {
		return cursor.getAttrValue(index);
	}
	
	/**
	 * DOCME
	 * 
	 * @param jQName Jiemamy QName
	 * @return value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT),
	 *   or if invalid attribute
	 * @throws IllegalArgumentException if attribute was not found 
	 */
	public String getAttrValue(JiemamyQName jQName) throws XMLStreamException {
		int index = cursor.findAttrIndex(jQName.getQName().getNamespaceURI(), jQName.getQName().getLocalPart());
		if (index == -1) {
			throw new IllegalArgumentException("attribute not found: " + jQName);
		}
		return cursor.getAttrValue(index);
	}
	
	/**
	 * Convenience accessor method to access an attribute that is
	 * not in a namespace (has no prefix). Equivalent to
	 * calling {@link #getAttrValue(String,String)} with
	 * 'null' for 'namespace URI' argument
	 * 
	 * @param localName local name
	 * @return value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrValue(java.lang.String)
	 */
	public String getAttrValue(String localName) throws XMLStreamException {
		return cursor.getAttrValue(localName);
	}
	
	/**
	 * Method that can be called when this cursor points to START_ELEMENT,
	 * and returns unmodified textual value
	 * of the specified attribute (if element has it), or null if
	 * element has no value for such attribute.
	 * 
	 * @param namespaceURI Namespace URI for the attribute, if any;
	 *   empty String or null if none.
	 * @param localName Local name of the attribute to access (in
	 *   namespace-aware mode: in non-namespace-aware mode, needs to
	 *   be the full name)
	 * @return  value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT),
	 *   or if invalid attribute 
	 * @see org.codehaus.staxmate.in.SMInputCursor#getAttrValue(java.lang.String, java.lang.String)
	 */
	public String getAttrValue(String namespaceURI, String localName) throws XMLStreamException {
		return cursor.getAttrValue(namespaceURI, localName);
	}
	
	/**
	 * Returns the type of event this cursor either currently points to
	 * (if in valid state), or pointed to (if ever iterated forward), or
	 * null if just created.
	 *
	 * @return Type of event this cursor points to, if it currently points
	 *   to one, or last one it pointed to otherwise (if ever pointed to
	 *   a valid event), or null if neither.
	 */
	public SMEvent getCurrEvent() {
		return cursor.getCurrEvent();
	}
	
	/**
	 * Convenience method for accessing type of the current event
	 * (as would be returned by {@link #getCurrEvent}) as
	 * one of event types defined in {@link XMLStreamConstants}
	 * (like {@link XMLStreamConstants#START_ELEMENT}).
	 * 
	 * @return event code
	 * @see org.codehaus.staxmate.in.SMInputCursor#getCurrEventCode()
	 */
	public int getCurrEventCode() {
		return cursor.getCurrEventCode();
	}
	
	/**
	 * Method to access starting Location of event (as defined by Stax
	 * specification)
	 * that this cursor points to.
	 * Method can only be called if the
	 * cursor is valid (as per {@link #readerAccessible}); if not,
	 * an exception is thrown
	 *
	 * @return Location of the event this cursor points to
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getCursorLocation()
	 */
	public Location getCursorLocation() throws XMLStreamException {
		return cursor.getCursorLocation();
	}
	
	/**
	 * Method for accessing application-provided data set previously
	 * by a {@link #setData} call.
	 * 
	 * @return object data
	 * @see org.codehaus.staxmate.in.SMInputCursor#getData()
	 */
	public Object getData() {
		return cursor.getData();
	}
	
	/**
	 * Method that can collect text <b>directly</b> contained within
	 * START_ELEMENT currently pointed by this cursor and convert
	 * it to a boolean value.
	 * For method to work, the value must be legal textual representation of
	 *<b>boolean</b>
	 * data type as specified by W3C Schema (as well as Stax2 Typed
	 * Access API).
	 * Element also can not contain mixed content (child elements;
	 * comments and processing instructions are allowed and ignored
	 * if encountered).
	 *
	 * @return value
	 * @throws XMLStreamException if content is not accessible or
	 *    convertible to required return type
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemBooleanValue()
	 */
	public boolean getElemBooleanValue() throws XMLStreamException {
		return cursor.getElemBooleanValue();
	}
	
	/**
	 * Similar to {@link #getElemBooleanValue()}, but instead of failing
	 * on invalid value, returns given default value.
	 * 
	 * @param defValue default value
	 * @return value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemBooleanValue(boolean)
	 */
	public boolean getElemBooleanValue(boolean defValue) throws XMLStreamException {
		return cursor.getElemBooleanValue(defValue);
	}
	
	/**
	 * Method that can collect text <b>directly</b> contained within
	 * START_ELEMENT currently pointed by this cursor and convert
	 * it to a double value.
	 * For method to work, the value must be legal textual representation of
	 *<b>double</b>
	 * data type as specified by W3C Schema (as well as Stax2 Typed
	 * Access API).
	 * Element also can not contain mixed content (child elements;
	 * comments and processing instructions are allowed and ignored
	 * if encountered).
	 *
	 * @return value
	 * @throws XMLStreamException if content is not accessible or
	 *    convertible to required return type
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemDoubleValue()
	 */
	public double getElemDoubleValue() throws XMLStreamException {
		return cursor.getElemDoubleValue();
	}
	
	/**
	 * Similar to {@link #getElemDoubleValue()}, but instead of failing
	 * on invalid value, returns given default value.
	 * 
	 * @param defValue default value
	 * @return value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemDoubleValue(double)
	 */
	public double getElemDoubleValue(double defValue) throws XMLStreamException {
		return cursor.getElemDoubleValue(defValue);
	}
	
	/**
	 * Method to access number of start elements cursor has traversed
	 * (including ones that were filtered out, if any).
	 * Starts with 0, and is incremented each time
	 * underlying stream reader's {@link XMLStreamReader#next} method
	 * is called and has moved over a start element, but not counting
	 * child cursors' element counts.
	 *
	 * @return Number of start elements cursor has traversed
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElementCount()
	 */
	public int getElementCount() {
		return cursor.getElementCount();
	}
	
	/**
	 * DOCME
	 * 
	 * @return DOCME
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElementInfoFactory()
	 */
	public final ElementInfoFactory getElementInfoFactory() {
		return cursor.getElementInfoFactory();
	}
	
	/**
	 * DOCME
	 * 
	 * @return DOCME
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElementTracking()
	 */
	public final Tracking getElementTracking() {
		return cursor.getElementTracking();
	}
	
	/**
	 * Method that can collect text <b>directly</b> contained within
	 * START_ELEMENT currently pointed by this cursor and convert
	 * it to one of enumerated values of given type, if textual
	 * value non-type, and otherwise to null.
	 * If a non-empty value that is not one of legal enumerated values
	 * is encountered, a {@link TypedXMLStreamException} is thrown.
	 *<p>
	 * Element also can not contain mixed content (child elements;
	 * comments and processing instructions are allowed and ignored
	 * if encountered).
	 * </p>
	 * 
	 * @param enumType enum
	 * @param <T> type of enum
	 * @return value
	 * @throws XMLStreamException if content is not accessible or
	 *    convertible to required return type
	 * @throws TypedXMLStreamException if element value is non-empty
	 *   and not one of allowed values for the enumeration type
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemEnumValue(java.lang.Class)
	 */
	public <T extends Enum<T>>T getElemEnumValue(Class<T> enumType) throws XMLStreamException {
		return cursor.getElemEnumValue(enumType);
	}
	
	/**
	 * Method that can collect text <b>directly</b> contained within
	 * START_ELEMENT currently pointed by this cursor and convert
	 * it to a int value.
	 * For method to work, the value must be legal textual representation of
	 *<b>int</b>
	 * data type as specified by W3C Schema (as well as Stax2 Typed
	 * Access API).
	 * Element also can not contain mixed content (child elements;
	 * comments and processing instructions are allowed and ignored
	 * if encountered).
	 *
	 * @return value
	 * @throws XMLStreamException if content is not accessible or
	 *    convertible to required return type
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemIntValue()
	 */
	public int getElemIntValue() throws XMLStreamException {
		return cursor.getElemIntValue();
	}
	
	/**
	 * Similar to {@link #getElemIntValue()}, but instead of failing
	 * on invalid value, returns given default value.
	 * 
	 * @param defValue default value
	 * @return value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemIntValue(int)
	 */
	public int getElemIntValue(int defValue) throws XMLStreamException {
		return cursor.getElemIntValue(defValue);
	}
	
	/**
	 * Method that can collect text <b>directly</b> contained within
	 * START_ELEMENT currently pointed by this cursor and convert
	 * it to a long value.
	 * For method to work, the value must be legal textual representation of
	 *<b>long</b>
	 * data type as specified by W3C Schema (as well as Stax2 Typed
	 * Access API).
	 * Element also can not contain mixed content (child elements;
	 * comments and processing instructions are allowed and ignored
	 * if encountered).
	 *
	 * @return value
	 * @throws XMLStreamException if content is not accessible or
	 *    convertible to required return type
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemLongValue()
	 */
	public long getElemLongValue() throws XMLStreamException {
		return cursor.getElemLongValue();
	}
	
	/**
	 * Similar to {@link #getElemLongValue()}, but instead of failing
	 * on invalid value, returns given default value.
	 * 
	 * @param defValue default value
	 * @return value
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemLongValue(long)
	 */
	public long getElemLongValue(long defValue) throws XMLStreamException {
		return cursor.getElemLongValue(defValue);
	}
	
	/**
	 * Method that can collect text <b>directly</b> contained within
	 * START_ELEMENT currently pointed by this cursor.
	 * This is different from {@link #collectDescendantText} in that
	 * it does NOT work for mixed content
	 * (child elements are not allowed:
	 * comments and processing instructions are allowed and ignored
	 * if encountered).
	 * If any ignorable white space (as per schema, dtd or so) is encountered,
	 * it will be ignored.
	 *<p>
	 * The main technical difference to  {@link #collectDescendantText} is
	 * that this method tries to make use of Stax2 v3.0 Typed Access API,
	 * if available, and can thus be more efficient.
	 * </p>
	 * 
	 * @return value
	 * @throws XMLStreamException if content is not accessible; may also
	 *  be thrown if child elements are encountered.
	 * @see org.codehaus.staxmate.in.SMInputCursor#getElemStringValue()
	 */
	public String getElemStringValue() throws XMLStreamException {
		return cursor.getElemStringValue();
	}
	
	/**
	 * For events with fully qualified names (START_ELEMENT, END_ELEMENT,
	 * ATTRIBUTE, NAMESPACE) returns the local component of the full
	 * name; for events with only non-qualified name (PROCESSING_INSTRUCTION,
	 * entity and notation declarations, references) returns the name, and
	 * for other events, returns null.
	 *
	 * @return Local component of the name
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getLocalName()
	 */
	public String getLocalName() throws XMLStreamException {
		return cursor.getLocalName();
	}
	
	/**
	 * Main iterating method. Will try to advance the cursor to the
	 * next visible event (visibility defined by the filter cursor
	 * is configured with, if any), and return event type.
	 * If no such events are available, will return null.
	 *<p>
	 * Note that one side-effect of calling this method is to invalidate
	 * the child cursor, if one was active. This is done by iterating over
	 * any events child cursor (and its descendants if any) might
	 * expose.
	 *
	 * @return Type of event (from {@link XMLStreamConstants}, such as
	 *   {@link XMLStreamConstants#START_ELEMENT}, if a new node was
	 *   iterated over; <code>null</code> when there are no more
	 *   nodes this cursor can iterate over.
	 * @throws XMLStreamException If there are underlying parsing
	 *   problems.
	 * @see org.codehaus.staxmate.in.SMInputCursor#getNext()
	 */
	public SMEvent getNext() throws XMLStreamException {
		return cursor.getNext();
	}
	
	/**
	 * Method to access number of nodes cursor has traversed
	 * (including ones that were filtered out, if any).
	 * Starts with 0, and is incremented each time
	 * underlying stream reader's {@link XMLStreamReader#next} method
	 * is called, but not counting child cursors' node counts.
	 * Whether END_ELEMENTs (end tags) are included depends on type
	 * of cursor: for nested (which do not return events for end tags)
	 * they are not counted, but for flattened one (that do return)
	 * they are counted as nodes.
	 *
	 * @return Number of nodes (events) cursor has traversed
	 * @see org.codehaus.staxmate.in.SMInputCursor#getNodeCount()
	 */
	public int getNodeCount() {
		return cursor.getNodeCount();
	}
	
	/**
	 * Method for accessing namespace URI of the START_ELEMENT this
	 * cursor points to.
	 *
	 * @return Namespace URI of currently pointed-to START_ELEMENT,
	 *   if it has one; "" if none
	 *
	 * @throws XMLStreamException if cursor does not point to START_ELEMENT
	 * @see org.codehaus.staxmate.in.SMInputCursor#getNsUri()
	 */
	public String getNsUri() throws XMLStreamException {
		return cursor.getNsUri();
	}
	
	/**
	 * Number of parent elements that the token/event cursor points to has,
	 * if it points to one. If not, either most recent valid parent
	 * count (if cursor is closed), or the depth that it will have
	 * once is is advanced. One practical result is that a nested
	 * cursor instance will always have a single constant value it
	 * returns, whereas flattening cursors can return different
	 * values during traversal. Another thing to notice that matching
	 * START_ELEMENT and END_ELEMENT will always correspond to the
	 * same parent count.
	 *<p>
	 * For example, here are expected return values
	 * for an example XML document:
	 *<pre>
	 *  &lt;!-- Comment outside tree --> [0]
	 *  &lt;root> [0]
	 *    Text [1]
	 *    &lt;branch> [1]
	 *      Inner text [2]
	 *      &lt;child /> [2]/[2]
	 *    &lt;/branch> [1]
	 *  &lt;/root> [0]
	 *</pre>
	 * Numbers in bracket are depths that would be returned when the
	 * cursor points to the node.
	 *<p>
	 * Note: depths are different from what many other xml processing
	 * APIs (such as Stax and XmlPull)return.
	 *
	 * @return Number of enclosing nesting levels, ie. number of parent
	 *   start elements for the node that cursor currently points to (or,
	 *   in case of initial state, that it will point to if scope has
	 *   node(s)).
	 * @see org.codehaus.staxmate.in.SMInputCursor#getParentCount()
	 */
	public int getParentCount() {
		return cursor.getParentCount();
	}
	
	/**
	 * DOCME
	 * 
	 * @return Information about the tracked element the parent cursor
	 *    had, if parent cursor existed and was tracking element
	 *    information.
	 * @see org.codehaus.staxmate.in.SMInputCursor#getParentTrackedElement()
	 */
	public SMElementInfo getParentTrackedElement() {
		return cursor.getParentTrackedElement();
	}
	
	/**
	 * Method that generates developer-readable description of
	 * the logical path of the event this cursor points to,
	 * assuming that <b>element tracking</b> is enabled.
	 * If it is, a path description will be constructed; if not,
	 * result will be "." ("unspecified current location").
	 *<p>
	 * Note: while results look similar to XPath expressions,
	 * they are not accurate (or even valid) XPath. 
	 * This is partly because of filtering, and partly because
	 * of differences between element/node index calculation.
	 * The idea is to make it easier to get reasonable idea
	 * of logical location, in addition to physical input location.
	 * </p>
	 * 
	 * @return path desc
	 * @see org.codehaus.staxmate.in.SMInputCursor#getPathDesc()
	 */
	public String getPathDesc() {
		return cursor.getPathDesc();
	}
	
	/**
	 * Method for accessing namespace prefix of the START_ELEMENT this
	 * cursor points to.
	 *
	 * @return Prefix of currently pointed-to START_ELEMENT,
	 *   if it has one; "" if none
	 * @throws XMLStreamException if cursor does not point to START_ELEMENT
	 * @see org.codehaus.staxmate.in.SMInputCursor#getPrefix()
	 */
	public String getPrefix() throws XMLStreamException {
		return cursor.getPrefix();
	}
	
	/**
	 * Returns a String representation of either the fully-qualified name
	 * (if the event has full name) or the local name (if event does not
	 * have full name but has local name); or if no name available, throws
	 * stream exception.
	 * 
	 * @return prefixed name
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getPrefixedName()
	 */
	public String getPrefixedName() throws XMLStreamException {
		return cursor.getPrefixedName();
	}
	
	/**
	 * DOCME
	 * 
	 * @return QName
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getQName()
	 */
	public QName getQName() throws XMLStreamException {
		return cursor.getQName();
	}
	
	/**
	 * Method to access Location that the underlying stream reader points
	 * to.
	 *
	 * @return Location of the event the underlying stream reader points
	 *   to (independent of whether this cursor points to that event)
	 * @see org.codehaus.staxmate.in.SMInputCursor#getStreamLocation()
	 */
	public Location getStreamLocation() {
		return cursor.getStreamLocation();
	}
	
	/**
	 * Method that can be used to get direct access to the underlying
	 * stream reader. Custom sub-classed versions (which can be constructed
	 * by overriding this classes factory methods) can choose to block
	 * such access, but the default implementation does allow access
	 * to it.
	 *<p>
	 * Note that this method should not be needed (or extensively used)
	 * for regular StaxMate usage, because direct access to the stream
	 * may cause cursor's understanding of stream reader state to be
	 * incompatible with its actual state.
	 * </p>
	 *
	 * @return Stream reader the cursor uses for getting XML events
	 * @see org.codehaus.staxmate.in.SMInputCursor#getStreamReader()
	 */
	public final XMLStreamReader2 getStreamReader() {
		return cursor.getStreamReader();
	}
	
	/**
	 * Method that can be used when this cursor points to a textual
	 * event; something for which {@link XMLStreamReader#getText} can
	 * be called. Note that it does not advance the cursor, or combine
	 * multiple textual events.
	 *
	 * @return Textual content of the current event that this cursor
	 *   points to, if any
	 *
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details); or if this cursor does
	 *   not currently point to an event.
	 * @see org.codehaus.staxmate.in.SMInputCursor#getText()
	 */
	public String getText() throws XMLStreamException {
		return cursor.getText();
	}
	
	/**
	 * DOCME
	 * 
	 * @return Information about last "tracked" element; element we have
	 *    last iterated over when tracking has been enabled.
	 * @see org.codehaus.staxmate.in.SMInputCursor#getTrackedElement()
	 */
	public SMElementInfo getTrackedElement() {
		return cursor.getTrackedElement();
	}
	
	/**
	 * DOCME for yamkazu
	 * 
	 * @param jQName Jiemamy QName
	 * @return if cursor has attr {@code ture}
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (cursor not valid or not pointing to START_ELEMENT),
	 *   or if invalid attribute
	 * @throws IllegalArgumentException if attribute was not found 
	 */
	public boolean hasAttr(JiemamyQName jQName) throws XMLStreamException {
		return -1 != cursor.findAttrIndex(jQName.getQName().getNamespaceURI(), jQName.getQName().getLocalPart());
	}
	
	/**
	 * Method for verifying whether current named event (one for which
	 * {@link #getLocalName} can be called)
	 * has the specified local name or not.
	 *
	 * @param expName name
	 * @return True if the local name associated with the event is
	 *   as expected
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#hasLocalName(java.lang.String)
	 */
	public boolean hasLocalName(String expName) throws XMLStreamException {
		return cursor.hasLocalName(expName);
	}
	
	/**
	 * Equivalent to calling {@link #hasName(String, String)} with
	 * namespace URI and local name contained in the argument QName
	 *
	 * @param jQName Name to compare name of current event (if any)
	 *   against.
	 * @return true if contains
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#hasName(javax.xml.namespace.QName)
	 */
	public boolean hasName(JiemamyQName jQName) throws XMLStreamException {
		return cursor.hasName(jQName.getQName());
	}
	
	/**
	 * Method for verifying whether current named event (one for which
	 * {@link #getLocalName} can be called) has the specified
	 * fully-qualified name or not.
	 * Both namespace URI and local name must match for the result
	 * to be true.
	 *
	 * @param expNsURI namespace URI
	 * @param expLN local name
	 * @return True if the fully-qualified name associated with the event is
	 *   as expected
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#hasName(java.lang.String, java.lang.String)
	 */
	public boolean hasName(String expNsURI, String expLN) throws XMLStreamException {
		return cursor.hasName(expNsURI, expLN);
	}
	
	/**
	 * DOCME
	 * 
	 * @param jQName Jiemamy QName
	 * @return true if current QName
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 */
	public boolean isQName(JiemamyQName jQName) throws XMLStreamException {
		return cursor.getQName().equals(jQName.getQName());
	}
	
	/**
	 * Method for determining whether this cursor iterates over root level of
	 *   the underlying stream reader
	 *
	 * @return True if this cursor iterates over root level of
	 *   the underlying stream reader
	 * @see org.codehaus.staxmate.in.SMInputCursor#isRootCursor()
	 */
	public final boolean isRootCursor() {
		return cursor.isRootCursor();
	}
	
	/**
	 * Method similar to {@link #collectDescendantText}, but will write
	 * the text to specified Writer instead of collecting it into a
	 * String.
	 *
	 * @param w Writer to use for outputting text found
	 * @param includeIgnorable Whether text for events of type SPACE should
	 *   be ignored in the results or not. If false, SPACE events will be
	 *   skipped; if true, white space will be included in results.
	 * @throws IOException if I/O error occured
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#processDescendantText(java.io.Writer, boolean)
	 */
	public void processDescendantText(Writer w, boolean includeIgnorable) throws IOException, XMLStreamException {
		cursor.processDescendantText(w, includeIgnorable);
	}
	
	/**
	 * Method that can be used to check whether this cursor is
	 * currently valid; that is, it is the cursor that points
	 * to the event underlying stream is at. Only one cursor
	 * at any given time is valid in this sense, although other
	 * cursors may be made valid by advancing them (and by process
	 * invalidating the cursor that was valid at that point).
	 * It is also possible that none of cursors is valid at
	 * some point: this is the case when formerly valid cursor
	 * reached end of its content (END_ELEMENT).
	 *
	 * @return True if the cursor is currently valid; false if not
	 * @see org.codehaus.staxmate.in.SMInputCursor#readerAccessible()
	 */
	public final boolean readerAccessible() {
		return cursor.readerAccessible();
	}
	
	/**
	 * Method for assigning per-cursor application-managed data,
	 * readable using {@link #getData}.
	 * 
	 * @param o object data
	 * @see org.codehaus.staxmate.in.SMInputCursor#setData(java.lang.Object)
	 */
	public void setData(Object o) {
		cursor.setData(o);
	}
	
	/**
	 * Set element info factory used for constructing
	 * {@link SMElementInfo} instances during traversal for this
	 * cursor, as well as all of its children.
	 * 
	 * @param f factory
	 * @see org.codehaus.staxmate.in.SMInputCursor#setElementInfoFactory(org.codehaus.staxmate.in.ElementInfoFactory)
	 */
	public final void setElementInfoFactory(ElementInfoFactory f) {
		cursor.setElementInfoFactory(f);
	}
	
	/**
	 * Changes tracking mode of this cursor to the new specified
	 * mode. Default mode for cursors is the one their parent uses;
	 * {@link Tracking#NONE} for root cursors with no parent.
	 *<p>
	 * See also {@link #getPathDesc} for information on how
	 * to display tracked path/element information.
	 * </p>
	 * 
	 * @param tracking Different tracking behaviors available for cursors.
	 * @see org.codehaus.staxmate.in.SMInputCursor#setElementTracking(org.codehaus.staxmate.in.SMInputCursor.Tracking)
	 */
	public final void setElementTracking(Tracking tracking) {
		cursor.setElementTracking(tracking);
	}
	
	/**
	 * Method for setting filter used for selecting which events
	 * are to be returned to the caller when {@link #getNext}
	 * is called.
	 * 
	 * @param f filter
	 * @see org.codehaus.staxmate.in.SMInputCursor#setFilter(org.codehaus.staxmate.in.SMFilter)
	 */
	public final void setFilter(SMFilter f) {
		cursor.setFilter(f);
	}
	
	/**
	 * Method for constructing and throwing stream exception with given
	 * message. Equivalent to throwing exception that
	 * {@link #constructStreamException} constructs and returns.
	 * 
	 * @param msg message
	 * @throws XMLStreamException if either the underlying parser has
	 *   problems (possibly including event type not being of textual
	 *   type, see Stax 1.0 specs for details)
	 * @see org.codehaus.staxmate.in.SMInputCursor#throwStreamException(java.lang.String)
	 */
	public void throwStreamException(String msg) throws XMLStreamException {
		cursor.throwStreamException(msg);
	}
	
	/**
	 * Overridden implementation will just display description of
	 * the event this cursor points to (or last pointed to, if not
	 * valid)
	 * 
	 * @return string expression
	 * @see org.codehaus.staxmate.in.SMInputCursor#toString()
	 */
	@Override
	public String toString() {
		return cursor.toString();
	}
}
