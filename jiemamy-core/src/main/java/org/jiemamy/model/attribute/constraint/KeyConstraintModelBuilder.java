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

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * {@link KeyConstraintModel}ビルダーの骨格実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 * @param <T> ビルド対象のインスタンスの型
 * @param <S> このビルダークラスの型
 */
// CHECKSTYLE:OFF
public abstract class KeyConstraintModelBuilder<T extends KeyConstraintModel, S extends KeyConstraintModelBuilder<T, S>>
		extends ConstraintModelBuilder<T, S> {
	
	// CHECKSTYLE:ON
	
	List<EntityRef<? extends ColumnModel>> keyColumns = Lists.newArrayList();
	

	/**
	 * キーカラムを追加する。
	 * 
	 * @param column キーカラム
	 * @return このビルダークラスのインスタンス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public S addKeyColumn(ColumnModel column) {
		Validate.notNull(column);
		return addKeyColumn(column.toReference());
	}
	
	/**
	 * キーカラムを追加する。
	 * 
	 * @param columnRef キーカラムの参照オブジェクト
	 * @return このビルダークラスのインスタンス
	 */
	public S addKeyColumn(final EntityRef<? extends ColumnModel> columnRef) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.keyColumns.add(columnRef);
			}
			
		});
		return getThis();
	}
	
	@Override
	protected void apply(T vo, S builder) {
		super.apply(vo, builder);
		
		for (EntityRef<? extends ColumnModel> columnRef : vo.getKeyColumns()) {
			builder.addKeyColumn(columnRef);
		}
	}
}
