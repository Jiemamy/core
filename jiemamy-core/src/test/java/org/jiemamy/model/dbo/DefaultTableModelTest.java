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
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.model.EntityLifecycleException;

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
	public void test_lifecycle() throws Exception {
		DefaultTableModel live = new DefaultTableModel();
		DefaultTableModel dead = new DefaultTableModel();
		
		live.setId(UUID.randomUUID());
		
		assertThat(live.getReference().getReferenceId(), is(live.getId()));
		
		try {
			dead.getReference();
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_equals() throws Exception {
		UUID randomUUID = UUID.randomUUID();
		DefaultTableModel live1 = new DefaultTableModel();
		DefaultTableModel live2 = new DefaultTableModel();
		DefaultTableModel live3 = new DefaultTableModel();
		DefaultTableModel dead = new DefaultTableModel();
		
		live1.setId(randomUUID);
		live2.setId(randomUUID);
		live3.setId(UUID.randomUUID());
		
		live1.setName("FOO");
		live2.setName("BAR");
		live2.setName("BAZ");
		live2.setName("QUX");
		
		assertThat(live1.equals(live1), is(true));
		assertThat(live1.equals(live2), is(true));
		assertThat(live1.equals(live3), is(false));
		assertThat(live1.equals(dead), is(false));
		
		assertThat(live2.equals(live1), is(true));
		assertThat(live2.equals(live2), is(true));
		assertThat(live2.equals(live3), is(false));
		assertThat(live2.equals(dead), is(false));
		
		assertThat(live3.equals(live1), is(false));
		assertThat(live3.equals(live2), is(false));
		assertThat(live3.equals(live3), is(true));
		assertThat(live3.equals(dead), is(false));
		
		assertThat(dead.equals(live1), is(false));
		assertThat(dead.equals(live2), is(false));
		assertThat(dead.equals(live3), is(false));
		assertThat(dead.equals(dead), is(false));
	}
}
