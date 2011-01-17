/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.model.dataset;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.script.ScriptString;

/**
 * レコード（INSERT文1つ分）モデル。
 * 
 * @author daisuke
 */
public final class DefaultRecordModel implements RecordModel {
	
	/** カラムに対応するデータ */
	private final Map<EntityRef<? extends ColumnModel>, ScriptString> values;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param values カラムに対応するデータ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 引数に{@code null}をキーに含む{@link Map}を与えた場合
	 */
	public DefaultRecordModel(Map<EntityRef<? extends ColumnModel>, ScriptString> values) {
		Validate.notNull(values);
		Validate.noNullElements(values.keySet());
		/* Validate.noNullElements(values.values()); */// valuesにはnullを含むことがある 
		this.values = Maps.newHashMap(values);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if ((obj instanceof DefaultRecordModel) == false) {
			return false;
		}
		DefaultRecordModel other = (DefaultRecordModel) obj;
		return values.equals(other.values);
	}
	
	public Map<EntityRef<? extends ColumnModel>, ScriptString> getValues() {
		assert values != null;
		return MutationMonitor.monitor(Maps.newHashMap(values));
	}
	
	@Override
	public int hashCode() {
		return values.hashCode();
	}
	
	public Iterable<Entry<EntityRef<? extends ColumnModel>, ScriptString>> toIterable(JiemamyContext context,
			EntityRef<? extends TableModel> tableRef) {
		Validate.notNull(context);
		Validate.notNull(tableRef);
		final TableModel tableModel = context.resolve(tableRef);
		Map<EntityRef<? extends ColumnModel>, ScriptString> sortedMap =
				Maps.newTreeMap(new ColumnOrderComparator(tableModel));
		sortedMap.putAll(values);
		return sortedMap.entrySet();
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	private static class ColumnOrderComparator implements Comparator<EntityRef<? extends ColumnModel>> {
		
		private final TableModel tableModel;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel 基準となるテーブル 
		 */
		private ColumnOrderComparator(TableModel tableModel) {
			Validate.notNull(tableModel);
			this.tableModel = tableModel;
		}
		
		public int compare(EntityRef<? extends ColumnModel> o1, EntityRef<? extends ColumnModel> o2) {
			int i1 = -1;
			int i2 = -1;
			List<ColumnModel> columns = tableModel.getColumns();
			for (ColumnModel columnModel : columns) {
				if (o1.isReferenceOf(columnModel)) {
					i1 = columns.indexOf(columnModel);
				}
				if (o2.isReferenceOf(columnModel)) {
					i2 = columns.indexOf(columnModel);
				}
			}
			if (i1 == -1 && i2 == -1) {
				return o1.getReferentId().compareTo(o2.getReferentId());
			}
			
			return i1 - i2;
		}
	}
}
