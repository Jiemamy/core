/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/04/06
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
package org.jiemamy;

import java.net.URL;

/**
 * DB接続に必要な設定情報を供給するインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface DbConnectionConfig {
	
	/**
	 * ドライバの完全修飾クラス名を取得する。
	 * 
	 * @return ドライバの完全修飾クラス名. 未設定の場合は{@code null}
	 */
	String getDriverClassName();
	
	/**
	 * ドライバJARファイルのパス配列を取得する。
	 * 
	 * @return ドライバJARファイルのパス配列. 未設定の場合は空の配列を返す。
	 */
	URL[] getDriverJarPaths();
	
	/**
	 * 接続パスワードを取得する。
	 * 
	 * @return 接続パスワード. 未設定の場合は{@code null}
	 */
	String getPassword();
	
	/**
	 * 接続URIを取得する。
	 * 
	 * @return 接続URI. 未設定の場合は{@code null}
	 */
	String getUri();
	
	/**
	 * 接続ユーザ名を取得する。
	 * 
	 * @return 接続ユーザ名. 未設定の場合は{@code null}
	 */
	String getUsername();
	
}
