/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/05/09
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

import static org.junit.Assert.fail;

import java.lang.ref.WeakReference;

import org.junit.Ignore;
import org.junit.Test;

/**
 * {@link JiemamyAssert}のテストクラス。
 * 
 * <p>cf. http://d.hatena.ne.jp/ashigeru/20080506/1210092877</p>
 * 
 * @author daisuke
 * TODO 下のIgnoreについてバグチケット作成
 */
public class JiemamyAssertTest {
	
	/**
	 * Test method for {@link JiemamyAssert#assertGc(java.lang.ref.WeakReference)}.
	 */
	@Test
	@Ignore("http://bamboo.jiemamy.org/browse/CORE-DEF-292")
	public void testAssertGc() {
		Object obj = new String("Hello, world!");
		WeakReference<Object> ref = new WeakReference<Object>(obj);
		
		// keeps only weak references
		obj = null;
		
		JiemamyAssert.assertGc(ref);
	}
	
	/**
	 * Test method for {@link JiemamyAssert#assertGc(java.lang.ref.WeakReference)}.
	 */
	@Test
	@Ignore("http://bamboo.jiemamy.org/browse/CORE-DEF-292")
	public void testAssertGcLeak() {
		Object obj = new String("Hello, world!");
		WeakReference<Object> ref = new WeakReference<Object>(obj);
		
		// keeps strong references
		// obj = null;
		
		try {
			JiemamyAssert.assertGc(ref);
			fail();
		} catch (AssertionError e) {
			// success
		}
	}
	
}
