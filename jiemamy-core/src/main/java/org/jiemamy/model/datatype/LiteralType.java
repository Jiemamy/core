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
package org.jiemamy.model.datatype;

/**
 * リテラルの種類をあらわす列挙型。
 * 
 * @since 0.2
 * @author daisuke
 */
public enum LiteralType {
	
	/** 文字（列）リテラル */
	CHARACTER {
		
		@Override
		public String convert(String value) {
			if (value == null) {
				return "NULL";
			}
			return "'" + value + "'";
		}
	},
	/** 数字リテラル */
	NUMERIC {
		
		@Override
		public String convert(String value) {
			if (value == null) {
				return "NULL";
			}
			return value;
		}
	},
	/** 真偽値リテラル */
	BOOLEAN {
		
		@Override
		public String convert(String value) {
			if (value == null) {
				return "NULL";
			}
			return value;
		}
	},
	/** 日付リテラル */
	DATE {
		
		@Override
		public String convert(String value) {
			if (value == null) {
				return "NULL";
			}
			return "DATE '" + value + "'";
		}
	},
	/** 時間リテラル */
	TIME {
		
		@Override
		public String convert(String value) {
			if (value == null) {
				return "NULL";
			}
			return "TIME '" + value + "'";
		}
	},
	/** タイムスタンプリテラル */
	TIMESTAMP {
		
		@Override
		public String convert(String value) {
			if (value == null) {
				return "NULL";
			}
			return "TIMESTAMP '" + value + "'";
		}
	},
	/** インターバルリテラル */
	INTERVAL {
		
		@Override
		public String convert(String value) {
			if (value == null) {
				return "NULL";
			}
			return "INTERVAL " + value;
		}
	},
	
	/** NULLリテラル */
	NULL {
		
		@Override
		public String convert(String value) {
			return "NULL";
		}
	},
	
	/** SQLフラグメント */
	FRAGMENT {
		
		@Override
		public String convert(String value) {
			if (value == null) {
				return "";
			}
			return value;
		}
	};
	
	/**
	 * 値をリテラル表記に変換する。
	 * 
	 * @param value 値
	 * @return リテラル表記
	 * @since 0.2
	 */
	public abstract String convert(String value);
}
