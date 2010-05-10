/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/02
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
package org.jiemamy.model.dbo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;

/**
 * TODO for daisuke
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DefaultTableModelTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_create() throws Exception {
		DefaultTableModel t = new DefaultTableModel();
		t.setName("T_DEPT");
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_equals() throws Exception {
		UUID randomUUID = UUID.randomUUID();
		DefaultTableModel tm1 = new DefaultTableModel();
		DefaultTableModel tm2 = new DefaultTableModel();
		DefaultTableModel tm3 = new DefaultTableModel();
		
		tm1.setId(randomUUID);
		tm2.setId(randomUUID);
		
		tm1.setName("FOO");
		tm2.setName("BAR");
		
		assertThat(tm1.equals(tm1), is(true));
		assertThat(tm2.equals(tm2), is(true));
		assertThat(tm3.equals(tm3), is(true));
		
		assertThat(tm1.equals(tm2), is(true));
		assertThat(tm1.equals(tm3), is(false));
		assertThat(tm2.equals(tm1), is(true));
		assertThat(tm2.equals(tm3), is(false));
		assertThat(tm3.equals(tm1), is(false));
		assertThat(tm3.equals(tm2), is(false));
		
	}
	
}
