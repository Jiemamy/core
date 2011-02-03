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
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.KeyConstraintModel;
import org.jiemamy.model.constraint.NotNullConstraintModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * {@link ForeignKeyConstraintModel}の構成を調べるバリデータ。
 * 
 * <ul>
 *   <li>{@link ForeignKeyConstraintModel#getKeyColumns()}と{@link ForeignKeyConstraintModel#getReferenceColumns()}の
 *   要素数は一致していなければならない。</li>
 *   <li>{@link ForeignKeyConstraintModel#getReferenceColumns()}は、参照先テーブルが持ついずれかの {@link KeyConstraintModel} の
 *   キー構成カラムと一致していなければならない。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class NotNullConstraintValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		for (TableModel tableModel : context.getTables()) {
			for (NotNullConstraintModel nn : tableModel.getConstraints(NotNullConstraintModel.class)) {
				if (nn.getColumnRef() == null) {
					problems.add(new NullTargetProblem(tableModel, nn));
				}
				
				try {
					tableModel.resolve(nn.getColumnRef());
				} catch (EntityNotFoundException e) {
					problems.add(new TargetNotFoundProblem(nn));
				}
			}
		}
		
		return problems;
	}
	

	static class NullTargetProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel テーブル
		 * @param nn 不正な非NULL制約
		 */
		public NullTargetProblem(TableModel tableModel, NotNullConstraintModel nn) {
			super(nn, "F0180");
			setArguments(new Object[] {
				tableModel.getName(),
				nn.getName(),
			});
		}
	}
	
	static class TargetNotFoundProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param nn 不正な非NULL制約
		 */
		public TargetNotFoundProblem(NotNullConstraintModel nn) {
			super(nn, "F0090");
			setArguments(new Object[] {
				nn.getName(),
			});
		}
	}
	
}
