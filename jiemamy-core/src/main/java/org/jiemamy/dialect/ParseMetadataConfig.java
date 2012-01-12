/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/03/30
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
package org.jiemamy.dialect;

import java.sql.DatabaseMetaData;

import org.jiemamy.model.DbObject;

/**
 * {@link Dialect}が{@link DatabaseMetaData}から{@link DbObject}を生成する際に必要となる設定情報を供給するインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface ParseMetadataConfig {
	
	/**
	 * SQL方言を取得する。
	 * 
	 * @return SQL方言. 未設定の場合は{@code null}
	 */
	Dialect getDialect();
	
	/**
	 * インポートするエンティティの種類を取得する。
	 * 
	 * <p>空配列の場合、何もインポートしないことを表す。{@code null}の場合は全てインポートすることを表す。</p>
	 * 
	 * @return インポートするエンティティの種類、未設定の場合は{@code null}
	 */
	String[] getEntityTypes();
	
	/**
	 * スキーマ名を取得する。
	 * 
	 * @return スキーマ名. 未設定の場合は{@code null}
	 */
	String getSchema();
	
	/**
	 * インポートするエンティティ名の配列を取得する。
	 * 
	 * <p>空配列の場合、何もインポートしないことを表す。{@code null}の場合は全てインポートすることを表す。</p>
	 * 
	 * @return インポートするエンティティ名の配列. 未設定の場合は{@code null}
	 */
	String[] getSelectedEntities();
	
	/**
	 * テーブルのコンテンツをデータセットとしてインポートするかどうかを取得する。
	 * 
	 * @return テーブルのコンテンツをデータセットとしてインポートするかどうか
	 */
	boolean isImportDataSet();
	
}
