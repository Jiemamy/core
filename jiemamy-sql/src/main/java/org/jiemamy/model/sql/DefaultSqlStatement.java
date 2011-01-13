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

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

/**
 * SQL文実装クラス。
 * 
 * @author daisuke
 */
public class DefaultSqlStatement implements SqlStatement {
	
	/** トークンのリスト */
	private List<Token> tokens = Lists.newArrayList();
	

	/**
	 * インスタンスを生成する。
	 */
	public DefaultSqlStatement() {
		super();
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param tokens トークンシーケンス
	 */
	public DefaultSqlStatement(List<Token> tokens) {
		this.tokens.addAll(tokens);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param token トークン
	 * @param tokenArray トークン
	 */
	public DefaultSqlStatement(Token token, Token... tokenArray) {
		tokens.add(token);
		for (Token token2 : tokenArray) {
			tokens.add(token2);
		}
	}
	
	/**
	 * ステートメントを構成するトークンシーケンスを取得する。
	 * 
	 * <p>このメソッドは、インスタンスの持つフィールドをそのまま返す。返される{@link List}を直接操作することで、
	 * このオブジェクトのフィールドとして保持される{@link List}を変更することができる。</p>
	 * 
	 * @return トークンシーケンス
	 * @see #toTokens()
	 */
	public List<Token> getTokens() {
		return tokens;
	}
	
	/**
	 * トークンを挿入する。
	 * 
	 * @param index 挿入位置インデックス
	 * @param tokens 挿入するトークンシーケンス
	 */
	public void insert(int index, List<Token> tokens) {
		for (Token token : tokens) {
			this.tokens.add(index++, token);
		}
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
