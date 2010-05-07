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
package org.jiemamy.model.attribute.constraint;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * TODO for daisuke
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DefaultColumnCheckConstraintModelTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		DefaultColumnCheckConstraintModel ccc1 = new DefaultColumnCheckConstraintModel("expression");
		DefaultColumnCheckConstraintModel ccc2 = new DefaultColumnCheckConstraintModel("expression");
		
		assertThat(ccc1.equals(ccc2), is(true));
		
		DefaultColumnCheckConstraintModel ccc3 = ccc1.changeNameTo("HOGE");
		
		assertThat(ccc1.equals(ccc3), is(false));
		
		assertThat(ccc1.changeExpressionTo("ex2").equals(ccc2.changeExpressionTo("ex2")), is(true));
	}
}
