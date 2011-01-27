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

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.table.TableModel;
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
	
	public Collection<Problem> validate(JiemamyContext rootModel) {
		Collection<Problem> result = Lists.newArrayList();
		Collection<TableModel> tableModels = rootModel.getTables();
		for (TableModel tableModel : tableModels) {
			int index = 0;
			for (ColumnModel columnModel : tableModel.getColumns()) {
				if (StringUtils.isEmpty(columnModel.getName())) {
					result.add(new EmptyColumnNameProblem(tableModel, columnModel, index));
				}
				if (columnModel.getDataType() == null) {
					result.add(new EmptyDataTypeProblem(tableModel, columnModel));
				}
				index++;
			}
		}
		return result;
	}
	

	static class EmptyColumnNameProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel 無名カラムが含まれるテーブル
		 * @param columnModel 不正なカラム
		 * @param index カラムのインデックス
		 */
		public EmptyColumnNameProblem(TableModel tableModel, ColumnModel columnModel, int index) {
			super(columnModel, "E0040");
			setArguments(new Object[] {
				tableModel.getName(),
				index
			});
		}
	}
	
	static class EmptyDataTypeProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel データ型無指定のカラムが含まれるテーブル
		 * @param columnModel データ型無指定のカラム
		 */
		public EmptyDataTypeProblem(TableModel tableModel, ColumnModel columnModel) {
			super(columnModel, "E0050");
			setArguments(new Object[] {
				tableModel.getName(),
				columnModel.getName()
			});
		}
	}
}
