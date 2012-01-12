/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import org.jiemamy.JiemamyContext;

/**
 * {@link JiemamyContext}をSQL化する際に必要となる設定情報を供給するインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface EmitConfig {
	
	/**
	 * CREATE SCHEMA文を出力するかどうかを取得する。
	 * 
	 * @return 出力する場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.3
	 */
	boolean emitCreateSchemaStatement();
	
	/**
	 * CREATE文の前にDROP文を出力するかどうかを取得する。
	 * 
	 * @return　出力する場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.3
	 */
	boolean emitDropStatements();
	
	/**
	 * 出力するデータセットのインデックスを取得する。
	 * 
	 * @return 出力するデータセットのインデックス。出力しない場合は負数。未設定の場合は{@code -1}を返す。
	 * @since 0.3
	 */
	int getDataSetIndex();
	
}
