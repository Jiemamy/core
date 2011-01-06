/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/01/21
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
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dialect.ReservedWordsChecker;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * 全モデルをトラバースするバリデータの骨格実装。
 * 
 * @author daisuke
 */
public abstract class AbstractIdentifierValidator extends AbstractValidator {
	
	Collection<Problem> result = Lists.newArrayList();
	
	/** 識別子としての妥当性を判断する正規表現パターン */
	private Pattern identifierPattern;
	
	/** 予約語チェッカー */
	private ReservedWordsChecker reservedWords;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param patternString 識別子としての妥当性を判断する正規表現パターン文字列
	 * @param reservedWords 予約語プロバイダ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractIdentifierValidator(String patternString, ReservedWordsChecker reservedWords) {
		Validate.notNull(patternString);
		Validate.notNull(reservedWords);
		
		identifierPattern = Pattern.compile(patternString);
		this.reservedWords = reservedWords;
	}
	
	public Collection<? extends Problem> validate(JiemamyContext context) {
		result = Lists.newArrayList();
		
		for (DatabaseObjectModel dbo : context.getDatabaseObjects()) {
			isValid(dbo.getName());
			if (dbo instanceof TableModel) {
				TableModel tableModel = (TableModel) dbo;
				for (ColumnModel columnModel : tableModel.getColumns()) {
					isValid(columnModel.getName());
				}
				
				for (ConstraintModel constraint : tableModel.getConstraints()) {
					isValid(constraint.getName());
				}
			}
		}
		
		return result;
	}
	
	boolean isValid(String name) {
		boolean valid = true;
		if (name == null) {
			return valid;
		}
		if (identifierPattern.matcher(name).matches() == false) {
			result.add(new InvalidNameProblem(name, identifierPattern));
			valid = false;
		}
		if (reservedWords.isReserved(name)) {
			result.add(new ReservedWordProblem(name));
			valid = false;
		}
		return valid;
	}
	

	static class InvalidNameProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param name 識別子名
		 * @param pattern 識別子が満たすべき正規表現パターン
		 */
		public InvalidNameProblem(String name, Pattern pattern) {
			super("E0010");
			setArguments(new Object[] {
				name,
				pattern.pattern()
			});
		}
	}
	
	static class ReservedWordProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param name 識別子名
		 */
		public ReservedWordProblem(String name) {
			super("E0020");
			setArguments(new Object[] {
				name
			});
		}
	}
	
}
