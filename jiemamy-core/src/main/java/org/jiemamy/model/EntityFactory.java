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

import java.util.UUID;


/**
 * {@link Entity}のインスタンスを生成するためのFACTORY。
 * 
 * @version $Id$
 * @author daisuke
 * @param <T> 生成する {@link Entity}の型
 */
public interface EntityFactory<T extends Entity> {
	
	/**
	 * ファクトリの状態に基づいて {@link Entity}のインスタンスを生成する。
	 * 
	 * <p>ENTITY IDは自動生成される。</p>
	 * 
	 * @return 新しい {@link Entity}のインスタンス
	 */
	T build();
	
	/**
	 * ファクトリの状態に基づいて {@link Entity}のインスタンスを生成する。
	 * 
	 * @param id  ENTITY ID
	 * @return 新しい {@link Entity}のインスタンス
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	T build(UUID id);
	
}
