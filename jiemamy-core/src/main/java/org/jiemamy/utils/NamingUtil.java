/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
import org.jiemamy.model.DbObject;
import org.jiemamy.model.SimpleDbObject;
import org.jiemamy.model.domain.JmDomain;
import org.jiemamy.model.index.JmIndex;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.view.JmView;

/**
 * {@link DbObject}名に関するユーティリティクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class NamingUtil {
	
	/**
	 * 自動的に{@link DbObject}名を生成し、設定する。
	 * 
	 * @param target 名前設定対象の{@link DbObject}
	 * @param context {@link JiemamyContext}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 名付けに対応していない {@link DbObject} を与えた場合
	 */
	public static void autoName(SimpleDbObject target, JiemamyContext context) {
		Validate.notNull(target);
		Validate.notNull(context);
		
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		boolean duplicated = true;
		while (duplicated) {
			if (target instanceof JmTable) {
				sb.append("TABLE_");
			} else if (target instanceof JmView) {
				sb.append("VIEW_");
			} else if (target instanceof JmDomain) {
				sb.append("DOMAIN_");
			} else if (target instanceof JmIndex) {
				sb.append("INDEX_");
			} else {
				throw new IllegalArgumentException("Unknown target: " + target.getClass().toString());
			}
			sb.append(++counter);
			
			duplicated = false;
			for (DbObject dbObject : context.getDbObjects()) {
				if (sb.toString().equals(dbObject.getName())) {
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
