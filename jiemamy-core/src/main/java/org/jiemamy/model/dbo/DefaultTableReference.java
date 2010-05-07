/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/19
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
package org.jiemamy.model.dbo;

import java.util.UUID;

import org.jiemamy.model.DefaultEntityRef;

/**
 * {@link TableModel}に対する参照オブジェクトの実装。Artemisにおける{@link TableRef}の実装クラス。
 * 
 * @author daisuke
 */
public final class DefaultTableReference extends DefaultEntityRef<TableModel> implements TableRef {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param decl 定義オブジェクト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultTableReference(TableModel decl) {
		super(decl);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param referenceId 参照先のモデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultTableReference(UUID referenceId) {
		super(referenceId);
	}
	
}