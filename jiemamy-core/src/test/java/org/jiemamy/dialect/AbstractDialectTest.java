/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/21
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
package org.jiemamy.dialect;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultTypeReference;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractDialectTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testabs01() throws Exception {
		Dialect dialect = getInstance();
		for (DataTypeCategory category : DataTypeCategory.values()) {
			dialect.getTypeParameterSpecs(new DefaultTypeReference(category));
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testabs02() throws Exception {
		Dialect dialect = getInstance();
		assertThat(dialect.getAllTypeReferences().size(), is(greaterThan(0)));
		assertThat(dialect.getAllTypeReferences(), not(hasItem(DefaultTypeReference.UNKNOWN)));
		assertThat(dialect.getConnectionUriTemplate(), is(notNullValue()));
		assertThat(dialect.getName(), is(notNullValue()));
		assertThat(dialect.getName(), is(not(dialect.getClass().getName())));
		assertThat(dialect.getValidator(), is(notNullValue()));
	}
	
	protected abstract Dialect getInstance();
}
