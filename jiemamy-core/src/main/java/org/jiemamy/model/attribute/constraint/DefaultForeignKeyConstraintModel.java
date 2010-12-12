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
package org.jiemamy.model.attribute.constraint;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.EntityRef;
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
	 * @param keyColumn キーカラム
	 * @param referenceColumn 参照カラム
	 * @return 新しい{@link DefaultForeignKeyConstraintModel}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static ForeignKeyConstraintModel of(ColumnModel keyColumn, ColumnModel referenceColumn) {
		List<EntityRef<? extends ColumnModel>> keyColumns = Lists.newArrayList();
		keyColumns.add(keyColumn.toReference());
		
		List<EntityRef<? extends ColumnModel>> referenceColumns = Lists.newArrayList();
		referenceColumns.add(referenceColumn.toReference());
		
		return new DefaultForeignKeyConstraintModel(null, null, null, keyColumns, null, referenceColumns, null, null,
				null);
	}
	

	/** 制約の根拠となるカラムのリスト */
	private final List<EntityRef<? extends ColumnModel>> referenceColumns;
	
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
	// CHECKSTYLE:OFF
	public DefaultForeignKeyConstraintModel(String name, String logicalName, String description,
			List<EntityRef<? extends ColumnModel>> keyColumns, DeferrabilityModel deferrability,
			List<EntityRef<? extends ColumnModel>> referenceColumns, ReferentialAction onDelete,
			ReferentialAction onUpdate, MatchType matchType) {
		// CHECKSTYLE:ON
		super(name, description, description, keyColumns, deferrability);
		Validate.isTrue(keyColumns.size() == referenceColumns.size());
		
		this.matchType = matchType;
		this.onDelete = onDelete;
		this.onUpdate = onUpdate;
		this.referenceColumns = new ArrayList<EntityRef<? extends ColumnModel>>(referenceColumns);
	}
	
	@Override
	public DefaultForeignKeyConstraintModel clone() {
		return (DefaultForeignKeyConstraintModel) super.clone();
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
		return referenceColumns;
	}
	
}
