/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/10
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
import java.sql.SQLException;

import org.jiemamy.JiemamyContext;

/**
 * {@link DatabaseMetaData}から情報を読み取り、インポートするパーサインターフェイス。
 * 
 * @author daisuke
 */
public interface DatabaseMetadataParser {
	
	/**
	 * {@link DatabaseMetaData}を {@link ParseMetadataConfig}の設定に従って解析し、
	 * 結果を{@link JiemamyContext}に格納する。
	 * 
	 * @param context 格納先 {@link JiemamyContext}
	 * @param meta 解析対象 {@link DatabaseMetaData}
	 * @param config 解析設定
	 * @throws SQLException 解析中に {@link SQLException} が発生した場合
	 */
	void parseMetadata(JiemamyContext context, DatabaseMetaData meta, ParseMetadataConfig config) throws SQLException;
	
}
