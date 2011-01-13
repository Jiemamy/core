/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/09
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
package org.jiemamy.transaction;

/**
 * EDITコマンドの実行を監視し、登録されている{@link StoredEventListener}にイベントを通知する。
 * 
 * @since 0.2
 * @author shin1ogawa
 */
public interface EventBroker {
	
	/**
	 * 指定されたリスナを登録する。
	 * 
	 * @param listener 登録するリスナ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	void addListener(StoredEventListener listener);
	
	/**
	 * 指定されたリスナを、通知の判断を行う戦略と共に登録する。
	 * 
	 * @param listener 登録するリスナ
	 * @param strategy リスナに対してEDITコマンドの通知を行うかどうかを判断する戦略
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	void addListener(StoredEventListener listener, DispatchStrategy strategy);
	
	/**
	 * EDITコマンドをイベントとして登録されたリスナに通知する。
	 * 
	 * <p>引数として与えるEDITコマンドは、既に実行済みであることが通例である。</p>
	 * 
	 * @param command 実行されたEDITコマンド
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	void fireCommandProcessed(StoredEvent<?> command);
	
	/**
	 * 指定されたリスナを削除する。
	 * 
	 * @param listener 削除するリスナ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	void removeListener(StoredEventListener listener);
	
	/**
	 * リスナに対する通知が必要かどうかを判断するための戦略を設定する。
	 * THINK setDefaultStrategy() という名称の方が良くないかな？
	 * 
	 * @param strategy デフォルトで適用される{@link DispatchStrategy}の実装インスタンス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	void setStrategy(DispatchStrategy strategy);
	
}
