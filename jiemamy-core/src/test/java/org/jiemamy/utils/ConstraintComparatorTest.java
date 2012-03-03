/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
package org.jiemamy.utils;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.constraint.JmCheckConstraint;
import org.jiemamy.model.constraint.JmConstraint;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.constraint.JmUniqueKeyConstraint;
import org.jiemamy.model.constraint.JmValueConstraint;

/**
 * {@link ConstraintComparator}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ConstraintComparatorTest {
	
	private static Logger logger = LoggerFactory.getLogger(ConstraintComparatorTest.class);
	
	
	/**
	 * インターフェースの順でソートする。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_インターフェースの順でソートする() throws Exception {
		JmPrimaryKeyConstraint pk = new JmPrimaryKeyConstraint();
		JmUniqueKeyConstraint uk = new JmUniqueKeyConstraint();
		JmForeignKeyConstraint fk = new JmForeignKeyConstraint();
		JmCheckConstraint c = new JmCheckConstraint();
		
		ArrayList<JmConstraint> models = Lists.newArrayList(uk, pk, c, fk); // 適当な順番で追加
		Collections.sort(models, ConstraintComparator.INSTANCE);
		
		logger.info("order = {}", models);
		assertThat(models.get(0), is(instanceOf(JmPrimaryKeyConstraint.class)));
		assertThat(models.get(1), is(instanceOf(JmUniqueKeyConstraint.class)));
		assertThat(models.get(2), is(instanceOf(JmForeignKeyConstraint.class)));
		assertThat(models.get(3), is(instanceOf(JmCheckConstraint.class)));
	}
	
	/**
	 * インターフェースで一緒の場合は実装クラス名でソートする。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_インターフェースで一緒の場合は実装クラス名でソートする() throws Exception {
		JmConstraint c1 = new SampleAaaConstraint();
		JmConstraint c2 = new SampleBbbConstraint();
		JmConstraint c3 = new SampleCccConstraint();
		JmConstraint c4 = new SampleDddConstraint();
		
		ArrayList<JmConstraint> models = Lists.newArrayList(c2, c4, c3, c1); // 適当な順番で追加
		Collections.sort(models, ConstraintComparator.INSTANCE);
		
		assertThat(models.get(0), is(instanceOf(SampleAaaConstraint.class)));
		assertThat(models.get(1), is(instanceOf(SampleBbbConstraint.class)));
		assertThat(models.get(2), is(instanceOf(SampleCccConstraint.class)));
		assertThat(models.get(3), is(instanceOf(SampleDddConstraint.class)));
	}
	
	/**
	 * 実装クラス名も一緒の場合はIDでソートする。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_実装クラス名も一緒の場合はIDでソートする() throws Exception {
		JmConstraint c1 = new SampleAaaConstraint();
		JmConstraint c2 = new SampleAaaConstraint();
		JmConstraint c3 = new SampleAaaConstraint();
		JmConstraint c4 = new SampleAaaConstraint();
		
		ArrayList<JmConstraint> constraints = Lists.newArrayList(c2, c4, c3, c1); // 適当な順番で追加
		Collections.sort(constraints, ConstraintComparator.INSTANCE);
		
		assertThat(constraints.get(0).getId().compareTo(constraints.get(1).getId()), is(-1));
		assertThat(constraints.get(1).getId().compareTo(constraints.get(2).getId()), is(-1));
		assertThat(constraints.get(2).getId().compareTo(constraints.get(3).getId()), is(-1));
	}
	
	
	static class SampleAaaConstraint extends JmValueConstraint {
		
		/**
		 * インスタンスを生成する。
		 */
		public SampleAaaConstraint() {
			super(UUID.randomUUID());
		}
		
	}
	
	static class SampleBbbConstraint extends JmValueConstraint {
		
		/**
		 * インスタンスを生成する。
		 */
		public SampleBbbConstraint() {
			super(UUID.randomUUID());
		}
		
	}
	
	static class SampleCccConstraint extends JmValueConstraint {
		
		/**
		 * インスタンスを生成する。
		 */
		public SampleCccConstraint() {
			super(UUID.randomUUID());
		}
		
	}
	
	static class SampleDddConstraint extends JmValueConstraint {
		
		/**
		 * インスタンスを生成する。
		 */
		public SampleDddConstraint() {
			super(UUID.randomUUID());
		}
		
	}
	
}
