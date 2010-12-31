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

import org.jiemamy.dddbase.ValueObject;
import org.jiemamy.dddbase.ValueObjectBuilder;

/**
 * {@link ConstraintModel}のビルダー骨格実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 * @param <T> ビルド対象のインスタンスの型
 * @param <S> このビルダークラスの型
 */
public abstract class ConstraintModelBuilder<T extends ConstraintModel & ValueObject, S extends ConstraintModelBuilder<T, S>>
		extends ValueObjectBuilder<T, S> {
	
	String name;
	
	String logicalName;
	
	String description;
	
	DeferrabilityModel deferrability;
	

	/**
	 * 遅延評価可能性モデルを設定する。
	 * 
	 * @param deferrability 遅延評価可能性モデル
	 * @return このビルダークラスのインスタンス
	 */
	public S setDeferrability(final DeferrabilityModel deferrability) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.deferrability = deferrability;
			}
		});
		return getThis();
	}
	
	/**
	 * 説明文を設定する。 
	 * @param description 説明文
	 * @return このビルダークラスのインスタンス
	 */
	public S setDescription(final String description) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.description = description;
			}
		});
		return getThis();
	}
	
	/**
	 * 論理名を設定する。 
	 * @param logicalName 論理名
	 * @return このビルダークラスのインスタンス
	 */
	public S setLogicalName(final String logicalName) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.logicalName = logicalName;
			}
			
		});
		return getThis();
	}
	
	/**
	 * 物理名を設定する。
	 * @param name 物理名
	 * @return このビルダークラスのインスタンス
	 */
	public S setName(final String name) {
		addConfigurator(new BuilderConfigurator<S>() {
			
			public void configure(S builder) {
				builder.name = name;
			}
			
		});
		return getThis();
	}
	
	@Override
	protected void apply(T vo, S builder) {
		builder.setName(vo.getName());
		builder.setLogicalName(vo.getLogicalName());
		builder.setDescription(vo.getDescription());
		builder.setDeferrability(vo.getDeferrability());
	}
	
}
