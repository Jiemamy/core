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
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.index.JmIndex;
import org.jiemamy.model.index.JmIndexColumn;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * {@link JmIndex}の構成を調べるバリデータ。
 * 
 * @author daisuke
 */
public class IndexValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> result = Lists.newArrayList();
//		Map<JmTable, Collection<UUID>> map = CollectionsUtil.newHashMap();
//		for (JmTable table : context.getTables()) {
//			Collection<UUID> columnIds = Lists.newArrayList();
//			for (JmColumn column : table.getColumns()) {
//				columnIds.add(column.getId());
//			}
//			map.put(table, columnIds);
//		}
		
		for (JmIndex index : context.getIndexes()) {
			Collection<UUID> referenceColumnIds = Lists.newArrayList();
			
			if (index.getIndexColumns().size() < 1) {
				result.add(new NoIndexColumnProblem(index));
			}
			for (JmIndexColumn indexColumn : index.getIndexColumns()) {
				EntityRef<? extends JmColumn> columnRef = indexColumn.getColumnRef();
				if (referenceColumnIds.contains(columnRef.getReferentId())) {
					JmColumn column = context.resolve(columnRef);
					result.add(new DuplicatedIndexColumnsProblem(index, column));
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
		 * @param index 同じカラムが複数設定されたインデックス
		 * @param column 複数設定されたカラム
		 */
		protected DuplicatedIndexColumnsProblem(JmIndex index, JmColumn column) {
			super(index, "E0100", new Object[] {
				StringUtils.isEmpty(index.getName()) ? index.getId().toString() : index.getName(),
				column.getName()
			});
		}
	}
	
	static class NoIndexColumnProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param index インデックスカラムを1つも持たないインデックス
		 */
		public NoIndexColumnProblem(JmIndex index) {
			super(index, "E0120", new Object[] {
				StringUtils.isEmpty(index.getName()) ? index.getId().toString() : index.getName(),
			});
		}
	}
}
