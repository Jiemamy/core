/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
package org.jiemamy;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.Serializable;

import javax.imageio.spi.ServiceRegistry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.MockDialect;

/**
 * {@link SimpleServiceLocator}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleServiceLocatorTest {
	
	private SimpleServiceLocator serviceLocator;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		serviceLocator = new SimpleServiceLocator();
	}
	
	/**
	 * コンストラクタのリフレクション呼び出しによるサービス取得。
	 * 
	 * <p> {@link MockDialect} は {@link SimpleServiceLocator} をロードした
	 * classloaderから辿れるのでインスタンスを取得できる。</p>
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@Ignore("MockDialectはサービスレジストリに登録されている。現在SimpleServiceLocatorはリフレクションによるサービス取得をサポートしない")
	public void test01コンストラクタのリフレクション呼び出しによるサービス取得() throws Exception {
		Dialect dialect = serviceLocator.getService(Dialect.class, "org.jiemamy.dialect.MockDialect");
		assertThat(dialect, is(notNullValue()));
		assertThat(dialect, is(instanceOf(MockDialect.class)));
	}
	
	/**
	 * {@link ServiceRegistry}経由によるサービス取得。
	 * 
	 * <p> {@link SampleServiceImpl1} と {@link SampleServiceImpl2} は
	 * {@code META-INF/services/org.jiemamy.SampleService}で定義してある。</p>
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_ServiceRegistry経由によるサービス取得() throws Exception {
		SampleService service1 = serviceLocator.getService(SampleService.class, "org.jiemamy.SampleServiceImpl1");
		assertThat(service1, is(notNullValue()));
		assertThat(service1, is(instanceOf(SampleServiceImpl1.class)));
		
		SampleService service2 = serviceLocator.getService(SampleService.class, "org.jiemamy.SampleServiceImpl2");
		assertThat(service2, is(notNullValue()));
		assertThat(service2, is(instanceOf(SampleServiceImpl2.class)));
	}
	
	/**
	 * nullはClassNotFoundException。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_nullはClassNotFoundException() throws Exception {
		try {
			serviceLocator.getService(Serializable.class, null);
			fail();
		} catch (ClassNotFoundException e) {
			// success
		}
	}
	
	/**
	 * nullはServiceNotFoundException。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_ClassNotFoundException() throws Exception {
		try {
			serviceLocator.getService(Serializable.class, "hoge");
			fail();
		} catch (ClassNotFoundException e) {
			// success
		}
	}
}
