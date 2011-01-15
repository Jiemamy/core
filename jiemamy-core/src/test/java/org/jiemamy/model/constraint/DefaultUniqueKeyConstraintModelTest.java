/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/15
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

import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.enumeNullable;
import static org.jiemamy.utils.RandomUtil.str;
import static org.jiemamy.utils.RandomUtil.strNullable;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.table.TableModel;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultUniqueKeyConstraintModelTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @param model
	 * @return
	 */
	public static DefaultUniqueKeyConstraintModel random(TableModel table) {
		DefaultUniqueKeyConstraintModel model = new DefaultUniqueKeyConstraintModel(UUID.randomUUID());
		model.setName(str());
		model.setLogicalName(strNullable());
		model.setDescription(strNullable());
		model.setDeferrability(enumeNullable(DefaultDeferrabilityModel.class));
		List<ColumnModel> columns = table.getColumns();
		for (ColumnModel columnModel : columns) {
			if (bool()) {
				model.addKeyColumn(columnModel.toReference());
			}
		}
		return model;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		
	}
	
}
