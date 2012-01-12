/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/12/08
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
package org.jiemamy.validator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

/**
 * {@link AbstractProblem}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class AbstractProblemTest {
	
	/**
	 * リソースファイルにアクセスできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_リソースファイルにアクセスできる() throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle(AbstractProblem.BUNDLE_NAME, Locale.JAPAN);
		assertThat(bundle.getString("T9999"), is("foo bar baz qux"));
	}
}
