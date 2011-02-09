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

import java.io.File;

import org.apache.commons.lang.Validate;

import org.jiemamy.composer.Exporter;
import org.jiemamy.composer.FileExportConfig;

/**
 * ファイルに対する{@link Exporter}に関する設定情報保持クラスの骨格実装。
 * 
 * @author daisuke
 */
public abstract class AbstractFileExportConfig implements FileExportConfig {
	
	/** 出力先ファイル */
	private File outputFile;
	
	/** 出力先ファイルが既に存在した場合、上書きするかどうか */
	private boolean overwrite;
	

	public File getOutputFile() {
		return outputFile;
	}
	
	public boolean isOverwrite() {
		return overwrite;
	}
	
	/**
	 * 出力ファイルを設定する。
	 * 
	 * @param outputFile 出力ファイル
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setOutputFile(File outputFile) {
		Validate.notNull(outputFile);
		this.outputFile = outputFile;
	}
	
	/**
	 * 出力ファイルが既に存在した場合、上書きするかどうかを設定する。
	 * 
	 * @param overwrite 上書きする場合は{@code true}、そうでない場合は{@code false}
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
}
