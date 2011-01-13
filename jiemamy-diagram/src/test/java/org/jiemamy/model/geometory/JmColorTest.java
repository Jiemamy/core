/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/04
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.jiemamy.utils.RandomUtil;

/**
 * {@link JmColor}のテストクラス。
 * 
 * @author daisuke
 */
public class JmColorTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public static JmColor random() {
		int r = RandomUtil.integer(255);
		int g = RandomUtil.integer(255);
		int b = RandomUtil.integer(255);
		return new JmColor(r, g, b);
	}
	
	/**
	 * {@link #toString()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_toString() throws Exception {
		assertThat(new JmColor(0, 0, 0).toString(), is("#000000"));
		assertThat(new JmColor(0, 1, 2).toString(), is("#000102"));
		assertThat(new JmColor(10, 11, 12).toString(), is("#0a0b0c"));
		assertThat(new JmColor(0x10, 0x11, 0x12).toString(), is("#101112"));
		assertThat(new JmColor(255, 255, 255).toString(), is("#ffffff"));
		assertThat(new JmColor(255, 255, 255).toString(), is("#ffffff"));
	}
	
	/**
	 * {@link JmColor#parse(String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_parse() throws Exception {
		assertThat(JmColor.parse("#000000"), is(new JmColor(0, 0, 0)));
		assertThat(JmColor.parse("#000102"), is(new JmColor(0, 1, 2)));
		assertThat(JmColor.parse("#0a0b0c"), is(new JmColor(10, 11, 12)));
		assertThat(JmColor.parse("#101112"), is(new JmColor(0x10, 0x11, 0x12)));
		assertThat(JmColor.parse("#ffffff"), is(new JmColor(255, 255, 255)));
	}
}
