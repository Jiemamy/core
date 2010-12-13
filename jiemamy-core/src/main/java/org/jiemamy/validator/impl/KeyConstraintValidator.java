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
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.EntityRef;
import org.jiemamy.JiemamyContext;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;
import org.jiemamy.model.dbo.TableModel;
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
	
	public Collection<Problem> validate(JiemamyContext rootModel) {
		Collection<Problem> result = Lists.newArrayList();
		for (TableModel tableModel : rootModel.getEntities(TableModel.class)) {
			Collection<UUID> columnIds = Lists.newArrayList();
			for (ColumnModel columnModel : tableModel.getColumns()) {
				columnIds.add(columnModel.getId());
			}
			for (KeyConstraintModel keyConstraint : tableModel.getConstraints(KeyConstraintModel.class)) {
				if (keyConstraint.getKeyColumns().size() < 1) {
					result.add(new NoKeyColumnProblem(tableModel, keyConstraint));
				}
				for (EntityRef<? extends ColumnModel> columnRef : keyConstraint.getKeyColumns()) {
					if (columnIds.contains(columnRef.getReferentId()) == false) {
						result.add(new IllegalKeyColumnRefProblem(columnRef, keyConstraint, tableModel));
					}
				}
			}
		}
		return result;
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
			super("E0130");
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
			super("E0140");
			setArguments(new Object[] {
				StringUtils.isEmpty(tableModel.getName()) ? tableModel.getId().toString() : tableModel.getName(),
				keyConstraint.getName(),
			});
		}
	}
}
