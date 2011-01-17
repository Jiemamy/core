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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.TableNotFoundException;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.table.TableModel;

/**
 * {@link ForeignKeyConstraintModel}のデフォルト実装クラス。
 * 
 * @author daisuke
 */
public final class DefaultForeignKeyConstraintModel extends AbstractKeyConstraintModel implements
		ForeignKeyConstraintModel {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param keyColumn キー制約を構成するカラム
	 * @param referenceColumn 制約を受けるカラム
	 * @return 新しい{@link DefaultForeignKeyConstraintModel}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 引数{@code keyColumns}と{@code referenceColumns}のサイズが一致していない場合
	 */
	public static ForeignKeyConstraintModel of(ColumnModel keyColumn, ColumnModel referenceColumn) {
		Validate.notNull(keyColumn);
		Validate.notNull(referenceColumn);
		DefaultForeignKeyConstraintModel fk = new DefaultForeignKeyConstraintModel(UUID.randomUUID());
		fk.addReferencing(keyColumn.toReference(), referenceColumn.toReference());
		return fk;
	}
	
	private static List<EntityRef<? extends ColumnModel>> toRefList(ColumnModel column) {
		Validate.notNull(column);
		return Collections.<EntityRef<? extends ColumnModel>> singletonList(column.toReference());
	}
	

	/** 制約の根拠となるカラムのリスト */
	private List<EntityRef<? extends ColumnModel>> referenceColumns = Lists.newArrayList();
	
	/** 削除時アクション */
	private ReferentialAction onDelete;
	
	/** 更新時アクション */
	private ReferentialAction onUpdate;
	
	/** マッチ型 */
	private MatchType matchType;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultForeignKeyConstraintModel(UUID id) {
		super(id);
	}
	
	public void addReferenceColumn(EntityRef<? extends ColumnModel> refColumn) {
		Validate.notNull(refColumn);
		referenceColumns.add(refColumn);
	}
	
	public void addReferencing(EntityRef<? extends ColumnModel> key, EntityRef<? extends ColumnModel> ref) {
		Validate.notNull(key);
		Validate.notNull(ref);
		addKeyColumn(key);
		addReferenceColumn(ref);
	}
	
	@Override
	public DefaultForeignKeyConstraintModel clone() {
		DefaultForeignKeyConstraintModel clone = (DefaultForeignKeyConstraintModel) super.clone();
		clone.referenceColumns = CloneUtil.cloneValueArrayList(referenceColumns);
		return clone;
	}
	
	public TableModel findDeclaringTable(Collection<TableModel> tables) {
		for (TableModel tableModel : tables) {
			if (tableModel.getConstraints().contains(this)) {
				return tableModel;
			}
		}
		throw new TableNotFoundException("contains " + this + " in " + tables);
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
	
	public List<EntityRef<? extends ColumnModel>> getReferenceColumns() {
		return MutationMonitor.monitor(Lists.newArrayList(referenceColumns));
	}
	
	public boolean isSelfReference(JiemamyContext context) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}
	
	public void setOnDelete(ReferentialAction onDelete) {
		this.onDelete = onDelete;
	}
	
	public void setOnUpdate(ReferentialAction onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	@Override
	public EntityRef<? extends DefaultForeignKeyConstraintModel> toReference() {
		return new DefaultEntityRef<DefaultForeignKeyConstraintModel>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(sb.length() - 1, "->" + referenceColumns);
		return sb.toString();
	}
}
