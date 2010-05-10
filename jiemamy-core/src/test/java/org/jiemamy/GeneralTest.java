/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/10
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.DefalutColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.dbo.DefaultTableModel;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class GeneralTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		Repository repository = new Repository();
		
		DefalutColumnModel col1 = new DefalutColumnModel();
		col1.setName("KEY");
		
		DefalutColumnModel col2 = new DefalutColumnModel();
		col2.setName("VALUE");
		
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setName("T_PROPERTY");
		tableModel.addColumn(col1);
		tableModel.addColumn(col2);
		@SuppressWarnings("unchecked")
		List<EntityRef<ColumnModel>> pk = Arrays.asList(col1.getReference());
		tableModel.getAttributes().add(new DefaultPrimaryKeyConstraintModel(null, null, null, pk, null));
		repository.add(tableModel);
		
		assertThat(tableModel.getAttributes().size(), is(1));
		assertThat(tableModel.getColumns().size(), is(2));
	}
}
