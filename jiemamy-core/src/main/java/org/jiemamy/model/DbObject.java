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
package org.jiemamy.model;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.parameter.ParameterKey;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * データベースオブジェクト（TableやView）の抽象モデルクラス。
 * 
 * @author daisuke
 */
public abstract class DbObject extends AbstractEntity {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明文 */
	private String description;
	
	/** パラメータの一覧 */
	protected ParameterMap params = new ParameterMap();
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DbObject(UUID id) {
		super(id);
	}
	
	@Override
	public DbObject clone() {
		DbObject clone = (DbObject) super.clone();
		return clone;
	}
	
	/**
	 * 候補の中から、このモデルを直接参照するモデルの組を返す。
	 * 
	 * @param context コンテキスト
	 * @return 、このモデルを直接参照するモデルの組
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Set<DbObject> findSubDbObjectsNonRecursive(JiemamyContext context) {
		Set<DbObject> collecter = Sets.newHashSet();
		for (DbObject dbObject : context.getDbObjects()) {
			if (dbObject.isSubDbObjectsNonRecursiveOf(this, context)) {
				collecter.add(dbObject);
			}
		}
		return collecter;
	}
	
	/**
	 * 候補の中から、このモデルに直接参照されるモデルの組を返す。
	 * 
	 * @param dbObjects 候補
	 * @return このモデルに直接参照されるモデルの組
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Set<DbObject> findSuperDbObjectsNonRecursive(Set<DbObject> dbObjects) {
		Validate.notNull(dbObjects);
		return Collections.emptySet();
	}
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getLogicalName() {
		return logicalName;
	}
	
	/**
	 * {@link DbObject}名を取得する。
	 * 
	 * @return {@link DbObject}名. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>T getParam(ParameterKey<T> key) {
		return params.get(key);
	}
	
	/**
	 * このモデルが持つ全パラメータを取得する。
	 * 
	 * @return カラムが持つ全パラメータ
	 */
	public ParameterMap getParams() {
		return params;
	}
	
	/**
	 * 自分が{@code target}に依存する{@link DbObject}かどうか調べる。
	 * 
	 * @param target 対象
	 * @param context コンテキスト
	 * @return {@code target}に依存する場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public boolean isSubDbObjectsNonRecursiveOf(DbObject target, JiemamyContext context) {
		return false;
	}
	
	/**
	 * パラメータを追加する。
	 * 
	 * @param key キー
	 * @param value 値
	 * @param <T> 値の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>void putParam(ParameterKey<T> key, T value) {
		params.put(key, value);
	}
	
	/**
	 * パラメータを削除する。
	 * 
	 * @param key キー
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeParam(ParameterKey<?> key) {
		params.remove(key);
	}
	
	/**
	 * 説明文を設定する。
	 * 
	 * @param description 説明文
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 論理名を設定する。
	 * 
	 * @param logicalName 論理名
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	/**
	 * 物理名を設定する。
	 * 
	 * @param name 物理名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public EntityRef<? extends DbObject> toReference() {
		return new EntityRef<DbObject>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + name + "]";
	}
}
