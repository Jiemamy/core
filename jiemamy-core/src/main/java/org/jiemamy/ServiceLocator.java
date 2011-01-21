/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/09/18
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

/**
 * プラグインによるサービスのインスタンスを取得する責務を負うインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface ServiceLocator {
	
	/**
	 * サービスを取得する。
	 * 
	 * @param <T> サービスクラスのインターフェイス型
	 * @param clazz サービスクラスのインターフェイス
	 * @param fqcn サービスクラスの完全修飾名
	 * @return サービスインスタンス
	 * @throws ClassNotFoundException サービスクラスが見つからない場合または引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	<T>T getService(Class<T> clazz, String fqcn) throws ClassNotFoundException;
	
}
