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
import org.jiemamy.model.DbObject;
import org.jiemamy.model.SimpleDbObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.constraint.JmConstraint;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmKeyConstraint;
import org.jiemamy.model.constraint.JmNotNullConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
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
public/*final*/class SimpleJmTable extends SimpleDbObject implements JmTable {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmTable.class);
	
	/** カラムのリスト */
	private OrderedOnMemoryRepository<JmColumn> columns = new OrderedOnMemoryRepository<JmColumn>();
	
	/** 制約のリスト */
	private OnMemoryRepository<JmConstraint> constraints = new OnMemoryRepository<JmConstraint>();
	
	private final EventBroker eventBroker = new EventBrokerImpl();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public SimpleJmTable() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public SimpleJmTable(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmTable clone() {
		SimpleJmTable clone = (SimpleJmTable) super.clone();
		clone.columns = columns.clone();
		clone.constraints = constraints.clone();
		return clone;
	}
	
	public boolean contains(EntityRef<?> reference) {
		Validate.notNull(reference);
		return contains(reference.getReferentId());
	}
	
	public boolean contains(UUID id) {
		return new OnMemoryCompositeEntityResolver(columns, constraints).contains(id);
	}
	
	/**
	 * テーブルからカラムを削除する。
	 * 
	 * @param reference カラムへの参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException このテーブルが指定したカラムを管理していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmColumn deleteColumn(EntityRef<? extends JmColumn> reference) {
		Validate.notNull(reference);
		JmColumn deleted = columns.delete(reference);
		logger.info("column deleted: " + deleted);
		eventBroker.fireEvent(new StoredEvent<JmColumn>(columns, deleted, null));
		return deleted;
	}
	
	/**
	 * テーブルから制約を削除する。
	 * 
	 * @param reference 制約への参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException このテーブルが指定した制約を管理していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmConstraint deleteConstraint(EntityRef<? extends JmConstraint> reference) {
		Validate.notNull(reference);
		JmConstraint deleted = constraints.delete(reference);
		logger.info("constraint deleted: " + deleted);
		eventBroker.fireEvent(new StoredEvent<JmConstraint>(constraints, deleted, null));
		return deleted;
	}
	
	public JmKeyConstraint findReferencedKeyConstraint(JmForeignKeyConstraint foreignKey) {
		Validate.notNull(foreignKey);
		for (JmKeyConstraint keyConstraint : getKeyConstraints()) {
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
	public Set<DbObject> findSuperDbObjectsNonRecursive(Set<DbObject> dbObjects) {
		Validate.notNull(dbObjects);
		Set<DbObject> results = Sets.newHashSet();
		for (JmForeignKeyConstraint foreignKey : getForeignKeyConstraints()) {
			JmKeyConstraint referencedKeyConstraint = foreignKey.findReferencedKeyConstraint(dbObjects);
			if (referencedKeyConstraint != null) {
				Iterable<JmTable> tables = Iterables.filter(dbObjects, JmTable.class);
				JmTable declaringTable = referencedKeyConstraint.findDeclaringTable(tables);
				results.add(declaringTable);
			}
		}
		return results;
	}
	
	public JmColumn getColumn(EntityRef<? extends JmColumn> reference) {
		Validate.notNull(reference);
		return resolve(reference);
	}
	
	public JmColumn getColumn(final String name) {
		assert columns != null;
		Collection<JmColumn> c = Collections2.filter(columns.getEntitiesAsList(), new Predicate<JmColumn>() {
			
			public boolean apply(JmColumn column) {
				return column.getName().equals(name);
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
	
	public List<JmColumn> getColumns() {
		assert columns != null;
		return columns.getEntitiesAsList();
	}
	
	public <T extends JmColumn>List<T> getColumns(Class<T> clazz) {
		Validate.notNull(clazz);
		List<T> result = Lists.newArrayList(Iterables.filter(getColumns(), clazz));
		return MutationMonitor.monitor(result);
	}
	
	public Set<? extends JmConstraint> getConstraints() {
		return constraints.getEntitiesAsSet();
	}
	
	public <T extends JmConstraint>Set<T> getConstraints(Class<T> clazz) {
		Validate.notNull(clazz);
		Set<T> result = Sets.newHashSet(Iterables.filter(getConstraints(), clazz));
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
	
	public Collection<? extends JmForeignKeyConstraint> getForeignKeyConstraints() {
		return getConstraints(JmForeignKeyConstraint.class);
	}
	
	public Collection<? extends JmKeyConstraint> getKeyConstraints() {
		return getConstraints(JmKeyConstraint.class);
	}
	
	public JmNotNullConstraint getNotNullConstraintFor(EntityRef<? extends JmColumn> reference) {
		Validate.notNull(reference);
		for (JmNotNullConstraint nn : getConstraints(JmNotNullConstraint.class)) {
			EntityRef<? extends JmColumn> columnRef = nn.getColumn();
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
	
	public JmPrimaryKeyConstraint getPrimaryKey() {
		Iterable<JmPrimaryKeyConstraint> pks =
				Iterables.filter(constraints.getEntitiesAsSet(), JmPrimaryKeyConstraint.class);
		try {
			return Iterables.getOnlyElement(pks);
		} catch (NoSuchElementException e) {
			// ignore and return null
		}
		return null;
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return Lists.newArrayList(Iterables.concat(getColumns(), getConstraints()));
	}
	
	public boolean isNotNullColumn(EntityRef<? extends JmColumn> columnRef) {
		Collection<JmNotNullConstraint> nns = getConstraints(JmNotNullConstraint.class);
		for (JmNotNullConstraint nn : nns) {
			EntityRef<? extends JmColumn> nnTargetColumnRef = nn.getColumn();
			if (nnTargetColumnRef != null && nnTargetColumnRef.equals(columnRef)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPrimaryKeyColumn(EntityRef<? extends JmColumn> reference) {
		JmPrimaryKeyConstraint primaryKey = getPrimaryKey();
		if (primaryKey == null) {
			return false;
		}
		return primaryKey.getKeyColumns().contains(reference);
	}
	
	@Override
	public boolean isSubDbObjectsNonRecursiveOf(DbObject target, JiemamyContext context) {
		Validate.notNull(target);
		Validate.notNull(context);
		Collection<JmTable> tables = context.getTables();
		for (JmForeignKeyConstraint foreignKey : getForeignKeyConstraints()) {
			if (foreignKey.getReferenceColumns().size() == 0) {
				continue;
			}
			EntityRef<? extends JmColumn> columnRef = foreignKey.getReferenceColumns().get(0);
			JmTable referenceTable = context.resolve(columnRef).findDeclaringTable(tables);
			if (referenceTable.equals(target)) {
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
	 * {@link EntityRef}から、{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子{@link Entity}も含む。</p>
	 * 
	 * @param <T> {@link Entity}の型
	 * @param reference {@link EntityRef}
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 */
	public <T extends Entity>T resolve(EntityRef<T> reference) {
		return new OnMemoryCompositeEntityResolver(columns, constraints).resolve(reference);
	}
	
	/**
	 * 指定したIDを持つ{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子{@link Entity}も含む。</p>
	 * 
	 * @param id ENTITY ID
	 * @return 見つかった{@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @since 1.0.0
	 */
	public Entity resolve(UUID id) {
		return new OnMemoryCompositeEntityResolver(columns, constraints).resolve(id);
	}
	
	/**
	 * テーブルにカラムを追加する。
	 * 
	 * @param column カラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(JmColumn column) {
		Validate.notNull(column);
//		Validate.notNull(column.getName());
		JmColumn old = columns.store(column);
		if (old == null) {
			logger.debug(LogMarker.LIFECYCLE, "column stored: " + column);
		} else {
			logger.debug(LogMarker.LIFECYCLE, "column updated: (old)" + old);
			logger.debug(LogMarker.LIFECYCLE, "                (new)" + column);
		}
		eventBroker.fireEvent(new StoredEvent<JmColumn>(columns, old, column));
	}
	
	/**
	 * テーブルに制約を追加する。
	 * 
	 * @param constraint 追加する制約
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(JmConstraint constraint) {
		Validate.notNull(constraint);
		JmConstraint old = constraints.store(constraint);
		eventBroker.fireEvent(new StoredEvent<JmConstraint>(constraints, old, constraint));
		if (old == null) {
			logger.debug(LogMarker.LIFECYCLE, "constraint stored: " + constraint);
		} else {
			logger.debug(LogMarker.LIFECYCLE, "constraint updated: (old)" + old);
			logger.debug(LogMarker.LIFECYCLE, "                    (new)" + constraint);
		}
	}
	
	/**
	 * 指定した2つの位置にあるカラムの順序を入れ替える。
	 * 
	 * @param index1 the index of the {@link JmColumn} to be swapped.
	 * @param index2 the index of the other {@link JmColumn} to be swapped.
	 * @throws IndexOutOfBoundsException if either {@code index1} or {@code index2}
	 *         is out of range (index1 &lt; 0 || index1 &gt;= list.size()
	 *         || index2 &lt; 0 || index2 &gt;= list.size()).
	 */
	public void swapColumn(int index1, int index2) {
		columns.swap(index1, index2);
		eventBroker.fireEvent(new StoredEvent<JmColumn>(columns, null, null));
	}
	
	@Override
	public EntityRef<? extends SimpleJmTable> toReference() {
		return new DefaultEntityRef<SimpleJmTable>(this);
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
