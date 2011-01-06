/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

/**
 * 抽象遅延評価可能性モデル。
 * 
 * @author daisuke
 */
public enum DefaultDeferrabilityModel implements DeferrabilityModel {
	
	/** 遅延評価不可であることを表す */
	INDEFERRABLE(false, null),

	/** 遅延評価可能であることを表す */
	DEFERRABLE(true, null),

	/** 遅延評価可能であるが、基本的に即時評価であることを表す */
	DEFERRABLE_IMMEDIATE(true, InitiallyCheckTime.IMMEDIATE),

	/** 遅延評価可能であり、基本的に遅延評価であることを表す。 */
	DEFERRABLE_DEFERRED(true, InitiallyCheckTime.DEFERRED);
	
	/**
	 * 遅延評価可能性
	 * 
	 * <p>遅延不可の制約は各SQL文の後すぐに検査される。
	 * 遅延可能な制約の検査は、トランザクションの終了時まで遅延させることができる。</p>
	 */
	private final boolean deferrable;
	
	/**
	 * 遅延評価の初期状態
	 * 
	 * <p>制約が遅延可能な場合、この句は制約検査を行うデフォルトの時期を指定する。</p>
	 */
	private final InitiallyCheckTime initiallyCheckTime;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param deferrable 遅延評価可能性
	 * @param initiallyCheckTime 遅延評価の初期状態
	 */
	DefaultDeferrabilityModel(boolean deferrable, InitiallyCheckTime initiallyCheckTime) {
		this.deferrable = deferrable;
		this.initiallyCheckTime = initiallyCheckTime;
	}
	
	public InitiallyCheckTime getInitiallyCheckTime() {
		return initiallyCheckTime;
	}
	
	public boolean isDeferrable() {
		return deferrable;
	}
	
}
