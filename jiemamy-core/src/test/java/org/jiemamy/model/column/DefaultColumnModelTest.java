/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy.model.column;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.jiemamy.utils.RandomUtil.str;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultTypeVariant;
import org.jiemamy.model.datatype.DefaultTypeVariantTest;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.model.parameter.BooleanConverter;
import org.jiemamy.model.parameter.IntegerConverter;
import org.jiemamy.model.parameter.StringConverter;
import org.jiemamy.utils.RandomUtil;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link DefaultColumnModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultColumnModelTest {
	
	/**
	 * 適当な {@link DefaultColumnModel} のインスタンスを作る。
	 * 
	 * @return {@link DefaultColumnModel}
	 */
	public static DefaultColumnModel random() {
		DefaultColumnModel model = new DefaultColumnModel(UUID.randomUUID());
		model.setName(str());
		model.setLogicalName(str());
		model.setDescription(str());
		model.setDefaultValue(str());
		model.setDataType(DefaultTypeVariantTest.random());
		
		// 適当にパラメータを追加する
		int integer = integer(5);
		for (int i = 0; i < integer; i++) {
			int p = RandomUtil.integer(2);
			if (p == 0) {
				model.putParam(new ColumnParameterKey<Boolean>(new BooleanConverter(), str()), bool());
			} else if (p == 1) {
				model.putParam(new ColumnParameterKey<Integer>(new IntegerConverter(), str()), integer(100));
			} else {
				model.putParam(new ColumnParameterKey<String>(new StringConverter(), str()), RandomUtil.str());
			}
		}
		return model;
	}
	

	private DefaultColumnModel column;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		column = new DefaultColumnModel(UUIDUtil.valueOfOrRandom("a"));
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		UUIDUtil.clear();
	}
	
	/**
	 * 作ったインスタンスの挙動チェック。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_作ったインスタンスの挙動チェック() throws Exception {
		TypeVariant type = DefaultTypeVariant.of(DataTypeCategory.INTEGER);
		
		assertThat(column.getName(), is(nullValue()));
		assertThat(column.getLogicalName(), is(nullValue()));
		assertThat(column.getDataType(), is(nullValue()));
		assertThat(column.getDefaultValue(), is(nullValue()));
		assertThat(column.getDescription(), is(nullValue()));
		assertThat(column.getId(), is(UUIDUtil.valueOfOrRandom("a")));
		assertThat(column.getSubEntities().size(), is(0));
		
		column.setName("name");
		column.setLogicalName("logicalName");
		column.setDataType(type);
		column.setDefaultValue("defaultValue");
		column.setDescription("description");
		
		assertThat(column.getName(), is("name"));
		assertThat(column.getLogicalName(), is("logicalName"));
		assertThat(column.getDataType(), is(type));
		assertThat(column.getDefaultValue(), is("defaultValue"));
		assertThat(column.getDescription(), is("description"));
	}
	
	/**
	 * {@link DefaultColumnModel#toReference()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_toReference() throws Exception {
		EntityRef<? extends ColumnModel> ref = column.toReference();
		assertThat(ref, is(notNullValue()));
		assertThat(ref.getReferentId(), is(UUIDUtil.valueOfOrRandom("a")));
	}
	
	/**
	 * {@link DefaultColumnModel#toString()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_toString() throws Exception {
		assertThat(column.toString(), is("Column[null]"));
		column.setName("name1");
		assertThat(column.toString(), is("Column[name1]"));
	}
	
	/**
	 * {@link DefaultColumnModel#clone()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_clone() throws Exception {
		column.setName("name2");
		DefaultColumnModel clone = column.clone();
		
		// cloneはcolumnとは別のインスタンスだが、同じ値を持つ
		assertThat(clone, is(not(sameInstance(column))));
		assertThat(clone, is(equalTo(column)));
		assertThat(clone.getName(), is("name2"));
		
		column.setName("name3");
		
		// cloneはcolumnの変更に影響されない
		assertThat(column.getName(), is("name3"));
		assertThat(clone.getName(), is("name2"));
	}
}
