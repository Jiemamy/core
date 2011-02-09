/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/15
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
 * リスナに対してEDITコマンドの通知を行うかどうかを判断するための戦略
 * 
 * @since 0.2
 * @author daisuke
 */
public interface DispatchStrategy {
	
	/**
	 * 指定されたリスナに対して、EDITコマンドの実行の通知が必要かどうかを判断する。
	 * 
	 * @param listener EDITコマンドリスナ
	 * @param command EDITコマンド
	 * @return dispatchが必要な場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.2
	 */
	boolean needToDispatch(StoredEventListener listener, StoredEvent<?> command);
	
}
