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
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.script.ScriptString;

/**
 * レコード（INSERT文1つ分）モデル。
 * 
 * @author daisuke
 */
public final class SimpleJmRecord implements JmRecord {
	
	/** カラムに対応するデータ */
	private final Map<EntityRef<? extends JmColumn>, ScriptString> values;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param values カラムに対応するデータ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 引数に{@code null}をキーに含む{@link Map}を与えた場合
	 */
	public SimpleJmRecord(Map<EntityRef<? extends JmColumn>, ScriptString> values) {
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
		if ((obj instanceof SimpleJmRecord) == false) {
			return false;
		}
		SimpleJmRecord other = (SimpleJmRecord) obj;
		return values.equals(other.values);
	}
	
	public Map<EntityRef<? extends JmColumn>, ScriptString> getValues() {
		assert values != null;
		return MutationMonitor.monitor(Maps.newHashMap(values));
	}
	
	@Override
	public int hashCode() {
		return values.hashCode();
	}
	
	public Iterable<Entry<EntityRef<? extends JmColumn>, ScriptString>> toIterable(JiemamyContext context,
			EntityRef<? extends JmTable> tableRef) {
		Validate.notNull(context);
		Validate.notNull(tableRef);
		final JmTable table = context.resolve(tableRef);
		Map<EntityRef<? extends JmColumn>, ScriptString> sortedMap = Maps.newTreeMap(new ColumnOrderComparator(table));
		sortedMap.putAll(values);
		return sortedMap.entrySet();
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	private static class ColumnOrderComparator implements Comparator<EntityRef<? extends JmColumn>> {
		
		private final JmTable table;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param table 基準となるテーブル 
		 */
		private ColumnOrderComparator(JmTable table) {
			Validate.notNull(table);
			this.table = table;
		}
		
		public int compare(EntityRef<? extends JmColumn> o1, EntityRef<? extends JmColumn> o2) {
			int i1 = -1;
			int i2 = -1;
			List<JmColumn> columns = table.getColumns();
			for (JmColumn column : columns) {
				if (o1.isReferenceOf(column)) {
					i1 = columns.indexOf(column);
				}
				if (o2.isReferenceOf(column)) {
					i2 = columns.indexOf(column);
				}
			}
			if (i1 == -1 && i2 == -1) {
				return o1.getReferentId().compareTo(o2.getReferentId());
			}
			
			return i1 - i2;
		}
	}
}
