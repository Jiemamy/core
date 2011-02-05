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
package org.jiemamy;

import static org.jiemamy.utils.RandomUtil.integer;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import org.jiemamy.model.script.SimpleJmAroundScriptTest;
import org.jiemamy.model.table.JmTable;

/**
 * {@link JiemamyContext}のテスト：Sql版。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContextSqlTest {
	
	/**
	 * 適当な {@link JiemamyContext} のインスタンスを作る。
	 * 
	 * @return {@link JiemamyContext}
	 */
	public static JiemamyContext random() {
		JiemamyContext context = JiemamyContextTest.random(SqlFacet.PROVIDER);
		
		SqlFacet facet = context.getFacet(SqlFacet.class);
		
		// tablemodelの生成
		Set<JmTable> set = context.getTables();
		if (set.size() > 1) {
			int c = integer(set.size() - 1) + 1;
			Iterator<JmTable> itr = set.iterator();
			for (int i = 0; i < c; i++) {
				facet.store(SimpleJmAroundScriptTest.random(itr.next()));
			}
		}
		
		return context;
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
