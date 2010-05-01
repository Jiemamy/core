/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.formatter;

import java.util.List;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.sql.Separator;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.model.sql.Token;

/**
 * 最も基本的なのSQLフォーマッタ。
 * 
 * @author daisuke
 */
public class DefaultSqlFormatter implements SqlFormatter {
	
	/** 空白文字 */
	public static final String WHITESPACE = " ";
	

	/**
	 * トークンがセパレータかどうか調べる。{@code null}だった場合は無条件で{@code true}を返す。
	 * 
	 * @param token トークン
	 * @return セパレータまたは{@code null}の場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isSeparator(Token token) {
		if (token == null) {
			return true;
		}
		return token instanceof Separator;
	}
	
	public String format(List<Token> tokens) {
		Validate.notNull(tokens);
		StringBuilder sb = new StringBuilder();
		Token lastToken = null;
		for (Token token : tokens) {
			if ((isSeparator(lastToken) == false && isSeparator(token) == false) || Separator.COMMA.equals(lastToken)) {
				sb.append(WHITESPACE);
			}
			sb.append(token);
			lastToken = token;
		}
		return sb.toString();
	}
	
	public String format(SqlStatement stmt) {
		Validate.notNull(stmt);
		return format(stmt.toTokens());
	}
	
}
