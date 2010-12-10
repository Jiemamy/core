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

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.jiemamy.IdentifiableEntity;

/**
 * {@link IdentifiableEntity}の骨格実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractIdentifiableEntity extends AbstractEntity implements IdentifiableEntity {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public AbstractIdentifiableEntity(UUID id) {
		super(id);
	}
	
	@Override
	public AbstractIdentifiableEntity clone() {
		return (AbstractIdentifiableEntity) super.clone();
	}
	
	public final UUID getId() {
		return (UUID) id;
	}
	
	public Collection<? extends IdentifiableEntity> getSubEntities() {
		return Collections.emptyList();
	}
}
