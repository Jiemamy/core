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
package org.jiemamy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.DatabaseObjectModel;

/**
 * エンティティの依存度を計算するクラス。
 * 
 * @author daisuke
 */
public final class EntityDependencyCalculator {
	
	// TODO refactor: フィールドを使わずにステートレスに実装を
	
	/** 並べ替えの結果 */
	private static List<DatabaseObjectModel> results;
	

	/**
	 * エンティティを、依存度の低い順に並べ替えたリストを取得します。
	 * 
	 * @param context 対象のデータベースモデル
	 * @return エンティティのリスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static List<DatabaseObjectModel> getSortedEntityList(JiemamyContext context) {
		Validate.notNull(context);
		
		results = new ArrayList<DatabaseObjectModel>(context.getDatabaseObjects().size());
		
		for (DatabaseObjectModel dom : context.getDatabaseObjects()) {
			addDependsdentsToResult(dom, context);
			addToResult(dom);
		}
		
		return results;
	}
	
	/**
	 * 対象に依存するentityを結果に追加します。
	 * 
	 * @param dom 依存調査対象となるentity
	 * @param context コンテキスト 
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	private static void addDependsdentsToResult(DatabaseObjectModel dom, JiemamyContext context) {
		Validate.notNull(dom);
		Validate.notNull(context);
		
		Collection<DatabaseObjectModel> superDatabaseObjectsNonRecursive =
				context.findSuperDatabaseObjectsNonRecursive(dom);
		for (DatabaseObjectModel dependent : superDatabaseObjectsNonRecursive) {
			if (dependent.equals(dom) == false) {
				addDependsdentsToResult(dependent, context);
				addToResult(dependent);
			}
		}
	}
	
	/**
	 * entityを結果に追加します。 但し、既に結果に含まれていた場合は何もしない。
	 * 
	 * @param dom 追加するentity
	 */
	private static void addToResult(DatabaseObjectModel dom) {
		assert results != null;
		if (results.contains(dom) == false) {
			results.add(dom);
		}
	}
	
	private EntityDependencyCalculator() {
	}
}
