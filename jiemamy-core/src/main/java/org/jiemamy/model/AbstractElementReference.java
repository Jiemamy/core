/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/19
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
package org.jiemamy.model;

import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 参照オブジェクトの骨格実装。
 * 
 * @param <T> 参照対象オブジェクトの型
 * @author daisuke
 */
public abstract class AbstractElementReference<T extends JiemamyElement> implements ElementReference<T> {
	
	private final UUID referenceId;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param decl 定義オブジェクト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractElementReference(T decl) {
		Validate.notNull(decl);
		referenceId = decl.getId();
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param referenceId 参照先のモデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractElementReference(UUID referenceId) {
		Validate.notNull(referenceId);
		
		this.referenceId = referenceId;
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
		AbstractElementReference<?> other = (AbstractElementReference<?>) obj;
		if (referenceId == null) {
			if (other.referenceId != null) {
				return false;
			}
		} else if (referenceId.equals(other.referenceId) == false) {
			return false;
		}
		return true;
	}
	
	public UUID getReferenceId() {
		return referenceId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((referenceId == null) ? 0 : referenceId.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		ReflectionToStringBuilder toStringBuilder =
				new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		toStringBuilder.setExcludeFieldNames(new String[] {
			"jiemamy"
		});
		
		return toStringBuilder.toString();
	}
}
