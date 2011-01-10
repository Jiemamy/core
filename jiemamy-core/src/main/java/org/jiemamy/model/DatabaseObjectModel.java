/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model;

import java.util.Set;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;

/**
 * リレーショナルデータベースモデルにおける「CREATE対象」を表すモデルインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface DatabaseObjectModel extends CoreElement {
	
	DatabaseObjectModel clone();
	
	/**
	 * 候補の中から、このモデルを直接参照するモデルの組を返す。
	 * 
	 * @param context コンテキスト
	 * @return 、このモデルを直接参照するモデルの組
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	Set<DatabaseObjectModel> findSubDatabaseObjectsNonRecursive(JiemamyContext context);
	
	/**
	 * 候補の中から、このモデルに直接参照されるモデルの組を返す。
	 * 
	 * @param candidates 候補
	 * @return このモデルに直接参照されるモデルの組
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	Set<DatabaseObjectModel> findSuperDatabaseObjectsNonRecursive(Set<DatabaseObjectModel> candidates);
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	String getDescription();
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	String getLogicalName();
	
	/**
	 * エンティティ名を取得する。
	 * 
	 * @return エンティティ名. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	String getName();
	
	/**
	 * パラメータを取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return 型パラメータ
	 */
	<T>DatabaseObjectParameter<T> getParam(Key<T> key);
	
	/**
	 * パラメータを取得する。
	 * 
	 * @return 型パラメータの{@link Set}
	 */
	Set<DatabaseObjectParameter<?>> getParams();
	
	/**
	 * 自分が{@code target}に依存する{@link DatabaseObjectModel}かどうか調べる。
	 * 
	 * @param target 対象
	 * @param context コンテキスト
	 * @return {@code target}に依存する場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	boolean isSubDatabaseObjectsNonRecursiveOf(DatabaseObjectModel target, JiemamyContext context);
	
	EntityRef<? extends DatabaseObjectModel> toReference();
}