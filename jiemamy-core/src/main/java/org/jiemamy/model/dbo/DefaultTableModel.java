/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/06/09
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.Validate;

import org.jiemamy.Entity;
import org.jiemamy.EntityRef;
import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.model.ModelConsistencyException;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ConstraintModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * テーブルモデル。
 * 
 * @author daisuke
 */
public class DefaultTableModel extends AbstractDatabaseObjectModel implements TableModel {
	
	/**
	 * {@code tables}の中から、このカラムが所属するテーブルを取得する。
	 * 
	 * @param tables 対象{@link TableModel}
	 * @param columnModel 対象カラム
	 * @return この属性が所属するテーブル. どのテーブルにも所属していない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	static TableModel findDeclaringTable(Collection<TableModel> tables, final ColumnModel columnModel) {
		Collection<TableModel> select = CollectionUtils.select(tables, new Predicate<TableModel>() {
			
			public boolean evaluate(TableModel tableModel) {
				return tableModel.getColumns().contains(columnModel);
			}
		});
		if (select.size() == 1) {
			return select.iterator().next();
		}
		if (select.size() == 0) {
			return null;
		}
		throw new TooManyTablesFoundException(select);
	}
	
	static DatabaseObjectModel findReferencedDatabaseObject(Collection<DatabaseObjectModel> databaseObjects,
			ForeignKeyConstraintModel foreignKey) {
		if (foreignKey.getReferenceColumns().size() == 0) {
			throw new ModelConsistencyException();
		}
		EntityRef<? extends ColumnModel> columnRef = foreignKey.getReferenceColumns().get(0);
		
		for (DatabaseObjectModel databaseObject : databaseObjects) {
			for (Entity entity : databaseObject.getSubEntities()) {
				if (columnRef.isReferenceOf(entity)) {
					return databaseObject;
				}
			}
		}
		return null;
	}
	
	/**
	 * {@code databaseObjects}の中から、指定した外部キーが参照するキー制約を取得する。
	 * 
	 * @param databaseObjects 対象{@link DatabaseObjectModel}
	 * @param foreignKey 対象外部キー
	 * @return 指定した外部キーが参照するキー. 該当するキーが存在しなかった場合、{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	static KeyConstraintModel findReferencedKeyConstraint(Collection<DatabaseObjectModel> databaseObjects,
			ForeignKeyConstraintModel foreignKey) {
		if (foreignKey.getReferenceColumns().size() == 0) {
			throw new ModelConsistencyException();
		}
		EntityRef<? extends ColumnModel> columnRef = foreignKey.getReferenceColumns().get(0);
		
		for (DatabaseObjectModel databaseObject : databaseObjects) {
			for (Entity entity : databaseObject.getSubEntities()) {
				if (columnRef.isReferenceOf(entity)) {
					if (databaseObject instanceof TableModel) {
						TableModel tableModel = (TableModel) databaseObject;
						return tableModel.findReferencedKeyConstraint(foreignKey);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * {@code databaseObjects}の中から、全ての{@link TableModel}を返す。
	 * 
	 * @param databaseObjects 対象{@link DatabaseObjectModel}
	 * @return 全ての{@link TableModel}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	static Collection<TableModel> findTables(Collection<DatabaseObjectModel> databaseObjects) {
		Validate.notNull(databaseObjects);
		List<TableModel> result = new ArrayList<TableModel>();
		for (Entity databaseObject : databaseObjects) {
			if (databaseObject instanceof TableModel) {
				result.add((TableModel) databaseObject);
			}
		}
		return result;
	}
	

	/** カラムのリスト */
	List<ColumnModel> columns = CollectionsUtil.newArrayList();
	
	/** 制約のリスト */
	List<ConstraintModel> constraints = CollectionsUtil.newArrayList();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultTableModel(UUID id) {
		super(id);
	}
	
