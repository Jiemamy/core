/*
 * Copyright 2010 Jiemamy Project and the others.
 * Created on 2010/12/07
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
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link DiagramFacet}のテストクラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DiagramFacetTest {
	
	private JiemamyContext context;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		context = new JiemamyContext(DiagramFacet.PROVIDER);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		DiagramFacet diagram = context.getFacet(DiagramFacet.class);
		assertThat(diagram, is(notNullValue()));
	}
	
}
