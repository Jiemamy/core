/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2012/01/23
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
package org.jiemamy.diff;

import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.table.JmTable;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class ChangeDataType implements DiffElement {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param leftTable
	 * @param leftColumn
	 * @param dataType
	 */
	public ChangeDataType(JmTable table, JmColumn column, DataType newDataType) {
		// TODO Auto-generated constructor stub
	}
	
}