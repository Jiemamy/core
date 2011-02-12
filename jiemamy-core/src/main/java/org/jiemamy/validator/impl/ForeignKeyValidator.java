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
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmKeyConstraint;
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
public class ForeignKeyValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		for (JmTable table : context.getTables()) {
			for (JmForeignKeyConstraint foreignKey : table.getForeignKeyConstraints()) {
				if (foreignKey.getKeyColumns().size() != foreignKey.getReferenceColumns().size()) {
					problems.add(new ReferenceMappingProblem(foreignKey));
				}
				
				if (foreignKey.findReferencedKeyConstraint(context.getTables()) == null) {
					problems.add(new ReferenceKeyProblem(foreignKey));
				}
			}
		}
		
		return problems;
	}
	

	static class ReferenceKeyProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param foreignKey 不正な外部キー
		 */
		public ReferenceKeyProblem(JmForeignKeyConstraint foreignKey) {
			super(foreignKey, "F0200", new Object[] {
				foreignKey.getName(),
				foreignKey.getKeyColumns().size(),
				foreignKey.getReferenceColumns().size()
			});
		}
	}
	
	static class ReferenceMappingProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param foreignKey 不正な外部キー
		 */
		public ReferenceMappingProblem(JmForeignKeyConstraint foreignKey) {
			super(foreignKey, "F0220", new Object[] {
				foreignKey.getName(),
				foreignKey.getKeyColumns().size(),
				foreignKey.getReferenceColumns().size()
			});
		}
	}
	
}
