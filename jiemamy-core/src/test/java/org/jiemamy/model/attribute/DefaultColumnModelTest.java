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
package org.jiemamy.model.attribute;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultTypeVariant;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.utils.UUIDUtil;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultColumnModelTest {
	
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
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01() throws Exception {
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
	 * TODO for daisuke
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
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03() throws Exception {
		UUID id = UUIDUtil.valueOfOrRandom("a");
		assertThat(column.toString(), is("DefaultColumnModel[name=<null>,logicalName=<null>,description=<null>,"
				+ "dataType=<null>,defaultValue=<null>,id=" + id.toString() + "]"));
		
		column.setName("name1");
		
		assertThat(column.toString(), is("DefaultColumnModel[name=name1,logicalName=<null>,description=<null>,"
				+ "dataType=<null>,defaultValue=<null>,id=" + id.toString() + "]"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04() throws Exception {
		column.setName("name2");
		DefaultColumnModel clone = column.clone();
		assertThat(clone, is(not(sameInstance(column))));
		assertThat(clone, is(equalTo(column)));
		assertThat(clone.getName(), is("name2"));
		
		column.setName("name3");
		
		assertThat(column.getName(), is("name3"));
		assertThat(clone.getName(), is("name2"));
	}
}
