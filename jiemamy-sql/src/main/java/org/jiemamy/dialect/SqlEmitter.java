/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

import java.util.List;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.sql.SqlStatement;

/**
 * {@link JiemamyContext}からSQLのリストを生成するエミッタインターフェイス。
 * 
 * @author daisuke
 */
public interface SqlEmitter {
	
	/**
	 * RootModelからSQLのリストを生成する。
	 * 
	 * @param rootModel 対象{@link JiemamyContext}
	 * @param config 設定 
	 * @return SQLのリスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	List<SqlStatement> emit(JiemamyContext rootModel, EmitConfig config);
	
}