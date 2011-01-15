/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import org.jiemamy.DatabaseConnectionConfig;
import org.jiemamy.composer.ImportConfig;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.ImportMetadataConfig;
import org.jiemamy.model.DatabaseObjectModel;

/**
 * {@link DatabaseImporter}に関する設定情報を供給するインターフェイス。
 * 
 * <p>DBに接続するときに必要となる情報を供給する責務（{@link DatabaseConnectionConfig}）と、
 * {@link Dialect}がエンティティ情報から{@link DatabaseObjectModel}を生成する際に必要とする設定情報を供給するインターフェイスを合体させると、
 * {@link DatabaseImporter}の実行に必要な設定情報を供給する責務となる。</p>
 * 
 * @author daisuke
 */
public interface DatabaseImportConfig extends ImportConfig, DatabaseConnectionConfig, ImportMetadataConfig {
	
}
