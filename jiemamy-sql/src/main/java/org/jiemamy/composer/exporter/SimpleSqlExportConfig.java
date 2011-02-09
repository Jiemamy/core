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

/**
 * {@link SqlExporter}に関する設定情報保持クラス。
 * 
 * @author daisuke
 */
public final class SimpleSqlExportConfig extends AbstractFileExportConfig implements SqlExportConfig {
	
	/** CREATE SCHAMA文を出力するかどうか */
	private boolean emitCreateSchema;
	
	/** CREATE文の前にDROP文を出力するかどうか */
	private boolean emitDropStatements;
	
	/** 出力するデータセットのインデックス */
	private int dataSetIndex;
	

	public boolean emitCreateSchemaStatement() {
		return emitCreateSchema;
	}
	
	public boolean emitDropStatements() {
		return emitDropStatements;
	}
	
	public int getDataSetIndex() {
		return dataSetIndex;
	}
	
	/**
	 * 出力するデータセットのインデックスを設定する。
	 * 
	 * <p>INSERT文を出力しない場合は、負数を設定する。</p>
	 * 
	 * @param dataSetIndex 出力するデータセットのインデックス
	 */
	public void setDataSetIndex(int dataSetIndex) {
		this.dataSetIndex = dataSetIndex;
	}
	
	/**
	 * CREATE文の前にCREATE SCHAMA文を出力するかどうかを設定する。
	 * 
	 * @param emitCreateSchema 出力する場合は{@code true}、そうでない場合は{@code false}
	 */
	public void setEmitCreateSchema(boolean emitCreateSchema) {
		this.emitCreateSchema = emitCreateSchema;
	}
	
	/**
	 * CREATE文の前にDROP文を出力するかどうかを設定する。
	 * 
	 * @param emitDropStatements 出力する場合は{@code true}、そうでない場合は{@code false}
	 */
	public void setEmitDropStatements(boolean emitDropStatements) {
		this.emitDropStatements = emitDropStatements;
	}
}
