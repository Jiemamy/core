/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.model.geometory;

import java.io.Serializable;

/**
 * 矩形モデル。
 * 
 * <p>イミュータブルなクラスである。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
@SuppressWarnings("serial")
public final class JmRectangle implements Serializable {
	
	/** デフォルトを表す値 */
	public static final int DEFAULT = -1;
	
	private static final int MIN = -1;
	
	/** X座標 */
	public final int x;
	
	/** Y座標 */
	public final int y;
	
	/**
	 * 幅
	 * 
	 * <p>{@code -1}は自動算出を表す。</p>
	 * @since 0.2
	 */
	public final int width;
	
	/**
	 * 高さ
	 * 
	 * <p>{@code -1}は自動算出を表す。</p>
	 * @since 0.2
	 */
	public final int height;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @since 0.2
	 */
	public JmRectangle(int x, int y) {
		this(x, y, DEFAULT, DEFAULT);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param x X座標（x >= 0）
	 * @param y Y座標（y >= 0）
	 * @param width 幅（width >= -1） {@code -1}は自動算出を表す。
	 * @param height 高さ（height >= -1） {@code -1}は自動算出を表す。
	 * @throws IllegalArgumentException 引数の値が指定範囲外の場合
	 * @since 0.2
	 */
	public JmRectangle(int x, int y, int width, int height) {
		this.width = validateAndNormalizeRange("width", width);
		this.height = validateAndNormalizeRange("width", height);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JmRectangle other = (JmRectangle) obj;
		if (height != other.height) {
			return false;
		}
		if (width != other.width) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	@Override
	public String toString() {
		return "Rect(" + x + ", " + y + ": " + width + ", " + height + ")";
	}
	
	/**
	 * 値の範囲をチェックの後、正規化する。
	 * 
	 * @param name 引数名
	 * @param target チェック対象
	 * @return 正規化された値
	 * @throws IllegalArgumentException 値が範囲外の場合
	 */
	private int validateAndNormalizeRange(String name, int target) {
		if (target < MIN) {
			throw new IllegalArgumentException(name + " is out of bound(" + target + ").");
		}
		if (target >= 0 && target < 10) {
			return 10;
		}
		return target;
	}
	
}
