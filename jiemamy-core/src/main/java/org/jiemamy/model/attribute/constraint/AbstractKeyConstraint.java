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

import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * 抽象キー制約モデル。
 * 
 * @author daisuke
 */
public abstract class AbstractKeyConstraint extends AbstractConstraintModel implements KeyConstraint {
	
	/** キー制約を構成するカラムのリスト */
	private List<EntityRef<ColumnModel>> keyColumns;
	
	/** 遅延評価可能性モデル */
	private Deferrability deferrability;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param keyColumns キー制約を構成するカラムのリスト
	 * @param deferrability 遅延評価可能性モデル
	 */
	public AbstractKeyConstraint(String name, String logicalName, String description,
			List<EntityRef<ColumnModel>> keyColumns, Deferrability deferrability) {
		super(name, logicalName, description);
		this.keyColumns = keyColumns;
		this.deferrability = deferrability;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof AbstractKeyConstraint)) {
			return false;
		}
		AbstractKeyConstraint other = (AbstractKeyConstraint) obj;
		if (deferrability == null) {
			if (other.deferrability != null) {
				return false;
			}
		} else if (!deferrability.equals(other.deferrability)) {
			return false;
		}
		if (keyColumns == null) {
			if (other.keyColumns != null) {
				return false;
			}
		} else if (!keyColumns.equals(other.keyColumns)) {
			return false;
		}
		return true;
	}
	
	public Deferrability getDeferrability() {
		return deferrability;
	}
	
	public List<EntityRef<ColumnModel>> getKeyColumns() {
		return keyColumns;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((deferrability == null) ? 0 : deferrability.hashCode());
		result = prime * result + ((keyColumns == null) ? 0 : keyColumns.hashCode());
		return result;
	}
	
}
