/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/12/08
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

import org.jiemamy.dddbase.EntityRef;

/**
 * {@link JmNode}のデフォルト実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public final class DbObjectNode extends JmNode {
	
	private final EntityRef<? extends DbObject> coreModelRef;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 * 
	 * @param coreModelRef コアモデルへの参照
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DbObjectNode(EntityRef<? extends DbObject> coreModelRef) {
		this(UUID.randomUUID(), coreModelRef);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @param coreModelRef コアモデルへの参照
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DbObjectNode(UUID id, EntityRef<? extends DbObject> coreModelRef) {
		super(id);
		Validate.notNull(coreModelRef);
		this.coreModelRef = coreModelRef;
	}
	
	@Override
	public DbObjectNode clone() {
		DbObjectNode clone = (DbObjectNode) super.clone();
		return clone;
	}
	
	/**
	 * コアモデルへの参照を取得する。
	 * 
	 * @return コアモデルへの参照。コアが無い場合は{@code null}
	 * @since 0.3
	 */
	public EntityRef<? extends DbObject> getCoreModelRef() {
		return coreModelRef;
	}
	
	@Override
	public EntityRef<? extends DbObjectNode> toReference() {
		return new EntityRef<DbObjectNode>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(sb.length() - 1, ", " + coreModelRef);
		return sb.toString();
	}
}
