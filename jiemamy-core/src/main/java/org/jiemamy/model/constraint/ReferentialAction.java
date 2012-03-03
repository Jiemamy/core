/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2012/03/03
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
package org.jiemamy.model.constraint;

/**
 * 連鎖参照整合性制約の設定。
 * 
 * @author daisuke
 */
public enum ReferentialAction {
	
	/** 連鎖的に修正する */
	CASCADE,
	
	/** NULLを設定する */
	SET_NULL,
	
	/** デフォルト値を設定する */
	SET_DEFAULT,
	
	/** 削除/変更を許可しない */
	RESTRICT,
	
	/** 特に何も行わない */
	NO_ACTION;
}