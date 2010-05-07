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
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultForeignKeyConstraintModelBuilder extends
		AbstractKeyConstraintModelBuilder<ForeignKeyConstraintModel> {
	
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
	 * somethingを設定する。 TODO for daisuke
	 * @param referenceColumn the referenceColumns to add
	 */
	public void addReferenceColumn(EntityRef<ColumnModel> referenceColumn) {
		referenceColumns.add(referenceColumn);
	}
	
	public ForeignKeyConstraintModel build() {
		return new DefaultForeignKeyConstraintModel(name, logicalName, description, keyColumns, deferrability,
				referenceColumns, onDelete, onUpdate, matchType);
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param matchType the matchType to set
	 */
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
		
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param onDelete the onDelete to set
	 */
	public void setOnDelete(ReferentialAction onDelete) {
		this.onDelete = onDelete;
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param onUpdate the onUpdate to set
	 */
	public void setOnUpdate(ReferentialAction onUpdate) {
		this.onUpdate = onUpdate;
	}
}
