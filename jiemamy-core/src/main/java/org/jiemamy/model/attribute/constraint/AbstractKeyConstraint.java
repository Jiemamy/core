/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/28
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
import java.util.UUID;

import org.jiemamy.model.attribute.ColumnRef;

/**
 * 抽象キー制約モデル。
 * 
 * @author daisuke
 */
public abstract class AbstractKeyConstraint extends AbstractConstraintModel implements KeyConstraint {
	
	/** キー制約を構成するカラムのリスト */
	private List<ColumnRef> keyColumns;
	
	/** 遅延評価可能性モデル */
	private Deferrability deferrability;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 */
	public AbstractKeyConstraint(UUID id) {
		super(id);
	}
	
	public Deferrability getDeferrability() {
		return deferrability;
	}
	
	public List<ColumnRef> getKeyColumns() {
		return keyColumns;
	}
	
	/**
	 * 
	 * 遅延評価可能性モデルを設定する
	 * 
	 * @param deferrability 遅延評価可能性モデル
	 * @since 0.3
	 */
	public void setDeferrability(Deferrability deferrability) {
		this.deferrability = deferrability;
	}
	
	/**
	 * キー制約を構成するカラムのリストを設定する。
	 * 
	 * @param keyColumns キー制約を構成するカラムのリスト
	 */
	public void setKeyColumns(List<ColumnRef> keyColumns) {
		this.keyColumns = keyColumns;
	}
	
}
