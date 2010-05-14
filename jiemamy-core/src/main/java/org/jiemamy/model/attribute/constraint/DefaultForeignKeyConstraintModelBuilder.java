/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/07
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

import java.util.List;

import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel.MatchType;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel.ReferentialAction;
import org.jiemamy.utils.CollectionsUtil;

/**
 * {@link DefaultForeignKeyConstraintModel}のビルダークラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultForeignKeyConstraintModelBuilder extends
		AbstractKeyConstraintModelBuilder<ForeignKeyConstraintModel, DefaultForeignKeyConstraintModelBuilder> {
	
	/**
	 * マッチ型
	 */
	MatchType matchType;
	
	/**
	 * 削除時アクション
	 */
	ReferentialAction onDelete;
	
	/**
	 * 更新時アクション
	 */
	ReferentialAction onUpdate;
	
	/**
	 * 参照カラムのリスト
	 */
	List<EntityRef<ColumnModel>> referenceColumns = CollectionsUtil.newArrayList();
	

	/**
	 * 参照カラムを追加する。
	 * 
	 * @param referenceColumn 参照カラム
	 * @return このビルダークラスのインスタンス
	 */
	public DefaultForeignKeyConstraintModelBuilder addReferenceColumn(ColumnModel referenceColumn) {
		return addReferenceColumn(referenceColumn.getReference());
	}
	
	/**
	 * 参照カラムを追加する。
	 * 
	 * @param referenceColumnRef 参照カラムの参照
	 * @return このビルダークラスのインスタンス
	 */
	public DefaultForeignKeyConstraintModelBuilder addReferenceColumn(EntityRef<ColumnModel> referenceColumnRef) {
		referenceColumns.add(referenceColumnRef);
		return getThis();
	}
	
	@Override
	public DefaultForeignKeyConstraintModelBuilder apply(ForeignKeyConstraintModel vo) {
		super.apply(vo);
		setMatchType(vo.getMatchType());
		setOnDelete(vo.getOnDelete());
		setOnUpdate(vo.getOnUpdate());
		
		referenceColumns.clear();
		for (EntityRef<ColumnModel> referenceColumnRef : vo.getReferenceColumns()) {
			addReferenceColumn(referenceColumnRef);
		}
		
		return getThis();
	}
	
	@Override
	public ForeignKeyConstraintModel build() {
		return new DefaultForeignKeyConstraintModel(name, logicalName, description, keyColumns, deferrability,
				referenceColumns, onDelete, onUpdate, matchType);
	}
	
	/**
	 * マッチ型を設定する。 
	 * @param matchType マッチ型
	 * @return このビルダークラスのインスタンス
	 */
	public DefaultForeignKeyConstraintModelBuilder setMatchType(MatchType matchType) {
		this.matchType = matchType;
		return getThis();
	}
	
	/**
	 * 削除時アクションを設定する。 
	 * @param onDelete 削除時アクション
	 * @return このビルダークラスのインスタンス
	 */
	public DefaultForeignKeyConstraintModelBuilder setOnDelete(ReferentialAction onDelete) {
		this.onDelete = onDelete;
		return getThis();
	}
	
	/**
	 * 更新時アクションを設定する。 
	 * @param onUpdate 更新時アクション
	 * @return このビルダークラスのインスタンス
	 */
	public DefaultForeignKeyConstraintModelBuilder setOnUpdate(ReferentialAction onUpdate) {
		this.onUpdate = onUpdate;
		return getThis();
	}
	
	@Override
	protected DefaultForeignKeyConstraintModelBuilder getThis() {
		return this;
	}
}
