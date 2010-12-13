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

import org.jiemamy.model.datatype.LiteralType;

/**
 * リテラルクラス。
 * 
 * <p>このクラスはイミュータブルである。</p>
 * 
 * @author daisuke
 */
public class Literal implements Token {
	
	/** NULL */
	public static final Literal NULL = new Literal("NULL", LiteralType.NULL);
	
	/** TRUE */
	public static final Literal TRUE = new Literal(true);
	
	/** FALSE */
	public static final Literal FALSE = new Literal(false);
	

	/**
	 * リテラルインスタンスを取得する。
	 * 
	 * @param bool リテラルのJava表現
	 * @return リテラルインスタンス
	 */
	public static Literal of(boolean bool) {
		return bool ? TRUE : FALSE;
	}
	
	/**
	 * リテラルインスタンスを取得する。
	 * 
	 * @param number リテラルのJava表現
	 * @return リテラルインスタンス
	 */
	public static Literal of(Number number) {
		if (number == null) {
			return NULL;
		}
		return new Literal(number);
	}
	
	/**
	 * リテラルインスタンスを取得する。
	 * 
	 * @param string リテラルのJava文字列表現
	 * @return リテラルインスタンス
	 */
	public static Literal of(String string) {
		if (string == null) {
			return NULL;
		}
		return new Literal(string, LiteralType.CHARACTER);
	}
	
	/**
	 * リテラルインスタンスを取得する。
	 * 
	 * @param string リテラルのJava文字列表現
	 * @param type リテラルの種類
	 * @return リテラルインスタンス
	 */
	public static Literal of(String string, LiteralType type) {
		if (string == null) {
			return NULL;
		}
		return new Literal(string, type);
	}
	

	/** 必要な場合クオートも含んだ文字列 */
	private final String string;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param string クオートを含まない、値をあらわす文字列
	 * @param type リテラルの種類
	 */
	protected Literal(String string, LiteralType type) {
		Validate.notNull(string);
		this.string = type.convert(string);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param bool 値
	 */
	Literal(boolean bool) {
		this(Boolean.valueOf(bool).toString(), LiteralType.BOOLEAN);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param number 値
	 */
	Literal(Number number) {
		this(number.toString(), LiteralType.NUMERIC);
	}
	
	@Override
	public String toString() {
		return string;
	}
}
