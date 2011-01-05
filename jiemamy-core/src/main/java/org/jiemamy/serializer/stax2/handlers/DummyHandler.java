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
package org.jiemamy.serializer.stax2.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;

/**
 * 全てのモデルをシリアライズ・デシリアライズできる「フリをする」ダミー実装。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DummyHandler extends SerializationHandler<Object> {
	
	private static Logger logger = LoggerFactory.getLogger(DummyHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 */
	public DummyHandler(SerializationDirector director) {
		super(director);
	}
	
	@Override
	public Object handle(DeserializationContext ctx) throws SerializationException {
		logger.error("DUMMY WORKER IS CALLED.");
		return null;
	}
	
	@Override
	public void handle(Object model, SerializationContext sctx) throws SerializationException {
		logger.error("DUMMY WORKER IS CALLED.");
	}
}
