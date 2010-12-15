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

import java.util.Collection;
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
public interface Repository<T extends Entity> extends Cloneable {
	
	/**
	 * リポジトリのクローンを取得する。
	 * 
	 * <p>リポジトリのプロパティとして保持する可変オブジェクト(主に{@link Collection})や、
	 * {@link Collection}の要素(主に管理エンティティ)も可変オブジェクトである場合は、その要素もクローンする。</p>
	 * 
	 * @return clone クローン
	 * @since 0.3
	 */
	Repository<T> clone();
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ref
	 * @return
	 * @throws EntityNotFoundException
	 */
	T delete(EntityRef<? extends T> ref);
	
	Set<T> getEntitiesAsSet();
	
	List<T> getEntitiesAsList();
	
	/**
	 * TODO for daisuke
	 * 
	 * @param <T>
	 * @param ref
	 * @return
	 * @throws EntityNotFoundException
	 */
	<E extends Entity>E resolve(EntityRef<E> ref);
	
	/**
	 * TODO for daisuke
	 * 
	 * @param id
	 * @return
	 * @throws EntityNotFoundException
	 */
	Entity resolve(UUID id);
	
	void store(T entity);
}
