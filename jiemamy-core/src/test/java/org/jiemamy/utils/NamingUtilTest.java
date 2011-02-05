/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/04
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.table.SimpleJmTable;

/**
 * {@link NamingUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class NamingUtilTest {
	
	private JiemamyContext context;
	
	private SimpleJmTable t1;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		context = new JiemamyContext();
		t1 = new SimpleJmTable();
		t1.setName("TABLE_1");
		context.store(t1);
	}
	
	/**
	 * autoNameで適切な自動命名が行われる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_autoNameで適切な自動命名が行われる() throws Exception {
		SimpleJmTable t2 = new SimpleJmTable();
		NamingUtil.autoName(t2, context);
		
		assertThat(t2.getName(), is(not("TABLE_1")));
		context.store(t2);
		
		SimpleJmTable t3 = new SimpleJmTable();
		NamingUtil.autoName(t3, context);
		
		assertThat(t3.getName(), is(not("TABLE_1")));
		assertThat(t3.getName(), is(not(t2.getName())));
	}
	
}
