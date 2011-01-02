/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/01
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
package org.jiemamy.serializer.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationWorker;

/**
 * {@link SerializationWorker}用のテストクラス骨格実装。
 * 
 * @param <T> テスト対象の{@link SerializationWorker}がシリアライズするモデルの型
 * @version $Id$
 * @author daisuke
 */
public abstract class SerializationWorkerTest<T> {
	
	private static Logger logger = LoggerFactory.getLogger(SerializationWorkerTest.class);
	
	private JiemamyContext context;
	
	private SerializationDirector director;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		context = new JiemamyContext();
		director = new SerializationDirector(context);
	}
	
	/**
	 * モデルを生成して、そのシリアライズ結果XMLを検証し、XMLをデシリアライズしたモデルを検証する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_モデルを生成して_そのシリアライズ結果XMLを検証し_XMLをデシリアライズしたモデルを検証する() throws Exception {
		T model = createModel();
		SerializationWorker<T> worker = createSerializationWorker(context, director);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
		XMLEventWriter writer = new IndentingXMLEventWriter(outFactory.createXMLEventWriter(out));
		
		worker.doSerialize(model, writer);
		
		writer.close();
		out.close();
		
		byte[] byteArray = out.toByteArray();
		String serialized = new String(byteArray, CharEncoding.UTF_8);
		logger.info(serialized);
		
		assertSerialized(serialized);
		
		XMLInputFactory inFactory = XMLInputFactory.newInstance();
		inFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
		ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
		XMLEventReader reader = inFactory.createXMLEventReader(in);
		
		T deserialized = null;
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) {
				deserialized = worker.doDeserialize(event.asStartElement(), reader);
			}
		}
		
		assertThat(deserialized, is(notNullValue()));
		assertDeserialized(deserialized);
		
		out = new ByteArrayOutputStream();
		writer = new IndentingXMLEventWriter(outFactory.createXMLEventWriter(out));
		
		worker.doSerialize(model, writer);
		
		writer.close();
		out.close();
		
		assertThat(out.toString(CharEncoding.UTF_8), is(serialized));
	}
	
	/**
	 * モデルをデシリアライズした結果を検証する。
	 * 
	 * @param deserialized デシリアライズした結果のモデル
	 * @throws AssertionError 検証の結果、異常である場合
	 */
	protected abstract void assertDeserialized(T deserialized);
	
	/**
	 * モデルをシリアライズした結果を検証する。
	 * 
	 * @param serialized シリアライズ結果のXML文字列
	 * @throws AssertionError 検証の結果、異常である場合
	 */
	protected abstract void assertSerialized(String serialized);
	
	/**
	 * テストに使用するモデルインスタンスを生成する。
	 * 
	 * @return モデルインスタンス
	 */
	protected abstract T createModel();
	
	/**
	 * テスト対象の {@link SerializationWorker} を生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 * @return {@link SerializationWorker}
	 */
	protected abstract SerializationWorker<T> createSerializationWorker(JiemamyContext context,
			SerializationDirector director);
}
