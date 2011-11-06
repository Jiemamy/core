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

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.validator.StandardValidator;
import org.jiemamy.validator.Validator;

/**
 * SQL方言の抽象実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
@SuppressWarnings("serial")
public final class GenericDialect extends AbstractDialect {
	
	private static List<Entry> typeEntries = Lists.newArrayList();
	
	static {
		// FORMAT-OFF
		// CHECKSTYLE:OFF
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.INTEGER),
				new HashMap<TypeParameterKey<?>, Necessity>() {{
						put(TypeParameterKey.SERIAL, Necessity.OPTIONAL);
				}}));
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.DECIMAL),
				new HashMap<TypeParameterKey<?>, Necessity>() {{
						put(TypeParameterKey.PRECISION, Necessity.REQUIRED);
						put(TypeParameterKey.SCALE, Necessity.REQUIRED);
				}}));
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.BOOLEAN)));
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.VARCHAR),
				new HashMap<TypeParameterKey<?>, Necessity>() {{
						put(TypeParameterKey.SIZE, Necessity.OPTIONAL);
				}}));
		typeEntries.add(new Entry(new SimpleRawTypeDescriptor(RawTypeCategory.TIMESTAMP),
				new HashMap<TypeParameterKey<?>, Necessity>() {{
						put(TypeParameterKey.WITH_TIMEZONE, Necessity.OPTIONAL);
				}}));
		// CHECKSTYLE:ON
		// FORMAT-ON
	}
	
	private Validator validator = new StandardValidator();
	
	
	/**
	 * インスタンスを生成する。
	 */
	public GenericDialect() {
		super("jdbc:", typeEntries);
	}
	
	public DatabaseMetadataParser getDatabaseMetadataParser() {
		return new DefaultDatabaseMetadataParser(this);
	}
	
	public String getName() {
		return "Generic Dialect";
	}
	
	public SqlEmitter getSqlEmitter() {
		return new DefaultSqlEmitter(this);
	}
	
	@Override
	public Validator getValidator() {
		return validator;
	}
}
