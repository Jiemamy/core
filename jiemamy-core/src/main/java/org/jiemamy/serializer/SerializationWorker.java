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
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;

import com.bea.xml.stream.EventFactory;
import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.xml.CoreQName;

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
	
	private final JiemamyContext context;
	
	private final Class<T> clazz;
	
	private final SerializationDirector director;
	
	private SerializationWorker<?> next;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param clazz シリアライズを担当できる型
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public SerializationWorker(Class<T> clazz, JiemamyContext context, SerializationDirector director) {
		this.context = context;
		this.clazz = clazz;
		this.director = director;
	}
	
	/**
	 * シリアライズを実行する。
	 * 
	 * <p>自分自身が{@code model}をシリアライズできる場合は {@link #doWork0(Object, XMLEventWriter)} をコールして
	 * シリアライズを行う。シリアライズできない場合は、次のチェーン要素に処理を委譲する。</p>
	 * 
	 * @param model シリアライズ対象モデル
	 * @param writer XML出力先
	 * @throws XMLStreamException StAXストリーム異常が発生した場合
	 * @throws SerializationException シリアライズできる {@link SerializationWorker} が見つからなかった場合
	 */
	public void doWork(Object model, XMLEventWriter writer) throws XMLStreamException, SerializationException {
		if (isSerializable(model)) {
			doWork0(clazz.cast(model), writer);
		} else if (next != null) {
			next.doWork(model, writer);
		} else {
			throw new SerializationException("worker not found.");
		}
	}
	
	/**
	 * 責務チェーンの次にあたる {@link SerializationWorker} を設定する。
	 * 
	 * @param next {@link SerializationWorker}
	 */
	public void setNext(SerializationWorker<?> next) {
		this.next = next;
	}
	
	protected Iterator<Attribute> createIdAndClassAttributes(UUID id, Object obj) {
		List<Attribute> result = Lists.newArrayList();
		result.add(EV_FACTORY.createAttribute(CoreQName.CLASS.getQName(), obj.getClass().getName()));
		result.add(EV_FACTORY.createAttribute(CoreQName.ID.getQName(), id.toString()));
		return result.iterator();
	}
	
	/**
	 * シリアライズ処理の実装メソッド。
	 * 
	 * @param model シリアライズ対象。
	 * @param writer XML出力先
	 * @throws XMLStreamException StAXストリーム異常が発生した場合
	 * @throws SerializationException 子要素においてシリアライズできる {@link SerializationWorker} が見つからなかった場合
	 */
	protected abstract void doWork0(T model, XMLEventWriter writer) throws XMLStreamException, SerializationException;
	
	protected final Iterator<Attribute> emptyAttributes() {
		return null;
	}
	
	protected final Iterator<Namespace> emptyNamespaces() {
		return null;
	}
	
	protected JiemamyContext getContext() {
		return context;
	}
	
	protected SerializationDirector getDirector() {
		return director;
	}
	
	/**
	 * 指定したモデルをシリアライズできるかどうか調べる。
	 * 
	 * @param model 対象モデル
	 * @return シリアライズできる場合は{@code true}、そうでない場合は{@code false}
	 */
	protected boolean isSerializable(Object model) {
		return model.getClass() == clazz;
	}
	
	protected void writeNameLogNameDesc(XMLEventWriter writer, String name, String logicalName, String description)
			throws XMLStreamException {
		writer.add(EV_FACTORY.createStartElement(CoreQName.NAME.getQName(), emptyAttributes(), emptyNamespaces()));
		writer.add(EV_FACTORY.createCharacters(name));
		writer.add(EV_FACTORY.createEndElement(CoreQName.NAME.getQName(), emptyNamespaces()));
		
		if (StringUtils.isEmpty(logicalName) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.LOGICAL_NAME.getQName(), emptyAttributes(),
					emptyNamespaces()));
			writer.add(EV_FACTORY.createCharacters(logicalName));
			writer.add(EV_FACTORY.createEndElement(CoreQName.LOGICAL_NAME.getQName(), emptyNamespaces()));
		}
		
		if (StringUtils.isEmpty(description) == false) {
			writer.add(EV_FACTORY.createStartElement(CoreQName.DESCRIPTION.getQName(), emptyAttributes(),
					emptyNamespaces()));
			writer.add(EV_FACTORY.createCharacters(description));
			writer.add(EV_FACTORY.createEndElement(CoreQName.DESCRIPTION.getQName(), emptyNamespaces()));
		}
	}
}
