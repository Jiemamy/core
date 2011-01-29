/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/04/04
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
package org.jiemamy.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link TestModelBuilder}で生成したモデルを使用してテストを行う為の基底クラス。
 * 
 * @author daisuke
 */
public abstract class TestModelsTestBase {
	
	private static Logger logger = LoggerFactory.getLogger(TestModelsTestBase.class);
	

	/**
	 * テストを行う。
	 * 
	 * @param entry テストモデルのエントリ
	 * @throws Exception 例外が発生した場合
	 */
	public abstract void doTest(TestModelBuilders entry) throws Exception;
	
	/**
	 * 全ての {@link TestModelBuilder}でテストを行うテストメソッド。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testAllEntries() throws Exception {
		for (TestModelBuilders entry : TestModelBuilders.values()) {
			logger.info("==== Test Model: " + entry.name());
			doTest(entry);
		}
	}
}