	/**
	 * テーブルにカラムを追加する。
	 * 
	 * @param column カラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void addColumn(ColumnModel column) {
		Validate.notNull(column);
		if (columns.contains(column)) {
			columns.set(columns.indexOf(column), column.clone());
		} else {
			columns.add(column.clone());
		}
	}
	
	/**
	 * テーブルに属性を追加する。
	 * 
	 * @param constraint 属性
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void addConstraint(ConstraintModel constraint) {
		Validate.notNull(constraint);
		constraints.add(constraint); // TODO add or replace
	}
	
	@Override
	public DefaultTableModel clone() {
		DefaultTableModel clone = (DefaultTableModel) super.clone();
		clone.columns = CollectionsUtil.newArrayList();
		for (ColumnModel column : columns) {
			clone.columns.add(column.clone());
		}
		clone.constraints = CollectionsUtil.newArrayList(constraints);
		return clone;
	}
	
	public KeyConstraintModel findReferencedKeyConstraint(ForeignKeyConstraintModel foreignKey) {
		for (KeyConstraintModel keyConstraint : getKeyConstraintModels()) {
			// サイズ不一致であれば、そもそもこのキーを参照したものではない
			if (keyConstraint.getKeyColumns().size() != foreignKey.getReferenceColumns().size()) {
				continue;
			}
			
			if (keyConstraint.getKeyColumns().containsAll(foreignKey.getReferenceColumns())) {
				return keyConstraint;
			}
		}
		return null;
	}
	
	@Override
	public Set<DatabaseObjectModel> findSuperDatabaseObjectsNonRecursive(Set<DatabaseObjectModel> databaseObjects) {
		Set<DatabaseObjectModel> results = CollectionsUtil.newHashSet();
		for (ForeignKeyConstraintModel foreignKey : getForeignKeyConstraintModels()) {
			results.add(findReferencedDatabaseObject(databaseObjects, foreignKey));
		}
		return results;
	}
	
	public ColumnModel getColumn(EntityRef<? extends ColumnModel> reference) {
		for (ColumnModel column : columns) {
			if (reference.isReferenceOf(column)) {
				return column;
			}
		}
		throw new ColumnNotFoundException();
	}
	
	public ColumnModel getColumn(final String name) {
		assert columns != null;
		Collection<ColumnModel> c = CollectionUtils.select(columns, new Predicate<ColumnModel>() {
			
			public boolean evaluate(ColumnModel col) {
				return col.getName().equals(name);
			}
		});
		if (c.size() == 1) {
			return c.iterator().next();
		}
		if (c.size() == 0) {
			throw new ColumnNotFoundException();
		}
		throw new TooManyColumnsFoundException(c);
	}
	
	public List<ColumnModel> getColumns() {
		assert columns != null;
		return columns;
	}
	
	public List<? extends ConstraintModel> getConstraints() {
		assert constraints != null;
		return constraints;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ConstraintModel>List<T> getConstraints(Class<T> clazz) {
		List<T> result = CollectionsUtil.newArrayList();
		for (ConstraintModel constraint : getConstraints()) {
			if (clazz.isInstance(constraint)) {
				result.add((T) constraint);
			}
		}
		return result;
	}
	
	public Collection<? extends ForeignKeyConstraintModel> getForeignKeyConstraintModels() {
		return findAttribute(ForeignKeyConstraintModel.class);
	}
	
	public Collection<? extends KeyConstraintModel> getKeyConstraintModels() {
		return findAttribute(KeyConstraintModel.class);
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return getColumns();
	}
	
	@Override
	public boolean isSubDatabaseObjectsNonRecursiveOf(Set<DatabaseObjectModel> databaseObjects,
			DatabaseObjectModel target, JiemamyContext context) {
		Validate.notNull(databaseObjects);
		Validate.notNull(target);
		Validate.notNull(context);
		Collection<TableModel> tables = findTables(databaseObjects);
		for (ForeignKeyConstraintModel foreignKey : getForeignKeyConstraintModels()) {
			if (foreignKey.getReferenceColumns().size() == 0) {
				continue;
			}
			EntityRef<? extends ColumnModel> columnRef = foreignKey.getReferenceColumns().get(0);
			TableModel referenceTableModel = findDeclaringTable(tables, context.resolve(columnRef));
			if (referenceTableModel.equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * テーブルからカラムを削除する。
	 * 
	 * @param ref カラム参照
	 * @throws EntityNotFoundException 指定した参照が表すカラムがこのテーブルに存在しない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeColumn(EntityRef<? extends ColumnModel> ref) {
		Validate.notNull(ref);
		for (ColumnModel column : CollectionsUtil.newArrayList(columns)) {
			if (ref.isReferenceOf(column)) {
				columns.remove(column);
				return;
			}
		}
		throw new EntityNotFoundException();
	}
	
	/**
	 * テーブルから属性を削除する。
	 * 
	 * @param attribute 属性
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeConstraint(ConstraintModel attribute) {
		Validate.notNull(attribute);
		constraints.remove(attribute);
	}
	
	public EntityRef<DefaultTableModel> toReference() {
		return new DefaultEntityRef<DefaultTableModel>(this);
	}
	
	private <T extends ConstraintModel>Collection<T> findAttribute(Class<T> clazz) {
		Collection<T> result = new ArrayList<T>();
		for (ConstraintModel attribute : constraints) {
			if (clazz.isAssignableFrom(attribute.getClass())) {
				result.add(clazz.cast(attribute));
			}
		}
		return result;
	}
}
