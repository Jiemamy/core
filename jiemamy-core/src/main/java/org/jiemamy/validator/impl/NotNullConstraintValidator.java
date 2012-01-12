/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmKeyConstraint;
import org.jiemamy.model.constraint.JmNotNullConstraint;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * {@link JmForeignKeyConstraint}の構成を調べるバリデータ。
 * 
 * <ul>
 *   <li>{@link JmForeignKeyConstraint#getKeyColumns()}と{@link JmForeignKeyConstraint#getReferenceColumns()}の
 *   要素数は一致していなければならない。</li>
 *   <li>{@link JmForeignKeyConstraint#getReferenceColumns()}は、参照先テーブルが持ついずれかの {@link JmKeyConstraint} の
 *   キー構成カラムと一致していなければならない。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class NotNullConstraintValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		for (JmTable table : context.getTables()) {
			for (JmNotNullConstraint nn : table.getConstraints(JmNotNullConstraint.class)) {
				if (nn.getColumn() == null) {
					problems.add(new NullTargetProblem(table, nn));
				}
				
				try {
					table.resolve(nn.getColumn());
				} catch (EntityNotFoundException e) {
					problems.add(new ReferenceProblem(nn, nn.getColumn()));
				}
			}
		}
		
		return problems;
	}
	
	
	static class NullTargetProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table テーブル
		 * @param nn 不正な非NULL制約
		 */
		public NullTargetProblem(JmTable table, JmNotNullConstraint nn) {
			super(nn, "F0180", new Object[] {
				table.getName(),
				nn.getName(),
			});
		}
	}
}
