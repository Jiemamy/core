/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/11
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
package org.jiemamy.model.datatype;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

/**
 * {@link TypeReference}のデフォルト実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultTypeReference implements TypeReference {
	
	public static final TypeReference UNKNOWN = new TypeReference() {
		
		public Collection<String> getAliasTypeNames() {
			return Collections.emptyList();
		}
		
		public DataTypeCategory getCategory() {
			return DataTypeCategory.UNKNOWN;
		}
		
		public String getTypeName() {
			return "UNKNOWN";
		}
		
		public boolean matches(String typeName) {
			return false;
		}
		
		@Override
		public String toString() {
			return "UNKNOWN";
		}
	};
	
	private String typeName;
	
	private DataTypeCategory category;
	
	private final String[] aliasTypeNames;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @throws NullPointerException 引数に{@code null}を与えた場合
	 */
	public DefaultTypeReference(DataTypeCategory category) {
		this(category, category.name());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @param typeName 型名の正式名
	 * @param aliasTypeNames エイリアス名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultTypeReference(DataTypeCategory category, String typeName, String... aliasTypeNames) {
		Validate.notNull(category);
		Validate.notNull(typeName);
		Validate.noNullElements(aliasTypeNames);
		this.category = category;
		this.typeName = typeName;
		this.aliasTypeNames = aliasTypeNames;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DefaultTypeReference other = (DefaultTypeReference) obj;
		if (category != other.category) {
			return false;
		}
		if (!typeName.equals(other.typeName)) {
			return false;
		}
		return true;
	}
	
	public Collection<String> getAliasTypeNames() {
		return Lists.newArrayList(aliasTypeNames);
	}
	
	public DataTypeCategory getCategory() {
		return category;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category.hashCode();
		result = prime * result + typeName.hashCode();
		return result;
	}
	
	public boolean matches(String typeName) {
		return this.typeName.equals(typeName) || getAliasTypeNames().contains(typeName);
	}
	
	@Override
	public String toString() {
		return typeName + "(" + category + ")";
	}
}
