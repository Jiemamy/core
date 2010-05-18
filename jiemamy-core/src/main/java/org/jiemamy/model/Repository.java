/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/10
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
package org.jiemamy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.Validate;

import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.model.dbo.TooManyColumnsFoundException;
import org.jiemamy.utils.CollectionsUtil;

/**
 * DDDにおけるREPOSITORYの実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class Repository {
	
	private Collection<DatabaseObjectModel> entities = CollectionsUtil.newArrayList();
	

	/**
	 * 引数に指定した {@link DatabaseObjectModel} をREPOSITORYの管理下に置く。
	 * 
	 * <p>{@link Entity}は、リポジトリの管理下に置かれることにより、ライフサイクルが開始する。</p>
	 * 
	 * @param dbo 管理対象
	 * @throws EntityLifecycleException 引数{@code dbo}のライフサイクルがaliveの場合
	 * @throws IllegalArgumentException 引数{@code dbo}に{@code null}を与えた場合
	 */
	public void add(DatabaseObjectModel dbo) {
		Validate.notNull(dbo);
		dbo.activate();
		entities.add(dbo);
	}
	
	/**
	 * このカラムが所属するテーブルを取得する。
	 * 
	 * @param columnModel 対象カラム
	 * @return この属性が所属するテーブル. どのテーブルにも所属していない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public TableModel findDeclaringTable(final ColumnModel columnModel) {
		Collection<TableModel> select = CollectionUtils.select(findTables(), new Predicate<TableModel>() {
			
			public boolean evaluate(TableModel tableModel) {
				return tableModel.getColumns().contains(columnModel);
			}
		});
		if (select.size() == 1) {
			return select.iterator().next();
		}
		if (select.size() == 0) {
			throw new EntityNotFoundException();
		}
		throw new ModelConsistencyException();
	}
	
	/**
	 * 指定した外部キーが参照するエンティティを取得する。
	 * @param fk 対象外部キー
	 * @return 指定した外部キーが参照するエンティティ. 参照エンティティが見つからない場合は{@code null}
	 * @throws ModelConsistencyException 参照カラムが1つもない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public TableModel findReferencedEntity(ForeignKeyConstraintModel fk) {
		if (fk.getReferenceColumns().size() == 0) {
			throw new ModelConsistencyException();
		}
		EntityRef<ColumnModel> columnRef = fk.getReferenceColumns().get(0);
		
		for (TableModel tableModel : findTables()) {
			for (ColumnModel columnModel : tableModel.getColumns()) {
				if (columnRef.getReferenceId().equals(columnModel.getId())) {
					return tableModel;
				}
			}
		}
		return null;
	}
	
	/**
	 * 指定した外部キーが参照するキー制約を取得する。
	 * 
	 * <p>該当するキー制約が存在しない、という状況はモデル不整合を表す。</p>
	 * 
	 * @param fk 対象外部キー
	 * @return 指定した外部キーが参照するキー. 該当するキーが存在しなかった場合、{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public KeyConstraintModel findReferencedKeyConstraint(ForeignKeyConstraintModel fk) {
		TableModel referenceTable = findReferencedEntity(fk);
		for (KeyConstraintModel keyConstraint : referenceTable.getKeyConstraintModels()) {
			
			// サイズ不一致であれば、そもそもこのキーを参照したものではない
			if (keyConstraint.getKeyColumns().size() != fk.getReferenceColumns().size()) {
				continue;
			}
			
			Collection<UUID> referenceSetIds = CollectionsUtil.newArrayList();
			for (EntityRef<ColumnModel> referenceKeyColumn : keyConstraint.getKeyColumns()) {
				referenceSetIds.add(referenceKeyColumn.getReferenceId());
			}
			
			boolean found = true;
			for (EntityRef<ColumnModel> target : fk.getReferenceColumns()) {
				if (referenceSetIds.contains(target.getReferenceId()) == false) {
					found = false;
				}
			}
			
			if (found) {
				return keyConstraint;
			}
		}
		return null;
	}
	
	/**
	 * 直接の依存モデルの集合を返す。
	 * 
	 * @param standardEntity 基準エンティティ
	 * @return 直接の依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSubEntitiesNonRecursive(DatabaseObjectModel standardEntity) {
		Validate.notNull(standardEntity);
		Set<DatabaseObjectModel> dependents = CollectionsUtil.newHashSet();
		
		for (Entity entityModel : entities) {
			if (entityModel instanceof TableModel) {
				TableModel dependentTableModel = (TableModel) entityModel;
				for (ForeignKeyConstraintModel foreignKey : dependentTableModel.getForeignKeyConstraintModels()) {
					if (foreignKey.getReferenceColumns().size() == 0) {
						continue;
					}
					EntityRef<ColumnModel> columnRef = foreignKey.getReferenceColumns().get(0);
					ColumnModel columnModel = resolveColumn(columnRef);
					TableModel referenceTableModel = findDeclaringTable(columnModel);
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
	}
	
	/**
	 * 全ての依存モデルの集合を返す。
	 * 
	 * @param standardEntity 基準エンティティ
	 * @return 全ての依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSubEntitiesRecursive(DatabaseObjectModel standardEntity) {
		Validate.notNull(standardEntity);
		Collection<DatabaseObjectModel> parentEntities = findSubEntitiesNonRecursive(standardEntity);
		Set<DatabaseObjectModel> result = CollectionsUtil.newHashSet();
		result.addAll(parentEntities);
		
		for (DatabaseObjectModel parentEntity : parentEntities) {
			if (standardEntity.equals(parentEntity) == false) {
				result.addAll(findSubEntitiesRecursive(parentEntity));
			}
		}
		
		return result;
	}
	
	/**
	 * 参照先親モデルを返す。
	 * 
	 * @param entityModel 対象エンティティ
	 * @return 親モデルのSet
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSuperEntitiesNonRecursive(DatabaseObjectModel entityModel) {
		Validate.notNull(entityModel);
		Collection<DatabaseObjectModel> parents = CollectionsUtil.newArrayList();
		
		if (entityModel instanceof TableModel) {
			TableModel tableModel = (TableModel) entityModel;
			for (ForeignKeyConstraintModel foreignKey : tableModel.getForeignKeyConstraintModels()) {
				DatabaseObjectModel keyEntity = findReferencedEntity(foreignKey);
				parents.add(keyEntity);
			}
		}
		
		return parents;
	}
	
	/**
	 * 全ての参照先 祖先モデルを返す。
	 * 
	 * @param entityModel 対象エンティティ
	 * @return 祖先モデルのSet
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSuperEntitiesRecursive(DatabaseObjectModel entityModel) {
		Validate.notNull(entityModel);
		Collection<DatabaseObjectModel> parentEntities = findSuperEntitiesNonRecursive(entityModel);
		Collection<DatabaseObjectModel> result = CollectionsUtil.newArrayList();
		result.addAll(parentEntities);
		
		for (DatabaseObjectModel parentEntity : parentEntities) {
			if (entityModel.equals(parentEntity) == false) {
				result.addAll(findSuperEntitiesRecursive(parentEntity));
			}
		}
		
		return result;
	}
	
	/**
	 * リポジトリが管理している全ての {@link TableModel}を返す。
	 * 
	 * @return リポジトリが管理している全ての {@link TableModel}
	 */
	public Collection<TableModel> findTables() {
		List<TableModel> result = new ArrayList<TableModel>();
		for (Entity entity : entities) {
			if (entity instanceof TableModel) {
				result.add((TableModel) entity);
			}
		}
		return result;
	}
	
	/**
	 * 引数に指定した {@link DatabaseObjectModel} をREPOSITORYの管理下から外す。
	 * 
	 * <p>{@link Entity}は、リポジトリの管理下から外れることにより、ライフサイクルが終了する。</p>
	 * 
	 * @param dbo 管理対象
	 * @throws IllegalArgumentException 引数{@code dbo}に{@code null}を与えた場合
	 * @throws IllegalArgumentException 引数{@code dbo}がこのREPOSITORY管理下にない場合
	 */
	public void remove(DatabaseObjectModel dbo) {
		Validate.notNull(dbo);
		if (entities.remove(dbo) == false) {
			throw new IllegalArgumentException();
		}
		dbo.deactivate();
	}
	
	/**
	 * このREPOSITORYの管理下にある {@link DatabaseObjectModel} の中から、{@code ref}が参照する {@link DatabaseObjectModel}を返す。
	 * 
	 * @param <T> {@link DatabaseObjectModel}の型
	 * @param ref 参照オブジェクト
	 * @return {@link DatabaseObjectModel}
	 * @throws EntityNotFoundException 該当する {@link DatabaseObjectModel} が見つからなかった場合
	 */
	public <T extends DatabaseObjectModel>T resolve(EntityRef<T> ref) {
		@SuppressWarnings("unchecked")
		T result = (T) resolve(ref.getReferenceId());
		return result;
	}
	
	/**
	 * このREPOSITORYの管理下にある {@link Entity} の中から、指定したENTITY IDを持つ {@link Entity}を返す。
	 * 
	 * @param id ENTITY ID
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 該当する {@link Entity} が見つからなかった場合
	 */
	public Entity resolve(UUID id) {
		for (Entity dbo : entities) {
			if (dbo.getId().equals(id)) {
				return dbo;
			}
		}
		throw new EntityNotFoundException();
	}
	
	/**
	 * このREPOSITORYの管理下にある {@link TableModel} の中から、{@code ref}が参照する {@link ColumnModel}を返す。
	 * 
	 * @param ref 参照オブジェクト
	 * @return {@link ColumnModel}
	 * @throws EntityNotFoundException 該当する {@link DatabaseObjectModel} が見つからなかった場合
	 * @throws TooManyColumnsFoundException 複数のカラムが見つかった場合
	 */
	public ColumnModel resolveColumn(final EntityRef<ColumnModel> ref) {
		Collection<ColumnModel> collector = CollectionsUtil.newArrayList();
		for (TableModel tableModel : findTables()) {
			CollectionUtils.select(tableModel.getColumns(), new Predicate<ColumnModel>() {
				
				public boolean evaluate(ColumnModel column) {
					return column.getId().equals(ref.getReferenceId());
				}
			}, collector);
		}
		if (collector.size() == 1) {
			return collector.iterator().next();
		}
		if (collector.size() == 0) {
			throw new EntityNotFoundException();
		}
		throw new TooManyColumnsFoundException(collector);
	}
}
