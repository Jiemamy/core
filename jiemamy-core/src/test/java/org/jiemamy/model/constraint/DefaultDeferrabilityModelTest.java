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
package org.jiemamy.model.constraint;

import static org.hamcrest.CoreMatchers.is;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import org.jiemamy.dddbase.ValueObject;
import org.jiemamy.model.constraint.DeferrabilityModel.InitiallyCheckTime;
import org.jiemamy.model.table.DefaultTableModel;

/**
 * {@link DefaultDeferrabilityModel}のテストクラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DefaultDeferrabilityModelTest {
	
	/**
	 * 適当な {@link DefaultTableModel} のインスタンスを返す。
	 * 
	 * @return {@link DefaultTableModel}
	 */
	public static DeferrabilityModel randomNullable() {
		DefaultDeferrabilityModel[] values = DefaultDeferrabilityModel.values();
		return bool() ? null : values[integer(values.length - 1)];
	}
	
	/**
	 * {@link DefaultDeferrabilityModel#equals(Object)}が、{@link ValueObject}として正しい実装になっていること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_equals() throws Exception {
		DefaultDeferrabilityModel d1 = DefaultDeferrabilityModel.DEFERRABLE;
		DefaultDeferrabilityModel d2 = DefaultDeferrabilityModel.DEFERRABLE_DEFERRED;
		DeferrabilityModel d3 = mock(DeferrabilityModel.class);
		when(d3.getInitiallyCheckTime()).thenReturn(InitiallyCheckTime.DEFERRED);
		when(d3.isDeferrable()).thenReturn(false);
		
		assertThat(d1.equals(d1), is(true));
		assertThat(d1.equals(d2), is(false));
		assertThat(d1.equals(d3), is(false));
		
		assertThat(d2.equals(d1), is(false));
		assertThat(d2.equals(d2), is(true));
		assertThat(d2.equals(d3), is(false));
		
		assertThat(d3.equals(d1), is(false));
		assertThat(d3.equals(d2), is(false));
		assertThat(d3.equals(d3), is(true));
	}
}
