/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;

/**
 * {@link JmLocalKeyConstraint}のデフォルト抽象実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class SimpleJmLocalKeyConstraint extends SimpleJmKeyConstraint implements JmLocalKeyConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmLocalKeyConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmLocalKeyConstraint clone() {
		SimpleJmLocalKeyConstraint clone = (SimpleJmLocalKeyConstraint) super.clone();
		return clone;
	}
	
	@Override
	public EntityRef<? extends SimpleJmLocalKeyConstraint> toReference() {
		return new DefaultEntityRef<SimpleJmLocalKeyConstraint>(this);
	}
}
