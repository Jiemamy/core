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
import java.util.Map;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.Necessity;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * カラムが保持するデータ型に対するバリデータ。
 * 
 * @author daisuke
 */
public class DataTypeValidator extends AbstractValidator {
	
	public Collection<Problem> validate(JiemamyContext context) {
		Collection<Problem> problems = Lists.newArrayList();
		Dialect dialect = null;
		try {
			dialect = context.findDialect();
		} catch (ClassNotFoundException e) {
			// ignore
		}
		
		for (JmTable table : context.getTables()) {
			for (JmColumn column : table.getColumns()) {
				DataType dataType = column.getDataType();
				if (dataType != null) {
					verify(dataType, dialect, table, column, problems);
				}
			}
		}
		return problems;
	}
	
	/**
	 * チェック制約のexpressionに問題がないかどうか調べる。
	 * 
	 * @param dataType 検査対象のデータ型
	 * @param dialect {@link Dialect}
	 * @param column データ型が設定してあるカラム
	 * @param table そのカラムが属するテーブル
	 * @param collector {@link Problem}があった場合に追加する {@link Collection}
	 * @throws UnsupportedOperationException {@code collector}が {@link Collection#add(Object)}をサポートしない場合
	 * @throws IllegalArgumentException 引数{@code dataType}または{@code collector}に{@code null}を与えた場合
	 */
	private void verify(DataType dataType, Dialect dialect, JmTable table, JmColumn column,
			Collection<Problem> collector) {
		Validate.notNull(dataType);
		Validate.notNull(collector);
		
		// TODO いつかは構文解析
//		BuiltinDataType builtinDataType = dataType.toBuiltinDataType(referenceResolver);
//		return builtinDataType != null;
		if (dataType.getRawTypeDescriptor() == SimpleRawTypeDescriptor.UNKNOWN) {
			collector.add(new UnknownDataTypeReferenceProblem(table, column));
		}
		if (dialect != null) {
			Map<TypeParameterKey<?>, Necessity> typeParameterSpecs =
					dialect.getTypeParameterSpecs(dataType.getRawTypeDescriptor());
			for (Map.Entry<TypeParameterKey<?>, Necessity> entry : typeParameterSpecs.entrySet()) {
				if (entry.getValue() == Necessity.REQUIRED && dataType.getParam(entry.getKey()) == null) {
					collector.add(new RequiredParameterNotFoundProblem(table, column));
				}
			}
		}
	}
	

	static class RequiredParameterNotFoundProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table 制約式未設定チェック制約が含まれるテーブル
		 * @param column 制約式未設定チェック制約が設定されているカラム
		 */
		RequiredParameterNotFoundProblem(JmTable table, JmColumn column) {
			super(column, "E0210");
			setArguments(new Object[] {
				table.getName(),
				column.getName()
			});
		}
	}
	
	static class UnknownDataTypeReferenceProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param table データ型未設定カラムが含まれるテーブル
		 * @param column データ型が設定されていないカラム
		 */
		public UnknownDataTypeReferenceProblem(JmTable table, JmColumn column) {
			super(column, "E0160");
			setArguments(new Object[] {
				table.getName(),
				column.getName()
			});
		}
	}
}
