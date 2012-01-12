/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/02/25
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

import java.sql.DatabaseMetaData;

import org.jiemamy.DbConnectionConfig;
import org.jiemamy.composer.ImportConfig;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.ParseMetadataConfig;
import org.jiemamy.model.DbObject;

/**
 * {@link DbImporter}に関する設定情報を供給するインターフェイス。
 * 
 * <p>DBに接続するときに必要となる情報を供給する責務（{@link DbConnectionConfig}）と、
 * {@link Dialect}が{@link DatabaseMetaData}情報から{@link DbObject}を生成する際に必要とする設定情報を供給する
 * インターフェイスを合体させると、{@link DbImporter}の実行に必要な設定情報を供給する責務となる。</p>
 * 
 * @author daisuke
 */
public interface DbImportConfig extends ImportConfig, DbConnectionConfig, ParseMetadataConfig {
	
}
