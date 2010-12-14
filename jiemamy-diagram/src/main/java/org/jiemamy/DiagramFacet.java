/*
 * Copyright 2010 Jiemamy Project and the others.
 * Created on 2010/12/07
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

import java.util.Set;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.DiagramModel;
import org.jiemamy.xml.DiagramNamespace;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * ER図表現ファセット。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DiagramFacet implements JiemamyFacet {
	
	/** プロバイダ */
	public static final FacetProvider PROVIDER = new FacetProvider() {
		
		public JiemamyFacet getFacet(JiemamyContext context) {
			return new DiagramFacet(context);
		}
		
		public Class<? extends JiemamyFacet> getFacetType() {
			return DiagramFacet.class;
		}
		
	};
	
	private Repository<DiagramModel> repos = new RepositoryImpl<DiagramModel>();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DiagramFacet(JiemamyContext context) {
		Validate.notNull(context);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param clazz
	 * @return
	 */
	public <T extends Entity>Set<T> getEntities(Class<T> clazz) {
		return repos.getEntities(clazz);
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return DiagramNamespace.values();
	}
	
	/**
	 * {@link DiagramModel}を保存する。
	 * 
	 * @param diagram ダイアグラム
	 */
	public void store(DiagramModel diagram) {
		repos.store(diagram);
	}
}
