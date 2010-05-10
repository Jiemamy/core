/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * 外部キーモデル。
 * 
 * @author daisuke
 */
public final class DefaultForeignKeyConstraintModel extends AbstractKeyConstraintModel implements
		ForeignKeyConstraintModel {
	
	/**
	 * TODO for daisuke
	 * 
	 * @param keyColumn
	 * @param referenceColumn
	 * @return
	 */
	public static ForeignKeyConstraintModel of(ColumnModel keyColumn, ColumnModel referenceColumn) {
		return new DefaultForeignKeyConstraintModelBuilder().addKeyColumn(keyColumn)
			.addReferenceColumn(referenceColumn).build();
	}
	

	/** 制約を受けるカラムのリスト */
	private final List<EntityRef<ColumnModel>> referenceColumns;
	
	/** 削除時アクション */
	private final ReferentialAction onDelete;
	
	/** 更新時アクション */
	private final ReferentialAction onUpdate;
	
	/** マッチ型 */
	private final MatchType matchType;
	

	/**
	 * インスタンスを生成する。
	 * 
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
	public DefaultForeignKeyConstraintModel(String name, String logicalName, String description,
			List<EntityRef<ColumnModel>> keyColumns, DeferrabilityModel deferrability,
			List<EntityRef<ColumnModel>> referenceColumns, ReferentialAction onDelete, ReferentialAction onUpdate,
			MatchType matchType) {
		super(name, description, description, keyColumns, deferrability);
		Validate.isTrue(keyColumns.size() == referenceColumns.size());
		
		this.matchType = matchType;
		this.onDelete = onDelete;
		this.onUpdate = onUpdate;
		this.referenceColumns = new ArrayList<EntityRef<ColumnModel>>(referenceColumns);
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
	
	public MatchType getMatchType() {
		return matchType;
	}
	
	public ReferentialAction getOnDelete() {
		return onDelete;
	}
	
	public ReferentialAction getOnUpdate() {
		return onUpdate;
	}
	
	public List<EntityRef<ColumnModel>> getReferenceColumns() {
		return new ArrayList<EntityRef<ColumnModel>>(referenceColumns);
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
}
