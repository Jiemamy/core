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
import org.jiemamy.model.index.IndexColumnModel;
import org.jiemamy.model.index.IndexModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * {@link IndexModel}の構成を調べるバリデータ。
 * 
 * @author daisuke
 */
public class IndexValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> result = Lists.newArrayList();
//		Map<TableModel, Collection<UUID>> map = CollectionsUtil.newHashMap();
//		for (TableModel tableModel : rootModel.getTables()) {
//			Collection<UUID> columnIds = CollectionsUtil.newArrayList();
//			for (ColumnModel columnModel : tableModel.getColumns()) {
//				columnIds.add(columnModel.getId());
//			}
//			map.put(tableModel, columnIds);
//		}
		
		for (IndexModel indexModel : context.getIndexes()) {
			Collection<UUID> referenceColumnIds = Lists.newArrayList();
			
			if (indexModel.getIndexColumns().size() < 1) {
				result.add(new NoIndexColumnProblem(indexModel));
			}
			for (IndexColumnModel indexColumnModel : indexModel.getIndexColumns()) {
				EntityRef<? extends ColumnModel> columnRef = indexColumnModel.getColumnRef();
				if (referenceColumnIds.contains(columnRef.getReferentId())) {
					ColumnModel columnModel = context.resolve(columnRef);
					result.add(new DuplicatedIndexColumnsProblem(indexModel, columnModel));
				}
				referenceColumnIds.add(columnRef.getReferentId());
			}
		}
		return result;
	}
	

	static class DuplicatedIndexColumnsProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param indexModel 同じカラムが複数設定されたインデックス
		 * @param columnModel 複数設定されたカラム
		 */
		protected DuplicatedIndexColumnsProblem(IndexModel indexModel, ColumnModel columnModel) {
			super("E0100");
			setArguments(new Object[] {
				StringUtils.isEmpty(indexModel.getName()) ? indexModel.getId().toString() : indexModel.getName(),
				columnModel.getName()
			});
		}
	}
	
	static class NoIndexColumnProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param indexModel インデックスカラムを1つも持たないインデックス
		 */
		public NoIndexColumnProblem(IndexModel indexModel) {
			super("E0120");
			setArguments(new Object[] {
				StringUtils.isEmpty(indexModel.getName()) ? indexModel.getId().toString() : indexModel.getName(),
			});
		}
	}
}
