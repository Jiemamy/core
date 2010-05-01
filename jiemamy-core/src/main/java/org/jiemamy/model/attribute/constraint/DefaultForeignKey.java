/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

import java.util.List;
import java.util.UUID;

import org.jiemamy.model.attribute.ColumnRef;
import org.jiemamy.model.entity.EntityModel;

/**
 * 外部キーモデル。
 * 
 * @author daisuke
 */
public class DefaultForeignKey extends AbstractKeyConstraint implements ForeignKey {
	
	/** マッチ型 */
	private MatchType matchType;
	
	/** 削除時アクション */
	private ReferentialAction onDelete;
	
	/** 更新時アクション */
	private ReferentialAction onUpdate;
	
	/** 制約を受けるカラムのリスト */
	private List<ColumnRef> referenceColumns;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultForeignKey(UUID id) {
		super(id);
	}
	
	public EntityModel findReferencedEntity() {
		/*if (getReferenceColumns().size() == 0) {
			throw new ModelConsistenceException();
		}
		Jiemamy jiemamy = getJiemamy();
		ColumnRef columnRef = getReferenceColumns().get(0);
		
		RootModel rootModel = jiemamy.getFactory().getRootModel();
		for (TableModel tableModel : rootModel.findEntities(TableModel.class)) {
			for (ColumnModel columnModel : tableModel.findColumns()) {
				if (columnRef.getReferenceId().equals(columnModel.getId())) {
					return tableModel;
				}
			}
		}*/
		return null;
	}
	
	public KeyConstraint findReferencedKeyConstraint() {
		/*TableModel referenceTable = (TableModel) findReferencedEntity();
		for (KeyConstraint keyConstraint : referenceTable.findAttributes(KeyConstraint.class, true)) {
			
			// サイズ不一致であれば、そもそもこのキーを参照したものではない
			if (keyConstraint.getKeyColumns().size() != getReferenceColumns().size()) {
				continue;
			}
			
			Collection<UUID> referenceSetIds = CollectionsUtil.newArrayList();
			for (ColumnRef referenceKeyColumn : keyConstraint.getKeyColumns()) {
				referenceSetIds.add(referenceKeyColumn.getReferenceId());
			}
			
			boolean found = true;
			for (ColumnRef target : getReferenceColumns()) {
				if (referenceSetIds.contains(target.getReferenceId()) == false) {
					found = false;
				}
			}
			
			if (found) {
				return keyConstraint;
			}
		}*/
		return null;
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
	
	public List<ColumnRef> getReferenceColumns() {
		return referenceColumns;
	}
	
	/**
	 * 
	 * マッチ型を設定する
	 * 
	 * @param matchType マッチ型
	 * @since 0.3
	 */
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}
	
	/**
	 * 
	 * 削除時アクションを設定する
	 * 
	 * @param onDelete 削除時アクション
	 * @since 0.3
	 */
	public void setOnDelete(ReferentialAction onDelete) {
		this.onDelete = onDelete;
	}
	
	/**
	 * 
	 * 更新時アクションを設定する
	 * 
	 * @param onUpdate 更新時アクション
	 * @since 0.3
	 */
	public void setOnUpdate(ReferentialAction onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	/**
	 * 参照カラムのリストを設定する。
	 * 
	 * @param referenceColumns 参照カラムのリスト
	 */
	public void setReferenceColumns(List<ColumnRef> referenceColumns) {
		this.referenceColumns = referenceColumns;
	}
	
}
