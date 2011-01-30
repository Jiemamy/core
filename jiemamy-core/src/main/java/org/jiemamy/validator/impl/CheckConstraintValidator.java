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
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * テーブルやカラムが保持するチェック制約に対するバリデータ。
 * 
 * <ul>
 *   <li>チェック制約には、制約式が設定されていなければならない。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class CheckConstraintValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		Collection<TableModel> tableModels = context.getTables();
		for (TableModel tableModel : tableModels) {
			int index = 0;
			for (CheckConstraintModel checkConstraint : tableModel.getConstraints(CheckConstraintModel.class)) {
				validateCheckConstraint(problems, tableModel, index, checkConstraint);
				index++;
			}
		}
		return problems;
	}
	
	/**
	 * チェック制約のexpressionに問題がないかどうか調べる。
	 * 
	 * @param checkConstraint 検査対象のチェック制約
	 * @return 問題がない場合は{@code true}、そうでない場合は{@code false}
	 */
	private boolean isValid(CheckConstraintModel checkConstraint) {
		// TODO いつかは構文解析
		return StringUtils.isEmpty(checkConstraint.getExpression()) == false;
	}
	
	private void validateCheckConstraint(Collection<Problem> problems, TableModel tableModel, int index,
			CheckConstraintModel checkConstraint) {
		if (isValid(checkConstraint) == false) {
			if (StringUtils.isEmpty(checkConstraint.getName())) {
				problems.add(new EmptyExpressionProblem(tableModel, checkConstraint, index));
			} else {
				problems.add(new EmptyExpressionProblem(tableModel, checkConstraint));
			}
		}
	}
	

	static class EmptyExpressionProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel 制約式未設定チェック制約が含まれるテーブル
		 * @param checkConstraintModel 制約式未設定チェック制約
		 */
		public EmptyExpressionProblem(TableModel tableModel, CheckConstraintModel checkConstraintModel) {
			super(checkConstraintModel, "E0030");
			setArguments(new Object[] {
				tableModel.getName(),
				checkConstraintModel.getName()
			});
		}
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel 制約式未設定チェック制約が含まれるテーブル
		 * @param checkConstraintModel 不正なチェック制約
		 * @param index チェック制約のインデックス
		 */
		public EmptyExpressionProblem(TableModel tableModel, CheckConstraintModel checkConstraintModel, int index) {
			super(checkConstraintModel, "E0031");
			setArguments(new Object[] {
				tableModel.getName(),
				index + 1
			});
		}
	}
}
