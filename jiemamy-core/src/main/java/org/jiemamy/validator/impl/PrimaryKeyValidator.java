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

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * {@link PrimaryKeyConstraintModel}の数を調べるバリデータ。
 * 
 * <ul>
 *   <li>1つのテーブルには複数の{@link PrimaryKeyConstraintModel}が含まれていてはならない。</li>
 *   <li>NOTICE: テーブルに{@link PrimaryKeyConstraintModel}が1つも存在しなかった場合。</p>
 * </ul>
 * 
 * @author daisuke
 */
public class PrimaryKeyValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext rootModel) {
		Collection<Problem> result = Lists.newArrayList();
		Collection<TableModel> tableModels = rootModel.getTables();
		for (TableModel tableModel : tableModels) {
			int size = tableModel.getConstraints(PrimaryKeyConstraintModel.class).size();
			if (size == 0) {
				result.add(new NoPrimaryKeyProblem(tableModel));
			} else if (size > 1) {
				result.add(new MultiplePrimaryKeyProblem(tableModel));
			}
		}
		return result;
	}
	

	static class MultiplePrimaryKeyProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel 複数の主キーが設定されたテーブル
		 */
		public MultiplePrimaryKeyProblem(TableModel tableModel) {
			super("E0150");
			setArguments(new Object[] {
				tableModel.getName()
			});
		}
	}
	
	static class NoPrimaryKeyProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel 主キーが存在しないテーブル
		 */
		public NoPrimaryKeyProblem(TableModel tableModel) {
			super("N0010");
			setArguments(new Object[] {
				tableModel.getName()
			});
		}
	}
}
