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

import org.jiemamy.model.column.ColumnParameterKey;
import org.jiemamy.model.parameter.Converters;
import org.jiemamy.model.parameter.ParameterMap;
import org.jiemamy.utils.RandomUtil;

/**
 * {@link DefaultTypeVariant}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultTypeVariantTest {
	
	/**
	 * 適当な {@link DefaultTypeVariant} のインスタンスを作る。
	 * 
	 * @return {@link DefaultTypeVariant}
	 */
	public static DefaultTypeVariant random() {
		DataTypeCategory category = enume(DataTypeCategory.class);
		String typeName = str();
		ParameterMap params = new ParameterMap();
		
		// 適当にパラメータを追加する
		int integer = integer(5);
		for (int i = 0; i < integer; i++) {
			int p = RandomUtil.integer(2);
			if (p == 0) {
				params.put(new ColumnParameterKey<Boolean>(Converters.BOOLEAN, strNotEmpty()), bool());
			} else if (p == 1) {
				params.put(new ColumnParameterKey<Integer>(Converters.INTEGER, strNotEmpty()), integer(100));
			} else {
				params.put(new ColumnParameterKey<String>(Converters.STRING, strNotEmpty()), str());
			}
		}
		
		return new DefaultTypeVariant(new DefaultTypeReference(category, typeName), params);
	}
	

	private DefaultTypeVariant typeVariant;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ParameterMap params = new ParameterMap();
		params.put(TypeParameterKey.SIZE, 10);
		typeVariant = new DefaultTypeVariant(new DefaultTypeReference(DataTypeCategory.VARCHAR, "VARCHAR"), params);
	}
	
	/**
	 * 作ったインスタンスの挙動チェック。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_作ったインスタンスの挙動チェック() throws Exception {
		assertThat(typeVariant.getTypeReference().getCategory(), is(DataTypeCategory.VARCHAR));
		assertThat(typeVariant.getTypeReference().getTypeName(), is("VARCHAR"));
		assertThat(typeVariant.getParam(TypeParameterKey.SIZE), is(10));
		assertThat(typeVariant.getParam(TypeParameterKey.PRECISION), is(nullValue()));
	}
}
