/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import org.jiemamy.Entity;
import org.jiemamy.EntityRef;

/**
 * 参照オブジェクトの骨格実装。
 * 
 * @param <T> 参照対象オブジェクトの型
 * @version $Id$
 * @author daisuke
 */
public class DefaultEntityRef<T extends Entity> implements EntityRef<T> {
	
	private final UUID referentId;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param referent 定義オブジェクト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultEntityRef(T referent) {
		Validate.notNull(referent);
		referentId = referent.getId();
		assert referentId != null;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param referentId 参照先のモデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultEntityRef(UUID referentId) {
		Validate.notNull(referentId);
		this.referentId = referentId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof EntityRef<?> == false) {
			return false;
		}
		return referentId.equals(((EntityRef<?>) obj).getReferentId());
	}
	
	public UUID getReferentId() {
		return referentId;
	}
	
	@Override
	public int hashCode() {
		return referentId.hashCode();
	}
	
	public boolean isReferenceOf(Entity target) {
		return referentId.equals(target.getId());
	}
	
	@Override
	public String toString() {
		return "EntitiyRef(" + referentId + ")";
	}
}
