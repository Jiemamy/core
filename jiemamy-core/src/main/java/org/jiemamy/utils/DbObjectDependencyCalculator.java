/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/09/10
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
package org.jiemamy.utils;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.ModelConsistencyException;
import org.jiemamy.validator.Validator;
import org.jiemamy.validator.impl.CyclicForeignReferenceValidator;

/**
 * {@link DbObject}の依存度を計算するクラス。
 * 
 * @author daisuke
 */
public final class DbObjectDependencyCalculator {
	
	// THINK コンパレータにできないかな
	
	private static final Validator VALIDATOR = new CyclicForeignReferenceValidator();
	

	/**
	 * {@link DbObject}を、依存度の低い順に並べ替えたリストを取得します。
	 * 
	 * @param context 対象の{@link JiemamyContext}
	 * @return {@link DbObject}の{@link List}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws ModelConsistencyException {@link DbObject}同士に循環依存がある場合
	 */
	public static List<DbObject> getSortedEntityList(JiemamyContext context) {
		Validate.notNull(context);
		
		if (VALIDATOR.validate(context).size() > 0) {
			throw new ModelConsistencyException();
		}
		List<DbObject> result = Lists.newArrayListWithCapacity(context.getDbObjects().size());
		
		for (DbObject dbObject : context.getDbObjects()) {
			addDependsdentsToResult(dbObject, context, result);
			addToResult(dbObject, result);
		}
		
		return result;
	}
	
	/**
	 * 対象に依存する{@link DbObject}を結果に追加します。
	 * 
	 * @param dbObject 依存調査対象となる{@link DbObject}
	 * @param context コンテキスト 
	 * @param result 結果収集用{@link List}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	private static void addDependsdentsToResult(DbObject dbObject, JiemamyContext context, List<DbObject> result) {
		Collection<DbObject> dependents = context.findSuperDbObjectsNonRecursive(dbObject);
		for (DbObject dependent : dependents) {
			if (dependent.equals(dbObject) == false) {
				addDependsdentsToResult(dependent, context, result);
				addToResult(dependent, result);
			}
		}
	}
	
	/**
	 * {@code dbObject}を結果に追加する。
	 * 
	 * <p>但し、既に結果に含まれていた場合は何もしない。</p>
	 * 
	 * @param dbObject 追加する{@link DbObject}
	 */
	private static void addToResult(DbObject dbObject, List<DbObject> result) {
		if (result.contains(dbObject) == false) {
			result.add(dbObject);
		}
	}
	
	private DbObjectDependencyCalculator() {
	}
}
