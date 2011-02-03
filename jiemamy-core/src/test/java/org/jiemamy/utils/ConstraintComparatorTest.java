/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.constraint.DefaultValueConstraintModel;
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.NotNullConstraintModel;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.UniqueKeyConstraintModel;

/**
 * {@link ConstraintComparator}のテストクラス。
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
	 * TODO for yamkazu
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_インターフェースの順でソート() throws Exception {
		PrimaryKeyConstraintModel pk = mock(PrimaryKeyConstraintModel.class);
		UniqueKeyConstraintModel uk = mock(UniqueKeyConstraintModel.class);
		ForeignKeyConstraintModel fk = mock(ForeignKeyConstraintModel.class);
		NotNullConstraintModel nn = mock(NotNullConstraintModel.class);
		CheckConstraintModel c = mock(CheckConstraintModel.class);
		ConstraintModel other = mock(ConstraintModel.class);
		
		ArrayList<ConstraintModel> models = Lists.newArrayList(nn, uk, pk, other, c, fk); // 適当な順番で追加
		Collections.sort(models, comparator);
		
		assertThat(models.get(0), is(instanceOf(PrimaryKeyConstraintModel.class)));
		assertThat(models.get(1), is(instanceOf(UniqueKeyConstraintModel.class)));
		assertThat(models.get(2), is(instanceOf(ForeignKeyConstraintModel.class)));
		assertThat(models.get(3), is(instanceOf(NotNullConstraintModel.class)));
		assertThat(models.get(4), is(instanceOf(CheckConstraintModel.class)));
		assertThat(models.get(5), is(instanceOf(ConstraintModel.class)));
	}
	
	/**
	 * TODO for yamkazu
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_インターフェースで一緒の場合は実装クラス名でソート() throws Exception {
		ConstraintModel c1 = new DefaultAaaConstraintModel();
		ConstraintModel c2 = new DefaultBbbConstraintModel();
		ConstraintModel c3 = new DefaultCccConstraintModel();
		ConstraintModel c4 = new DefaultDddConstraintModel();
		
		ArrayList<ConstraintModel> models = Lists.newArrayList(c2, c4, c3, c1); // 適当な順番で追加
		Collections.sort(models, comparator);
		
		assertThat(models.get(0), is(instanceOf(DefaultAaaConstraintModel.class)));
		assertThat(models.get(1), is(instanceOf(DefaultBbbConstraintModel.class)));
		assertThat(models.get(2), is(instanceOf(DefaultCccConstraintModel.class)));
		assertThat(models.get(3), is(instanceOf(DefaultDddConstraintModel.class)));
	}
	
	/**
	 * TODO for yamkazu
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_実装クラス名も一緒の場合はIDでソート() throws Exception {
		ConstraintModel c1 = new DefaultAaaConstraintModel();
		ConstraintModel c2 = new DefaultAaaConstraintModel();
		ConstraintModel c3 = new DefaultAaaConstraintModel();
		ConstraintModel c4 = new DefaultAaaConstraintModel();
		
		ArrayList<ConstraintModel> models = Lists.newArrayList(c2, c4, c3, c1); // 適当な順番で追加
		Collections.sort(models, comparator);
		
		assertThat(models.get(0).getId().compareTo(models.get(1).getId()), is(-1));
		assertThat(models.get(1).getId().compareTo(models.get(2).getId()), is(-1));
		assertThat(models.get(2).getId().compareTo(models.get(3).getId()), is(-1));
	}
	

	static class DefaultAaaConstraintModel extends DefaultValueConstraintModel {
		
		/**
		 * インスタンスを生成する。
		 */
		public DefaultAaaConstraintModel() {
			super(UUIDUtil.valueOfOrRandom(RandomUtil.strNotEmpty()));
		}
		
	}
	
	static class DefaultBbbConstraintModel extends DefaultValueConstraintModel {
		
		/**
		 * インスタンスを生成する。
		 */
		public DefaultBbbConstraintModel() {
			super(UUIDUtil.valueOfOrRandom(RandomUtil.strNotEmpty()));
		}
		
	}
	
	static class DefaultCccConstraintModel extends DefaultValueConstraintModel {
		
		/**
		 * インスタンスを生成する。
		 */
		public DefaultCccConstraintModel() {
			super(UUIDUtil.valueOfOrRandom(RandomUtil.strNotEmpty()));
		}
		
	}
	
	static class DefaultDddConstraintModel extends DefaultValueConstraintModel {
		
		/**
		 * インスタンスを生成する。
		 */
		public DefaultDddConstraintModel() {
			super(UUIDUtil.valueOfOrRandom(RandomUtil.strNotEmpty()));
		}
		
	}
	
}
