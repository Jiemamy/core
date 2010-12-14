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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
import org.jiemamy.serializer.JiemamyXmlWriter;
import org.jiemamy.utils.ConstraintComparator;
import org.jiemamy.utils.EntityUtil;
import org.jiemamy.utils.MutationMonitor;
import org.jiemamy.utils.collection.CollectionsUtil;
import org.jiemamy.xml.CoreQName;

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
	

	/** カラムのリスト */
	private List<ColumnModel> columns = Lists.newArrayList();
	
	/** 制約のリスト */
	private SortedSet<ConstraintModel> constraints = Sets.newTreeSet(new ConstraintComparator());
	

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
	 * テーブルに属性を追加する。
	 * 
	 * @param constraint 属性
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void addConstraint(ConstraintModel constraint) {
		Validate.notNull(constraint);
		if (constraint instanceof ForeignKeyConstraintModel) {
			constraint = ((ForeignKeyConstraintModel) constraint).clone();
		}
		CollectionsUtil.addOrReplace(constraints, constraint);
	}
	
	public List<ColumnModel> breachEncapsulationOfColumns() {
		return columns;
	}
	
	public SortedSet<? extends ConstraintModel> breachEncapsulationOfConstraints() {
		return constraints;
	}
	
	@Override
	public DefaultTableModel clone() {
		DefaultTableModel clone = (DefaultTableModel) super.clone();
		clone.columns = EntityUtil.cloneEntityList(columns);
		TreeSet<ConstraintModel> set = new TreeSet<ConstraintModel>(new ConstraintComparator());
		set.addAll(constraints);
		clone.constraints = set;
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
		for (ColumnModel column : Lists.newArrayList(columns)) {
			if (ref.isReferenceOf(column)) {
				columns.remove(column);
				return;
			}
		}
		throw new EntityNotFoundException();
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
		Collection<ColumnModel> c = CollectionUtils.select(columns, new Predicate<ColumnModel>() {
			
			public boolean evaluate(ColumnModel col) {
				return col.getName().equals(name);
			}
		});
		if (c.size() == 1) {
			return c.iterator().next().clone();
		}
		if (c.size() == 0) {
			throw new ColumnNotFoundException();
		}
		throw new TooManyColumnsFoundException(c);
	}
	
	public List<ColumnModel> getColumns() {
		assert columns != null;
		return MutationMonitor.monitor(EntityUtil.cloneEntityList(columns));
	}
	
	public SortedSet<? extends ConstraintModel> getConstraints() {
		assert constraints != null;
		TreeSet<ConstraintModel> result = new TreeSet<ConstraintModel>(new ConstraintComparator());
		result.addAll(constraints);
		return MutationMonitor.monitor(result);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ConstraintModel>List<T> getConstraints(Class<T> clazz) {
		List<T> result = Lists.newArrayList();
		for (ConstraintModel constraint : getConstraints()) {
			if (clazz.isInstance(constraint)) {
				result.add((T) constraint);
			}
		}
		return MutationMonitor.monitor(result);
	}
	
	public Collection<? extends ForeignKeyConstraintModel> getForeignKeyConstraintModels() {
		return findConstraints(ForeignKeyConstraintModel.class);
	}
	
	public Collection<? extends KeyConstraintModel> getKeyConstraintModels() {
		return findConstraints(KeyConstraintModel.class);
	}
	
	@Override
	public Collection<? extends ColumnModel> getSubEntities() {
		return getColumns();
	}
	
	@Override
	public JiemamyXmlWriter getWriter(JiemamyContext context) {
		return new EntityXmlWriterImpl(context);
	}
	
	@Override
	public boolean isSubDatabaseObjectsNonRecursiveOf(DatabaseObjectModel target, JiemamyContext context) {
		Validate.notNull(target);
		Validate.notNull(context);
		Collection<TableModel> tables = context.getEntities(TableModel.class);
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
	 * テーブルから属性を削除する。
	 * 
	 * @param attribute 属性
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeConstraint(ConstraintModel attribute) {
		Validate.notNull(attribute);
		constraints.remove(attribute);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity>T resolve(EntityRef<T> reference) {
		for (ColumnModel column : columns) {
			if (reference.isReferenceOf(column)) {
				return (T) column.clone();
			}
		}
		throw new ColumnNotFoundException();
	}
	
	public Entity resolve(UUID id) {
		for (ColumnModel column : columns) {
			if (column.getId().equals(id)) {
				return column;
			}
		}
		throw new EntityNotFoundException();
	}
	
	/**
	 * テーブルにカラムを追加する。
	 * 
	 * @param column カラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(ColumnModel column) {
		Validate.notNull(column);
		if (columns.contains(column)) {
			columns.set(columns.indexOf(column), column.clone());
		} else {
			columns.add(column.clone());
		}
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
	
	private <T extends ConstraintModel>Collection<T> findConstraints(Class<T> clazz) {
		Collection<T> result = Lists.newArrayList();
		for (ConstraintModel attribute : constraints) {
			if (clazz.isAssignableFrom(attribute.getClass())) {
				result.add(clazz.cast(attribute));
			}
		}
		return result;
	}
	

	private class EntityXmlWriterImpl extends AbstractJiemamyXmlWriter {
		
		private final JiemamyContext context;
		

		public EntityXmlWriterImpl(JiemamyContext context) {
			this.context = context;
		}
		
		@Override
		public Iterator<Namespace> nss() {
			return null;
		}
		
		public void writeTo(XMLEventWriter writer) throws XMLStreamException {
			writer.add(EV_FACTORY.createStartElement(CoreQName.TABLE.getQName(), atts(), nss()));
			write1Misc(writer);
			write2Columns(writer);
			write3Constraints(writer);
			writer.add(EV_FACTORY.createEndElement(CoreQName.TABLE.getQName(), nss()));
		}
		
		private Iterator<Attribute> atts() {
			return createIdAndClassAtts(getId(), DefaultTableModel.this);
		}
		
		private void write1Misc(XMLEventWriter writer) throws XMLStreamException {
			writeNameLogNameDesc(writer, getName(), getLogicalName(), getDescription());
		}
		
		private void write2Columns(XMLEventWriter writer) throws XMLStreamException {
			if (getColumns().size() <= 0) {
				return;
			}
			writer.add(EV_FACTORY.createStartElement(CoreQName.COLUMNS.getQName(), null, nss()));
			for (ColumnModel columnModel : getColumns()) {
				columnModel.getWriter(context).writeTo(writer);
			}
			writer.add(EV_FACTORY.createEndElement(CoreQName.COLUMNS.getQName(), nss()));
		}
		
		private void write3Constraints(XMLEventWriter writer) throws XMLStreamException {
			if (getConstraints().size() <= 0) {
				return;
			}
			writer.add(EV_FACTORY.createStartElement(CoreQName.CONSTRAINTS.getQName(), null, nss()));
			for (ConstraintModel constraintModel : getConstraints()) {
				constraintModel.getWriter(context).writeTo(writer);
			}
			writer.add(EV_FACTORY.createEndElement(CoreQName.CONSTRAINTS.getQName(), nss()));
		}
	}
}
