/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * テーブルのバリデータ。
 * 
 * <ul>
 *   <li>テーブル名は必須である。</li>
 *   <li>テーブルにはカラムが1つ以上ひつようである。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class TableValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext rootModel) {
		Collection<Problem> result = Lists.newArrayList();
		Collection<TableModel> tableModels = rootModel.getEntities(TableModel.class);
		for (TableModel tableModel : tableModels) {
			List<? extends ColumnModel> columns = tableModel.getColumns();
			if (columns.size() == 0) {
				result.add(new NoColumnProblem(tableModel));
			}
			if (StringUtils.isEmpty(tableModel.getName())) {
				result.add(new EmptyTableNameProblem(tableModel));
			}
		}
		return result;
	}
	

	static class EmptyTableNameProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel テーブル名が設定されていないテーブル
		 */
		public EmptyTableNameProblem(TableModel tableModel) {
			super("E0170");
			setArguments(new Object[] {
				tableModel.getId().toString()
			});
		}
	}
	
	static class NoColumnProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel カラムの存在しないテーブル
		 */
		public NoColumnProblem(TableModel tableModel) {
			super("W0010");
			setArguments(new Object[] {
				tableModel.getName()
			});
		}
	}
}
