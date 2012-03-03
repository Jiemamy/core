/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import org.jiemamy.JiemamyError;
import org.jiemamy.utils.sql.metadata.KeyMeta.Deferrability;

/**
 * 遅延評価可能性モデル。
 * 
 * @author daisuke
 */
public enum JmDeferrability {
	
	/** 遅延評価不可であることを表す */
	INDEFERRABLE(false, null),
	
	/** 遅延評価可能であることを表す */
	DEFERRABLE(true, null),
	
	/** 遅延評価可能であるが、基本的に即時評価であることを表す */
	DEFERRABLE_IMMEDIATE(true, InitiallyCheckTime.IMMEDIATE),
	
	/** 遅延評価可能であり、基本的に遅延評価であることを表す。 */
	DEFERRABLE_DEFERRED(true, InitiallyCheckTime.DEFERRED);
	
	/**
	 * {@link Deferrability}の値から {@link JmDeferrability}の値を引き当てる。
	 * 
	 * @param deferrability {@link Deferrability}の値
	 * @return 対応する {@link JmDeferrability}の値、見つからなかった場合は{@code null}
	 */
	public static JmDeferrability fromDeferrability(Deferrability deferrability) {
		if (deferrability == Deferrability.INITIALLY_DEFERRED) {
			return DEFERRABLE_DEFERRED;
		} else if (deferrability == Deferrability.INITIALLY_IMMEDIATE) {
			return DEFERRABLE_IMMEDIATE;
		} else if (deferrability == Deferrability.NOT_DEFERRABLE) {
			return INDEFERRABLE;
		} else {
			return null;
		}
	}
	
	/**
	 * {@link Deferrability}の値から {@link JmDeferrability}の値を引き当てる。
	 * 
	 * @param deferrable 遅延可能な場合は{@code true}、そうでない場合は{@code false}
	 * @param initiallyCheckTime 制約検査を行うデフォルトの時期
	 * @return 対応する {@link JmDeferrability}の値
	 */
	public static JmDeferrability valueOf(boolean deferrable, InitiallyCheckTime initiallyCheckTime) {
		if (deferrable) {
			if (initiallyCheckTime == null) {
				return DEFERRABLE;
			} else if (initiallyCheckTime == InitiallyCheckTime.IMMEDIATE) {
				return DEFERRABLE_IMMEDIATE;
			} else if (initiallyCheckTime == InitiallyCheckTime.DEFERRED) {
				return DEFERRABLE_DEFERRED;
			} else {
				throw new JiemamyError("unknown value: " + initiallyCheckTime);
			}
		}
		return INDEFERRABLE;
	}
	
	
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
	JmDeferrability(boolean deferrable, InitiallyCheckTime initiallyCheckTime) {
		this.deferrable = deferrable;
		this.initiallyCheckTime = initiallyCheckTime;
	}
	
	/**
	 * 遅延評価の初期状態を取得する。
	 * 
	 * @return 遅延評価の初期状態. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public InitiallyCheckTime getInitiallyCheckTime() {
		return initiallyCheckTime;
	}
	
	/**
	 * 遅延評価可能性を取得する。
	 * 
	 * @return 遅延評価可能性
	 * @since 0.3
	 */
	public boolean isDeferrable() {
		return deferrable;
	}
	
}
