/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/12/06
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

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

/**
 * SQL文実装クラス。
 * 
 * @author daisuke
 */
public class SimpleSqlStatement implements SqlStatement {
	
	/** トークンのリスト */
	private final List<Token> tokens;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param tokens トークンシーケンス
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public SimpleSqlStatement(List<Token> tokens) {
		Validate.noNullElements(tokens);
		this.tokens = ImmutableList.copyOf(tokens);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param token トークン
	 * @param tokenArray トークン
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleSqlStatement(Token token, Token... tokenArray) {
		Validate.notNull(token);
		Validate.noNullElements(tokenArray);
		List<Token> tokens = Lists.newArrayList();
		tokens.add(token);
		for (Token tokenRest : tokenArray) {
			tokens.add(tokenRest);
		}
		this.tokens = ImmutableList.copyOf(tokens);
	}
	
	@Override
	public String toString() {
		return toString(new DefaultSqlFormatter());
	}
	
	public String toString(SqlFormatter formatter) {
		Validate.notNull(formatter);
		return formatter.format(this);
	}
	
	public List<Token> toTokens() {
		return Lists.newArrayList(tokens);
	}
}
