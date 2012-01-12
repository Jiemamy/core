/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/14
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

import org.junit.Test;

import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.JmTable;

/**
 * {@link SimpleJmPrimaryKeyConstraint}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmPrimaryKeyConstraintTest {
	
	/**
	 * 適当な {@link SimpleJmPrimaryKeyConstraint} のインスタンスを作る。
	 * 
	 * @param table 対象テーブル
	 * @return {@link SimpleJmPrimaryKeyConstraint}
	 */
	public static SimpleJmPrimaryKeyConstraint random(JmTable table) {
		SimpleJmPrimaryKeyConstraint model = new SimpleJmPrimaryKeyConstraint();
		model.setName(str());
		model.setLogicalName(strNullable());
		model.setDescription(strNullable());
		model.setDeferrability(enumeNullable(SimpleJmDeferrability.class));
		List<JmColumn> columns = table.getColumns();
		for (JmColumn column : columns) {
			if (bool()) {
				model.addKeyColumn(column.toReference());
			}
		}
		return model;
	}
	
	/**
	 * 特にまだテストしたいことはないけどテストメソッドが1つもないとエラーが発生するので仕方なく置いてやってるメソッド。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
	}
}
