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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.InternalKeyReference;
import org.jiemamy.Repository;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.attribute.DefalutColumnModel;

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
	public void test01_create() throws Exception {
		DefaultTableModel t = new DefaultTableModel();
		t.setName("T_DEPT");
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_equals() throws Exception {
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		DefaultTableModel live1 = new DefaultTableModel();
		DefaultTableModel live2 = new DefaultTableModel();
		DefaultTableModel live3 = new DefaultTableModel();
		DefaultTableModel dead = new DefaultTableModel();
		
		live1.setId(uuid1, InternalKeyReference.KEY);
		live2.setId(uuid1, InternalKeyReference.KEY);
		live3.setId(uuid2, InternalKeyReference.KEY);
		
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
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_lifecycle() throws Exception {
		DefaultTableModel live = new DefaultTableModel();
		DefaultTableModel dead = new DefaultTableModel();
		
		live.setId(UUID.randomUUID(), InternalKeyReference.KEY);
		
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
	public void test04_column_lifecycle1() throws Exception {
		DefalutColumnModel col1 = new DefalutColumnModel();
		DefaultTableModel table = new DefaultTableModel();
		table.addColumn(col1);
		
		Repository repository = new Repository();
		repository.add(table);
		
		assertThat(table.getId(), is(notNullValue()));
		assertThat(col1.getId(), is(notNullValue()));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_column_lifecycle2() throws Exception {
		DefaultTableModel table = new DefaultTableModel();
		
		Repository repository = new Repository();
		repository.add(table);
		
		DefalutColumnModel col2 = new DefalutColumnModel();
		table.addColumn(col2);
		
		assertThat(table.getId(), is(notNullValue()));
		assertThat(col2.getId(), is(notNullValue()));
	}
}
