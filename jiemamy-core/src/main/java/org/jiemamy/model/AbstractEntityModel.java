/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/11
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

/**
 * {@link Entity}の骨格実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractEntityModel implements Entity {
	
	private final UUID id;
	
	EntityLifecycle lifecycle;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	protected AbstractEntityModel(UUID id) {
		Validate.notNull(id);
		this.id = id;
		lifecycle = EntityLifecycle.FREE;
	}
	
	public void activate() {
		lifecycle.checkActivate();
		lifecycle = EntityLifecycle.ACTIVE;
	}
	
	public void bind() {
		lifecycle.checkBind();
		lifecycle = EntityLifecycle.BOUND;
	}
	
	public void deactivate() {
		lifecycle.checkDeactivate();
		lifecycle = EntityLifecycle.BOUND;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof Entity == false) {
			return false;
		}
		return id.equals(((Entity) obj).getId());
	}
	
	public void free() {
		lifecycle.checkFree();
		lifecycle = EntityLifecycle.FREE;
	}
	
	public EntityLifecycle getEntityLifecycle() {
		return lifecycle;
	}
	
	public UUID getId() {
		return id;
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}
