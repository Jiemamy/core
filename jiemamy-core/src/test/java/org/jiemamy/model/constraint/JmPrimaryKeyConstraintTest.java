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

import static org.hamcrest.Matchers.is;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.enumeNullable;
import static org.jiemamy.utils.RandomUtil.str;
import static org.jiemamy.utils.RandomUtil.strNullable;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.google.common.collect.Iterables;

import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;

/**
 * {@link JmPrimaryKeyConstraint}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmPrimaryKeyConstraintTest {
	
	/**
	 * 適当な {@link JmPrimaryKeyConstraint} のインスタンスを作る。
	 * 
	 * @param table 対象テーブル
	 * @return {@link JmPrimaryKeyConstraint}
	 */
	public static JmPrimaryKeyConstraint random(JmTable table) {
		JmPrimaryKeyConstraint model = new JmPrimaryKeyConstraint();
		model.setName(str());
		model.setLogicalName(strNullable());
		model.setDescription(strNullable());
		model.setDeferrability(enumeNullable(JmDeferrability.class));
		List<JmColumn> columns = table.getColumns();
		for (JmColumn column : columns) {
			if (bool()) {
				model.addKeyColumn(column.toReference());
			}
		}
		return model;
	}
	
	/**
	 * {@link JmPrimaryKeyConstraint#SimpleJmPrimaryKeyConstraint(JmTableBuilder, String...)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_builderConstructor() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		JmTableBuilder builder = new JmTableBuilder("TTTT");
		ctx.add(builder
			.with(new JmColumnBuilder("CCCC_PK").type(
					new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.INTEGER, "BIGINT"))).build())
			.with(new JmColumnBuilder("CCCC_NORMAL").type(
					new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.CLOB, "TEXT"))).build())
			.with(new JmPrimaryKeyConstraint(builder, "CCCC_PK")).build());
		
		JmTable t = ctx.getTable("TTTT");
		assertThat(t.getColumns().size(), is(2));
		assertThat(t.getColumns().get(0).getName(), is("CCCC_PK"));
		assertThat(t.getColumns().get(1).getName(), is("CCCC_NORMAL"));
		JmPrimaryKeyConstraint pk = t.getPrimaryKey();
		assertThat(Iterables.getOnlyElement(pk.getKeyColumns()).isReferenceOf(t.getColumns().get(0)), is(true));
	}
}
