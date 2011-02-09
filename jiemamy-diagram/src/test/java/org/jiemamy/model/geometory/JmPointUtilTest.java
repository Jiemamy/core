/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/12/01
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * {@link JmPointUtil}のテストクラス。
 * 
 * @author daisuke
 */
public class JmPointUtilTest {
	
	/**
	 * deltaテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_deltaテスト() throws Exception {
		assertThat(JmPointUtil.delta(new JmPoint(1, 2), new JmPoint(3, 4)), equalTo(new JmPoint(-2, -2)));
		assertThat(JmPointUtil.delta(new JmPoint(5, 4), new JmPoint(3, 4)), equalTo(new JmPoint(2, 0)));
	}
	
	/**
	 * shiftテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_shiftテスト() throws Exception {
		JmPoint target = new JmPoint(0, 0);
		target = JmPointUtil.shiftNegative(target, new JmPoint(2, 2));
		assertThat(target, equalTo(new JmPoint(-2, -2)));
		target = JmPointUtil.shiftNegative(target, new JmPoint(10, 4));
		assertThat(target, equalTo(new JmPoint(-12, -6)));
		target = JmPointUtil.shiftNegative(target, 1, 3);
		assertThat(target, equalTo(new JmPoint(-13, -9)));
		
		target = JmPointUtil.shiftPositive(target, new JmPoint(2, 2));
		assertThat(target, equalTo(new JmPoint(-11, -7)));
		target = JmPointUtil.shiftPositive(target, new JmPoint(10, 4));
		assertThat(target, equalTo(new JmPoint(-1, -3)));
		target = JmPointUtil.shiftPositive(target, 1, 3);
		assertThat(target, equalTo(new JmPoint(0, 0)));
		
	}
}
