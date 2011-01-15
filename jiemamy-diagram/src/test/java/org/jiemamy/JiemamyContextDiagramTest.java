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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.jiemamy.model.DefaultDiagramModelTest;

/**
 * TODO for daisuke
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
		
		// tablemodelの生成
		int count = integer(5) + 1;
		for (int i = 0; i < count; i++) {
			facet.store(DefaultDiagramModelTest.random(context.getTables()));
		}
		
		return context;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_() throws Exception {
		JiemamyContext context = new JiemamyContext(DiagramFacet.PROVIDER);
		assertThat(context.hasFacet(DiagramFacet.class), is(true));
		assertThat(context.getFacet(DiagramFacet.class), is(notNullValue()));
	}
}
