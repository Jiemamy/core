/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/08
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
package org.jiemamy.model.datatype;

import java.sql.Types;

/**
 * データ型のカテゴリを表す列挙型。
 * 
 * @author daisuke
 */
public enum DataTypeCategory {
	
	/** CHARACTER型 */
	CHARACTER(Types.CHAR, LiteralType.CHARACTER),

	/** VARCHAR型 */
	VARCHAR(Types.VARCHAR, LiteralType.CHARACTER),

	/** CLOB型 */
	CLOB(Types.CLOB, LiteralType.CHARACTER),

	/** BLOB型 */
	BLOB(Types.BLOB, LiteralType.CHARACTER),

	/** BIT型 */
	BIT(Types.BIT, LiteralType.CHARACTER),

	/** VARBIT型 */
	VARBIT(Types.OTHER, LiteralType.CHARACTER),

	/** NUMERIC型 */
	NUMERIC(Types.NUMERIC, LiteralType.NUMERIC),

	/** DECIMAL型 */
	DECIMAL(Types.DECIMAL, LiteralType.NUMERIC),

	/** INTEGER型 */
	INTEGER(Types.INTEGER, LiteralType.NUMERIC),

	/** SMALLINT型 */
	SMALLINT(Types.SMALLINT, LiteralType.NUMERIC),

	/** FLOAT型 */
	FLOAT(Types.FLOAT, LiteralType.NUMERIC),

	/** REAL型 */
	REAL(Types.REAL, LiteralType.NUMERIC),

	/** DOUBLE型 */
	DOUBLE(Types.DOUBLE, LiteralType.NUMERIC),

	/** BOOLEAN型 */
	BOOLEAN(Types.BOOLEAN, LiteralType.BOOLEAN),

	/** DATE型 */
	DATE(Types.DATE, LiteralType.DATE),

	/** TIME型 */
	TIME(Types.TIME, LiteralType.TIME),

	/** TIMESTAMP型 */
	TIMESTAMP(Types.TIMESTAMP, LiteralType.TIMESTAMP),

	/** INTERVAL型 */
	INTERVAL(Types.OTHER, LiteralType.INTERVAL),

	/** その他型 */
	OTHER(Types.OTHER, LiteralType.CHARACTER);
	
	/**
	 * {@link Types}の値から、カテゴリを取得する。
	 * 
	 * @param sqlType {@link Types}の値
	 * @return カテゴリ. 適切なカテゴリが見つからなかった場合は{@code null}
	 * @since 0.2
	 */
	public static DataTypeCategory fromSqlType(int sqlType) {
		for (DataTypeCategory category : values()) {
			if (category.sqlType == sqlType) {
				return category;
			}
		}
		return OTHER;
	}
	

	private LiteralType literalType;
	
	private final int sqlType;
	

	DataTypeCategory(int sqlType, LiteralType literalType) {
		assert literalType != null;
		this.sqlType = sqlType;
		this.literalType = literalType;
	}
	
	/**
	 * このカテゴリのリテラルの種類を取得する。
	 * 
	 * @return リテラルの種類
	 * @since 0.2
	 */
	public LiteralType getLiteralType() {
		return literalType;
	}
	
	/**
	 * このカテゴリの{@link Types}の値を取得する。
	 * 
	 * @return {@link Types}の値
	 * @since 0.2
	 */
	public int getSqlType() {
		return sqlType;
	}
}
