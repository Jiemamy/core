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
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.KeyConstraintModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.utils.EntityToIdTransformer;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * {@link KeyConstraintModel#getKeyColumns()}の構成を調べるバリデータ。
 * 
 * <ul>
 *   <li>keyColumnsは、自テーブルのカラムへの参照で構成されていなければならない。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class KeyConstraintValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		for (TableModel tableModel : context.getTables()) {
			Collection<UUID> columnIds = Lists.transform(tableModel.getColumns(), new EntityToIdTransformer());
			for (KeyConstraintModel keyConstraint : tableModel.getConstraints(KeyConstraintModel.class)) {
				if (keyConstraint.getKeyColumns().size() < 1) {
					problems.add(new NoKeyColumnProblem(tableModel, keyConstraint));
				}
				for (EntityRef<? extends ColumnModel> columnRef : keyConstraint.getKeyColumns()) {
					if (columnIds.contains(columnRef.getReferentId()) == false) {
						problems.add(new IllegalKeyColumnRefProblem(columnRef, keyConstraint, tableModel));
					}
				}
			}
		}
		return problems;
	}
	

	static class IllegalKeyColumnRefProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param columnRef 参照の切れた参照オブジェクト
		 * @param keyConstraint 参照オブジェクトを保持するキー
		 * @param tableModel キーを保持するテーブル
		 */
		public IllegalKeyColumnRefProblem(EntityRef<? extends ColumnModel> columnRef, KeyConstraintModel keyConstraint,
				TableModel tableModel) {
			super(keyConstraint, "F0130");
			setArguments(new Object[] {
				tableModel.getName(),
				tableModel.getId().toString(),
				keyConstraint.getName(),
				columnRef.getReferentId().toString()
			});
		}
	}
	
	static class NoKeyColumnProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel キーカラムを1つも持たないキー制約を持つテーブル
		 * @param keyConstraint キーカラムを1つも持たないキー制約
		 */
		public NoKeyColumnProblem(TableModel tableModel, KeyConstraintModel keyConstraint) {
			super(keyConstraint, "F0140");
			setArguments(new Object[] {
				StringUtils.isEmpty(tableModel.getName()) ? tableModel.getId().toString() : tableModel.getName(),
				keyConstraint.getName(),
			});
		}
	}
}
