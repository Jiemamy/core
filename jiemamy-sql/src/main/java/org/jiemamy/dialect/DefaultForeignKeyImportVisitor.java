/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.constraint.JmDeferrability;
import org.jiemamy.model.constraint.JmDeferrability;
import org.jiemamy.model.constraint.ReferentialAction;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.table.JmTable;
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
	
	private Map<String, JmForeignKeyConstraint> importedForeignKeys = Maps.newHashMap();
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param dialect {@link Dialect}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultForeignKeyImportVisitor(Dialect dialect) {
		Validate.notNull(dialect);
		// 現状dialectは無視する。
		// DefaultDbObjectImportVisitorとの対称性を維持するために引数に持っている。
	}
	
	public void initialize(JiemamyContext context) {
		Validate.notNull(context);
		this.context = context;
	}
	
	public Void visit(KeyMeta keys) {
		Validate.notNull(keys);
		JmTable constrainedTable = (JmTable) context.getTable(keys.fkTableName);
		JmTable referenceTable = (JmTable) context.getTable(keys.pkTableName);
		
		if (constrainedTable != null && referenceTable != null) {
			JmForeignKeyConstraint foreignKey = importedForeignKeys.get(keys.fkName);
			if (foreignKey == null) {
				foreignKey = new JmForeignKeyConstraint();
				foreignKey.setName(keys.fkName);
				importedForeignKeys.put(keys.fkName, foreignKey);
			}
			JmColumn fkColumn = constrainedTable.getColumn(keys.fkColumnName);
			JmColumn pkColumn = referenceTable.getColumn(keys.pkColumnName);
			foreignKey.addReferencing(fkColumn.toReference(), pkColumn.toReference());
			
			if (keys.updateRule != null) {
				ReferentialAction onUpdate = ReferentialAction.valueOf(keys.updateRule.name());
				foreignKey.setOnUpdate(onUpdate);
			}
			
			if (keys.deleteRule != null) {
				ReferentialAction onDelete = ReferentialAction.valueOf(keys.deleteRule.name());
				foreignKey.setOnDelete(onDelete);
			}
			
			JmDeferrability deferrability = JmDeferrability.fromDeferrability(keys.deferrability);
			foreignKey.setDeferrability(deferrability);
			
			constrainedTable.store(foreignKey);
		}
		context.store(constrainedTable);
		return null;
	}
}
