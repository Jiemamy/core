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
package org.jiemamy.model.datatype;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.enume;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.jiemamy.utils.RandomUtil.str;
import static org.jiemamy.utils.RandomUtil.strNotEmpty;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.parameter.Converters;
import org.jiemamy.utils.RandomUtil;

/**
 * {@link SimpleDataType}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleDataTypeTest {
	
	/**
	 * 適当な {@link SimpleDataType} のインスタンスを作る。
	 * 
	 * @return {@link SimpleDataType}
	 */
	public static SimpleDataType random() {
		SimpleDataType typeVariant = new SimpleDataType(new SimpleRawTypeDescriptor(enume(RawTypeCategory.class)));
		
		// 適当にパラメータを追加する
		int integer = integer(5);
		for (int i = 0; i < integer; i++) {
			int p = RandomUtil.integer(2);
			if (p == 0) {
				typeVariant.putParam(new TypeParameterKey<Boolean>(Converters.BOOLEAN, strNotEmpty()), bool());
			} else if (p == 1) {
				typeVariant.putParam(new TypeParameterKey<Integer>(Converters.INTEGER, strNotEmpty()), integer(100));
			} else {
				typeVariant.putParam(new TypeParameterKey<String>(Converters.STRING, strNotEmpty()), str());
			}
		}
		
		return typeVariant;
	}
	

	private SimpleDataType typeVariant;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		typeVariant = new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.VARCHAR, "VARCHAR"));
		typeVariant.putParam(TypeParameterKey.SIZE, 10);
	}
	
	/**
	 * 作ったインスタンスの挙動チェック。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_作ったインスタンスの挙動チェック() throws Exception {
		assertThat(typeVariant.getRawTypeDescriptor().getCategory(), is(RawTypeCategory.VARCHAR));
		assertThat(typeVariant.getRawTypeDescriptor().getTypeName(), is("VARCHAR"));
		assertThat(typeVariant.getParam(TypeParameterKey.SIZE), is(10));
		assertThat(typeVariant.getParam(TypeParameterKey.PRECISION), is(nullValue()));
	}
}
