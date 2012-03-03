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

import org.jiemamy.dddbase.EntityRef;

/**
 * 値に対する単純な制約を表す制約モデル。
 * 
 * <p>主にNOT NULL制約や、CHECK制約を表す。</p>
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public abstract class JmValueConstraint extends JmConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmValueConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public JmValueConstraint clone() {
		JmValueConstraint clone = (JmValueConstraint) super.clone();
		return clone;
	}
	
	@Override
	public EntityRef<? extends JmValueConstraint> toReference() {
		return new EntityRef<JmValueConstraint>(this);
	}
}
