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

/**
 * {@link EntityFactory}の抽象実装クラス。
 * 
 * @param <T> 生成する {@link Entity}の型
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractEntityFactory<T extends Entity> implements EntityFactory<T> {
	
	public T build() {
		return build(UUID.randomUUID());
	}
	
}
