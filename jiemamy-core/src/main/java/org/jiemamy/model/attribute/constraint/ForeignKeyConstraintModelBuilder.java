/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/19
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

import org.apache.commons.lang.Validate;

import org.jiemamy.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel.MatchType;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel.ReferentialAction;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * {@link ForeignKeyConstraintModel}ビルダーの骨格実装クラス。
 * 
 * @version $Id$
 * @author Keisuke.K
 * @param <T> ビルド対象のインスタンスの型
 * @param <S> このビルダークラスの型
 */
// CHECKSTYLE:OFF
public abstract class ForeignKeyConstraintModelBuilder<T extends ForeignKeyConstraintModel, S extends ForeignKeyConstraintModelBuilder<T, S>>
		extends KeyConstraintModelBuilder<T, S> {
	
// CHECKSTYLE:ON
	
	MatchType matchType;
	
	ReferentialAction onDelete;
	
	ReferentialAction onUpdate;
	
	List<EntityRef<ColumnModel>> referenceColumns = CollectionsUtil.newArrayList();
	

	/**
	 * 参照カラムを追加する。
	 * 
	 * @param referenceColumn 参照カラム
	 * @return このビルダークラスのインスタンス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public S addReferenceColumn(ColumnModel referenceColumn) {
		Validate.notNull(referenceColumn);
		return addReferenceColumn(referenceColumn.toReference());
	}
	
	/**
	 * 参照カラムを追加する。
	 * 
	 * @param referenceColumnRef 参照カラムの参照
	 * @return このビルダークラスのインスタンス
	 */
	public S addReferenceColumn(final EntityRef<ColumnModel> referenceColumnRef) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.referenceColumns.add(referenceColumnRef);
			}
			
		});
		return getThis();
	}
	
	/**
	 * マッチ型を設定する。 
	 * @param matchType マッチ型
	 * @return このビルダークラスのインスタンス
	 */
	public S setMatchType(final MatchType matchType) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.matchType = matchType;
			}
			
		});
		return getThis();
	}
	
	/**
	 * 削除時アクションを設定する。 
	 * @param onDelete 削除時アクション
	 * @return このビルダークラスのインスタンス
	 */
	public S setOnDelete(final ReferentialAction onDelete) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.onDelete = onDelete;
			}
			
		});
		return getThis();
	}
	
	/**
	 * 更新時アクションを設定する。 
	 * @param onUpdate 更新時アクション
	 * @return このビルダークラスのインスタンス
	 */
	public S setOnUpdate(final ReferentialAction onUpdate) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.onUpdate = onUpdate;
			}
			
		});
		return getThis();
	}
	
	@Override
	protected void apply(T vo, S builder) {
		super.apply(vo, builder);
		
		builder.setMatchType(vo.getMatchType());
		builder.setOnDelete(vo.getOnDelete());
		builder.setOnUpdate(vo.getOnUpdate());
		
		for (EntityRef<ColumnModel> referenceColumnRef : vo.getReferenceColumns()) {
			builder.addReferenceColumn(referenceColumnRef);
		}
	}
	
}
