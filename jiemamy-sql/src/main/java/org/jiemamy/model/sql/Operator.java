/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/02/12
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
package org.jiemamy.model.sql;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.sql.Token;

/**
 * 演算子クラス。
 * 
 * <p>このクラスはイミュータブルである。</p>
 * 
 * @author daisuke
 */
public enum Operator implements Token {
	
	/** IS 演算子 */
	IS("IS"),

	/** NOT 演算子 */
	NOT("NOT");
	
	/** 演算子の文字列表現 */
	private final String string;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param string 演算子の文字列表現
	 */
	Operator(String string) {
		Validate.notNull(string);
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}
	
}
