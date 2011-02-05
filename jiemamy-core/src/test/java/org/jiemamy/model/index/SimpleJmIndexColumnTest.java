/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.model.index;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Test;

import org.jiemamy.model.column.SimpleJmColumn;
import org.jiemamy.model.index.JmIndexColumn.SortOrder;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link SimpleJmIndexColumn}のテスト。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmIndexColumnTest {
	
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
		SimpleJmColumn column1 = new SimpleJmColumn(UUIDUtil.valueOfOrRandom("a"));
		SimpleJmColumn column2 = new SimpleJmColumn(UUIDUtil.valueOfOrRandom("b"));
		SimpleJmIndexColumn model = new SimpleJmIndexColumn(column1.toReference());
		
		assertThat(model.getColumnRef(), is(notNullValue()));
		assertThat(model.getColumnRef().isReferenceOf(column1), is(true));
		assertThat(model.getColumnRef().isReferenceOf(column2), is(false));
		
		assertThat(model.getSortOrder(), is(nullValue()));
	}
	
	/**
	 * {@link SimpleJmIndexColumn#equals(Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_equals() throws Exception {
		SimpleJmColumn column1 = new SimpleJmColumn(UUIDUtil.valueOfOrRandom("a"));
		SimpleJmColumn column2 = new SimpleJmColumn(UUIDUtil.valueOfOrRandom("b"));
		SimpleJmIndexColumn icm1 = SimpleJmIndexColumn.of(column1);
		SimpleJmIndexColumn icm2 = SimpleJmIndexColumn.of(column2);
		SimpleJmIndexColumn icm1asc = SimpleJmIndexColumn.of(column1, SortOrder.ASC);
		SimpleJmIndexColumn icm1asc_ = SimpleJmIndexColumn.of(column1, SortOrder.ASC);
		SimpleJmIndexColumn icm2asc = SimpleJmIndexColumn.of(column2, SortOrder.ASC);
		SimpleJmIndexColumn icm1desc = SimpleJmIndexColumn.of(column1, SortOrder.DESC);
		SimpleJmIndexColumn icm2desc = SimpleJmIndexColumn.of(column2, SortOrder.DESC);
		
		assertThat(icm1, is(notNullValue()));
		assertThat(icm2, is(notNullValue()));
		assertThat(icm1asc, is(notNullValue()));
		assertThat(icm2asc, is(notNullValue()));
		assertThat(icm1desc, is(notNullValue()));
		assertThat(icm2desc, is(notNullValue()));
		
		assertThat(icm1.equals(icm1), is(true));
		assertThat(icm1.equals(icm2), is(false));
		assertThat(icm1.equals(icm1asc), is(false));
		assertThat(icm1.equals(icm2asc), is(false));
		assertThat(icm1.equals(icm1desc), is(false));
		assertThat(icm1.equals(icm2desc), is(false));
		
		assertThat(icm1asc.equals(icm1), is(false));
		assertThat(icm1asc.equals(icm2), is(false));
		assertThat(icm1asc.equals(icm1asc), is(true));
		assertThat(icm1asc.equals(icm1asc_), is(true));
		assertThat(icm1asc.equals(icm2asc), is(false));
		assertThat(icm1asc.equals(icm1desc), is(false));
		assertThat(icm1asc.equals(icm2desc), is(false));
		
		assertThat(icm1.equals(null), is(false));
		assertThat(icm1.equals(new Object()), is(false));
		
		assertThat(icm1asc.getSortOrder(), is(SortOrder.ASC));
		assertThat(icm1desc.getSortOrder(), is(SortOrder.DESC));
		
	}
}
