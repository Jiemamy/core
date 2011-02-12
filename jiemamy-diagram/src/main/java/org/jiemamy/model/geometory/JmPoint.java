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
 * 座標モデル。
 * 
 * <p>イミュータブルなクラスである。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
@SuppressWarnings("serial")
public final class JmPoint implements Serializable {
	
	/** X座標 */
	public final int x;
	
	/** Y座標 */
	public final int y;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @since 0.3
	 */
	public JmPoint(int x, int y) {
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
		final JmPoint other = (JmPoint) obj;
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
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	@Override
	public String toString() {
		return "Point(" + x + ", " + y + ")";
	}
}
