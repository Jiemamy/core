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
package org.jiemamy.model.constraint;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.jiemamy.utils.RandomUtil.enumeNullable;
import static org.jiemamy.utils.RandomUtil.str;
import static org.jiemamy.utils.RandomUtil.strNotEmpty;
import static org.jiemamy.utils.RandomUtil.strNullable;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * {@link JmCheckConstraint}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmCheckConstraintTest {
	
	/**
	 * 適当な {@link JmCheckConstraint} のインスタンスを作る。
	 * 
	 * @return {@link JmCheckConstraint}
	 */
	public static JmCheckConstraint random() {
		JmCheckConstraint model = new JmCheckConstraint();
		model.setName(str());
		model.setLogicalName(strNullable());
		model.setDescription(strNullable());
		model.setDeferrability(enumeNullable(JmDeferrability.class));
		model.setExpression(strNotEmpty());
		return model;
	}
	
	/**
	 * {@link JmCheckConstraint#of(String)}及び
	 * {@link JmCheckConstraint#of(String, String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_of() throws Exception {
		JmCheckConstraint model1 = JmCheckConstraint.of("exp1");
		assertThat(model1, is(notNullValue()));
		assertThat(model1.getExpression(), is("exp1"));
		assertThat(model1.getName(), is(nullValue()));
		assertThat(model1.getLogicalName(), is(nullValue()));
		assertThat(model1.getDescription(), is(nullValue()));
		assertThat(model1.getDeferrability(), is(nullValue()));
		
		JmCheckConstraint model2 = JmCheckConstraint.of("exp2", "name");
		assertThat(model2, is(notNullValue()));
		assertThat(model2.getExpression(), is("exp2"));
		assertThat(model2.getName(), is("name"));
		assertThat(model2.getLogicalName(), is(nullValue()));
		assertThat(model2.getDescription(), is(nullValue()));
		assertThat(model2.getDeferrability(), is(nullValue()));
	}
}
