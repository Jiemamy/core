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

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Jiemamyプロジェクト用の、特殊JUnitアサーション集。
 * 
 * <p>cf. http://d.hatena.ne.jp/ashigeru/20080506/1210092877</p>
 * 
 * @author daisuke
 */
public class JiemamyAssert {
	
	private static final int ALLOCATE_SIZE = 65536;
	

	/**
	 * GCを発生させ、指定した弱参照が解放されることを表明する。
	 * 
	 * @param ref 弱参照
	 */
	public static void assertGc(WeakReference<?> ref) {
		SoftReference<LinkedList<int[]>> memoryEater1 = new SoftReference<LinkedList<int[]>>(new LinkedList<int[]>());
		SoftReference<LinkedList<int[]>> memoryEater2 = new SoftReference<LinkedList<int[]>>(new LinkedList<int[]>());
		
		while (true) {
			System.gc();
			if (consumeMemory(memoryEater1)) {
				break;
			}
			if (consumeMemory(memoryEater2)) {
				break;
			}
		}
		
		if (ref.get() != null) {
			throw new AssertionError();
		}
	}
	
	private static boolean consumeMemory(SoftReference<LinkedList<int[]>> eater) {
		LinkedList<int[]> list = eater.get();
		if (list == null) {
			return true;
		}
		list.add(new int[ALLOCATE_SIZE]);
		return false;
	}
	
	private JiemamyAssert() {
	}
}
