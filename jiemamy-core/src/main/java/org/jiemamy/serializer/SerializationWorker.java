/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/31
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
package org.jiemamy.serializer;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.bea.xml.stream.EventFactory;
import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.JiemamyNamespace;
import org.jiemamy.xml.JiemamyQName;

/**
 * シリアライズ処理実装クラスの骨格実装。
 * 
 * @param <T> シリアライズを担当できる型
 * @version $Id$
 * @author daisuke
 */
public abstract class SerializationWorker<T> {
	
	/** 共用の{@link EventFactory}インスタンス */
	protected static final XMLEventFactory EV_FACTORY = EventFactory.newInstance();
	

	protected static Iterator<Attribute> createIdAndClassAttributes(UUID id, Object obj) {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(CoreQName.CLASS.getQName(), obj.getClass().getName()));
		result.add(EV_FACTORY.createAttribute(CoreQName.ID.getQName(), id.toString()));
		return result.iterator();
	}
	
	protected static final Iterator<Attribute> emptyAttributes() {
		return null;
	}
	
	protected static final Iterator<Namespace> emptyNamespaces() {
		return null;
	}
	
	protected static String getAttributeAsString(StartElement element, JiemamyQName qName) {
		return element.getAttributeByName(qName.getQName()).getValue();
	}
	
	protected static boolean isStartElementOf(XMLEvent event, JiemamyQName qName) {
		return event.isStartElement() && event.asStartElement().getName().equals(qName.getQName());
	}
	
	protected static void writeNameLogNameDesc(XMLEventWriter writer, String name, String logicalName,
			String description) throws XMLStreamException {
		writer.add(EV_FACTORY.createStartElement(CoreQName.NAME.getQName(), emptyAttributes(), emptyNamespaces()));
		writeCharacters(writer, name);
		writer.add(EV_FACTORY.createEndElement(CoreQName.NAME.getQName(), emptyNamespaces()));
		
		if (logicalName != null) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.LOGICAL_NAME.getQName(), emptyAttributes(),
					emptyNamespaces()));
			writer.add(EV_FACTORY.createCharacters(logicalName));
			writer.add(EV_FACTORY.createEndElement(CoreQName.LOGICAL_NAME.getQName(), emptyNamespaces()));
		}
		
		if (description != null) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.DESCRIPTION.getQName(), emptyAttributes(),
					emptyNamespaces()));
			writer.add(EV_FACTORY.createCharacters(description));
			writer.add(EV_FACTORY.createEndElement(CoreQName.DESCRIPTION.getQName(), emptyNamespaces()));
		}
	}
	
	private static void writeCharacters(XMLEventWriter writer, String chars) throws XMLStreamException {
		if (StringUtils.isEmpty(chars) == false) {
			writer.add(EV_FACTORY.createCharacters(chars));
		}
	}
	

	private final Class<T> clazz;
	
	private final JiemamyQName headElementQName;
	
	private final SerializationDirector director;
	
	private SerializationWorker<?> next;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param clazz シリアライズを担当できる型
	 * @param headElementQName 
	 * @param director 親となるディレクタ
	 */
	public SerializationWorker(Class<T> clazz, JiemamyQName headElementQName, SerializationDirector director) {
		this.clazz = clazz;
		this.headElementQName = headElementQName;
		this.director = director;
	}
	
	/**
	 * デシリアライズを実行する。
	 * 
	 * @param startElement デシリアライズの切っ掛けとなった要素開始イベント
	 * @param reader 続きのイベントを読むための {@link XMLEventReader}
	 * @return デシリアライズした結果モデル
	 * @throws XMLStreamException StAXストリーム異常が発生した場合
	 * @throws SerializationException デシリアライズできる {@link SerializationWorker} が見つからなかった場合
	 */
	public T doDeserialize(StartElement startElement, XMLEventReader reader) throws XMLStreamException,
			SerializationException {
		if (canDeserialize(startElement)) {
			return doDeserialize0(startElement, reader);
		} else if (next != null) {
			return clazz.cast(next.doDeserialize(startElement, reader));
		} else {
			throw new SerializationException("worker not found.");
		}
	}
	
	/**
	 * シリアライズを実行する。
	 * 
	 * <p>自分自身が{@code model}をシリアライズできる場合は {@link #doSerialize0(Object, XMLEventWriter)} をコールして
	 * シリアライズを行う。シリアライズできない場合は、次のチェーン要素に処理を委譲する。</p>
	 * 
	 * @param model シリアライズ対象モデル
	 * @param writer XML出力先
	 * @throws XMLStreamException StAXストリーム異常が発生した場合
	 * @throws SerializationException シリアライズできる {@link SerializationWorker} が見つからなかった場合
	 */
	public final void doSerialize(Object model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException {
		if (canSerialize(model)) {
			doSerialize0(clazz.cast(model), writer);
		} else if (next != null) {
			next.doSerialize(model, writer);
		} else {
			throw new SerializationException("worker not found.");
		}
	}
	
	/**
	 * 責務チェーンの次にあたる {@link SerializationWorker} を設定する。
	 * 
	 * @param next {@link SerializationWorker}
	 * @return {@code next}
	 */
	public SerializationWorker<?> setNext(SerializationWorker<?> next) {
		this.next = next;
		return next;
	}
	
	/**
	 * 指定した要素開始イベントをデシリアライズできるかどうか調べる。
	 * 
	 * @param startElement 要素開始イベント
	 * @return デシリアライズできる場合は{@code true}、そうでない場合は{@code false}
	 */
	protected boolean canDeserialize(StartElement startElement) {
		if (headElementQName == null) {
			return true;
		}
		
		if (startElement.getName().equals(headElementQName.getQName()) == false) {
			return false;
		}
		Attribute classAttribute = startElement.getAttributeByName(CoreQName.CLASS.getQName());
		if (classAttribute == null) {
			return true;
		}
		return clazz.getName().equals(classAttribute.getValue());
	}
	
	/**
	 * 指定したモデルをシリアライズできるかどうか調べる。
	 * 
	 * @param model 対象モデル
	 * @return シリアライズできる場合は{@code true}、そうでない場合は{@code false}
	 */
	protected boolean canSerialize(Object model) {
		return model.getClass() == clazz;
	}
	
	/**
	 * デシリアライズ処理の実装メソッド。
	 * 
	 * @param startElement デシリアライズの切っ掛けとなった要素開始イベント
	 * @param reader 続きのイベントを読むための {@link XMLEventReader}
	 * @return デシリアライズした結果モデル
	 * @throws XMLStreamException StAXストリーム異常が発生した場合
	 * @throws SerializationException デシリアライズできる {@link SerializationWorker} が見つからなかった場合
	 */
	protected abstract T doDeserialize0(StartElement startElement, XMLEventReader reader) throws XMLStreamException,
			SerializationException;
	
	/**
	 * シリアライズ処理の実装メソッド。
	 * 
	 * @param model シリアライズ対象。
	 * @param writer XML出力先
	 * @throws XMLStreamException StAXストリーム異常が発生した場合
	 * @throws SerializationException 子要素においてシリアライズできる {@link SerializationWorker} が見つからなかった場合
	 */
	protected abstract void doSerialize0(T model, XMLEventWriter writer) throws XMLStreamException,
			SerializationException;
	
	protected SerializationDirector getDirector() {
		return director;
	}
	
	protected JiemamyQName getHeadElementQName() {
		return headElementQName;
	}
	
	protected Iterator<Namespace> getNamespaces() {
		List<Namespace> result = Lists.newArrayList();
		for (JiemamyNamespace jns : director.getContext().getNamespaces()) {
			Namespace ns = EV_FACTORY.createNamespace(jns.getPrefix(), jns.getNamespaceURI().toString());
			if (StringUtils.isEmpty(ns.getNamespaceURI()) == false) {
				result.add(ns);
			}
		}
		return result.iterator();
	}
	
	protected Iterator<Namespace> getNamespacesIfNotAvailable() {
		if (director.isNamespaceAvailable()) {
			return null;
		}
		return getNamespaces();
	}
	
	protected boolean isNamespaceAvailable() {
		return director.isNamespaceAvailable();
	}
	
	protected void setNamespaceAvailable(boolean namespaceAvailable) {
		director.setNamespaceAvailable(namespaceAvailable);
	}
}
