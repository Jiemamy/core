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
package org.jiemamy.serializer.impl;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.events.StartElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationWorker;

/**
 * 全てのモデルをシリアライズ・デシリアライズできる「フリをする」ダミー実装。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DummyWorker extends SerializationWorker<Object> {
	
	private static Logger logger = LoggerFactory.getLogger(DummyWorker.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public DummyWorker(JiemamyContext context, SerializationDirector director) {
		super(Object.class, null, context, director);
	}
	
	@Override
	protected boolean canDeserialize(StartElement startElement) {
		return true;
	}
	
	@Override
	protected boolean canSerialize(Object model) {
		return true;
	}
	
	@Override
	protected Object doDeserialize0(StartElement startElement, XMLEventReader reader) {
		logger.error("DUMMY WORKER IS CALLED.");
		return null;
	}
	
	@Override
	protected void doSerialize0(Object model, XMLEventWriter writer) {
		logger.error("DUMMY WORKER IS CALLED.");
	}
}
