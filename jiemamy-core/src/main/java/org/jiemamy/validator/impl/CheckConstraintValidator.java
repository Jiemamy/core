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
import org.jiemamy.model.constraint.JmCheckConstraint;
import org.jiemamy.model.table.JmTable;
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
		for (JmTable table : context.getTables()) {
			int index = 0;
			for (JmCheckConstraint checkConstraint : table.getConstraints(JmCheckConstraint.class)) {
				validateCheckConstraint(problems, table, index, checkConstraint);
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
	private boolean isValid(JmCheckConstraint checkConstraint) {
		// TODO いつかは構文解析
		return StringUtils.isEmpty(checkConstraint.getExpression()) == false;
	}
	
	private void validateCheckConstraint(Collection<Problem> problems, JmTable table, int index,
			JmCheckConstraint checkConstraint) {
		if (isValid(checkConstraint) == false) {
			if (StringUtils.isEmpty(checkConstraint.getName())) {
				problems.add(new EmptyExpressionProblem(table, checkConstraint, index));
			} else {
				problems.add(new EmptyExpressionProblem(table, checkConstraint));
			}
		}
	}
	

	static class EmptyExpressionProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table 制約式未設定チェック制約が含まれるテーブル
		 * @param checkConstraint 不正なチェック制約
		 * @param index チェック制約のインデックス
		 */
		public EmptyExpressionProblem(JmTable table, JmCheckConstraint checkConstraint, int index) {
			super(checkConstraint, "E0031");
			setArguments(new Object[] {
				table.getName(),
				index + 1
			});
		}
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table 制約式未設定チェック制約が含まれるテーブル
		 * @param checkConstraint 制約式未設定チェック制約
		 */
		EmptyExpressionProblem(JmTable table, JmCheckConstraint checkConstraint) {
			super(checkConstraint, "E0030");
			setArguments(new Object[] {
				table.getName(),
				checkConstraint.getName()
			});
		}
	}
}
