/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/01/28
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

import org.jiemamy.dddbase.ValueObject;

/**
 * 遅延評価可能性モデル。
 * 
 * @author daisuke
 */
public interface JmDeferrability extends ValueObject {
	
	/**
	 * 遅延評価の初期状態を取得する。
	 * 
	 * @return 遅延評価の初期状態. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	InitiallyCheckTime getInitiallyCheckTime();
	
	/**
	 * 遅延評価可能性を取得する。
	 * 
	 * @return 遅延評価可能性
	 * @since 0.2
	 */
	boolean isDeferrable();
	

	/**
	 * 制約検査を行うデフォルトの時期。
	 * 
	 * @author daisuke
	 * @since 0.2
	 */
	public enum InitiallyCheckTime {
		
		/** 各文の実行後に検査 */
		IMMEDIATE,

		/** トランザクションの終了時にのみ検査 */
		DEFERRED;
	}
	
}
