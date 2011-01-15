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

import static org.jiemamy.utils.RandomUtil.enumeNullable;
import static org.jiemamy.utils.RandomUtil.strNullable;

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.model.column.ColumnModel;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultNotNullConstraintModelTest {
	
	/**
	 * 適当な {@link DefaultNotNullConstraintModel} のインスタンスを作る。
	 * 
	 * @param columnModel 対象カラム
	 * @return {@link DefaultNotNullConstraintModel}
	 */
	public static DefaultNotNullConstraintModel random(ColumnModel columnModel) {
		DefaultNotNullConstraintModel nn = new DefaultNotNullConstraintModel(UUID.randomUUID());
		nn.setName(strNullable());
		nn.setLogicalName(strNullable());
		nn.setDeferrability(enumeNullable(DefaultDeferrabilityModel.class));
		nn.setDescription(strNullable());
		nn.setColumn(columnModel.toReference());
		return nn;
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
