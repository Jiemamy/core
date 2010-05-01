/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/09/17
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
package org.jiemamy.model.entity;

import java.util.Collection;

import org.jiemamy.model.JiemamyElement;

/**
 * リレーショナルデータベースモデルにおける「実体」を表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface EntityModel extends JiemamyElement {
	
	/**
	 * このエンティティ<b>に</b>依存しているエンティティ（A → {@code this}）の集合を取得する。
	 * 
	 * <ul>
	 *   <li>外部キー制約によってこのエンティティを参照しているテーブル</li>
	 *   <li>このエンティティに依存したビュー</li>
	 * </ul>
	 * 
	 * @param recursive 再帰的に検索する場合は{@code true}、そうでない場合は{@code false}
	 * @return エンティティの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	Collection<EntityModel> findSubEntities(boolean recursive);
	
	/**
	 * このエンティティ<b>が</b>依存している（{@code this} → A）エンティティの集合を取得する。
	 * 
	 * <ul>
	 *   <li>このテーブルの外部キー制約が参照しているテーブル</li>
	 *   <li>このビューが参照しているテーブル</li>
	 * </ul>
	 * 
	 * @param recursive 再帰的に検索する場合は{@code true}、そうでない場合は{@code false}
	 * @return エンティティの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	Collection<EntityModel> findSuperEntities(boolean recursive);
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getDescription();
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getLogicalName();
	
	/**
	 * エンティティ名を取得する。
	 * 
	 * @return エンティティ名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getName();
}
