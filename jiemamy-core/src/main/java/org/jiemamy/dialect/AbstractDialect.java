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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.RawTypeDescriptor;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.validator.StandardValidator;
import org.jiemamy.validator.Validator;

/**
 * SQL方言の抽象実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractDialect implements Dialect {
	
	/** JDBC接続URLの雛形文字列 */
	private final String connectionUriTemplate;
	
	private final List<Entry> typeEntries;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param connectionUriTemplate JDBC接続URLの雛形文字列
	 * @param typeEntries 型エントリのリスト
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public AbstractDialect(String connectionUriTemplate, List<Entry> typeEntries) {
		Validate.notNull(connectionUriTemplate);
		Validate.noNullElements(typeEntries);
		this.connectionUriTemplate = connectionUriTemplate;
		this.typeEntries = ImmutableList.copyOf(typeEntries);
	}
	
	public List<RawTypeDescriptor> getAllRawTypeDescriptors() {
		return Lists.transform(typeEntries, new Function<Entry, RawTypeDescriptor>() {
			
			public RawTypeDescriptor apply(Entry from) {
				return from.descriptor;
			}
		});
	}
	
	public String getConnectionUriTemplate() {
		return connectionUriTemplate;
	}
	
	/**
	 * 型エントリのリストを取得する。
	 * 
	 * @return 型エントリのリスト
	 */
	public List<Entry> getTypeEntries() {
		return Lists.newArrayList(typeEntries);
	}
	
	public Map<TypeParameterKey<?>, Necessity> getTypeParameterSpecs(RawTypeDescriptor reference) {
		RawTypeDescriptor normalized = normalize(reference);
		if (normalized.getCategory() == RawTypeCategory.UNKNOWN) {
			return Collections.emptyMap();
		}
		for (Entry typeEntry : typeEntries) {
			if (typeEntry.descriptor.equals(normalized)) {
				return Maps.newHashMap(typeEntry.typeParameterSpecs);
			}
		}
		throw new Error(reference.toString());
	}
	
	public Validator getValidator() {
		return new StandardValidator();
	}
	
	public RawTypeDescriptor normalize(RawTypeCategory category) {
		return normalize(new SimpleRawTypeDescriptor(category));
	}
	
	public final RawTypeDescriptor normalize(RawTypeDescriptor in) {
		RawTypeDescriptor result = normalize0(in);
		assert result.getCategory() == RawTypeCategory.UNKNOWN || getAllRawTypeDescriptors().contains(result) : result
			.toString();
		return result;
	}
	
	/**
	 * {@link #normalize(RawTypeDescriptor)}の実装メソッド。
	 * 
	 * <p>このメソッドの戻り値は、 {@link #getAllRawTypeDescriptors()}に含まれる値であるか、
	 * または {@link SimpleRawTypeDescriptor#UNKNOWN} でなければならない。</p>
	 * 
	 * @param in 入力型記述子
	 * @return 正規化した型記述子
	 */
	protected RawTypeDescriptor normalize0(RawTypeDescriptor in) {
		for (Entry typeEntry : typeEntries) {
			if (typeEntry.getDescriptor().equals(in)) {
				return in;
			}
		}
		if (in.getCategory() != RawTypeCategory.UNKNOWN) {
			for (Entry typeEntry : typeEntries) {
				if (typeEntry.getDescriptor().getCategory().equals(in.getCategory())) {
					return typeEntry.getDescriptor();
				}
			}
		}
		return SimpleRawTypeDescriptor.UNKNOWN;
	}
	
	
	/**
	 * 型エントリ。
	 * 
	 * @version $Id$
	 * @author daisuke
	 */
	public static final class Entry {
		
		private final RawTypeDescriptor descriptor;
		
		private final Map<TypeParameterKey<?>, Necessity> typeParameterSpecs;
		
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param descriptor {@link RawTypeDescriptor}
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		public Entry(RawTypeDescriptor descriptor) {
			this(descriptor, new HashMap<TypeParameterKey<?>, Necessity>());
		}
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param descriptor {@link RawTypeDescriptor}
		 * @param typeParameterSpecs データ型のパラメータの仕様
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		public Entry(RawTypeDescriptor descriptor, Map<TypeParameterKey<?>, Necessity> typeParameterSpecs) {
			Validate.notNull(descriptor);
			Validate.notNull(typeParameterSpecs);
			this.descriptor = descriptor;
			this.typeParameterSpecs = Maps.newHashMap(typeParameterSpecs);
		}
		
		/**
		 * {@link RawTypeDescriptor}を取得する。
		 * 
		 * @return {@link RawTypeDescriptor}
		 */
		public RawTypeDescriptor getDescriptor() {
			return descriptor;
		}
		
		/**
		 * データ型のパラメータの仕様を取得する。
		 * 
		 * @return データ型のパラメータの仕様
		 */
		public Map<TypeParameterKey<?>, Necessity> getTypeParameterSpecs() {
			return Maps.newHashMap(typeParameterSpecs);
		}
	}
}
