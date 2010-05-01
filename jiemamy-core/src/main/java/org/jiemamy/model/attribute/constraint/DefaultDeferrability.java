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

import java.util.UUID;

import org.jiemamy.model.AbstractJiemamyElement;

/**
 * 抽象遅延評価可能性モデル。
 * 
 * @author daisuke
 */
public class DefaultDeferrability extends AbstractJiemamyElement implements Deferrability {
	
	/**
	 * 遅延評価可能性
	 * 
	 * <p>遅延不可の制約は各SQL文の後すぐに検査される。
	 * 遅延可能な制約の検査は、トランザクションの終了時まで遅延させることができる。</p>
	 */
	private boolean deferrable;
	
	/**
	 * 遅延評価の初期状態
	 * 
	 * <p>制約が遅延可能な場合、この句は制約検査を行うデフォルトの時期を指定する。</p>
	 */
	private InitiallyCheckTime initiallyCheckTime;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 */
	public DefaultDeferrability(UUID id) {
		super(id);
	}
	
	public InitiallyCheckTime getInitiallyCheckTime() {
		if (isDeferrable() == false) {
			return null;
		}
		return initiallyCheckTime;
	}
	
	public boolean isDeferrable() {
		return deferrable;
	}
	
	public void setDeferrable(boolean deferrable) {
		this.deferrable = deferrable;
	}
	
	/**
	 * 
	 * 遅延評価の初期状態を設定する
	 * 
	 * @param initiallyCheckTime 遅延評価の初期状態
	 * @since 0.3
	 */
	public void setInitiallyCheckTime(InitiallyCheckTime initiallyCheckTime) {
		this.initiallyCheckTime = initiallyCheckTime;
	}
	
}
