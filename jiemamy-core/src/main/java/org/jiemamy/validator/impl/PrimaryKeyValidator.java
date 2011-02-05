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

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * {@link JmPrimaryKeyConstraint}の数を調べるバリデータ。
 * 
 * <ul>
 *   <li>1つのテーブルには複数の{@link JmPrimaryKeyConstraint}が含まれていてはならない。</li>
 *   <li>NOTICE: テーブルに{@link JmPrimaryKeyConstraint}が1つも存在しなかった場合。</p>
 * </ul>
 * 
 * @author daisuke
 */
public class PrimaryKeyValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> result = Lists.newArrayList();
		Collection<JmTable> tables = context.getTables();
		for (JmTable table : tables) {
			int size = table.getConstraints(JmPrimaryKeyConstraint.class).size();
			if (size == 0) {
				result.add(new NoPrimaryKeyProblem(table));
			} else if (size > 1) {
				result.add(new MultiplePrimaryKeyProblem(table));
			}
		}
		return result;
	}
	

	static class MultiplePrimaryKeyProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table 複数の主キーが設定されたテーブル
		 */
		public MultiplePrimaryKeyProblem(JmTable table) {
			super(table, "F0150");
			setArguments(new Object[] {
				table.getName()
			});
		}
	}
	
	static class NoPrimaryKeyProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table 主キーが存在しないテーブル
		 */
		public NoPrimaryKeyProblem(JmTable table) {
			super(table, "N0080");
			setArguments(new Object[] {
				table.getName()
			});
		}
	}
}
