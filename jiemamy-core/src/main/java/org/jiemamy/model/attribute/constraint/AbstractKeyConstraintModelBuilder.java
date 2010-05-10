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

import java.util.ArrayList;
import java.util.List;

import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 * @param <T> ビルド対象のインスタンスの型
 * @param <S> このビルダークラスの型
 */
public abstract class AbstractKeyConstraintModelBuilder<T extends KeyConstraintModel, S extends AbstractKeyConstraintModelBuilder<T, S>>
		extends AbstractConstraintModelBuilder<T, S> {
	
	DeferrabilityModel deferrability;
	
	List<EntityRef<ColumnModel>> keyColumns = new ArrayList<EntityRef<ColumnModel>>();
	

	/**
	 * インスタンスを生成する。
	 */
	public AbstractKeyConstraintModelBuilder() {
		// noop
	}
	
	/**
	 * コピーコンストラクタ。
	 * 
	 * @param vo コピー元の{@link KeyConstraintModel}
	 */
	public AbstractKeyConstraintModelBuilder(T vo) {
		super(vo);
		
		this.deferrability = vo.getDeferrability();
		this.keyColumns = vo.getKeyColumns();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param column キーカラム
	 * @return このビルダークラスのインスタンス
	 */
	public S addKeyColumn(ColumnModel column) {
		return addKeyColumn(column.getReference());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param columnRef キーカラムの参照オブジェクト
	 * @return このビルダークラスのインスタンス
	 */
	public S addKeyColumn(EntityRef<ColumnModel> columnRef) {
		keyColumns.add(columnRef);
		return getThis();
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * 
	 * @param deferrability the deferrability to set
	 * @return このビルダークラスのインスタンス
	 */
	public S setDeferrability(DeferrabilityModel deferrability) {
		this.deferrability = deferrability;
		return getThis();
	}
}
