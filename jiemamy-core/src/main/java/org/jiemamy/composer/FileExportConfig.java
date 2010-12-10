/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.composer;

import java.io.File;

/**
 * ファイルにを出力する{@link Exporter}に関する設定情報を提供するインターフェイス。
 * 
 * @author daisuke
 */
public interface FileExportConfig extends ExportConfig {
	
	/**
	 * 出力ファイルを取得する。
	 * 
	 * @return 出力ファイル. 未設定の場合は{@code null} 
	 */
	File getOutputFile();
	
	/**
	 * 出力ファイルが既に存在した場合、上書きするかどうか調べる。
	 * 
	 * @return 上書きする場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isOverwrite();
	
}
