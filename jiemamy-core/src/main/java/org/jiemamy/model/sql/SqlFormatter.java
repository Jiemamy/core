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

import java.util.List;

/**
 * SQLを整形するフォーマッタインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface SqlFormatter {
	
	/**
	 * SQL文字列に整形する。
	 * 
	 * @param tokens トークン列
	 * @return 整形済みSQL文字列
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	String format(List<Token> tokens);
	
	/**
	 * SQL文字列に整形する。
	 * 
	 * @param stmt SQL文
	 * @return 整形済みSQL文字列
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	String format(SqlStatement stmt);
}
