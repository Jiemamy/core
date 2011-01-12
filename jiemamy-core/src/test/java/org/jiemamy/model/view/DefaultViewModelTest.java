/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/12
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
package org.jiemamy.model.view;

import static org.hamcrest.CoreMatchers.is;
import static org.jiemamy.utils.RandomUtil.str;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;

/**
 * {@link DefaultViewModel}のテストクラス。
 * 
 * @version $Id$
 * @author yamkazu
 */
public class DefaultViewModelTest {
	
	/**
	 * 適当な {@link DefaultViewModel} のインスタンスを作る。
	 * 
	 * @return {@link DefaultViewModel}
	 */
	public static DefaultViewModel random() {
		DefaultViewModel model = new DefaultViewModel(UUID.randomUUID());
		
		model.setDefinition("SELECT * FROM " + str());
		model.setDescription(str());
		model.setLogicalName(str());
		model.setName(str());
		
		return model;
	}
	
	/**
	 * オブジェクト生成テスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_create() throws Exception {
		try {
			@SuppressWarnings("unused")
			DefaultViewModel unused = new DefaultViewModel(null);
		} catch (IllegalArgumentException e) {
			// success
		}
		
		UUID id = UUID.randomUUID();
		DefaultViewModel model = new DefaultViewModel(id);
		model.setDescription("HOGE");
		model.setName("FOO");
		model.setLogicalName("BAR");
		model.setDescription("I am View");
		model.setDefinition("SELECT * FROM BAR");
		
		assertThat(model.getId(), is(id));
		assertThat(model.getName(), is("FOO"));
		assertThat(model.getLogicalName(), is("BAR"));
		assertThat(model.getDescription(), is("I am View"));
		assertThat(model.getDefinition(), is("SELECT * FROM BAR"));
	}
	
}
