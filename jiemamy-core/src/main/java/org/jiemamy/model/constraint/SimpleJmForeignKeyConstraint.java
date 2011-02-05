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
package org.jiemamy.model.constraint;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.ModelConsistencyException;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.JmTable;

/**
 * {@link JmForeignKeyConstraint}のデフォルト実装クラス。
 * 
 * @author daisuke
 */
public final class SimpleJmForeignKeyConstraint extends SimpleJmKeyConstraint implements JmForeignKeyConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param keyColumn キー制約を構成するカラム
	 * @param referenceColumn 制約を受けるカラム
	 * @return 新しい{@link SimpleJmForeignKeyConstraint}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 引数{@code keyColumns}と{@code referenceColumns}のサイズが一致していない場合
	 */
	public static JmForeignKeyConstraint of(JmColumn keyColumn, JmColumn referenceColumn) {
		Validate.notNull(keyColumn);
		Validate.notNull(referenceColumn);
		SimpleJmForeignKeyConstraint fk = new SimpleJmForeignKeyConstraint();
		fk.addReferencing(keyColumn.toReference(), referenceColumn.toReference());
		return fk;
	}
	

	/** 制約の根拠となるカラムのリスト */
	private List<EntityRef<? extends JmColumn>> referenceColumns = Lists.newArrayList();
	
	/** 削除時アクション */
	private ReferentialAction onDelete;
	
	/** 更新時アクション */
	private ReferentialAction onUpdate;
	
	/** マッチ型 */
	private MatchType matchType;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public SimpleJmForeignKeyConstraint() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public SimpleJmForeignKeyConstraint(UUID id) {
		super(id);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>この実装では、このメソッドは必ず {@link #addReferenceColumn(EntityRef)}と
	 * ペアで利用し、それぞれのカラム数が一致するように注意しなければならない。
	 * この契約に違反しないために、基本的にこのメソッドの利用は推奨せず、代わりに
	 * {@link #addReferencing(EntityRef, EntityRef)}を利用すべきである。</p>
	 */
	@Override
	public void addKeyColumn(EntityRef<? extends JmColumn> keyColumn) {
		// javadocのためにオーバーライドし、FindBugs警告を消すためにnullチェックした
		Validate.notNull(keyColumn);
		super.addKeyColumn(keyColumn);
	}
	
	/**
	 * 参照カラムを追加する。
	 * 
	 * <p>この実装では、このメソッドは必ず {@link #addKeyColumn(EntityRef)}と
	 * ペアで利用し、それぞれのカラム数が一致するように注意しなければならない。
	 * この契約に違反しないために、基本的にこのメソッドの利用は推奨せず、代わりに
	 * {@link #addReferencing(EntityRef, EntityRef)}を利用すべきである。</p>
	 * 
	 * @param referenceColumn 参照カラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void addReferenceColumn(EntityRef<? extends JmColumn> referenceColumn) {
		Validate.notNull(referenceColumn);
		referenceColumns.add(referenceColumn);
	}
	
	/**
	 * キーカラムと参照カラムのペアを追加する。
	 * 
	 * @param keyColumn キーカラム
	 * @param referenceColumn 参照カラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void addReferencing(EntityRef<? extends JmColumn> keyColumn, EntityRef<? extends JmColumn> referenceColumn) {
		Validate.notNull(keyColumn);
		Validate.notNull(referenceColumn);
		addKeyColumn(keyColumn);
		addReferenceColumn(referenceColumn);
	}
	
	@Override
	public void clearKeyColumns() {
		super.clearKeyColumns();
		referenceColumns.clear();
	}
	
	@Override
	public SimpleJmForeignKeyConstraint clone() {
		SimpleJmForeignKeyConstraint clone = (SimpleJmForeignKeyConstraint) super.clone();
		clone.referenceColumns = CloneUtil.cloneValueArrayList(referenceColumns);
		return clone;
	}
	
	public JmKeyConstraint findReferencedKeyConstraint(Iterable<? extends DbObject> dbObjects) {
		Validate.notNull(dbObjects);
		
		List<EntityRef<? extends JmColumn>> referenceColumns = getReferenceColumns();
		if (referenceColumns.size() == 0) {
			throw new ModelConsistencyException();
		}
		EntityRef<? extends JmColumn> columnRef = referenceColumns.get(0);
		
		for (DbObject dbObject : dbObjects) {
			Validate.notNull(dbObject);
			for (Entity subEntity : dbObject.getSubEntities()) {
				if (columnRef.isReferenceOf(subEntity)) {
					JmTable table = (JmTable) dbObject;
					return table.findReferencedKeyConstraint(this);
				}
			}
		}
		return null;
	}
	
	public JmTable findReferenceTable(Iterable<JmTable> tables) {
		Validate.notNull(tables);
		JmKeyConstraint keyConstraint = findReferencedKeyConstraint(tables);
		if (keyConstraint != null) {
			return keyConstraint.findDeclaringTable(tables);
		}
		return null;
	}
	
	public MatchType getMatchType() {
		return matchType;
	}
	
	public ReferentialAction getOnDelete() {
		return onDelete;
	}
	
	public ReferentialAction getOnUpdate() {
		return onUpdate;
	}
	
	public List<EntityRef<? extends JmColumn>> getReferenceColumns() {
		return MutationMonitor.monitor(Lists.newArrayList(referenceColumns));
	}
	
	public boolean isSelfReference(JiemamyContext context) {
		Validate.notNull(context);
		Set<JmTable> tables = context.getTables();
		JmTable declaringTableOfForeignKey = findDeclaringTable(tables);
		JmKeyConstraint referencedKeyConstraint = findReferencedKeyConstraint(tables);
		JmTable declaringTableOfReferenceKey = referencedKeyConstraint.findDeclaringTable(tables);
		return declaringTableOfForeignKey.equals(declaringTableOfReferenceKey);
	}
	
	/**
	 * マッチ型を設定する。
	 * 
	 * @param matchType マッチ型
	 */
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}
	
	/**
	 * 削除時アクションを設定する。
	 * 
	 * @param onDelete 削除時アクション
	 */
	public void setOnDelete(ReferentialAction onDelete) {
		this.onDelete = onDelete;
	}
	
	/**
	 * 更新時アクションを設定する。
	 * 
	 * @param onUpdate 更新時アクション
	 */
	public void setOnUpdate(ReferentialAction onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	@Override
	public EntityRef<? extends SimpleJmForeignKeyConstraint> toReference() {
		return new DefaultEntityRef<SimpleJmForeignKeyConstraint>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(sb.length() - 1, "->" + referenceColumns);
		return sb.toString();
	}
}
