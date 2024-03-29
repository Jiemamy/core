/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
import static org.jiemamy.utils.RandomUtil.strNotEmpty;

import org.junit.Test;

import org.jiemamy.model.table.JmTable;

/**
 * {@link SimpleJmAroundScript}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmAroundScriptTest {
	
	/**
	 * 適当な {@link SimpleJmAroundScript} のインスタンスを作る。
	 * 
	 * @param table 対象となるテーブル
	 * @return {@link SimpleJmAroundScript}
	 */
	public static SimpleJmAroundScript random(JmTable table) {
		SimpleJmAroundScript model = new SimpleJmAroundScript();
		model.setCoreModelRef(table.toReference());
		// TODO engineもたまに指定する
		if (bool()) {
			model.setScript(Position.BEGIN, strNotEmpty());
		} else {
			model.setScript(Position.END, strNotEmpty());
		}
		return model;
	}
	
	/**
	 * 特にまだテストしたいことはないけどテストメソッドが1つもないとエラーが発生するので仕方なく置いてやってるメソッド。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
	}
}
