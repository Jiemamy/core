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
 * RGBによる色モデル。
 * 
 * <p>イミュータブルなクラスである。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
@SuppressWarnings("serial")
public final class JmColor implements Serializable {
	
	private static final int MAX = 255;
	
	private static final int MIN = 0;
	

	/**
	 * 文字列から{@link JmColor}を取得する。
	 * 
	 * <p>入力は、"#xxxxxx"形式の文字列とする。</p>
	 * 
	 * @param value 文字列
	 * @return {@link JmColor}
	 * @since 0.2
	 */
	public static JmColor parse(String value) {
		if (value == null || value.length() != 7) {
			return null;
		}
		int red = Integer.valueOf(value.substring(1, 3), 16);
		int green = Integer.valueOf(value.substring(3, 5), 16);
		int blue = Integer.valueOf(value.substring(5, 7), 16);
		
		return new JmColor(red, green, blue);
	}
	

	/** 赤 */
	public final int red;
	
	/** 緑 */
	public final int green;
	
	/** 青 */
	public final int blue;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param red 赤（0-255）
	 * @param green 緑（0-255）
	 * @param blue 青（0-255）
	 * @throws IllegalArgumentException 値が範囲外の場合
	 * @since 0.2
	 */
	public JmColor(int red, int green, int blue) {
		validateRange("red", red);
		validateRange("green", green);
		validateRange("blue", blue);
		this.red = red;
		this.green = green;
		this.blue = blue;
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
		final JmColor other = (JmColor) obj;
		if (blue != other.blue) {
			return false;
		}
		if (green != other.green) {
			return false;
		}
		if (red != other.red) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + green;
		result = prime * result + red;
		return result;
	}
	
	/**
	 * #xxxxxx形式の文字列として出力を行う。
	 * 
	 * @since 0.2
	 */
	@Override
	public String toString() {
		return String.format("#%02x%02x%02x", red, green, blue);
	}
	
	/**
	 * 値の範囲をチェックする。
	 * 
	 * @param name 引数名
	 * @param target チェック対象
	 * @throws IllegalArgumentException 値が範囲外の場合
	 * @since 0.2
	 */
	private void validateRange(String name, int target) {
		if (target < MIN || target > MAX) {
			throw new IllegalArgumentException(name + " is out of bound(" + target + ").");
		}
	}
}
