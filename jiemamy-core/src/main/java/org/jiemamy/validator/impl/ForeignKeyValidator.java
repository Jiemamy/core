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
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;
import org.jiemamy.model.dbo.TableModel;
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
public class ForeignKeyValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext rootModel) {
		Collection<Problem> result = Lists.newArrayList();
		for (TableModel tableModel : rootModel.getEntities(TableModel.class)) {
			for (ForeignKeyConstraintModel foreignKey : tableModel.getConstraints(ForeignKeyConstraintModel.class)) {
				if (foreignKey.getKeyColumns().size() != foreignKey.getReferenceColumns().size()) {
					result.add(new ReferenceMappingProblem(foreignKey));
				}
				
				if (tableModel.findReferencedKeyConstraint(foreignKey) == null) {
					result.add(new ReferenceKeyProblem(foreignKey));
				}
			}
		}
		
		return result;
	}
	

	static class ReferenceKeyProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param foreignKey 不正な外部キー
		 */
		public ReferenceKeyProblem(ForeignKeyConstraintModel foreignKey) {
			super("E0080");
			setArguments(new Object[] {
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
		public ReferenceMappingProblem(ForeignKeyConstraintModel foreignKey) {
			super("E0090");
			setArguments(new Object[] {
				foreignKey.getName(),
				foreignKey.getKeyColumns().size(),
				foreignKey.getReferenceColumns().size()
			});
		}
	}
	
}
