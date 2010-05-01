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
package org.jiemamy.model.entity;

import java.util.Collection;
import java.util.UUID;

import org.jiemamy.model.AbstractJiemamyElement;

/**
 * エンティティ（TableやView）の抽象モデルクラス。
 * 
 * @author daisuke
 */
public abstract class AbstractEntityModel extends AbstractJiemamyElement implements EntityModel {
	
	/**
	 * 直接の依存モデルの集合を返す。
	 * 
	 * @param standardEntity 基準エンティティ
	 * @return 直接の依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	private static Collection<EntityModel> getSubEntitiesNonRecursive(EntityModel standardEntity) {
		/*
		Validate.notNull(standardEntity);
		Set<EntityModel> dependents = CollectionsUtil.newHashSet();
		Jiemamy jiemamy = standardEntity.getJiemamy();
		RootModel rootModel = jiemamy.getFactory().getRootModel();
		ReferenceResolver resolver = jiemamy.getReferenceResolver();
		
		for (EntityModel entityModel : rootModel.getEntities()) {
			if (entityModel instanceof TableModel) {
				TableModel dependentTableModel = (TableModel) entityModel;
				for (ForeignKey foreignKey : dependentTableModel.findAttributes(ForeignKey.class)) {
					if (foreignKey.getReferenceColumns().size() == 0) {
						continue;
					}
					ColumnRef columnRef = foreignKey.getReferenceColumns().get(0);
					ColumnModel columnModel = resolver.resolve(columnRef);
					TableModel referenceTableModel = columnModel.findDeclaringTable();
					if (referenceTableModel == null) {
						continue;
					}
					if (referenceTableModel.getId().equals(standardEntity.getId())) {
						dependents.add(dependentTableModel);
					}
				}
			}
		}
		
		return dependents;
		*/
		return null;
	}
	
	/**
	 * 全ての依存モデルの集合を返す。
	 * 
	 * @param standardEntity 基準エンティティ
	 * @return 全ての依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	private static Collection<EntityModel> getSubEntitiesRecursive(EntityModel standardEntity) {
		/*
		Validate.notNull(standardEntity);
		Collection<EntityModel> parentEntities = getSubEntitiesNonRecursive(standardEntity);
		Set<EntityModel> entities = CollectionsUtil.newHashSet();
		entities.addAll(parentEntities);
		
		for (EntityModel parentEntity : parentEntities) {
			if (standardEntity.equals(parentEntity) == false) {
				entities.addAll(getSubEntitiesRecursive(parentEntity));
			}
		}
		
		return entities;
		*/
		return null;
	}
	
	/**
	 * 参照先親モデルを返す。
	 * 
	 * @param entityModel 対象エンティティ
	 * @return 親モデルのSet
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	private static Collection<EntityModel> getSuperEntitiesNonRecursive(EntityModel entityModel) {
		/*
		Validate.notNull(entityModel);
		Collection<EntityModel> parents = CollectionsUtil.newArrayList();
		
		if (entityModel instanceof TableModel) {
			TableModel tableModel = (TableModel) entityModel;
			for (ForeignKey foreignKey : tableModel.findAttributes(ForeignKey.class)) {
				EntityModel keyEntity = foreignKey.findReferencedEntity();
				parents.add(keyEntity);
			}
		}
		
		return parents;
		*/
		return null;
	}
	
	/**
	 * 全ての参照先 祖先モデルを返す。
	 * 
	 * @param entityModel 対象エンティティ
	 * @return 祖先モデルのSet
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	private static Collection<EntityModel> getSuperEntitiesRecursive(EntityModel entityModel) {
		/*
		Validate.notNull(entityModel);
		Collection<EntityModel> parentEntities = getSuperEntitiesNonRecursive(entityModel);
		Collection<EntityModel> entities = CollectionsUtil.newArrayList();
		entities.addAll(parentEntities);
		
		for (EntityModel parentEntity : parentEntities) {
			if (entityModel.equals(parentEntity) == false) {
				entities.addAll(getSuperEntitiesRecursive(parentEntity));
			}
		}
		
		return entities;
		*/
		return null;
	}
	

	/** 名前 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明文 */
	private String description;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractEntityModel(UUID id) {
		super(id);
	}
	
	public Collection<EntityModel> findSubEntities(boolean recursive) {
		return recursive ? getSubEntitiesRecursive(this) : getSubEntitiesNonRecursive(this);
	}
	
	public Collection<EntityModel> findSuperEntities(boolean recursive) {
		return recursive ? getSuperEntitiesRecursive(this) : getSuperEntitiesNonRecursive(this);
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLogicalName() {
		return logicalName;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * 説明文を設定する
	 * 
	 * @param description 説明文
	 * @since 0.3
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * 論理名を設定する
	 * 
	 * @param logicalName 論理名
	 * @since 0.3
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	/**
	 * 
	 * 物理名を設定する
	 * 
	 * @param name 物理名
	 * @since 0.3
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Entity " + getName();
	}
}
