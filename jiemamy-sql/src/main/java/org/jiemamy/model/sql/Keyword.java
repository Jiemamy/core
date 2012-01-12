/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/02/11
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

/**
 * キーワードクラス。
 * 
 * <p>このクラスはイミュータブルである。</p>
 * 
 * @author daisuke
 */
public class Keyword implements Token {
	
	/** CREATE */
	public static final Keyword CREATE = new Keyword("CREATE");
	
	/** DROP */
	public static final Keyword DROP = new Keyword("DROP");
	
	/** TABLE */
	public static final Keyword TABLE = new Keyword("TABLE");
	
	/** VIEW */
	public static final Keyword VIEW = new Keyword("VIEW");
	
	/** AS */
	public static final Keyword AS = new Keyword("AS");
	
	/** KEY */
	public static final Keyword KEY = new Keyword("KEY");
	
	/** PRIMARY */
	public static final Keyword PRIMARY = new Keyword("PRIMARY");
	
	/** UNIQUE */
	public static final Keyword UNIQUE = new Keyword("UNIQUE");
	
	/** FOREIGN */
	public static final Keyword FOREIGN = new Keyword("FOREIGN");
	
	/** CHECK */
	public static final Keyword CHECK = new Keyword("CHECK");
	
	/** SCHEMA */
	public static final Keyword SCHEMA = new Keyword("SCHEMA");
	
	/** CONSTRAINT */
	public static final Keyword CONSTRAINT = new Keyword("CONSTRAINT");
	
	/** DEFAULT */
	public static final Keyword DEFAULT = new Keyword("DEFAULT");
	
	/** REFERENCES */
	public static final Keyword REFERENCES = new Keyword("REFERENCES");
	
	/** ON */
	public static final Keyword ON = new Keyword("ON");
	
	/** DELETE */
	public static final Keyword DELETE = new Keyword("DELETE");
	
	/** UPDATE */
	public static final Keyword UPDATE = new Keyword("UPDATE");
	
	/** SET */
	public static final Keyword SET = new Keyword("SET");
	
	/** NULL */
	public static final Keyword NULL = new Keyword("NULL");
	
	/** CASCADE */
	public static final Keyword CASCADE = new Keyword("CASCADE");
	
	/** RESTRICT */
	public static final Keyword RESTRICT = new Keyword("RESTRICT");
	
	/** NO */
	public static final Keyword NO = new Keyword("NO");
	
	/** ACTION */
	public static final Keyword ACTION = new Keyword("ACTION");
	
	/** NOT */
	public static final Keyword NOT = new Keyword("NOT");
	
	/** DEFERRABLE */
	public static final Keyword DEFERRABLE = new Keyword("DEFERRABLE");
	
	/** INITIALLY */
	public static final Keyword INITIALLY = new Keyword("INITIALLY");
	
	/** IMMEDIATE */
	public static final Keyword IMMEDIATE = new Keyword("IMMEDIATE");
	
	/** DEFERRED */
	public static final Keyword DEFERRED = new Keyword("DEFERRED");
	
	/** MATCH */
	public static final Keyword MATCH = new Keyword("MATCH");
	
	/** SIMPLE */
	public static final Keyword SIMPLE = new Keyword("SIMPLE");
	
	/** FULL */
	public static final Keyword FULL = new Keyword("FULL");
	
	/** PARTIAL */
	public static final Keyword PARTIAL = new Keyword("PARTIAL");
	
	/** WITH */
	public static final Keyword WITH = new Keyword("WITH");
	
	/** WITHOUT */
	public static final Keyword WITHOUT = new Keyword("WITHOUT");
	
	/** TIMEZONE */
	public static final Keyword TIMEZONE = new Keyword("TIMEZONE");
	
	/** INSERT */
	public static final Keyword INSERT = new Keyword("INSERT");
	
	/** INTO */
	public static final Keyword INTO = new Keyword("INTO");
	
	/** VALUES */
	public static final Keyword VALUES = new Keyword("VALUES");
	
	/** INDEX */
	public static final Keyword INDEX = new Keyword("INDEX");
	
	/** ASC */
	public static final Keyword ASC = new Keyword("ASC");
	
	/** DESC */
	public static final Keyword DESC = new Keyword("DESC");
	
	/** WHERE */
	public static final Keyword WHERE = new Keyword("WHERE");
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param keyword キーワード文字列
	 * @return インスタンス
	 */
	public static Keyword of(String keyword) {
		return new Keyword(keyword);
	}
	
	
	/** キーワード文字列 */
	private final String string;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param string キーワード文字列
	 */
	protected Keyword(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}
}
