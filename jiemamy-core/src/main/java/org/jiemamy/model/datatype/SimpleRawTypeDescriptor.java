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
 * {@link RawTypeDescriptor}のデフォルト実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleRawTypeDescriptor implements RawTypeDescriptor {
	
	/**
	 * {@link RawTypeCategory#UNKNOWN}を表す型記述子
	 * 
	 * <p>他のどの型記述子オブジェクトとも{@link #equals(Object)}がfalseとなるよう、
	 * {@link SimpleRawTypeDescriptor}のインスタンスではない。</p>
	 */
	public static final RawTypeDescriptor UNKNOWN = new RawTypeDescriptor() {
		
		public Collection<String> getAliasTypeNames() {
			return Collections.emptyList();
		}
		
		public RawTypeCategory getCategory() {
			return RawTypeCategory.UNKNOWN;
		}
		
		public String getTypeName() {
			return "UNKNOWN";
		}
		
		@Override
		public String toString() {
			return "UNKNOWN";
		}
	};
	

	private static <T>T notNull(T object) {
		Validate.notNull(object);
		return object;
	}
	

	private String typeName;
	
	private RawTypeCategory category;
	
	private final String[] aliasTypeNames;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleRawTypeDescriptor(RawTypeCategory category) {
		this(category, notNull(category).name());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param category 型カテゴリ
	 * @param typeName 型名の正式名
	 * @param aliasTypeNames エイリアス名
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public SimpleRawTypeDescriptor(RawTypeCategory category, String typeName, String... aliasTypeNames) {
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
		RawTypeDescriptor other = (RawTypeDescriptor) obj;
		if (category != other.getCategory()) {
			return false;
		}
		if (typeName.equals(other.getTypeName()) == false) {
			return false;
		}
		return true;
	}
	
	public Collection<String> getAliasTypeNames() {
		return Lists.newArrayList(aliasTypeNames);
	}
	
	public RawTypeCategory getCategory() {
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
	
	@Override
	public String toString() {
		return typeName + "(" + category + ")";
	}
}
