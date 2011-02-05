/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.serializer.stax2;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全てのモデルをシリアライズ・デシリアライズできる「フリをする」ダミー実装。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DummyHandler extends StaxHandler<Object> {
	
	private static Logger logger = LoggerFactory.getLogger(DummyHandler.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DummyHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public Object handleDeserialization(DeserializationContext dctx) {
		Validate.notNull(dctx);
		logger.debug("dummy handler invoked.");
		return null;
	}
	
	@Override
	public void handleSerialization(Object model, SerializationContext sctx) {
		Validate.notNull(model);
		Validate.notNull(sctx);
		logger.debug("dummy handler invoked.");
	}
}
