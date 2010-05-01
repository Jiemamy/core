/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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


/**
 * エンティティ（TableやView）の抽象モデルクラス。
 * 
 * @author daisuke
 */
public abstract class AbstractDatabaseObjectModel implements DatabaseObjectModel {
	
	/** 名前 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明文 */
	private String description;
	

	public String getDescription() {
		return description;
	}
	
	public String getLogicalName() {
		return logicalName;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * 説明文を設定する
	 * 
	 * @param description 説明文
	 * @since 0.3
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * 論理名を設定する
	 * 
	 * @param logicalName 論理名
	 * @since 0.3
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	/**
	 * 
	 * 物理名を設定する
	 * 
	 * @param name 物理名
	 * @since 0.3
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Entity " + getName();
	}
}
