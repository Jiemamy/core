/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/06/22
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
package org.jiemamy.exception;

import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ForeignKey;
import org.jiemamy.model.entity.TableModel;

/**
 * 指定された名前の要素が見つからない時スローされる例外。
 * 
 * @since 0.2
 * @author daisuke
 */
@SuppressWarnings("serial")
public class ElementNotFoundException extends JiemamyRuntimeException {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param foreignKey 外部キーモデル
	 * @param referenceColumn 参照カラム
	 * @since 0.2
	 */
	public ElementNotFoundException(ForeignKey foreignKey, ColumnModel referenceColumn) {
		super(referenceColumn.getName() + "(" + referenceColumn.getId() + ")" + " is not found in " + foreignKey + ".");
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param rootModel ルートモデル
	 * @param entityName エンティティ名
	 * @since 0.2
	 *
	 * TODO 引数にRootModelを受け取るので、一旦コメントアウトします
	 *  
	public ElementNotFoundException(RootModel rootModel, String entityName) {
		super(entityName + " is not found in RootModel(" + rootModel.getId() + ").");
	}
	*/
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param tableModel 検索対象テーブル
	 * @param clazz 検索条件の型
	 * @since 0.2
	 */
	public ElementNotFoundException(TableModel tableModel, Class<? extends AttributeModel> clazz) {
		super(clazz.getName() + " is not found in Table(" + tableModel.getName() + ", " + tableModel.getId() + ").");
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param tableModel 検索対象親テーブル
	 * @param columnName カラム名
	 * @since 0.2
	 */
	public ElementNotFoundException(TableModel tableModel, String columnName) {
		super(columnName + " is not found in Table(" + tableModel.getName() + ", " + tableModel.getId() + ").");
	}
	
}
