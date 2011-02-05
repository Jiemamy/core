/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/26
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
package org.jiemamy.model;

import org.junit.Test;

import org.jiemamy.model.geometory.JmColorTest;
import org.jiemamy.model.geometory.JmRectangleTest;
import org.jiemamy.utils.RandomUtil;

/**
 * {@link JmStickyNode}のテストクラス。
 * 
 * @version $Id$
 * @author yamkazu
 */
public class JmStickyNodeTest {
	
	/**
	 * 適当な {@link JmStickyNode} のインスタンスを作る。
	 * 
	 * @return {@link JmStickyNode}
	 */
	public static JmStickyNode random() {
		JmStickyNode model = new JmStickyNode();
		model.setBoundary(JmRectangleTest.random());
		model.setColor(JmColorTest.random());
		model.setContents(RandomUtil.str());
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
