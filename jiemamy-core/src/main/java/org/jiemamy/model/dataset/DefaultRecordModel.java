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

import java.util.Map;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.column.ColumnModel;

/**
 * レコード（INSERT文1つ分）モデル。
 * 
 * @author daisuke
 */
public final class DefaultRecordModel implements RecordModel {
	
	/** カラムに対応するデータ */
	private final Map<EntityRef<? extends ColumnModel>, String> values;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param values カラムに対応するデータ
	 */
	public DefaultRecordModel(Map<EntityRef<? extends ColumnModel>, String> values) {
		Validate.notNull(values);
		Validate.noNullElements(values.keySet());
		Validate.noNullElements(values.values());
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
	
	public Map<EntityRef<? extends ColumnModel>, String> getValues() {
		assert values != null;
		return MutationMonitor.monitor(Maps.newHashMap(values));
	}
	
	@Override
	public int hashCode() {
		return values.hashCode();
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
