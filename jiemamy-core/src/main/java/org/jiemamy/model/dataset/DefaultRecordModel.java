/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

import org.apache.commons.collections15.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.model.ValueObject;
import org.jiemamy.model.attribute.ColumnRef;

/**
 * レコード（INSERT文1つ分）モデル。
 * 
 * @author daisuke
 */
public final class DefaultRecordModel implements RecordModel, ValueObject {
	
	/** カラムに対応するデータ */
	private final Map<ColumnRef, String> values;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param values カラムに対応するデータ
	 */
	public DefaultRecordModel(Map<ColumnRef, String> values) {
		this.values = MapUtils.unmodifiableMap(values);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DefaultRecordModel)) {
			return false;
		}
		DefaultRecordModel other = (DefaultRecordModel) obj;
		if (values == null) {
			if (other.values != null) {
				return false;
			}
		} else if (!values.equals(other.values)) {
			return false;
		}
		return true;
	}
	
	public Map<ColumnRef, String> getValues() {
		assert values != null;
		return values;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
