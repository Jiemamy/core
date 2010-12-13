/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/13
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

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultTypeVariant implements TypeVariant {
	
	/**
	 * TODO for daisuke
	 * 
	 * @param integer
	 */
	public static DefaultTypeVariant of(DataTypeCategory category) {
		return new DefaultTypeVariant(category, category.name(), new HashSet<TypeParameter<?>>());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param varchar
	 * @param size
	 * @return
	 */
	public static DefaultTypeVariant of(DataTypeCategory category, TypeParameter<?>... params) {
		Set<TypeParameter<?>> p = Sets.newHashSetWithExpectedSize(params.length);
		for (TypeParameter<?> typeParameter : params) {
			p.add(typeParameter);
		}
		return new DefaultTypeVariant(category, category.name(), p);
		
	}
	

	private DataTypeCategory category;
	
	private String typeName;
	
	private Set<TypeParameter<?>> params;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param category
	 * @param typeName
	 * @param params
	 */
	public DefaultTypeVariant(DataTypeCategory category, String typeName, Set<TypeParameter<?>> params) {
		Validate.notNull(category);
		Validate.notNull(typeName);
		Validate.noNullElements(params);
		this.category = category;
		this.typeName = typeName;
		this.params = Sets.newHashSet(params);
	}
	
	public DataTypeCategory getCategory() {
		return category;
	}
	
	public Set<TypeParameter<?>> getParams() {
		return Sets.newHashSet(params);
	}
	
	public String getTypeName() {
		return typeName;
	}
	
}
