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
 * {@link DatabaseConnectionConfig}のデフォルト実装クラス。
 * 
 * @author daisuke
 */
public class DefaultDatabaseConnectionConfig implements DatabaseConnectionConfig {
	
	/** ドライバの完全修飾クラス名 */
	private String driverClassName;
	
	/** ドライバJARファイルのパス配列 */
	private URL[] driverJarPaths;
	
	/** 接続パスワード */
	private String password;
	
	/** 接続URI */
	private String uri;
	
	/** 接続ユーザ名 */
	private String username;
	

	public String getDriverClassName() {
		return driverClassName;
	}
	
	public URL[] getDriverJarPaths() {
		if (driverJarPaths == null) {
			return new URL[0];
		}
		return driverJarPaths.clone();
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * ドライバの完全修飾クラス名を設定する。
	 * 
	 * @param driverClassName ドライバの完全修飾クラス名
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	
	/**
	 * ドライバJARファイルのパス配列を設定する。
	 * 
	 * @param driverJarPaths ドライバJARファイルのパス配列
	 */
	public void setDriverJarPaths(URL[] driverJarPaths) {
		if (driverJarPaths != null) {
			this.driverJarPaths = driverJarPaths.clone();
		} else {
			this.driverJarPaths = null;
		}
	}
	
	/**
	 * 接続パスワードを設定する。
	 * 
	 * @param password 接続パスワード
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 接続URIを設定する。
	 * 
	 * @param uri 接続URI
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	/**
	 * 接続ユーザ名を設定する。
	 * 
	 * @param username 接続ユーザ名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
}
