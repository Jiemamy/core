/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/17
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
package org.jiemamy.model.constraint;

import java.util.UUID;

import org.jiemamy.dddbase.DefaultUUIDEntityRef;
import org.jiemamy.dddbase.UUIDEntityRef;

/**
 * {@link JmValueConstraint}のデフォルト抽象実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class SimpleJmValueConstraint extends SimpleJmConstraint implements JmValueConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmValueConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmValueConstraint clone() {
		SimpleJmValueConstraint clone = (SimpleJmValueConstraint) super.clone();
		return clone;
	}
	
	@Override
	public UUIDEntityRef<? extends SimpleJmValueConstraint> toReference() {
		return new DefaultUUIDEntityRef<SimpleJmValueConstraint>(this);
	}
}
