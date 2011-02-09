/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy.dialect;

import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dialect.TypeParameterSpec.Necessity;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.validator.AllValidator;
import org.jiemamy.validator.Validator;

/**
 * テスト用のモックSQL方言実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class MockDialect extends AbstractDialect {
	
	private static List<Entry> typeEntries;
	
	static {
		typeEntries = Lists.newArrayList();
		// FORMAT-OFF
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.INTEGER), Arrays.asList(
				TypeParameterSpec.of(TypeParameterKey.SERIAL, Necessity.OPTIONAL)
		)));
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.DECIMAL), Arrays.asList(
				TypeParameterSpec.of(TypeParameterKey.PRECISION, Necessity.REQUIRED),
				TypeParameterSpec.of(TypeParameterKey.SCALE, Necessity.REQUIRED)
		)));
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.BOOLEAN)));
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.VARCHAR), Arrays.asList(
				TypeParameterSpec.of(TypeParameterKey.SIZE, Necessity.OPTIONAL)
		)));
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.TIMESTAMP), Arrays.asList(
				TypeParameterSpec.of(TypeParameterKey.WITH_TIMEZONE, Necessity.OPTIONAL)
		)));
		// FORMAT-ON
	}
	
	private Validator validator = new AllValidator();
	

	/**
	 * インスタンスを生成する。
	 */
	public MockDialect() {
		super("jdbc:dummy:foobar", typeEntries);
	}
	
	public DatabaseMetadataParser getDatabaseMetadataParser() {
		return new DatabaseMetadataParser() {
			
			public void parseMetadata(JiemamyContext context, DatabaseMetaData meta, ParseMetadataConfig config) {
				// do nothing
			}
		};
	}
	
	public String getName() {
		return "Mock Dialect (for test only. DO NOT USE.)";
	}
	
	public SqlEmitter getSqlEmitter() {
		return new SqlEmitter() {
			
			public List<SqlStatement> emit(JiemamyContext context, EmitConfig config) {
				// do nothing
				return Collections.emptyList();
			}
		};
	}
	
	@Override
	public Validator getValidator() {
		return validator;
	}
}
