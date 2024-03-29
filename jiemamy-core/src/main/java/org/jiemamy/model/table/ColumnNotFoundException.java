/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/05/10
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
package org.jiemamy.model.table;

import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.model.column.JmColumn;

/**
 * {@link JmTable}に対するクエリの結果、該当する {@link JmColumn} が見つからなかったことを表す例外クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
@SuppressWarnings("serial")
public class ColumnNotFoundException extends EntityNotFoundException {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param predicate 条件など
	 */
	public ColumnNotFoundException(String predicate) {
		super(predicate);
	}
	
}
