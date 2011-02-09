/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.dialect;

import java.util.List;

import org.jiemamy.model.sql.Token;

/**
 * Enum等を{@link Token}のシーケンスに変換するリゾルバ。
 * 
 * @author daisuke
 */
public interface TokenResolver {
	
	/**
	 * オブジェクトをトークン列に変換する。
	 * 
	 * <p>指定したオブジェクトの変換に対応しない場合は、空のリストを返す。</p>
	 * 
	 * @param value 変換対象オブジェクト
	 * @return トークンシーケンス. 引数に{@code null}を与えた場合は空のリスト
	 */
	List<Token> resolve(Object value);
	
}
