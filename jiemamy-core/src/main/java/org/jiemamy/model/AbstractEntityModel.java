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

import org.jiemamy.model.Repository.InternalCredential;

/**
 * {@link Entity}の骨格実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractEntityModel implements Entity {
	
	private final UUID id;
	
	private boolean alive;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	protected AbstractEntityModel(UUID id) {
		Validate.notNull(id);
		this.id = id;
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
	
	public void initiate(InternalCredential key) {
		if (key == null) {
			throw new AssertionError();
		}
		alive = true;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void kill(InternalCredential key) {
		if (key == null) {
			throw new AssertionError();
		}
		alive = false;
	}
	
}
