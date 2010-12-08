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

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.utils.collection.CollectionsUtil;
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
		Collection<Problem> result = CollectionsUtil.newArrayList();
		Collection<TableModel> tableModels = rootModel.getCore().getEntities(TableModel.class);
		for (TableModel tableModel : tableModels) {
			int index = 0;
			for (ColumnModel columnModel : tableModel.getColumns()) {
				if (StringUtils.isEmpty(columnModel.getName())) {
					result.add(new EmptyColumnNameProblem(tableModel, index));
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
		 * @param index カラムのインデックス
		 */
		public EmptyColumnNameProblem(TableModel tableModel, int index) {
			super("E0040");
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
			super("E0050");
			setArguments(new Object[] {
				tableModel.getName(),
				columnModel.getName()
			});
		}
	}
}
