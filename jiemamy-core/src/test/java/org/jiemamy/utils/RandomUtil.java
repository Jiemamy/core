/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/06
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
package org.jiemamy.utils;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

/**
 * 乱数を使って適当なデータを生成するためのテスト用ユーティリティクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class RandomUtil {
	
	static final Random R = new Random();
	

	public static <T extends Enum<T>>T enume(Class<T> clazz) {
		T[] enumConstants = clazz.getEnumConstants();
		return enumConstants[R.nextInt(enumConstants.length)];
	}
	
	public static <T extends Enum<T>>T enumeNullable(Class<T> clazz) {
		T[] enumConstants = clazz.getEnumConstants();
		return R.nextBoolean() ? null : enumConstants[R.nextInt(enumConstants.length)];
	}
	
	public static int integer(int limit) {
		return R.nextInt() % limit;
	}
	
	public static String str() {
		return R.nextBoolean() ? "" : RandomStringUtils.randomAlphanumeric(5);
	}
	
	public static String strNullable() {
		int x = R.nextInt() % 3;
		// FORMAT-OFF
		switch (x) {
			case 0:	return "";
			case 1:	return RandomStringUtils.randomAlphanumeric(5);
			default: return null;
		}
		// FORAMT-ON
	}
	
	private RandomUtil(){}
}
