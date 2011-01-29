/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/23
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
package org.jiemamy.test;

import java.util.UUID;

/**
 * テストモデルを組み立てる際、モデルIDをランダム生成するか、毎回固定で生成するかを決める戦略クラス。
 * 
 * @author daisuke
 */
public enum UuidStrategy {
	
	/** 固定 */
	FIXED {
		
		@Override
		UUID get(String name) {
			return UUID.fromString(name);
		}
	},
	
	/** 都度生成 */
	GENERATE {
		
		@Override
		UUID get(String name) {
			return UUID.randomUUID();
		}
	};
	
	abstract UUID get(String name);
}
