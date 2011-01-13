/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/09/26
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

import org.apache.commons.lang.Validate;

/**
 * 座標操作ユーティリティクラス。
 * @author daisuke
 */
public final class JmPointUtil {
	
	/**
	 * 2点の差を求める。
	 * 
	 * @param from 基準点
	 * @param to 対象点
	 * @return 差
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static JmPoint delta(JmPoint from, JmPoint to) {
		Validate.notNull(from);
		Validate.notNull(to);
		return new JmPoint(from.x - to.x, from.y - to.y);
	}
	
	/**
	 * 矩形の配置点（左上）2点の差を求める。
	 * 
	 * @param from 基準矩形
	 * @param to 対象矩形
	 * @return 差
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static JmPoint delta(JmRectangle from, JmRectangle to) {
		Validate.notNull(from);
		Validate.notNull(to);
		return new JmPoint(from.x - to.x, from.y - to.y);
	}
	
	/**
	 * 位置を負方向に移動させる。
	 * 
	 * @param target 移動対象
	 * @param x X軸移動量
	 * @param y Y軸移動量
	 * @return 移動結果
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static JmPoint shiftNegative(JmPoint target, int x, int y) {
		Validate.notNull(target);
		return new JmPoint(target.x - x, target.y - y);
	}
	
	/**
	 * 位置を負方向に移動させる。
	 * 
	 * @param target 移動対象
	 * @param delta 移動量
	 * @return 移動結果
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static JmPoint shiftNegative(JmPoint target, JmPoint delta) {
		Validate.notNull(target);
		return new JmPoint(target.x - delta.x, target.y - delta.y);
	}
	
	/**
	 * 位置を正方向に移動させる。
	 * 
	 * @param target 移動対象
	 * @param x X軸移動量
	 * @param y Y軸移動量
	 * @return 移動結果
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static JmPoint shiftPositive(JmPoint target, int x, int y) {
		Validate.notNull(target);
		return new JmPoint(target.x + x, target.y + y);
	}
	
	/**
	 * 位置を正方向に移動させる。
	 * 
	 * @param target 移動対象
	 * @param delta 移動量
	 * @return 移動結果
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static JmPoint shiftPositive(JmPoint target, JmPoint delta) {
		Validate.notNull(target);
		return new JmPoint(target.x + delta.x, target.y + delta.y);
	}
	
	private JmPointUtil() {
	}
}
