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
package org.jiemamy;

import static org.jiemamy.utils.RandomUtil.integer;

import org.junit.Test;

import org.jiemamy.model.JmDiagramTest;

/**
 * {@link JiemamyContext}のテスト：Diagram版。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContextDiagramTest {
	
	/**
	 * 適当な {@link JiemamyContext} のインスタンスを作る。
	 * 
	 * @return {@link JiemamyContext}
	 */
	public static JiemamyContext random() {
		JiemamyContext context = JiemamyContextTest.random(DiagramFacet.PROVIDER);
		DiagramFacet facet = context.getFacet(DiagramFacet.class);
		
		// tableの生成
		int count = integer(5) + 1;
		for (int i = 0; i < count; i++) {
			facet.store(JmDiagramTest.random(context.getTables()));
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
