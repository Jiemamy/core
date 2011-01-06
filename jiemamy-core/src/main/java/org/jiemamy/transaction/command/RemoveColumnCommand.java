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
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.transaction.Command;
import org.jiemamy.transaction.EventBroker;

/**
 * {@link TableModel}から{@link ColumnModel}を削除するEDITコマンド。
 * 
 * @author daisuke
 */
public class RemoveColumnCommand extends AbstractRemoveFromCollectionCommand<DefaultTableModel, ColumnModel> {
	
	/** 削除元テーブル */
	private final DefaultTableModel tableModel;
	
	/** 削除されるカラム */
	private final ColumnModel columnModel;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param eventBroker イベント通知用{@link EventBroker}
	 * @param tableModel 削除元テーブル
	 * @param attributeModel 削除されるカラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public RemoveColumnCommand(EventBroker eventBroker, DefaultTableModel tableModel, ColumnModel attributeModel) {
		super(eventBroker, tableModel, attributeModel);
		Validate.notNull(tableModel);
		Validate.notNull(attributeModel);
		
		this.tableModel = tableModel;
		columnModel = attributeModel;
		
	}
	
	public Command getNegateCommand() {
		return new AddColumnCommand(getEventBroker(), tableModel, columnModel);
	}
	
	@Override
	public Collection<? super ColumnModel> getTargetCollection() {
		return tableModel.breachEncapsulationOfColumns();
	}
	
}
