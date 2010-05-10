/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/10
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


/**
 * 子 {@link Entity}を持つ {@link Entity}をあらわすインターフェイス。
 * 
 * @version $Id$
 * @author daisuke
 */
public interface CompositEntity extends Entity {
	
	/**
	 * {@link EntityListener}を追加する。
	 * 
	 * @param listener 追加するリスナ
	 */
	void addListener(EntityListener listener);
	
	/**
	 * 子 {@link Entity} の集合を返す。
	 * 
	 * @return 子 {@link Entity} の集合
	 */
	Collection<? extends Entity> getChildren();
	
	/**
	 * {@link EntityListener}を削除する。
	 * 
	 * @param listener 削除するリスナ
	 */
	void removeListener(EntityListener listener);
	
}
