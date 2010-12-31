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

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyFacet;
import org.jiemamy.serializer.impl.DefaultTableModelSerializationWorker;
import org.jiemamy.serializer.impl.DummyWorker;
import org.jiemamy.serializer.impl.JiemamyContextSerializationWorker;

/**
 * 各モデルのシリアライズ時に起点となり、{@link SerializationWorker}にシリアライズ指示を出すクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SerializationDirector {
	
	private SerializationWorker<?> head;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 */
	public SerializationDirector(JiemamyContext context) {
		Validate.notNull(context);
		add(new DummyWorker(context, this)); // FIXME これがケツ持ちをしてる
		add(new JiemamyContextSerializationWorker(context, this));
		add(new DefaultTableModelSerializationWorker(context, this));
		
		for (JiemamyFacet jiemamyFacet : context.getFacets()) {
			for (SerializationWorker<?> worker : jiemamyFacet.getSerializationWorkers(this)) {
				add(worker);
			}
		}
		
		// check invariant
		assert head != null;
	}
	
	/**
	 * {@link SerializationWorker}に対してシリアライズを指示し、XMLを出力する。
	 * 
	 * @param model シリアライズ対象
	 * @param writer 出力先ライター
	 * @throws XMLStreamException StAXストリーム異常が発生した場合 
	 * @throws SerializationException シリアライズに失敗した場合
	 */
	public void direct(Object model, XMLEventWriter writer) throws XMLStreamException, SerializationException {
		head.doWork(model, writer);
	}
	
	/**
	 * チェーンに{@link SerializationWorker}を追加する。
	 * 
	 * @param worker 追加する{@link SerializationWorker}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected void add(SerializationWorker<?> worker) {
		Validate.notNull(worker);
		if (head != null) {
			worker.setNext(head);
		}
		head = worker;
	}
	
}
