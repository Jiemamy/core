/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.model.view;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.table.JmTable;

/**
 * ビューモデル
 * 
 * @author daisuke
 */
public final class JmView extends DbObject {
	
	/** VIEW定義SELECT文 */
	private String definition;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public JmView() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public JmView(UUID id) {
		super(id);
	}
	
	@Override
	public JmView clone() {
		JmView clone = (JmView) super.clone();
		return clone;
	}
	
	@Override
	public Set<DbObject> findSuperDbObjectsNonRecursive(Set<DbObject> dbObjects) {
		// TODO definitionのパースによって、依存テーブルを出すべき
		// 現状は全てのテーブルに依存することになっている。
		Set<DbObject> result = Sets.newHashSet();
		for (DbObject dbObject : dbObjects) {
			if (dbObject instanceof JmTable) {
				result.add(dbObject);
			}
		}
		return result;
	}
	
	/**
	 * VIEW定義SELECT文を取得する。
	 * 
	 * @return VIEW定義SELECT文. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getDefinition() {
		return definition;
	}
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>T getParam(ViewParameterKey<T> key) {
		return super.getParam(key);
	}
	
	/**
	 * パラメータを追加する。
	 * 
	 * @param key キー
	 * @param value 値
	 * @param <T> 値の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>void putParam(ViewParameterKey<T> key, T value) {
		super.putParam(key, value);
	}
	
	/**
	 * パラメータを削除する。
	 * 
	 * @param key キー
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeParam(ViewParameterKey<?> key) {
		super.removeParam(key);
	}
	
	/**
	 * VIEW定義SELECT文を設定する。 
	 * @param definition VIEW定義SELECT文
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	@Override
	public EntityRef<? extends JmView> toReference() {
		return new EntityRef<JmView>(this);
	}
}
