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

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.AbstractEntityModel;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * データベースオブジェクト（TableやView）の抽象モデルクラス。
 * 
 * @author daisuke
 */
public abstract class AbstractDatabaseObjectModel extends AbstractEntityModel implements DatabaseObjectModel {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明文 */
	private String description;
	

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
	
	public Set<DatabaseObjectModel> findSubDatabaseObjectsNonRecursive(Set<DatabaseObjectModel> databaseObjects,
			JiemamyContext context) {
		Set<DatabaseObjectModel> collecter = CollectionsUtil.newHashSet();
		for (DatabaseObjectModel databaseObject : databaseObjects) {
			if (databaseObject.isSubDatabaseObjectsNonRecursiveOf(databaseObjects, this, context)) {
				collecter.add(databaseObject);
			}
		}
		return collecter;
	}
	
	public Set<DatabaseObjectModel> findSuperDatabaseObjectsNonRecursive(Set<DatabaseObjectModel> databaseObjects) {
		return null;
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
	
	public boolean isSubDatabaseObjectsNonRecursiveOf(Set<DatabaseObjectModel> databaseObjects,
			DatabaseObjectModel target, JiemamyContext context) {
		return false;
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
	public String toString() {
		return "Entity " + getName();
	}
	
}
