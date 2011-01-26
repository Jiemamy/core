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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * DatabaseObject名が衝突しているかどうかを調べるバリデータ。
 * 
 * <ul>
 *   <li>{@link JiemamyContext} が保持する全てのエンティティ間で、名前の重複があってはならない。</li>
 * </ul>
 * 
 * @author daisuke
 */
public class DatabaseObjectNameCollisionValidator extends AbstractValidator {
	
	public Collection<? extends Problem> validate(JiemamyContext context) {
		Collection<Problem> result = Lists.newArrayList();
		Map<String, Collection<DatabaseObjectModel>> map = Maps.newHashMap();
		
		for (DatabaseObjectModel dom : context.getDatabaseObjects()) {
			String name = dom.getName();
			if (map.containsKey(name) == false) {
				map.put(name, new ArrayList<DatabaseObjectModel>());
			}
			map.get(name).add(dom);
		}
		
		for (Map.Entry<String, Collection<DatabaseObjectModel>> e : map.entrySet()) {
			if (e.getValue().size() != 1) {
				result.add(new DatabaseObjectNameCollisionProblem(e.getKey(), e.getValue()));
			}
		}
		
		return result;
	}
	

	/**
	 * DatabaseObject名が衝突していることを示す{@link Problem}オブジェクト。
	 * 
	 * @author daisuke
	 */
	static class DatabaseObjectNameCollisionProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param entityName 衝突したエンティティ名
		 * @param entities 衝突したエンティティの集合
		 */
		public DatabaseObjectNameCollisionProblem(String entityName, Collection<DatabaseObjectModel> entities) {
			super(null, "E0070");
			setArguments(new Object[] {
				entityName
			});
		}
	}
}
