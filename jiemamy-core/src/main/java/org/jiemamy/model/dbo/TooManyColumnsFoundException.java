/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/11
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

import java.util.Collection;

import org.jiemamy.model.ModelConsistencyException;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * {@link TableModel}に対するクエリの結果、該当する {@link ColumnModel} が複数見つかったことを表す例外クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
@SuppressWarnings("serial")
public class TooManyColumnsFoundException extends ModelConsistencyException {
	
	private final Collection<ColumnModel> columns;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param columns 見つかった複数のカラムの集合
	 */
	public TooManyColumnsFoundException(Collection<ColumnModel> columns) {
		super(String.valueOf(columns.size()));
		this.columns = columns;
	}
	
	/**
	 * 見つかったカラムの集合を返す。
	 * 
	 * @return 見つかったカラムの集合
	 */
	public Collection<ColumnModel> getColumns() {
		return columns;
	}
	
}