/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2012/01/13
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

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleEmitConfig implements EmitConfig {
	
	final boolean emitCreateSchemaStatement;
	
	final boolean emitDropStatements;
	
	final int dataSetIndex;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param emitCreateSchemaStatement スキーマを出力する場合は{@code true}
	 * @param emitDropStatements ドロップ文を出力する場合は{@code ture}
	 * @param dataSetIndex データセットのインデックス
	 */
	public SimpleEmitConfig(boolean emitCreateSchemaStatement, boolean emitDropStatements, int dataSetIndex) {
		this.emitCreateSchemaStatement = emitCreateSchemaStatement;
		this.emitDropStatements = emitDropStatements;
		this.dataSetIndex = dataSetIndex;
	}
	
	public boolean emitCreateSchemaStatement() {
		return emitCreateSchemaStatement;
	}
	
	public boolean emitDropStatements() {
		return emitDropStatements;
	}
	
	public int getDataSetIndex() {
		return dataSetIndex;
	}
}
