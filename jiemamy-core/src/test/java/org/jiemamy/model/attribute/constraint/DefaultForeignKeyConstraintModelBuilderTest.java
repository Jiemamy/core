/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/07
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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel.MatchType;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel.ReferentialAction;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultForeignKeyConstraintModelBuilderTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_build() throws Exception {
		EntityRef<ColumnModel> ref1 = new DefaultEntityRef<ColumnModel>(UUID.randomUUID());
		EntityRef<ColumnModel> ref2 = new DefaultEntityRef<ColumnModel>(UUID.randomUUID());
		
		DefaultForeignKeyConstraintModelBuilder builder = new DefaultForeignKeyConstraintModelBuilder();
		
		// FORMAT-OFF
		ForeignKeyConstraintModel fk = builder
				.setName("FK_A_B")
				.addKeyColumn(ref1).addReferenceColumn(ref2)
				.setMatchType(MatchType.SIMPLE)
				.setOnDelete(ReferentialAction.CASCADE)
				.build();
		// FORMAT-ON
		
		assertThat(fk.getName(), is("FK_A_B"));
		assertThat(fk.getKeyColumns(), hasItem(ref1));
		assertThat(fk.getReferenceColumns(), hasItem(ref2));
		assertThat(fk.getDeferrability(), is(nullValue()));
		assertThat(fk.getMatchType(), is(MatchType.SIMPLE));
		assertThat(fk.getOnDelete(), is(ReferentialAction.CASCADE));
		assertThat(fk.getOnUpdate(), is(nullValue()));
	}
}
