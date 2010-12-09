/*
 * Copyright 2010 Jiemamy Project and the others.
 * Created on 2010/12/08
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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.EmitConfig;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.xml.JiemamyNamespace;
import org.jiemamy.xml.SqlNamespace;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public class SqlFacet implements JiemamyFacet {
	
	public static final FacetProvider PROVIDER = new FacetProvider() {
		
		public JiemamyFacet getFacet(JiemamyContext context) {
			return new SqlFacet(context);
		}
		
		public Class<? extends JiemamyFacet> getFacetType() {
			return SqlFacet.class;
		}
		
	};
	
	private final JiemamyContext context;
	

	public SqlFacet(JiemamyContext context) {
		Validate.notNull(context);
		this.context = context;
	}
	
	/**
	 * {@link JiemamyContext}からSQL文のリストを生成する。
	 * 
	 * @param context {@link JiemamyContext}
	 * @param config 設定オブジェクト
	 * @return SQL文のリスト
	 * @throws UnsupportedOperationException SQL文の出力をサポートしていない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	public List<SqlStatement> emitStatements(Dialect dialect, EmitConfig config) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <T extends Entity>Set<T> getEntities(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return SqlNamespace.values();
	}
	
}
