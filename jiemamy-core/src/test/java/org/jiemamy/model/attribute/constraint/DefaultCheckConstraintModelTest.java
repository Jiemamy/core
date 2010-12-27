/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * {@link DefaultCheckConstraintModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultCheckConstraintModelTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_of() throws Exception {
		DefaultCheckConstraintModel model1 = DefaultCheckConstraintModel.of("exp1");
		assertThat(model1, is(notNullValue()));
		assertThat(model1.getExpression(), is("exp1"));
		assertThat(model1.getName(), is(nullValue()));
		assertThat(model1.getLogicalName(), is(nullValue()));
		assertThat(model1.getDescription(), is(nullValue()));
		assertThat(model1.getDeferrability(), is(nullValue()));
		assertThat(model1, hasToString(is("Constraint null[exp1]")));
		
		DefaultCheckConstraintModel model2 = DefaultCheckConstraintModel.of("exp2", "name");
		assertThat(model2, is(notNullValue()));
		assertThat(model2.getExpression(), is("exp2"));
		assertThat(model2.getName(), is("name"));
		assertThat(model2.getLogicalName(), is(nullValue()));
		assertThat(model2.getDescription(), is(nullValue()));
		assertThat(model2.getDeferrability(), is(nullValue()));
		assertThat(model2, hasToString(is("Constraint name[exp2]")));
	}
}