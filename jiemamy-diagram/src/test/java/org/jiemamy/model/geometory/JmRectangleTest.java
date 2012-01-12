/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/13
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
package org.jiemamy.model.geometory;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.jiemamy.utils.RandomUtil;

/**
 * {@link JmRectangle}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmRectangleTest {
	
	/**
	 * 適当な {@link JmRectangle} のインスタンスを作る。
	 * 
	 * @return {@link JmRectangle}
	 */
	public static JmRectangle random() {
		int x = RandomUtil.integer(100);
		int y = RandomUtil.integer(100);
		return new JmRectangle(x, y);
	}
	
	/**
	 * インスタンス生成テスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_instance() throws Exception {
		JmRectangle rectangle = new JmRectangle(100, 101);
		assertThat(rectangle.x, is(100));
		assertThat(rectangle.y, is(101));
		assertThat(rectangle.width, is(JmRectangle.DEFAULT));
		assertThat(rectangle.height, is(JmRectangle.DEFAULT));
	}
	
	/**
	 * インスタンス生成テスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_instance() throws Exception {
		JmRectangle rectangle = new JmRectangle(100, 101, 50, 51);
		assertThat(rectangle.x, is(100));
		assertThat(rectangle.y, is(101));
		assertThat(rectangle.width, is(50));
		assertThat(rectangle.height, is(51));
	}
	
	/**
	 * 幅に負数を与えると例外。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void test03_幅に負数を与えると例外() throws Exception {
		new JmRectangle(100, 101, -50, 51);
	}
	
	/**
	 * 高さに負数を与えると例外。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void test04_高さに負数を与えると例外() throws Exception {
		new JmRectangle(100, 101, 50, -51);
	}
	
	/**
	 * 幅と高さは10以下を指定すると10になる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_幅と高さは10以下を指定すると10になる() throws Exception {
		JmRectangle rectangle = new JmRectangle(100, 101, 3, 8);
		assertThat(rectangle.x, is(100));
		assertThat(rectangle.y, is(101));
		assertThat(rectangle.width, is(10));
		assertThat(rectangle.height, is(10));
	}
}
