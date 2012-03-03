/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.GenericDialect;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.dataset.SimpleJmRecord;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.RawTypeDescriptor;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.script.ScriptString;

/**
 * {@link TestModelBuilder}がモデル生成時に用いるユーティリティクラス。
 * 
 * @author daisuke
 */
final class ModelUtil {
	
	/**
	 * 新しい {@link RecordBuilder} を返す。
	 * 
	 * @return new {@link RecordBuilder}
	 */
	public static RecordBuilder newRecord() {
		return new RecordBuilder();
	}
	
	static SimpleDataType createDataType(JiemamyContext jiemamy, RawTypeCategory category) {
		Dialect dialect;
		try {
			dialect = jiemamy.findDialect();
		} catch (ClassNotFoundException e) {
			dialect = new GenericDialect();
		}
		RawTypeDescriptor normalize = dialect.normalize(new SimpleRawTypeDescriptor(category));
		return new SimpleDataType(normalize);
	}
	
	private ModelUtil() {
	}
	
	
	/**
	 * {@link SimpleJmRecord}インスタンスのビルダクラス。
	 * 
	 * @version $Id$
	 * @author daisuke
	 */
	public static class RecordBuilder {
		
		private HashMap<EntityRef<? extends JmColumn>, ScriptString> map;
		
		
		private RecordBuilder() {
			map = Maps.newHashMap();
		}
		
		public RecordBuilder addValue(JmColumn col, String val) {
			Validate.notNull(col);
			Validate.notNull(val);
			map.put(col.toReference(), new ScriptString(val));
			return this;
		}
		
		public SimpleJmRecord build() {
			return new SimpleJmRecord(map);
		}
		
	}
}
