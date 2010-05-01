/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import org.jiemamy.model.ValueObject;

/**
 * 抽象遅延評価可能性モデル。
 * 
 * @author daisuke
 */
public final class DefaultDeferrability implements Deferrability, ValueObject {
	
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
	public DefaultDeferrability(boolean deferrable, InitiallyCheckTime initiallyCheckTime) {
		this.deferrable = deferrable;
		this.initiallyCheckTime = initiallyCheckTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DefaultDeferrability)) {
			return false;
		}
		DefaultDeferrability other = (DefaultDeferrability) obj;
		if (deferrable != other.deferrable) {
			return false;
		}
		if (initiallyCheckTime == null) {
			if (other.initiallyCheckTime != null) {
				return false;
			}
		} else if (!initiallyCheckTime.equals(other.initiallyCheckTime)) {
			return false;
		}
		return true;
	}
	
	public InitiallyCheckTime getInitiallyCheckTime() {
		return initiallyCheckTime;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (deferrable ? 1231 : 1237);
		result = prime * result + ((initiallyCheckTime == null) ? 0 : initiallyCheckTime.hashCode());
		return result;
	}
	
	public boolean isDeferrable() {
		return deferrable;
	}
	
}
