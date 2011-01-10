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
package org.jiemamy.model.constraint;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.utils.MutationMonitor;

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
		return new DefaultForeignKeyConstraintModel(UUID.randomUUID(), null, null, null, toRefList(keyColumn), null,
				toRefList(referenceColumn), null, null, null);
	}
	
	private static List<EntityRef<? extends ColumnModel>> toRefList(ColumnModel column) {
		// THINK java.util.Collections.singletonList(T) つかえないかな
		List<EntityRef<? extends ColumnModel>> result = Lists.newArrayListWithCapacity(1);
		result.add(column.toReference());
		return result;
	}
	

	/** 制約の根拠となるカラムのリスト */
	private List<EntityRef<? extends ColumnModel>> referenceColumns;
	
	/** 削除時アクション */
	private ReferentialAction onDelete;
	
	/** 更新時アクション */
	private ReferentialAction onUpdate;
	
	/** マッチ型 */
	private MatchType matchType;
	
	private final UUID id;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param keyColumn キー制約を構成するカラム
	 * @param referenceColumn 制約を受けるカラム
	 * @throws IllegalArgumentException 引数{@code keyColumns}と{@code referenceColumns}のサイズが一致していない場合
	 */
	public DefaultForeignKeyConstraintModel(ColumnModel keyColumn, ColumnModel referenceColumn) {
		this(UUID.randomUUID(), null, null, null, toRefList(keyColumn), null, toRefList(referenceColumn), null, null,
				null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param keyColumn キー制約を構成するカラム
	 * @param referenceColumn 制約を受けるカラム
	 * @throws IllegalArgumentException 引数{@code keyColumns}と{@code referenceColumns}のサイズが一致していない場合
	 */
	public DefaultForeignKeyConstraintModel(String name, ColumnModel keyColumn, ColumnModel referenceColumn) {
		this(UUID.randomUUID(), name, null, null, toRefList(keyColumn), null, toRefList(referenceColumn), null, null,
				null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @param name 物理名
	 * @param keyColumn キー制約を構成するカラム
	 * @param referenceColumn 制約を受けるカラム
	 * @throws IllegalArgumentException 引数{@code keyColumns}と{@code referenceColumns}のサイズが一致していない場合
	 */
	public DefaultForeignKeyConstraintModel(UUID id, String name, ColumnModel keyColumn, ColumnModel referenceColumn) {
		this(id, name, null, null, toRefList(keyColumn), null, toRefList(referenceColumn), null, null, null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param keyColumns キー制約を構成するカラムのリスト
	 * @param deferrability 遅延評価可能性モデル
	 * @param referenceColumns 制約を受けるカラムのリスト
	 * @param onDelete 削除時アクション
	 * @param onUpdate 更新時アクション
	 * @param matchType マッチ型
	 * @throws IllegalArgumentException 引数{@code keyColumns}と{@code referenceColumns}のサイズが一致していない場合
	 */
	// CHECKSTYLE:OFF
	public DefaultForeignKeyConstraintModel(UUID id, String name, String logicalName, String description,
			List<EntityRef<? extends ColumnModel>> keyColumns, DeferrabilityModel deferrability,
			List<EntityRef<? extends ColumnModel>> referenceColumns, ReferentialAction onDelete,
			ReferentialAction onUpdate, MatchType matchType) {
		// CHECKSTYLE:ON
		super(name, description, description, keyColumns, deferrability);
		Validate.notNull(id);
		Validate.isTrue(keyColumns.size() == referenceColumns.size());
		
		this.id = id;
		this.matchType = matchType;
		this.onDelete = onDelete;
		this.onUpdate = onUpdate;
		this.referenceColumns = Lists.newArrayList(referenceColumns);
	}
	
	@Override
	public DefaultForeignKeyConstraintModel clone() {
		DefaultForeignKeyConstraintModel clone = (DefaultForeignKeyConstraintModel) super.clone();
		clone.referenceColumns = CloneUtil.cloneValueArrayList(referenceColumns);
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof DefaultForeignKeyConstraintModel)) {
			return false;
		}
		DefaultForeignKeyConstraintModel other = (DefaultForeignKeyConstraintModel) obj;
		if (matchType == null) {
			if (other.matchType != null) {
				return false;
			}
		} else if (!matchType.equals(other.matchType)) {
			return false;
		}
		if (onDelete == null) {
			if (other.onDelete != null) {
				return false;
			}
		} else if (!onDelete.equals(other.onDelete)) {
			return false;
		}
		if (onUpdate == null) {
			if (other.onUpdate != null) {
				return false;
			}
		} else if (!onUpdate.equals(other.onUpdate)) {
			return false;
		}
		if (referenceColumns == null) {
			if (other.referenceColumns != null) {
				return false;
			}
		} else if (!referenceColumns.equals(other.referenceColumns)) {
			return false;
		}
		return true;
	}
	
	public UUID getId() {
		return id;
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
	
	public Collection<? extends Entity> getSubEntities() {
		return Collections.emptyList();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((matchType == null) ? 0 : matchType.hashCode());
		result = prime * result + ((onDelete == null) ? 0 : onDelete.hashCode());
		result = prime * result + ((onUpdate == null) ? 0 : onUpdate.hashCode());
		result = prime * result + ((referenceColumns == null) ? 0 : referenceColumns.hashCode());
		return result;
	}
	
	@Override
	public void setDeferrability(DeferrabilityModel deferrability) {
		super.setDeferrability(deferrability);
	}
	
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
	}
	
	public void setOnDelete(ReferentialAction onDelete) {
		this.onDelete = onDelete;
	}
	
	public void setOnUpdate(ReferentialAction onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	public EntityRef<DefaultForeignKeyConstraintModel> toReference() {
		return new DefaultEntityRef<DefaultForeignKeyConstraintModel>(this);
	}
	
}