/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dialect.ReservedWordsChecker;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * 全モデルをトラバースするバリデータの骨格実装。
 * 
 * @author daisuke
 */
public abstract class AbstractIdentifierValidator extends AbstractValidator {
	
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
		Collection<Problem> result = Lists.newArrayList();
		
		for (DatabaseObjectModel dbo : context.getDatabaseObjects()) {
			collectProblem(dbo, dbo.getName(), result, false);
			if (dbo instanceof TableModel) {
				TableModel tableModel = (TableModel) dbo;
				for (ColumnModel columnModel : tableModel.getColumns()) {
					collectProblem(columnModel, columnModel.getName(), result, false);
				}
				
				for (ConstraintModel constraint : tableModel.getConstraints()) {
					collectProblem(constraint, constraint.getName(), result, true);
				}
			}
		}
		
		return result;
	}
	
	int collectProblem(Entity entity, String name, Collection<Problem> result, boolean emptyValid) {
		int before = result.size();
		if (StringUtils.isEmpty(name)) {
			if (emptyValid) {
				return 0;
			} else {
				result.add(new EmptyNameProblem(entity));
				return 1;
			}
		}
		
		if (identifierPattern.matcher(name).matches() == false) {
			result.add(new InvalidNameProblem(entity, name, identifierPattern));
		}
		if (reservedWords.isReserved(name)) {
			result.add(new ReservedWordProblem(entity, name));
		}
		return result.size() - before;
	}
	

	static class EmptyNameProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param target 
		 */
		public EmptyNameProblem(Entity target) {
			super(target, "E0011");
		}
	}
	
	static class InvalidNameProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param target 不正な名前を持つエンティティ
		 * @param name 識別子名
		 * @param pattern 識別子が満たすべき正規表現パターン
		 */
		public InvalidNameProblem(Entity target, String name, Pattern pattern) {
			super(target, "E0010");
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
		 * @param target 不正な名前を持つエンティティ
		 * @param name 識別子名
		 */
		public ReservedWordProblem(Entity target, String name) {
			super(target, "E0020");
			setArguments(new Object[] {
				name
			});
		}
	}
	
}
