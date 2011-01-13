/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/03/02
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

import org.apache.commons.lang.Validate;

import org.jiemamy.utils.collection.ArrayEssentialStack;
import org.jiemamy.utils.collection.EssentialStack;

/**
 * セーブポイント情報を保持するインターフェイス。
 * 
 * <p>実装はイミュータブルであることが望ましい。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public class SavePoint {
	
	private final EssentialStack<StoredEvent> undoStackSnapShot;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param undoStack save時のundo stackの状態
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	SavePoint(EssentialStack<StoredEvent> undoStack) {
		Validate.notNull(undoStack);
		undoStackSnapShot = new ArrayEssentialStack<StoredEvent>(undoStack);
	}
	
	/**
	 * スナップショット取得時点のundoスタックを取得する。
	 * 
	 * @return スナップショット取得時点のundoスタック
	 */
	EssentialStack<StoredEvent> getUndoStackSnapshot() {
		// イミュータブルにする為、防御コピーを返す
		return new ArrayEssentialStack<StoredEvent>(undoStackSnapShot);
	}
	
}
