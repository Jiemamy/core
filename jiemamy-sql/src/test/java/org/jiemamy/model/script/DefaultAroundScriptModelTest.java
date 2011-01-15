/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/13
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
package org.jiemamy.model.script;

import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.enume;
import static org.jiemamy.utils.RandomUtil.strNotEmpty;

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.model.table.TableModel;

/**
 * {@link DefaultAroundScriptModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultAroundScriptModelTest {
	
	/**
	 * 適当な {@link DefaultAroundScriptModel} のインスタンスを作る。
	 * 
	 * @param table 対象となるテーブル
	 * @return {@link DefaultAroundScriptModel}
	 */
	public static DefaultAroundScriptModel random(TableModel table) {
		DefaultAroundScriptModel model = new DefaultAroundScriptModel(UUID.randomUUID());
		model.setCoreModelRef(table.toReference());
		if (bool()) {
			model.setScript(Position.BEGIN, strNotEmpty());
		}
		if (bool()) {
			model.setScript(Position.END, strNotEmpty());
		}
		
		if (bool()) {
			model.setScriptEngineClassName(enume(Position.class), strNotEmpty());
		}
		
		return model;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		
	}
}
