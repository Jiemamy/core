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
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryCompositeEntityResolver;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.dddbase.OrderedOnMemoryRepository;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.DefaultDatabaseObjectModel;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.KeyConstraintModel;
import org.jiemamy.model.constraint.NotNullConstraintModel;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.parameter.ParameterMap;
import org.jiemamy.transaction.EventBroker;
import org.jiemamy.transaction.EventBrokerImpl;
import org.jiemamy.transaction.StoredEvent;
import org.jiemamy.utils.LogMarker;
import org.jiemamy.utils.UUIDUtil;

/**
 * テーブルモデル。
 * 
 * @author daisuke
 */
public/*final*/class DefaultTableModel extends DefaultDatabaseObjectModel implements TableModel {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultTableModel.class);
	

	private static Set<TableModel> filter(Set<DatabaseObjectModel> databaseObjects) {
		Set<TableModel> result = Sets.newHashSet();
		for (DatabaseObjectModel databaseObjectModel : databaseObjects) {
			if (databaseObjectModel instanceof TableModel) {
				result.add((TableModel) databaseObjectModel);
			}
		}
		return result;
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
	
	public boolean contains(EntityRef<?> ref) {
		return contains(ref.getReferentId());
	}
	
	public boolean contains(UUID id) {
		return new OnMemoryCompositeEntityResolver(columns, constraints).contains(id);
	}
	
	/**
	 * テーブルからカラムを削除する。
	 * 
	 * @param ref カラムへの参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException このテーブルが指定したカラムを管理していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ColumnModel deleteColumn(EntityRef<? extends ColumnModel> ref) {
		Validate.notNull(ref);
		ColumnModel deleted = columns.delete(ref);
		logger.info("column deleted: " + deleted);
		eventBroker.fireEvent(new StoredEvent<ColumnModel>(columns, deleted, null));
		return deleted;
	}
	
	/**
	 * テーブルから制約を削除する。
	 * 
	 * @param ref 制約への参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException このテーブルが指定した制約を管理していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ConstraintModel deleteConstraint(EntityRef<? extends ConstraintModel> ref) {
		Validate.notNull(ref);
		ConstraintModel deleted = constraints.delete(ref);
		logger.info("constraint deleted: " + deleted);
		eventBroker.fireEvent(new StoredEvent<ConstraintModel>(constraints, deleted, null));
		return deleted;
	}
	
	public KeyConstraintModel findReferencedKeyConstraint(ForeignKeyConstraintModel foreignKey) {
		Validate.notNull(foreignKey);
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
		Validate.notNull(databaseObjects);
		Set<DatabaseObjectModel> results = Sets.newHashSet();
		for (ForeignKeyConstraintModel foreignKey : getForeignKeyConstraintModels()) {
			KeyConstraintModel referencedKeyConstraint = foreignKey.findReferencedKeyConstraint(databaseObjects);
			if (referencedKeyConstraint != null) {
				TableModel declaringTable =
						referencedKeyConstraint.findDeclaringTable(DefaultTableModel.filter(databaseObjects));
				results.add(declaringTable);
			}
		}
		return results;
	}
	
	public ColumnModel getColumn(EntityRef<? extends ColumnModel> reference) {
		Validate.notNull(reference);
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
	
	@SuppressWarnings("unchecked")
	public <T extends ColumnModel>List<T> getColumns(Class<T> clazz) {
		Validate.notNull(clazz);
		List<T> result = Lists.newArrayList();
		for (ColumnModel column : getColumns()) {
			if (clazz.isInstance(column)) {
				result.add((T) column);
			}
		}
		return MutationMonitor.monitor(result);
	}
	
	public Set<? extends ConstraintModel> getConstraints() {
		return constraints.getEntitiesAsSet();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ConstraintModel>Set<T> getConstraints(Class<T> clazz) {
		Validate.notNull(clazz);
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
			EntityRef<? extends ColumnModel> columnRef = nn.getColumn();
			if (columnRef == null) {
				logger.warn("target column of NOT NULL is null: " + UUIDUtil.toShortString(nn.getId()));
				continue;
			}
			if (columnRef.equals(reference)) {
				return nn;
			}
		}
		return null;
	}
	
	public <T>T getParam(TableParameterKey<T> key) {
		return super.getParam(key);
	}
	
	public PrimaryKeyConstraintModel getPrimaryKey() {
		Iterable<PrimaryKeyConstraintModel> pks =
				Iterables.filter(constraints.getEntitiesAsSet(), PrimaryKeyConstraintModel.class);
		try {
			return Iterables.getOnlyElement(pks);
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
			EntityRef<? extends ColumnModel> columnRef = nn.getColumn();
			if (columnRef != null && columnRef.equals(ref)) {
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
			TableModel referenceTableModel = context.resolve(columnRef).findDeclaringTable(tables);
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
	
	/**
	 * エンティティ参照から、{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子エンティティも含む。</p>
	 * 
	 * @param <T> エンティティの型
	 * @param reference エンティティ参照
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 */
	@Deprecated
	public <T extends Entity>T resolve(EntityRef<T> reference) {
		return new OnMemoryCompositeEntityResolver(columns, constraints).resolve(reference);
	}
	
	/**
	 * 指定したIDを持つ{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子エンティティも含む。</p>
	 * 
	 * @param id ENTITY ID
	 * @return 見つかった{@link Entity}
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 * @since 1.0.0
	 */
	@Deprecated
	public Entity resolve(UUID id) {
		return new OnMemoryCompositeEntityResolver(columns, constraints).resolve(id);
	}
	
	/**
	 * テーブルにカラムを追加する。
	 * 
	 * @param column カラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(ColumnModel column) {
		Validate.notNull(column);
//		Validate.notNull(column.getName());
		ColumnModel old = columns.store(column);
		if (old == null) {
			logger.info(LogMarker.LIFECYCLE, "column stored: " + column);
		} else {
			logger.info(LogMarker.LIFECYCLE, "column updated: (old)" + old);
			logger.info(LogMarker.LIFECYCLE, "                (new)" + column);
		}
		eventBroker.fireEvent(new StoredEvent<ColumnModel>(columns, old, column));
	}
	
	/**
	 * テーブルに属性を追加する。
	 * 
	 * @param constraint 属性
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(ConstraintModel constraint) {
		Validate.notNull(constraint);
		ConstraintModel old = constraints.store(constraint);
		eventBroker.fireEvent(new StoredEvent<ConstraintModel>(constraints, old, constraint));
		if (old == null) {
			logger.info(LogMarker.LIFECYCLE, "constraint stored: " + constraint);
		} else {
			logger.info(LogMarker.LIFECYCLE, "constraint updated: (old)" + old);
			logger.info(LogMarker.LIFECYCLE, "                    (new)" + constraint);
		}
	}
	
	/**
	 * 指定した2つの位置にあるカラムの順序を入れ替える。
	 * 
	 * @param index1 the index of the {@link ColumnModel} to be swapped.
	 * @param index2 the index of the other {@link ColumnModel} to be swapped.
	 * @throws IndexOutOfBoundsException if either {@code index1} or {@code index2}
	 *         is out of range (index1 &lt; 0 || index1 &gt;= list.size()
	 *         || index2 &lt; 0 || index2 &gt;= list.size()).
	 */
	public void swapColumn(int index1, int index2) {
		columns.swap(index1, index2);
		eventBroker.fireEvent(new StoredEvent<ColumnModel>(columns, null, null));
	}
	
	@Override
	public EntityRef<? extends DefaultTableModel> toReference() {
		return new DefaultEntityRef<DefaultTableModel>(this);
	}
	
	/**
	 * {@link ParameterMap} を取得する。
	 * 
	 * <p>このメソッドは内部で保持している {@link ParameterMap} オブジェクトの参照を返すことにより
	 * 内部表現を暴露していることに注意すること。</p>
	 * 
	 * @return {@link ParameterMap}の内部参照
	 */
	ParameterMap breachEncapsulationOfParams() {
		return params;
	}
}
