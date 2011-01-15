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
package org.jiemamy.model.dataset;

import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.jiemamy.utils.RandomUtil.strNullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.junit.Test;

import org.jiemamy.model.table.TableModel;

/**
 * {@link DefaultDataSetModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultDataSetModelTest {
	
	/**
	 * TODO for daisuke
	 * @param tables 
	 * 
	 * @return
	 */
	public static DefaultDataSetModel random(Set<TableModel> tables) {
		DefaultDataSetModel model = new DefaultDataSetModel(UUID.randomUUID());
		model.setName(strNullable());
		for (TableModel tableModel : tables) {
			if (bool()) {
				int size = integer(5) + 1;
				List<RecordModel> records = Lists.newArrayListWithCapacity(size);
				for (int i = 0; i < size; i++) {
					records.add(DefaultRecordModelTest.random(tableModel.getColumns()));
				}
				model.putRecord(tableModel.toReference(), records);
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
