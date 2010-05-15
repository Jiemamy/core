/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/14
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * {@link AbstractConstraintModelBuilder}のテストクラス。
 * 
 * @version $Id$
 * @author Keisuke.K
 */
public class AbstractConstraintModelBuilderTest {
	
	/**
	 * Test method for {@link org.jiemamy.model.attribute.constraint.AbstractConstraintModelBuilder#apply(org.jiemamy.model.attribute.constraint.ConstraintModel, org.jiemamy.model.attribute.constraint.AbstractConstraintModelBuilder)}.
	 */
	@Test
	public final void testApply() {
		ConstraintModel model = mock(ConstraintModel.class);
		when(model.getName()).thenReturn("name");
		when(model.getLogicalName()).thenReturn("logicalName");
		when(model.getDescription()).thenReturn("description");
		
		BuilderMock builder = new BuilderMock();
		BuilderMock testBuilder = new BuilderMock();
		
		builder.apply(model, testBuilder);
		assertThat(testBuilder.name, is("name"));
		assertThat(testBuilder.logicalName, is("logicalName"));
		assertThat(testBuilder.description, is("description"));
		
		testBuilder = new BuilderMock();
		builder.setName("eman").apply(model, testBuilder);
		assertThat(testBuilder.name, is("eman"));
		assertThat(testBuilder.logicalName, is("logicalName"));
		assertThat(testBuilder.description, is("description"));
		
		testBuilder = new BuilderMock();
		builder.setLogicalName("emaNlacigoL").apply(model, testBuilder);
		assertThat(testBuilder.name, is("eman"));
		assertThat(testBuilder.logicalName, is("emaNlacigoL"));
		assertThat(testBuilder.description, is("description"));
		
		testBuilder = new BuilderMock();
		builder.setDescription("noitpircsed").apply(model, testBuilder);
		assertThat(testBuilder.name, is("eman"));
		assertThat(testBuilder.logicalName, is("emaNlacigoL"));
		assertThat(testBuilder.description, is("noitpircsed"));
	}
	
	/**
	 * Test method for {@link org.jiemamy.model.attribute.constraint.AbstractConstraintModelBuilder#setDescription(java.lang.String)}.
	 */
	@Test
	public final void testSetDescription() {
		BuilderMock builder = new BuilderMock();
		builder.setDescription("description");
		
		assertThat(builder.description, is("description"));
	}
	
	/**
	 * Test method for {@link org.jiemamy.model.attribute.constraint.AbstractConstraintModelBuilder#setLogicalName(java.lang.String)}.
	 */
	@Test
	public final void testSetLogicalName() {
		BuilderMock builder = new BuilderMock();
		builder.setLogicalName("logicalName");
		
		assertThat(builder.logicalName, is("logicalName"));
	}
	
	/**
	 * Test method for {@link org.jiemamy.model.attribute.constraint.AbstractConstraintModelBuilder#setName(java.lang.String)}.
	 */
	@Test
	public final void testSetName() {
		BuilderMock builder = new BuilderMock();
		builder.setName("name");
		
		assertThat(builder.name, is("name"));
	}
	

	// テスト用モッククラス
	class BuilderMock extends AbstractConstraintModelBuilder<ConstraintModel, BuilderMock> {
		
		@Override
		public ConstraintModel build() {
			// noop
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
