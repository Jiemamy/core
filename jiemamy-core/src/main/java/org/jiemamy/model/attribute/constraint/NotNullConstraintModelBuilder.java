/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/07/13
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

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.ValueObjectBuilder.BuilderConfigurator;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * {@link NotNullConstraintModel}のビルダー骨格実装クラス。
 * 
 * @version $Id$
 * @author Keisuke.K
 * @param <T> ビルド対象のインスタンス型
 * @param <S> このビルダークラスの型
 */
// CHECKSTYLE:OFF
public abstract class NotNullConstraintModelBuilder<T extends NotNullConstraintModel, S extends NotNullConstraintModelBuilder<T, S>>
		extends ConstraintModelBuilder<T, S> {
	
	// CHECKSTYLE:ON
	
	EntityRef<? extends ColumnModel> column;
	

	/**
	 * 対象カラムを設定する。
	 * 
	 * @param column 対象カラム
	 * @return このビルダークラスのインスタンス
	 */
	public S setColumn(final EntityRef<? extends ColumnModel> column) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.column = column;
			}
		});
		return getThis();
	}
	
	@Override
	protected void apply(T vo, S builder) {
		super.apply(vo, builder);
		
		builder.column = vo.getColumn();
	}
	
}
