/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.model.table;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.JiemamyContext;
import org.jiemamy.TableNotFoundException;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.dddbase.OrderedOnMemoryRepository;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.DefaultDatabaseObjectModel;
import org.jiemamy.model.ModelConsistencyException;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.KeyConstraintModel;
import org.jiemamy.model.constraint.NotNullConstraintModel;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.transaction.EventBroker;
import org.jiemamy.transaction.EventBrokerImpl;
import org.jiemamy.transaction.StoredEvent;

/**
 * テーブルモデル。
 * 
 * @author daisuke
 */
public/*final*/class DefaultTableModel extends DefaultDatabaseObjectModel implements TableModel {
	
	/**
	 * {@code tables}の中から、このカラムが所属するテーブルを取得する。
	 * 
	 * @param tables 対象{@link TableModel}
	 * @param columnModel 対象カラム
	 * @return この属性が所属するテーブル. どのテーブルにも所属していない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	static TableModel findDeclaringTable(Collection<TableModel> tables, final ColumnModel columnModel) {
		Collection<TableModel> c = Collections2.filter(tables, new Predicate<TableModel>() {
			
			public boolean apply(TableModel tableModel) {
				return tableModel.getColumns().contains(columnModel);
			}
		});
		
		try {
			return Iterables.getOnlyElement(c);
		} catch (NoSuchElementException e) {
			throw new TableNotFoundException("column=" + columnModel);
		} catch (IllegalArgumentException e) {
			throw new TooManyTablesFoundException(c);
		}
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
	

	/** カラムのリスト */
	private OrderedOnMemoryRepository<ColumnModel> columns = new OrderedOnMemoryRepository<ColumnModel>();
	
	/** 制約のリスト */
	private OnMemoryRepository<ConstraintModel> constraints = new OnMemoryRepository<ConstraintModel>();
	
	private final EventBroker eventBroker = new EventBrokerImpl();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultTableModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultTableModel clone() {
		DefaultTableModel clone = (DefaultTableModel) super.clone();
		clone.columns = columns.clone();
		clone.constraints = constraints.clone();
		return clone;
	}
	
	/**
	 * テーブルからカラムを削除する。
	 * 
	 * @param ref カラム参照
	 * @throws EntityNotFoundException 指定した参照が表すカラムがこのテーブルに存在しない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void delete(EntityRef<? extends ColumnModel> ref) {
		Validate.notNull(ref);
		ColumnModel deleted = columns.delete(ref);
		eventBroker.fireEvent(new StoredEvent<ColumnModel>(this, deleted, null));
	}
	
	/**
	 * テーブルから制約を削除する。
	 * 
	 * @param ref 制約
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void deleteConstraint(EntityRef<? extends ConstraintModel> ref) {
		constraints.delete(ref);
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
		Set<DatabaseObjectModel> results = Sets.newHashSet();
		for (ForeignKeyConstraintModel foreignKey : getForeignKeyConstraintModels()) {
			results.add(findReferencedDatabaseObject(databaseObjects, foreignKey));
		}
		return results;
	}
	
	public ColumnModel getColumn(EntityRef<? extends ColumnModel> reference) {
		return resolve(reference);
	}
	
	public ColumnModel getColumn(final String name) {
		assert columns != null;
		Collection<ColumnModel> c = Collections2.filter(columns.getEntitiesAsList(), new Predicate<ColumnModel>() {
			
			public boolean apply(ColumnModel col) {
				return col.getName().equals(name);
			}
		});
		
		try {
			return Iterables.getOnlyElement(c);
		} catch (NoSuchElementException e) {
			throw new ColumnNotFoundException("name=" + name);
		} catch (IllegalArgumentException e) {
			throw new TooManyColumnsFoundException(c);
		}
	}
	
	public List<ColumnModel> getColumns() {
		assert columns != null;
		return columns.getEntitiesAsList();
	}
	
	public Set<? extends ConstraintModel> getConstraints() {
		return constraints.getEntitiesAsSet();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ConstraintModel>Set<T> getConstraints(Class<T> clazz) {
		Set<T> result = Sets.newHashSet();
		for (ConstraintModel constraint : getConstraints()) {
			if (clazz.isInstance(constraint)) {
				result.add((T) constraint);
			}
		}
		return MutationMonitor.monitor(result);
	}
	
	/**
	 * {@link EventBroker}を取得する。
	 * 
	 * @return {@link EventBroker}
	 */
	public EventBroker getEventBroker() {
		return eventBroker;
	}
	
	public Collection<? extends ForeignKeyConstraintModel> getForeignKeyConstraintModels() {
		return getConstraints(ForeignKeyConstraintModel.class);
	}
	
	public Collection<? extends KeyConstraintModel> getKeyConstraintModels() {
		return getConstraints(KeyConstraintModel.class);
	}
	
	public NotNullConstraintModel getNotNullConstraintFor(EntityRef<? extends ColumnModel> reference) {
		Validate.notNull(reference);
		for (NotNullConstraintModel nn : getConstraints(NotNullConstraintModel.class)) {
			if (nn.getColumn().equals(reference)) {
				return nn;
			}
		}
		return null;
	}
	
	public <T>T getParam(TableParameterKey<T> key) {
		return super.getParam(key);
	}
	
	public PrimaryKeyConstraintModel getPrimaryKey() {
		Collection<ConstraintModel> pks =
				Collections2.filter(constraints.getEntitiesAsSet(),
						Predicates.instanceOf(PrimaryKeyConstraintModel.class));
		try {
			return (PrimaryKeyConstraintModel) Iterables.getOnlyElement(pks);
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return Lists.newArrayList(Iterables.concat(getColumns(), getConstraints()));
	}
	
	public boolean isNotNullColumn(EntityRef<? extends ColumnModel> ref) {
		Collection<NotNullConstraintModel> nns = getConstraints(NotNullConstraintModel.class);
		for (NotNullConstraintModel nn : nns) {
			if (nn.getColumn().equals(ref)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPrimaryKeyColumn(EntityRef<? extends ColumnModel> ref) {
		PrimaryKeyConstraintModel primaryKey = getPrimaryKey();
		if (primaryKey == null) {
			return false;
		}
		return primaryKey.getKeyColumns().contains(ref);
	}
	
	@Override
	public boolean isSubDatabaseObjectsNonRecursiveOf(DatabaseObjectModel target, JiemamyContext context) {
		Validate.notNull(target);
		Validate.notNull(context);
		Collection<TableModel> tables = context.getTables();
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
	 * パラメータを追加する。
	 * 
	 * @param key キー
	 * @param value 値
	 * @param <T> 値の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>void putParam(TableParameterKey<T> key, T value) {
		super.putParam(key, value);
	}
	
	/**
	 * パラメータを削除する。
	 * 
	 * @param key キー
	 */
	public void removeParam(TableParameterKey<?> key) {
		super.removeParam(key);
	}
	
	public <T extends Entity>T resolve(EntityRef<T> reference) {
		return columns.resolve(reference);
	}
	
	public Entity resolve(UUID id) {
		return columns.resolve(id);
	}
	
	/**
	 * テーブルにカラムを追加する。
	 * 
	 * @param column カラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(ColumnModel column) {
		Validate.notNull(column);
		columns.store(column);
		eventBroker.fireEvent(new StoredEvent<ColumnModel>(this, null, column));
	}
	
	/**
	 * テーブルに属性を追加する。
	 * 
	 * @param constraint 属性
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(ConstraintModel constraint) {
		constraints.store(constraint);
	}
	
	public EntityRef<DefaultTableModel> toReference() {
		return new DefaultEntityRef<DefaultTableModel>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (JiemamyContext.isDebug()) {
			sb.append(ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE));
		} else {
			sb.append("Table ").append(getName());
		}
		return sb.toString();
	}
}
