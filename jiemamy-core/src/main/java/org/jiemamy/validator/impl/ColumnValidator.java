/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/26
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
package org.jiemamy.validator.impl;

import java.util.Collection;

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * テーブルに存在するカラムの数のバリデータ。
 * 
 * <ul>
 *   <li>テーブルにカラムは1つ以上必要である。</li>
 *   <li>カラムには、データ型を設定する必要がある。</li>
 *   <li>カラムに対して設定されたキー制約は、キーカラムのサイズが1でなければならない。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class ColumnValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		for (JmTable table : context.getTables()) {
			int index = 0;
			for (JmColumn column : table.getColumns()) {
				if (StringUtils.isEmpty(column.getName())) {
					problems.add(new EmptyColumnNameProblem(table, column, index));
				}
				if (column.getDataType() == null) {
					problems.add(new EmptyDataTypeProblem(table, column));
				}
				index++;
			}
		}
		return problems;
	}
	
	
	static class EmptyColumnNameProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table 無名カラムが含まれるテーブル
		 * @param column 不正なカラム
		 * @param index カラムのインデックス
		 */
		public EmptyColumnNameProblem(JmTable table, JmColumn column, int index) {
			super(column, "E0040", new Object[] {
				table.getName(),
				index + 1
			});
		}
	}
	
	static class EmptyDataTypeProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table データ型無指定のカラムが含まれるテーブル
		 * @param column データ型無指定のカラム
		 */
		public EmptyDataTypeProblem(JmTable table, JmColumn column) {
			super(column, "E0050", new Object[] {
				table.getName(),
				column.getName()
			});
		}
	}
}
