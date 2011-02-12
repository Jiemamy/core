/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/12/04
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

import org.jiemamy.dddbase.ValueObject;

/**
 * SQL文をあらわすインターフェイス。
 * 
 * <p>このインターフェイスの実装は、各DB実装に依存している。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public interface SqlStatement extends ValueObject {
	
	/**
	 * SQL文に変換する。
	 * 
	 * <p>変換ロジックには、環境によって定められたデフォルトのロジックが用いられる。</p>
	 * 
	 * @return SQL文
	 * @since 0.3
	 */
	String toString();
	
	/**
	 * 指定したフォーマッタを用いてSQL文に変換する。
	 * 
	 * @param formatter フォーマッタ
	 * @return SQL文
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	String toString(SqlFormatter formatter);
	
	/**
	 * トークンシーケンスに変換する。
	 * 
	 * <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return トークンシーケンス
	 * @since 0.3
	 */
	List<Token> toTokens();
}
