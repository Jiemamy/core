/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/03/31
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
import org.jiemamy.utils.sql.metadata.KeyMeta;
import org.jiemamy.utils.visitor.ForEachUtil.TypeSafeResultSetVisitor;

/**
 * 外部キーメタ情報から{@link JiemamyContext}にインポートを行うビジターインターフェイス。
 * 
 * @author daisuke
 */
public interface ForeignKeyImportVisitor extends TypeSafeResultSetVisitor<KeyMeta, Void, RuntimeException> {
	
	/**
	 * ビジターを初期化する。
	 * 
	 * @param context インポート先の{@link JiemamyContext}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	void initialize(JiemamyContext context);
	
}
