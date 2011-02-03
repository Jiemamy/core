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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * 外部キーの循環参照を検出するバリデータ。
 * 
 * <ul>
 *   <li>テーブル名は必須である。</li>
 *   <li>テーブルにはカラムが1つ以上必要である。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class CyclicForeignReferenceValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		for (TableModel tableModel : context.getTables()) {
			Collection<DatabaseObjectModel> superDatabaseObjectsRecursive =
					context.findSuperDatabaseObjectsRecursive(tableModel);
			if (superDatabaseObjectsRecursive.size() == 1
					&& Iterables.getOnlyElement(superDatabaseObjectsRecursive).equals(tableModel)) {
				// 自己参照FK
				continue;
			}
			if (superDatabaseObjectsRecursive.contains(tableModel)) {
				problems.add(new CyclicForeignReferenceProblem(tableModel));
				// 循環ノード数分検出してしまうので、1つ検出したらすぐに戻る
				// TODO 循環の数＝輪の数を検出し、エラーの数にできないか？
				return problems;
			}
		}
		return problems;
	}
	

	static class CyclicForeignReferenceProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel テーブル名が設定されていないテーブル
		 */
		public CyclicForeignReferenceProblem(TableModel tableModel) {
			super(tableModel, "F230");
			setArguments(new Object[] {
				tableModel.getId().toString()
			});
		}
	}
}
