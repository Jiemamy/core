/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/25
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
package org.jiemamy;

/**
 * {@link JmMetadata}のデフォルト実装。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmMetadata implements JmMetadata {
	
	private String dialectClassName;
	
	private String description;
	
	private String schemaName;
	

	@Override
	public SimpleJmMetadata clone() {
		try {
			return (SimpleJmMetadata) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error("clone not supported", e);
		}
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getDialectClassName() {
		return dialectClassName;
	}
	
	public String getSchemaName() {
		return schemaName;
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
	 * SQL方言IDを設定する。
	 * 
	 * @param dialectClassName SQL方言ID
	 */
	public void setDialectClassName(String dialectClassName) {
		this.dialectClassName = dialectClassName;
	}
	
	/**
	 * スキーマ名を設定する。
	 * 
	 * @param schemaName スキーマ名
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
}
