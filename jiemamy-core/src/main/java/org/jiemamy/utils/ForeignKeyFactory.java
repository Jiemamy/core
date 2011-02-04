/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/17
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
package org.jiemamy.utils;

import java.util.UUID;

import com.google.common.collect.Iterables;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.constraint.LocalKeyConstraintModel;
import org.jiemamy.model.table.ColumnNotFoundException;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.model.table.TooManyColumnsFoundException;

/**
 * {@link DefaultForeignKeyConstraintModel}に適切なデフォルト値を与えるファクトリ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class ForeignKeyFactory {
	
	/**
	 * 指定したテーブルからテーブルに貼られるデフォルト値を持った {@link DefaultForeignKeyConstraintModel} を生成して返す。
	 * 
	 * @param context {@link JiemamyContext}
	 * @param declaringTable 制約の定義先テーブル
	 * @param referenceTable 制約が参照するテーブル
	 * @return 新しい {@link DefaultForeignKeyConstraintModel}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static DefaultForeignKeyConstraintModel create(JiemamyContext context, TableModel declaringTable,
			TableModel referenceTable) {
		return setup(new DefaultForeignKeyConstraintModel(UUID.randomUUID()), context, declaringTable, referenceTable);
	}
	
	/**
	 * 入力の{@code fk}に対して指定したテーブルからテーブルに貼られるデフォルト値を設定する。
	 * 
	 * @param fk 設定対象外部キー
	 * @param context {@link JiemamyContext}
	 * @param declaringTable 制約の定義先テーブル
	 * @param referenceTable 制約が参照するテーブル
	 * @return {@code fk}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static DefaultForeignKeyConstraintModel setup(DefaultForeignKeyConstraintModel fk, JiemamyContext context,
			TableModel declaringTable, TableModel referenceTable) {
		Validate.notNull(context);
		Validate.notNull(declaringTable);
		Validate.notNull(referenceTable);
		
		LocalKeyConstraintModel key = referenceTable.getPrimaryKey();
		if (key == null) {
			Iterable<LocalKeyConstraintModel> lks =
					Iterables.filter(referenceTable.getConstraints(), LocalKeyConstraintModel.class);
			try {
				key = Iterables.get(lks, 0);
			} catch (IndexOutOfBoundsException e) {
				// ignore
			}
		}
		if (key == null) {
			throw new IllegalArgumentException("no local keys in " + referenceTable);
		}
		
		fk.clearKeyColumns();
		
		int index = 0;
		for (EntityRef<? extends ColumnModel> referenceColumnRef : key.getKeyColumns()) {
			ColumnModel referenceColumn = context.resolve(referenceColumnRef);
			
			ColumnModel keyColumn =
					getColumn(declaringTable, referenceTable.getName() + "_" + referenceColumn.getName(),
							referenceTable.getName());
			if (keyColumn == null) {
				try {
					keyColumn = Iterables.get(declaringTable.getColumns(), index++);
				} catch (IndexOutOfBoundsException e) {
					throw new IllegalArgumentException();
				}
			}
			fk.addReferencing(keyColumn.toReference(), referenceColumnRef);
		}
		return fk;
	}
	
	private static ColumnModel getColumn(TableModel table, String... names) {
		for (String name : names) {
			try {
				return table.getColumn(name);
			} catch (ColumnNotFoundException e) {
				// ignore
			} catch (TooManyColumnsFoundException e) {
				return Iterables.get(e.getColumns(), 0);
			}
		}
		return null;
	}
	
	private ForeignKeyFactory() {
	}
}
