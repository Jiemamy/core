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

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.model.geometory.JmColorTest;
import org.jiemamy.model.geometory.JmRectangleTest;
import org.jiemamy.utils.RandomUtil;

/**
 * TODO for yamkazu
 * 
 * @version $Id$
 * @author yamkazu
 */
public class StickyNodeModelTest {
	
	/**
	 * 適当な {@link StickyNodeModel} のインスタンスを作る。
	 * 
	 * @return {@link StickyNodeModel}
	 */
	public static StickyNodeModel random() {
		StickyNodeModel model = new StickyNodeModel(UUID.randomUUID());
		model.setBoundary(JmRectangleTest.random());
		model.setColor(JmColorTest.random());
		model.setContents(RandomUtil.str());
		return model;
	}
	
	/**
	 * TODO for yamkazu
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		
	}
}