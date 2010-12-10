/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/02/25
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
package org.jiemamy.sql;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.sql.DefaultSqlFormatter;
import org.jiemamy.model.sql.Identifier;
import org.jiemamy.model.sql.Keyword;
import org.jiemamy.model.sql.Literal;
import org.jiemamy.model.sql.Separator;
import org.jiemamy.model.sql.Token;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * {@link DefaultSqlFormatter}のテストクラス。
 * 
 * @author daisuke
 */
public class DefaultSqlFormatterTest {
	
	private static final String NL = SystemUtils.LINE_SEPARATOR;
	
	private DefaultSqlFormatter formatter;
	
	private static Logger logger = LoggerFactory.getLogger(DefaultSqlFormatterTest.class);
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		formatter = new DefaultSqlFormatter();
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		formatter = null;
	}
	
	/**
	 * 基本的なCREATE文が出力できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_基本的なCREATE文が出力できる() throws Exception {
		List<Token> tokens = CollectionsUtil.newArrayList();
		tokens.add(Keyword.CREATE);
		tokens.add(Keyword.TABLE);
		tokens.add(Identifier.of("T_USER"));
		tokens.add(Separator.LEFT_PAREN);
		
		tokens.add(Identifier.of("NAME"));
		tokens.add(Keyword.of("VARCHAR"));
		tokens.add(Separator.LEFT_PAREN);
		tokens.add(Literal.of(32));
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Separator.COMMA);
		
		tokens.add(Identifier.of("PASSWORD"));
		tokens.add(Keyword.of("VARCHAR"));
		tokens.add(Separator.LEFT_PAREN);
		tokens.add(Literal.of(64));
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Keyword.NOT);
		tokens.add(Keyword.NULL);
		tokens.add(Separator.COMMA);
		
		tokens.add(Identifier.of("DISABLED"));
		tokens.add(Keyword.of("BOOLEAN"));
		tokens.add(Keyword.NOT);
		tokens.add(Keyword.NULL);
		tokens.add(Separator.COMMA);
		
		tokens.add(Keyword.PRIMARY);
		tokens.add(Keyword.KEY);
		tokens.add(Separator.LEFT_PAREN);
		tokens.add(Identifier.of("NAME"));
		tokens.add(Separator.RIGHT_PAREN);
		
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Separator.SEMICOLON);
		
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE T_USER (").append(NL);
		sb.append("  NAME VARCHAR(32),").append(NL);
		sb.append("  PASSWORD VARCHAR(64) NOT NULL,").append(NL);
		sb.append("  DISABLED BOOLEAN NOT NULL").append(NL);
		sb.append("  PRIMARY KEY(NAME)").append(NL);
		sb.append(");").append(NL);
//		String expected = sb.toString(); // TODO formatterが出来るまでコメントアウト
		String expected =
				"CREATE TABLE T_USER(NAME VARCHAR(32), PASSWORD VARCHAR(64)NOT NULL, "
						+ "DISABLED BOOLEAN NOT NULL, PRIMARY KEY(NAME));";
		
		doAssertion(tokens, expected);
	}
	
	/**
	 * 基本的なCREATE文が出力できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_カラム制約PKのCREATE文が出力できる() throws Exception {
		List<Token> tokens = CollectionsUtil.newArrayList();
		tokens.add(Keyword.CREATE);
		tokens.add(Keyword.TABLE);
		tokens.add(Identifier.of("T_USER"));
		tokens.add(Separator.LEFT_PAREN);
		
		tokens.add(Identifier.of("NAME"));
		tokens.add(Keyword.of("VARCHAR"));
		tokens.add(Separator.LEFT_PAREN);
		tokens.add(Literal.of(32));
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Keyword.PRIMARY);
		tokens.add(Keyword.KEY);
		tokens.add(Separator.COMMA);
		
		tokens.add(Identifier.of("PASSWORD"));
		tokens.add(Keyword.of("VARCHAR"));
		tokens.add(Separator.LEFT_PAREN);
		tokens.add(Literal.of(64));
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Keyword.NOT);
		tokens.add(Keyword.NULL);
		tokens.add(Separator.COMMA);
		
		tokens.add(Identifier.of("DISABLED"));
		tokens.add(Keyword.of("BOOLEAN"));
		tokens.add(Keyword.NOT);
		tokens.add(Keyword.NULL);
		
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Separator.SEMICOLON);
		
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE T_USER (").append(NL);
		sb.append("  NAME VARCHAR(32) PRIMARY KEY,").append(NL);
		sb.append("  PASSWORD VARCHAR(64) NOT NULL,").append(NL);
		sb.append("  DISABLED BOOLEAN NOT NULL").append(NL);
		sb.append(");").append(NL);
//		String expected = sb.toString(); // TODO formatterが出来るまでコメントアウト
		String expected =
				"CREATE TABLE T_USER(NAME VARCHAR(32)PRIMARY KEY, PASSWORD VARCHAR(64)NOT NULL, "
						+ "DISABLED BOOLEAN NOT NULL);";
		
		doAssertion(tokens, expected);
	}
	
	/**
	 * 基本的なINSERT文が出力できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11_基本的なINSERT文が出力できる() throws Exception {
		List<Token> tokens = CollectionsUtil.newArrayList();
		tokens.add(Keyword.INSERT);
		tokens.add(Keyword.INTO);
		tokens.add(Identifier.of("T_USER"));
		tokens.add(Separator.LEFT_PAREN);
		tokens.add(Identifier.of("NAME"));
		tokens.add(Separator.COMMA);
		tokens.add(Identifier.of("PASSWORD"));
		tokens.add(Separator.COMMA);
		tokens.add(Identifier.of("DISABLED"));
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Keyword.VALUES);
		tokens.add(Separator.LEFT_PAREN);
		tokens.add(Literal.of("daisuke"));
		tokens.add(Separator.COMMA);
		tokens.add(Literal.of("miyamoto"));
		tokens.add(Separator.COMMA);
		tokens.add(Literal.of(false));
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Separator.SEMICOLON);
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO T_USER (NAME, PASSWORD, DISABLED)").append(NL);
		sb.append("  VALUES ('daisuke', 'miyamoto', false);").append(NL);
//		String expected = sb.toString(); // TODO formatterが出来るまでコメントアウト
		String expected = "INSERT INTO T_USER(NAME, PASSWORD, DISABLED)VALUES('daisuke', 'miyamoto', false);";
		
		doAssertion(tokens, expected);
	}
	
	/**
	 * assertionを実行する。
	 * 
	 * @param tokens トークンシーケンス
	 * @param expected 期待する出力
	 */
	private void doAssertion(List<Token> tokens, String expected) {
		String formated = formatter.format(tokens);
		logger.info(formated);
		assertThat(formated, is(expected));
	}
}
