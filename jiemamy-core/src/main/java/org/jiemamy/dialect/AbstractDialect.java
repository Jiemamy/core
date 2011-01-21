/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy.dialect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultTypeReference;
import org.jiemamy.model.datatype.TypeReference;
import org.jiemamy.validator.AllValidator;
import org.jiemamy.validator.Validator;

/**
 * SQL方言の抽象実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractDialect implements Dialect {
	
	private final String connectionUriTemplate;
	
	protected final List<Entry> typeEntries;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param connectionUriTemplate
	 */
	public AbstractDialect(String connectionUriTemplate, List<Entry> typeEntries) {
		Validate.notNull(connectionUriTemplate);
		Validate.noNullElements(typeEntries);
		this.connectionUriTemplate = connectionUriTemplate;
		this.typeEntries = ImmutableList.copyOf(typeEntries);
	}
	
	public List<TypeReference> getAllTypeReferences() {
		return Lists.transform(typeEntries, new Function<Entry, TypeReference>() {
			
			public TypeReference apply(Entry from) {
				return from.descriptor;
			}
		});
	}
	
	public String getConnectionUriTemplate() {
		return connectionUriTemplate;
	}
	
	public List<Entry> getTypeEntries() {
		return Lists.newArrayList(typeEntries);
	}
	
	public Collection<TypeParameterSpec> getTypeParameterSpecs(TypeReference reference) {
		TypeReference normalized = normalize(reference);
		if (normalized.getCategory() == DataTypeCategory.UNKNOWN) {
			return Collections.emptyList();
		}
		for (Entry typeEntry : typeEntries) {
			if (typeEntry.descriptor.equals(normalized)) {
				return Lists.newArrayList(typeEntry.typeParameterSpecs);
			}
		}
		throw new Error(reference.toString());
	}
	
	public Validator getValidator() {
		return new AllValidator();
	}
	
	public final TypeReference normalize(String typeName) {
		for (Entry typeEntry : typeEntries) {
			if (typeEntry.descriptor.matches(typeName)) {
				return typeEntry.descriptor;
			}
		}
		return DefaultTypeReference.UNKNOWN;
	}
	
	public final TypeReference normalize(TypeReference in) {
		TypeReference result = normalize0(in);
		assert result.getCategory() == DataTypeCategory.UNKNOWN || getAllTypeReferences().contains(result) : result
			.toString();
		return result;
	}
	
	public String toString(TypeReference typeReference) {
		return normalize(typeReference).getTypeName();
	}
	
	protected TypeReference normalize0(TypeReference in) {
		for (Entry typeEntry : typeEntries) {
			if (typeEntry.getDescriptor().equals(in)) {
				return in;
			}
		}
		if (in.getCategory() != DataTypeCategory.UNKNOWN) {
			for (Entry typeEntry : typeEntries) {
				if (typeEntry.getDescriptor().getCategory().equals(in.getCategory())) {
					return typeEntry.getDescriptor();
				}
			}
		}
		return DefaultTypeReference.UNKNOWN;
	}
	

	public static class Entry {
		
		private final TypeReference descriptor;
		
		private final Collection<TypeParameterSpec> typeParameterSpecs;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param descriptor
		 */
		public Entry(TypeReference descriptor) {
			this(descriptor, new ArrayList<TypeParameterSpec>());
		}
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param descriptor
		 * @param typeParameterSpecs
		 */
		public Entry(TypeReference descriptor, Collection<TypeParameterSpec> typeParameterSpecs) {
			Validate.notNull(descriptor);
			Validate.notNull(typeParameterSpecs);
			this.descriptor = descriptor;
			this.typeParameterSpecs = Lists.newArrayList(typeParameterSpecs);
		}
		
		/**
		 * somethingを取得する。 TODO for daisuke
		 * @return the descriptor
		 */
		public TypeReference getDescriptor() {
			return descriptor;
		}
		
		/**
		 * somethingを取得する。 TODO for daisuke
		 * @return the typeParameterSpecs
		 */
		public Collection<TypeParameterSpec> getTypeParameterSpecs() {
			return typeParameterSpecs;
		}
		
	}
}
