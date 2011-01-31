/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/04/06
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
package org.jiemamy.test;

import java.util.HashMap;

import com.google.common.collect.Maps;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.GenericDialect;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.dataset.DefaultRecordModel;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultTypeReference;
import org.jiemamy.model.datatype.DefaultDataType;
import org.jiemamy.model.datatype.TypeReference;
import org.jiemamy.script.ScriptString;

/**
 * {@link TestModelBuilder}がモデル生成時に用いるユーティリティクラス。
 * 
 * @author daisuke
 */
final class ModelUtil {
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public static RecordBuilder newRecord() {
		return new RecordBuilder();
	}
	
	static DefaultDataType createDataType(JiemamyContext jiemamy, DataTypeCategory category) {
		Dialect dialect;
		try {
			dialect = jiemamy.findDialect();
		} catch (ClassNotFoundException e) {
			dialect = new GenericDialect();
		}
		TypeReference normalize = dialect.normalize(new DefaultTypeReference(category));
		return new DefaultDataType(normalize);
	}
	
	private ModelUtil() {
	}
	

	/**
	 * TODO for daisuke
	 * 
	 * @version $Id$
	 * @author daisuke
	 */
	public static class RecordBuilder {
		
		private HashMap<EntityRef<? extends ColumnModel>, ScriptString> map;
		

		private RecordBuilder() {
			map = Maps.newHashMap();
		}
		
		public RecordBuilder addValue(ColumnModel col, String val) {
			map.put(col.toReference(), new ScriptString(val));
			return this;
		}
		
		public DefaultRecordModel build() {
			return new DefaultRecordModel(map);
		}
		
	}
}
