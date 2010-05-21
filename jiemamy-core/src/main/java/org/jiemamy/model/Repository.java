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

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.utils.CollectionsUtil;

/**
 * DDDにおけるREPOSITORYの実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class Repository {
	
	private final Set<DatabaseObjectModel> databaseObjects = CollectionsUtil.newHashSet();
	

	/**
	 * 引数に指定した {@link DatabaseObjectModel} をREPOSITORYの管理下に置く。
	 * 
	 * <p>{@link DatabaseObjectModel}は、リポジトリの管理下に置かれることにより、ライフサイクルが開始する。</p>
	 * 
	 * @param dbo 管理対象
	 * @throws EntityLifecycleException 引数{@code dbo}のライフサイクルがaliveの場合
	 * @throws IllegalArgumentException 引数{@code dbo}に{@code null}を与えた場合
	 */
	public void add(DatabaseObjectModel dbo) {
		Validate.notNull(dbo);
		dbo.activate();
		boolean added = databaseObjects.add(dbo);
		assert added; // TODO add or replace処理
	}
	
	/**
	 * 直接の依存モデルの集合を返す。
	 * 
	 * @param databaseObject 対象{@link DatabaseObjectModel}
	 * @return 直接の依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSubDatabaseObjectsNonRecursive(DatabaseObjectModel databaseObject) {
		Validate.notNull(databaseObject);
		return databaseObject.findSubDatabaseObjectsNonRecursive(CollectionsUtil.newHashSet(databaseObjects));
	}
	
	/**
	 * 全ての依存モデルの集合を返す。
	 * 
	 * @param standard 基準モデル
	 * @return 全ての依存モデルの集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSubDatabaseObjectsRecursive(DatabaseObjectModel standard) {
		Validate.notNull(standard);
		Collection<DatabaseObjectModel> subModels = findSubDatabaseObjectsNonRecursive(standard);
		Set<DatabaseObjectModel> result = CollectionsUtil.newHashSet(subModels);
		
		for (DatabaseObjectModel subModel : subModels) {
			if (standard.equals(subModel) == false) {
				result.addAll(findSubDatabaseObjectsRecursive(subModel));
			}
		}
		
		return result;
	}
	
	/**
	 * 参照先親モデルを返す。
	 * 
	 * @param databaseObject 対象{@link DatabaseObjectModel}
	 * @return 親モデルのSet
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSuperDatabaseObjectsNonRecursive(DatabaseObjectModel databaseObject) {
		Validate.notNull(databaseObject);
		return databaseObject.findSuperDatabaseObjectsNonRecursive(CollectionsUtil.newHashSet(databaseObjects));
	}
	
	/**
	 * 全ての参照先 祖先モデルを返す。
	 * 
	 * @param databaseObject 対象エンティティ
	 * @return 祖先モデルのSet
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Collection<DatabaseObjectModel> findSuperDatabaseObjectsRecursive(DatabaseObjectModel databaseObject) {
		Validate.notNull(databaseObject);
		Collection<DatabaseObjectModel> superModels = findSuperDatabaseObjectsNonRecursive(databaseObject);
		Collection<DatabaseObjectModel> result = CollectionsUtil.newArrayList();
		result.addAll(superModels);
		
		for (DatabaseObjectModel superModel : superModels) {
			if (databaseObject.equals(superModel) == false) {
				result.addAll(findSuperDatabaseObjectsRecursive(superModel));
			}
		}
		
		return result;
	}
	
	/**
	 * somethingを取得する。 TODO for daisuke
	 * @return the databaseObjects
	 */
	public Set<DatabaseObjectModel> getDatabaseObjects() {
		return CollectionsUtil.newHashSet(databaseObjects);
	}
	
	/**
	 * 引数に指定した {@link DatabaseObjectModel} をREPOSITORYの管理下から外す。
	 * 
	 * <p>{@link DatabaseObjectModel}は、リポジトリの管理下から外れることにより、ライフサイクルが終了する。</p>
	 * 
	 * @param dbo 管理対象
	 * @throws IllegalArgumentException 引数{@code dbo}に{@code null}を与えた場合
	 * @throws IllegalArgumentException 引数{@code dbo}がこのREPOSITORY管理下にない場合
	 */
	public void remove(DatabaseObjectModel dbo) {
		Validate.notNull(dbo);
		if (databaseObjects.remove(dbo) == false) {
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
	 * このREPOSITORYの管理下にある {@link DatabaseObjectModel} の中から、指定したENTITY IDを持つ {@link DatabaseObjectModel}を返す。
	 * 
	 * @param id ENTITY ID
	 * @return {@link DatabaseObjectModel}
	 * @throws EntityNotFoundException 該当する {@link DatabaseObjectModel} が見つからなかった場合
	 */
	public DatabaseObjectModel resolve(UUID id) {
		for (DatabaseObjectModel dbo : databaseObjects) {
			if (dbo.getId().equals(id)) {
				return dbo;
			}
		}
		throw new EntityNotFoundException();
	}
}
