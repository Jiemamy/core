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
package org.jiemamy.model.geometory;

import org.junit.Test;

import org.jiemamy.utils.RandomUtil;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmRectangleTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public static JmRectangle random() {
		int x = RandomUtil.integer(100);
		int y = RandomUtil.integer(100);
		return new JmRectangle(x, y);
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