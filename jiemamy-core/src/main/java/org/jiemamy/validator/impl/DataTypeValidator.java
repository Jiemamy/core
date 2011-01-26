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

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.TypeParameterSpec;
import org.jiemamy.dialect.TypeParameterSpec.Necessity;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.datatype.DefaultTypeReference;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.model.table.TableModel;
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
		Collection<Problem> result = Lists.newArrayList();
		Dialect dialect = null;
		try {
			dialect = context.findDialect();
		} catch (ClassNotFoundException e) {
// ignore			
		}
		
		for (TableModel tableModel : context.getTables()) {
			for (ColumnModel columnModel : tableModel.getColumns()) {
				TypeVariant dataType = columnModel.getDataType();
				if (dataType != null) {
					verify(dataType, dialect, tableModel, columnModel, result);
				}
			}
		}
		return result;
	}
	
	/**
	 * チェック制約のexpressionに問題がないかどうか調べる。
	 * 
	 * @param dataType 検査対象のデータ型
	 * @param dialect 
	 * @param context リファレンスリゾルバ
	 * @param columnModel 
	 * @param tableModel 
	 * @param collector 
	 * @return 問題がない場合は{@code true}、そうでない場合は{@code false}
	 */
	private void verify(TypeVariant dataType, Dialect dialect, TableModel tableModel, ColumnModel columnModel,
			Collection<Problem> collector) {
		// TODO いつかは構文解析
//		BuiltinDataType builtinDataType = dataType.toBuiltinDataType(referenceResolver);
//		return builtinDataType != null;
		if (dataType.getTypeReference() == DefaultTypeReference.UNKNOWN) {
			collector.add(new UnknownDataTypeReferenceProblem(tableModel, columnModel));
		}
		if (dialect != null) {
			Collection<TypeParameterSpec> typeParameterSpecs =
					dialect.getTypeParameterSpecs(dataType.getTypeReference());
			for (TypeParameterSpec typeParameterSpec : typeParameterSpecs) {
				if (typeParameterSpec.getNecessity() == Necessity.REQUIRED
						&& dataType.getParam(typeParameterSpec.getKey()) == null) {
					collector.add(new RequiredParameterNotFoundProblem(tableModel, columnModel));
				}
			}
		}
	}
	

	static class RequiredParameterNotFoundProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel 制約式未設定チェック制約が含まれるテーブル
		 * @param columnModel 制約式未設定チェック制約が設定されているカラム
		 */
		public RequiredParameterNotFoundProblem(TableModel tableModel, ColumnModel columnModel) {
			super(columnModel, "E0210");
			setArguments(new Object[] {
				tableModel.getName(),
				columnModel.getName()
			});
		}
	}
	
	static class UnknownDataTypeReferenceProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param tableModel 制約式未設定チェック制約が含まれるテーブル
		 * @param columnModel 制約式未設定チェック制約が設定されているカラム
		 */
		public UnknownDataTypeReferenceProblem(TableModel tableModel, ColumnModel columnModel) {
			super(columnModel, "E0160");
			setArguments(new Object[] {
				tableModel.getName(),
				columnModel.getName()
			});
		}
	}
}
