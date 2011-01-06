/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/18
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
package org.jiemamy.transaction.command;

import java.util.Collection;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.transaction.Command;
import org.jiemamy.transaction.EventBroker;

/**
 * {@link ColumnModel}を{@link TableModel}に追加するEDITコマンド。
 * 
 * @author daisuke
 */
public class AddColumnCommand extends AbstractAddToCollectionCommand<DefaultTableModel, ColumnModel> {
	
	/** 追加されるテーブル */
	private final DefaultTableModel tableModel;
	
	/** 追加する属性 */
	private final ColumnModel columnModel;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param eventBroker イベント通知用{@link EventBroker}
	 * @param tableModel 追加されるテーブル
	 * @param columnModel 追加するカラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AddColumnCommand(EventBroker eventBroker, DefaultTableModel tableModel, ColumnModel columnModel) {
		super(eventBroker, tableModel, columnModel);
		Validate.notNull(tableModel);
		Validate.notNull(columnModel);
		
		this.tableModel = tableModel;
		this.columnModel = columnModel;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param eventBroker イベント通知用{@link EventBroker}
	 * @param tableModel 追加されるテーブル
	 * @param index 追加位置をあらわすインデックス値
	 * @param columnModel 追加するカラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AddColumnCommand(EventBroker eventBroker, DefaultTableModel tableModel, int index, ColumnModel columnModel) {
		super(eventBroker, tableModel, columnModel, index);
		Validate.notNull(tableModel);
		Validate.notNull(columnModel);
		
		this.tableModel = tableModel;
		this.columnModel = columnModel;
	}
	
	public Command getNegateCommand() {
		return new RemoveColumnCommand(getEventBroker(), tableModel, columnModel);
	}
	
	@Override
	public Collection<? super ColumnModel> getTargetCollection() {
		return tableModel.breachEncapsulationOfColumns();
	}
	
}
