/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/12
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

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.column.DefaultColumnModel;
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.constraint.DefaultCheckConstraintModel;
import org.jiemamy.model.constraint.DefaultNotNullConstraintModel;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultUniqueKeyConstraintModel;
import org.jiemamy.model.constraint.NotNullConstraintModel;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.UniqueKeyConstraintModel;
import org.jiemamy.utils.ConstraintComparator;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class ConstraintComparatorTest {
	
	private ConstraintComparator comparator;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		comparator = new ConstraintComparator();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_() throws Exception {
		PrimaryKeyConstraintModel pk1 = mock(PrimaryKeyConstraintModel.class);
		PrimaryKeyConstraintModel pk2 = mock(PrimaryKeyConstraintModel.class);
		CheckConstraintModel ch1 = mock(CheckConstraintModel.class);
		CheckConstraintModel ch2 = mock(CheckConstraintModel.class);
		ConstraintModel uk = mock(ConstraintModel.class);
		
		assertThat(comparator.compare(pk1, pk1), is(0));
		assertThat(comparator.compare(pk1, pk2), is(0));
		assertThat(comparator.compare(pk2, pk1), is(0));
		assertThat(comparator.compare(pk2, pk2), is(0));
		assertThat(comparator.compare(pk1, ch1), is(lessThan(0)));
		assertThat(comparator.compare(pk1, ch2), is(lessThan(0)));
		assertThat(comparator.compare(pk1, uk), is(lessThan(0)));
		assertThat(comparator.compare(ch1, uk), is(lessThan(0)));
		assertThat(comparator.compare(null, pk1), is(lessThan(0)));
		assertThat(comparator.compare(pk1, null), is(greaterThan(0)));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		TreeSet<ConstraintModel> set = new TreeSet<ConstraintModel>(comparator);
		DefaultColumnModel c1 = new DefaultColumnModel(UUID.randomUUID());
		DefaultColumnModel c2 = new DefaultColumnModel(UUID.randomUUID());
		
		assertThat(set.size(), is(0));
		set.add(DefaultNotNullConstraintModel.of(c1));
		assertThat(set.size(), is(1));
		set.add(DefaultCheckConstraintModel.of("a > 0"));
		assertThat(set.size(), is(2));
		set.add(DefaultPrimaryKeyConstraintModel.of(c2));
		assertThat(set.size(), is(3));
		set.add(DefaultNotNullConstraintModel.of(c1)); // x
		assertThat(set.size(), is(3));
		set.add(DefaultNotNullConstraintModel.of(c2));
		assertThat(set.size(), is(4));
		set.add(DefaultCheckConstraintModel.of("a > 0")); // x
		assertThat(set.size(), is(4));
		set.add(DefaultCheckConstraintModel.of("b > 0"));
		assertThat(set.size(), is(5));
		set.add(DefaultPrimaryKeyConstraintModel.of(c2)); // x
		assertThat(set.size(), is(5));
		set.add(DefaultPrimaryKeyConstraintModel.of(c1)); // x
		assertThat(set.size(), is(5));
		set.add(DefaultUniqueKeyConstraintModel.of(c1));
		assertThat(set.size(), is(6));
		set.add(DefaultUniqueKeyConstraintModel.of(c1, c2));
		assertThat(set.size(), is(7));
		set.add(DefaultUniqueKeyConstraintModel.of(c2, c1)); // x
		assertThat(set.size(), is(7));
		
		Iterator<ConstraintModel> iterator = set.iterator();
		assertThat(iterator.next(), is(instanceOf(PrimaryKeyConstraintModel.class)));
		assertThat(iterator.next(), is(instanceOf(UniqueKeyConstraintModel.class))); // c1
		assertThat(iterator.next(), is(instanceOf(UniqueKeyConstraintModel.class))); // c1, c2
		assertThat(iterator.next(), is(instanceOf(NotNullConstraintModel.class))); // c1
		assertThat(iterator.next(), is(instanceOf(NotNullConstraintModel.class))); // c2
		assertThat(iterator.next(), is(instanceOf(CheckConstraintModel.class))); // a
		assertThat(iterator.next(), is(instanceOf(CheckConstraintModel.class))); // b
		try {
			iterator.next();
			fail();
		} catch (NoSuchElementException e) {
			// success
		}
	}
}
