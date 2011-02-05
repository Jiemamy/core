/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * テーブルのバリデータ。
 * 
 * <ul>
 *   <li>テーブル名は必須である。</li>
 *   <li>テーブルにはカラムが1つ以上必要である。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class TableValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		for (JmTable table : context.getTables()) {
			List<? extends JmColumn> columns = table.getColumns();
			if (columns.size() == 0) {
				problems.add(new NoColumnProblem(table));
			}
			if (StringUtils.isEmpty(table.getName())) {
				problems.add(new EmptyTableNameProblem(table));
			}
		}
		return problems;
	}
	

	static class EmptyTableNameProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table テーブル名が設定されていないテーブル
		 */
		public EmptyTableNameProblem(JmTable table) {
			super(table, "E0170");
			setArguments(new Object[] {
				table.getId().toString()
			});
		}
	}
	
	static class NoColumnProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table カラムの存在しないテーブル
		 */
		public NoColumnProblem(JmTable table) {
			super(table, "W0060");
			setArguments(new Object[] {
				table.getName()
			});
		}
	}
}
