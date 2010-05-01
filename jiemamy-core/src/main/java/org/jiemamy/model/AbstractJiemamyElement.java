/*
 * Copyright 2007-2009 MIYAMOTO Daisuke, jiemamy.org and the Others.
 * Created on 2009/01/02
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
 * {@link JiemamyElement}の骨格実装。
 * 
 * @author daisuke
 */
public abstract class AbstractJiemamyElement implements JiemamyElement {
	
	/** モデルID */
	private final UUID id;
	

	/**
	 * インスタンスを生成する。
	 * @param id モデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractJiemamyElement(UUID id) {
		Validate.notNull(id);
		this.id = id;
	}
	
	public UUID getId() {
		return id;
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
