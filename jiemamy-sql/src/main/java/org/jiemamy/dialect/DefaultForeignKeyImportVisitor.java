/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/03/30
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

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.DefaultDeferrabilityModel;
import org.jiemamy.model.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.constraint.DeferrabilityModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel.ReferentialAction;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.utils.sql.metadata.KeyMeta;
import org.jiemamy.utils.visitor.AbstractCollectionVisitor;

/**
 * {@link ForeignKeyImportVisitor}のデフォルト実装クラス。
 * 
 * @author daisuke
 */
public class DefaultForeignKeyImportVisitor extends AbstractCollectionVisitor<KeyMeta, Void, RuntimeException>
		implements ForeignKeyImportVisitor {
	
	/** 書き込み先モデル */
	private JiemamyContext context;
	
	private Map<String, DefaultForeignKeyConstraintModel> importedForeignKeys = Maps.newHashMap();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param dialect
	 */
	public DefaultForeignKeyImportVisitor(Dialect dialect) {
	}
	
	public void initialize(JiemamyContext context) {
		Validate.notNull(context);
		this.context = context;
	}
	
	public Void visit(KeyMeta keys) {
		Validate.notNull(keys);
		DefaultTableModel constrainedTable = (DefaultTableModel) context.getTable(keys.fkTableName);
		DefaultTableModel referenceTable = (DefaultTableModel) context.getTable(keys.pkTableName);
		
		if (constrainedTable != null && referenceTable != null) {
			DefaultForeignKeyConstraintModel foreignKey = importedForeignKeys.get(keys.fkName);
			if (foreignKey == null) {
				foreignKey = new DefaultForeignKeyConstraintModel(UUID.randomUUID());
				foreignKey.setName(keys.fkName);
				importedForeignKeys.put(keys.fkName, foreignKey);
			}
			ColumnModel fkColumn = constrainedTable.getColumn(keys.fkColumnName);
			ColumnModel pkColumn = referenceTable.getColumn(keys.pkColumnName);
			foreignKey.addReferencing(fkColumn.toReference(), pkColumn.toReference());
			
			if (keys.updateRule != null) {
				ReferentialAction onUpdate = ReferentialAction.valueOf(keys.updateRule.name());
				foreignKey.setOnUpdate(onUpdate);
			}
			
			if (keys.deleteRule != null) {
				ReferentialAction onDelete = ReferentialAction.valueOf(keys.deleteRule.name());
				foreignKey.setOnDelete(onDelete);
			}
			
			DeferrabilityModel deferrability = DefaultDeferrabilityModel.fromDeferrability(keys.deferrability);
			foreignKey.setDeferrability(deferrability);
			
			constrainedTable.store(foreignKey);
		}
		context.store(constrainedTable);
		return null;
	}
}
