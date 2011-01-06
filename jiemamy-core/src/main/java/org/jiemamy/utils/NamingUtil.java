/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/04
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

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.dbo.DefaultDatabaseObjectModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.dbo.DomainModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.model.dbo.ViewModel;
import org.jiemamy.model.dbo.index.IndexModel;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class NamingUtil {
	
	/**
	 * 自動的にエンティティ名を生成し、設定する。
	 * 
	 * @param target 設定対象のエンティティ
	 * @param context ルートモデル
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void autoName(DefaultDatabaseObjectModel target, JiemamyContext context) {
		Validate.notNull(target);
		Validate.notNull(context);
		
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		boolean duplicated = true;
		while (duplicated) {
			if (target instanceof TableModel) {
				sb.append("TABLE_");
			} else if (target instanceof ViewModel) {
				sb.append("VIEW_");
			} else if (target instanceof DomainModel) {
				sb.append("DOMAIN_");
			} else if (target instanceof IndexModel) {
				sb.append("INDEX_");
			} else {
				throw new IllegalStateException("Unknown DatabaseObjectModel: " + target.getClass().toString());
			}
			sb.append(++counter);
			
			duplicated = false;
			for (DatabaseObjectModel dom : context.getDatabaseObjects()) {
				if (sb.toString().equals(dom.getName())) {
					duplicated = true;
					sb.setLength(0);
					break;
				}
			}
		}
		
		target.setName(sb.toString());
	}
	
	private NamingUtil() {
	}
	
}
