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
package org.jiemamy.composer.exporter;

import org.jiemamy.JiemamyContext;
import org.jiemamy.composer.FileExportConfig;
import org.jiemamy.dialect.EmitConfig;
import org.jiemamy.model.sql.SqlStatement;

/**
 * {@link SqlExporter}に関する設定情報を供給するインターフェイス。
 * 
 * <p>{@link JiemamyContext}を {@link SqlStatement}のリストに変換するときに必要となる情報を供給する責務（{@link EmitConfig}）と、
 * 「何か」をファイルに書き出すときに必要な情報を供給する責務（{@link FileExportConfig}）を合体させると、
 * {@link SqlExporter}の実行に必要な設定情報を供給する責務となる。</p>
 * 
 * @author daisuke
 */
public interface SqlExportConfig extends EmitConfig, FileExportConfig {
	
}
