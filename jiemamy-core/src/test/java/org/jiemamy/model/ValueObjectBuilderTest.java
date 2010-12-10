/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/19
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
package org.jiemamy.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Iterator;

import org.junit.Test;

import org.jiemamy.ValueObject;
import org.jiemamy.model.ValueObjectBuilder.BuilderConfigurator;

/**
 * {@link ValueObjectBuilder}のテストクラス。
 * 
 * @version $Id$
 * @author Keisuke.K
 */
public class ValueObjectBuilderTest {
	
	/**
	 * Test method for {@link org.jiemamy.model.ValueObjectBuilder#addConfigurator(org.jiemamy.model.ValueObjectBuilder.BuilderConfigurator)}.
	 */
	@Test
	public final void testAddConfigurator() {
		BuilderMock builder = new BuilderMock();
		
		@SuppressWarnings("unchecked")
		BuilderConfigurator<BuilderMock> configurator1 = mock(BuilderConfigurator.class);
		@SuppressWarnings("unchecked")
		BuilderConfigurator<BuilderMock> configurator2 = mock(BuilderConfigurator.class);
		
		builder.addConfigurator(configurator1);
		
		assertThat(builder.configurators.size(), is(1));
		assertThat(builder.configurators.iterator().next(), is(configurator1));
		
		builder.addConfigurator(configurator2);
		
		assertThat(builder.configurators.size(), is(2));
		
		Iterator<BuilderConfigurator<BuilderMock>> iterator = builder.configurators.iterator();
		assertThat(iterator.next(), is(configurator1));
		assertThat(iterator.next(), is(configurator2));
	}
	
	/**
	 * Test method for {@link org.jiemamy.model.ValueObjectBuilder#build()}.
	 */
	@Test
	public final void testBuild() {
		BuilderMock builder = new BuilderMock() {
			
			@Override
			protected ValueObject createValueObject() {
				return new ValueObject() {
					
					@Override
					public String toString() {
						return "success";
					}
				};
			}
		};
		
		@SuppressWarnings("unchecked")
		BuilderConfigurator<BuilderMock> configurator = mock(BuilderConfigurator.class);
		builder.addConfigurator(configurator);
		
		ValueObject vo = builder.build();
		
		verify(configurator).configure(builder);
		assertThat(vo, is(notNullValue()));
		assertThat(vo.toString(), is("success"));
	}
	

	// テスト用ビルダークラス
	static class BuilderMock extends ValueObjectBuilder<ValueObject, BuilderMock> {
		
		@Override
		protected void apply(ValueObject vo, BuilderMock builder) {
		}
		
		@Override
		protected ValueObject createValueObject() {
			return null;
		}
		
		@Override
		protected BuilderMock getThis() {
			return this;
		}
		
		@Override
		protected BuilderMock newInstance() {
			return new BuilderMock();
		}
		
	}
	
}
