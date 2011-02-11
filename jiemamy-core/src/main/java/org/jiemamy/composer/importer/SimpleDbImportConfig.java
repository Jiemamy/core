/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/07/12
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
package org.jiemamy.composer.importer;

import org.jiemamy.SimpleDbConnectionConfig;
import org.jiemamy.dialect.Dialect;

/**
 * {@link DbImporter}に関する設定情報保持クラス。
 * 
 * @author daisuke
 */
public class SimpleDbImportConfig extends SimpleDbConnectionConfig implements DbImportConfig {
	
	/** SQL方言 */
	private Dialect dialect;
	
	/** インポートするエンティティの種類 */
	private String[] entityTypes;
	
	/** インポートするエンティティ名の配列 */
	private String[] selectedEntities;
	
	/** スキーマ名 */
	private String schema;
	
	/** テーブルのコンテンツをデータセットとしてインポートするかどうか */
	private boolean importDataSet;
	

	/**
	 * インスタンスを生成する。
	 */
	public SimpleDbImportConfig() {
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param dialect {@link Dialect}
	 */
	public SimpleDbImportConfig(Dialect dialect) {
		this.dialect = dialect;
	}
	
	public Dialect getDialect() {
		return dialect;
	}
	
	public String[] getEntityTypes() {
		if (entityTypes == null) {
			// checkstyle警告は無視。ここはnull返しでなければならない。docコメント参照。
			return null;
		}
		return entityTypes.clone();
	}
	
	public String getSchema() {
		return schema;
	}
	
	public String[] getSelectedEntities() {
		if (selectedEntities == null) {
			// checkstyle警告は無視。ここはnull返しでなければならない。docコメント参照。
			return null;
		}
		return selectedEntities.clone();
	}
	
	public boolean isImportDataSet() {
		return importDataSet;
	}
	
	/**
	 * SQL方言を設定する。
	 * 
	 * @param dialect SQL方言
	 */
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	
	/**
	 * インポートするエンティティの種類を設定する。
	 * 
	 * @param entityTypes インポートするエンティティの種類
	 */
	public void setEntityTypes(String[] entityTypes) {
		this.entityTypes = entityTypes.clone();
	}
	
	/**
	 * テーブルのコンテンツをデータセットとしてインポートするかどうかを設定する。
	 * 
	 * @param importDataSet テーブルのコンテンツをデータセットとしてインポートするかどうか
	 */
	public void setImportDataSet(boolean importDataSet) {
		this.importDataSet = importDataSet;
	}
	
	/**
	 * スキーマ名を設定する。
	 * 
	 * @param schema スキーマ名
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	/**
	 * インポートするエンティティ名の配列を設定する。
	 * 
	 * @param selectedEntities インポートするエンティティ名の配列
	 */
	public void setSelectedEntities(String[] selectedEntities) {
		this.selectedEntities = selectedEntities.clone();
	}
	
}
