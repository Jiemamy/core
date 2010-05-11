/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/11
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
package org.jiemamy.model;

import org.junit.Test;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class EntityLifecycleTest {
	
	/**
	 * ACTIVEからはactivateできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityLifecycleException.class)
	public void test_active_to_activate() throws Exception {
		Entity entity = new MockEntity();
		entity.activate();
		entity.activate();
	}
	
	/**
	 * ACTIVEからはbindできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityLifecycleException.class)
	public void test_active_to_bind() throws Exception {
		Entity entity = new MockEntity();
		entity.activate();
		entity.bind();
	}
	
	/**
	 * ACTIVEからdeactivateできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_active_to_deactivate() throws Exception {
		Entity entity = new MockEntity();
		entity.activate();
		entity.deactivate();
	}
	
	/**
	 * ACTIVEからfreeできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_active_to_free() throws Exception {
		Entity entity = new MockEntity();
		entity.activate();
		entity.free();
	}
	
	/**
	 * BOUNDからactivateできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_bound_to_activate() throws Exception {
		Entity entity = new MockEntity();
		entity.bind();
		entity.activate();
	}
	
	/**
	 * BOUNDからはbindできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityLifecycleException.class)
	public void test_bound_to_bind() throws Exception {
		Entity entity = new MockEntity();
		entity.bind();
		entity.bind();
	}
	
	/**
	 * BOUNDからはdeactivateできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityLifecycleException.class)
	public void test_bound_to_deactivate() throws Exception {
		Entity entity = new MockEntity();
		entity.bind();
		entity.deactivate();
	}
	
	/**
	 * BOUNDからfreeできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_bound_to_free() throws Exception {
		Entity entity = new MockEntity();
		entity.bind();
		entity.free();
	}
	
	/**
	 * FREEからactivateできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_free_to_activate() throws Exception {
		Entity entity = new MockEntity();
		entity.activate();
	}
	
	/**
	 * FREEからbindできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_free_to_bind() throws Exception {
		Entity entity = new MockEntity();
		entity.bind();
	}
	
	/**
	 * FREEからはdeactivateできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityLifecycleException.class)
	public void test_free_to_deactivate() throws Exception {
		Entity entity = new MockEntity();
		entity.deactivate();
	}
	
	/**
	 * FREEからはfreeできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityLifecycleException.class)
	public void test_free_to_free() throws Exception {
		Entity entity = new MockEntity();
		entity.free();
	}
}
