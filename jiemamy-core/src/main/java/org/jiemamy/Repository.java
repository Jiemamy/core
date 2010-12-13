/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/13
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
package org.jiemamy;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * TODO for daisuke
 * 
 * @param <T> 管理するエンティティの型
 * @version $Id$
 * @author daisuke
 */
interface Repository<T extends Entity> {
	
	void delete(EntityRef<? extends T> ref);
	
	<T2 extends Entity>Set<T2> getEntities(Class<T2> clazz);
	
	<T2 extends Entity>List<T2> getEntitiesAsList(Class<T2> clazz);
	
	<T2 extends Entity>T2 resolve(EntityRef<T2> ref);
	
	Entity resolve(UUID id);
	
	void store(T entity);
	
}
