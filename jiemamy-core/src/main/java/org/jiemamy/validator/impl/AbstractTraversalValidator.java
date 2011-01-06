/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/01/21
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
package org.jiemamy.validator.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.KeyConstraintModel;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.UniqueKeyConstraintModel;
import org.jiemamy.model.dataset.DataSetModel;
import org.jiemamy.model.dataset.RecordModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * 全モデルをトラバースするバリデータの骨格実装。
 * 
 * @param <T> 識別子として使用する型
 * @author daisuke
 */
public abstract class AbstractTraversalValidator<T> extends AbstractValidator {
	
	private static final String TABLE = "table";
	

	private static String index(int i) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(i).append("]");
		return sb.toString();
	}
	

	/** 結果として出力する問題点の集合 */
	protected Collection<Problem> result;
	
	/** 識別子：位置情報文字列のマップ */
	protected Map<T, String> ids;
	
	protected JiemamyContext context;
	

	public Collection<? extends Problem> validate(JiemamyContext context) {
		this.context = context;
		result = Lists.newArrayList();
		ids = Maps.newHashMap();
		check(context, "rootModel");
		
		int i = 0;
		for (DatabaseObjectModel entityModel : context.getDatabaseObjects()) {
			check(entityModel, "entity" + index(i));
			if (entityModel instanceof TableModel) {
				TableModel tableModel = (TableModel) entityModel;
				int j = 0;
				for (ColumnModel columnModel : tableModel.getColumns()) {
					check(columnModel, TABLE + index(i) + "/column" + index(j));
					check(columnModel.getDataType(), TABLE + index(i) + "/column" + index(j) + "/dataType");
					j++;
				}
				j = 0;
				for (ConstraintModel attributeModel : tableModel.getConstraints()) {
					check(attributeModel, TABLE + index(i) + "/constraint" + index(j));
					checkConstraint(i, j, attributeModel);
					j++;
				}
			}
			i++;
		}
		
		i = 0;
		for (DataSetModel dataSetModel : context.getDataSets()) {
			check(dataSetModel, "dataSet" + index(i));
			int j = 0;
			for (Map.Entry<EntityRef<? extends TableModel>, List<RecordModel>> entry : dataSetModel.getRecords()
				.entrySet()) {
				check(entry.getKey(), "dataSet" + index(i) + "/tableRef");
				for (RecordModel recordModel : entry.getValue()) {
					check(recordModel, "dataSet" + index(i) + "/record" + index(j));
					j++;
				}
			}
			i++;
		}
		
		return result;
	}
	
	abstract void check(Object element, String pos);
	
	private void checkConstraint(int i, int j, ConstraintModel constraintModel) {
		if (constraintModel instanceof KeyConstraintModel) {
			if (constraintModel instanceof PrimaryKeyConstraintModel) {
				PrimaryKeyConstraintModel primaryKey = (PrimaryKeyConstraintModel) constraintModel;
				int k = 0;
				for (EntityRef<? extends ColumnModel> columnRef : primaryKey.getKeyColumns()) {
					check(columnRef, TABLE + index(i) + "/pk" + index(j) + "/columnRef" + index(k));
					k++;
				}
			} else if (constraintModel instanceof UniqueKeyConstraintModel) {
				UniqueKeyConstraintModel uniqueKey = (UniqueKeyConstraintModel) constraintModel;
				int k = 0;
				for (EntityRef<? extends ColumnModel> columnRef : uniqueKey.getKeyColumns()) {
					check(columnRef, TABLE + index(i) + "/uk" + index(j) + "/columnRef" + index(k));
					k++;
				}
			} else if (constraintModel instanceof ForeignKeyConstraintModel) {
				ForeignKeyConstraintModel foreignKey = (ForeignKeyConstraintModel) constraintModel;
				int k = 0;
				for (EntityRef<? extends ColumnModel> columnRef : foreignKey.getKeyColumns()) {
					check(columnRef, TABLE + index(i) + "/fk" + index(j) + "/columnRef" + index(k));
					k++;
				}
				
				k = 0;
				for (EntityRef<? extends ColumnModel> columnRef : foreignKey.getReferenceColumns()) {
					check(columnRef, TABLE + index(i) + "/fk" + index(j) + "/refColumnRef" + index(k));
					k++;
				}
			}
		} else if (constraintModel instanceof CheckConstraintModel) {
			CheckConstraintModel checkConstraintModel = (CheckConstraintModel) constraintModel;
			check(checkConstraintModel.getExpression(), TABLE + index(i) + "/check" + index(j) + "/expression");
		}
	}
	
}
