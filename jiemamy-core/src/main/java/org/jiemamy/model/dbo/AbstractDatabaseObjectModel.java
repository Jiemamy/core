/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.dbo;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.AbstractJiemamyEntity;
import org.jiemamy.model.params.ModelParameter.Key;
import org.jiemamy.utils.MutationMonitor;

/**
 * データベースオブジェクト（TableやView）の抽象モデルクラス。
 * 
 * @author daisuke
 */
public abstract class AbstractDatabaseObjectModel extends AbstractJiemamyEntity implements DatabaseObjectModel {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明文 */
	private String description;
	
	private Set<DatabaseObjectParameter<?>> params;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public AbstractDatabaseObjectModel(UUID id) {
		super(id);
	}
	
	@Override
	public AbstractDatabaseObjectModel clone() {
		return (AbstractDatabaseObjectModel) super.clone();
	}
	
	public Set<DatabaseObjectModel> findSubDatabaseObjectsNonRecursive(JiemamyContext context) {
		Set<DatabaseObjectModel> collecter = Sets.newHashSet();
		for (DatabaseObjectModel databaseObject : context.getDatabaseObjects()) {
			if (databaseObject.isSubDatabaseObjectsNonRecursiveOf(this, context)) {
				collecter.add(databaseObject);
			}
		}
		return collecter;
	}
	
	public Set<DatabaseObjectModel> findSuperDatabaseObjectsNonRecursive(Set<DatabaseObjectModel> databaseObjects) {
		return null; // TODO
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLogicalName() {
		return logicalName;
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public <T>DatabaseObjectParameter<T> getParam(Key<T> key) {
		for (DatabaseObjectParameter<?> param : params) {
			if (param.getKey().equals(key)) {
				return (DatabaseObjectParameter<T>) param;
			}
		}
		return null;
	}
	
	public Set<DatabaseObjectParameter<?>> getParams() {
		return MutationMonitor.monitor(Sets.newHashSet(params));
	}
	
	public boolean isSubDatabaseObjectsNonRecursiveOf(DatabaseObjectModel target, JiemamyContext context) {
		return false;
	}
	
	public <T>void putParam(Key<T> key, T value) {
		params.add(new DefaultDatabaseObjectParameter<T>(key, value));
	}
	
	public <T>void removeParam(Key<T> key) {
		params.remove(new DefaultDatabaseObjectParameter<T>(key, null));
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
	public abstract String toString();
	
}
