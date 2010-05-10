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
package org.jiemamy;

import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.Entity;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.model.EntityRef;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.utils.CollectionsUtil;

/**
 * DDDにおけるREPOSITORYの実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class Repository implements EntityListener {
	
	final InternalCredential key = new InternalCredential();
	
	private Collection<Entity> entities = CollectionsUtil.newArrayList();
	

	/**
	 * 引数に指定した {@link DatabaseObjectModel} をREPOSITORYの管理下に置く。
	 * 
	 * <p>{@link Entity}は、リポジトリの管理下に置かれることにより、ライフサイクルが開始する。</p>
	 * 
	 * <p>{@code dbo}が {@link CompositEntity}インターフェイスを実装している場合は、その子 {@link Entity}も
	 * 同時にリポジトリ管理下に置かれる。</p>
	 * 
	 * @param dbo 管理対象
	 * @throws EntityLifecycleException {@code dbo}が既にいずれかのREPOSITORY管理下にある場合
	 */
	public void add(DatabaseObjectModel dbo) {
		entityAdded(new EntityEvent(dbo));
	}
	
	public void entityAdded(EntityEvent e) {
		Entity source = (Entity) e.getSource();
		add(source);
	}
	
	public void entityRemoved(EntityEvent e) {
		Entity source = (Entity) e.getSource();
		remove(source);
	}
	
	/**
	 * 引数に指定した {@link DatabaseObjectModel} をREPOSITORYの管理下から外す。
	 * 
	 * <p>{@link Entity}は、リポジトリの管理下から外れることにより、ライフサイクルが終了する。</p>
	 * 
	 * <p>{@code dbo}が {@link CompositEntity}インターフェイスを実装している場合は、その子 {@link Entity}も
	 * 同時にリポジトリ管理下から外れる。</p>
	 * 
	 * @param dbo 管理対象
	 * @throws EntityLifecycleException {@code dbo}がこのREPOSITORY管理下にない場合
	 */
	public void remove(DatabaseObjectModel dbo) {
		entityRemoved(new EntityEvent(dbo));
	}
	
	/**
	 * このREPOSITORYの管理下にある {@link Entity} の中から、{@code ref}が参照する {@link Entity}を返す。
	 * 
	 * @param <T> {@link Entity}の型
	 * @param ref 参照オブジェクト
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 該当する {@link Entity} が見つからなかった場合
	 */
	public <T extends Entity>T resolve(EntityRef<T> ref) {
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
	
	void add(Entity entity) {
		if (entity.isAlive()) {
			throw new EntityLifecycleException();
		}
		
		entity.initiate(key);
		if (entity instanceof CompositEntity) {
			CompositEntity compositEntity = (CompositEntity) entity;
			for (Entity child : compositEntity.getChildren()) {
				add(child);
			}
			compositEntity.addListener(this);
		}
		entities.add(entity);
	}
	
	void remove(Entity entity) {
		Validate.notNull(entity.getId());
		
		boolean removed = entities.remove(entity);
		if (removed == false) {
			throw new IllegalArgumentException();
		}
		
		entity.kill(key);
		if (entity instanceof CompositEntity) {
			CompositEntity compositEntity = (CompositEntity) entity;
			compositEntity.removeListener(this);
			for (Entity child : compositEntity.getChildren()) {
				remove(child);
			}
		}
	}
	

	/**
	 * {@link Entity#initiate(InternalCredential)}及び {@link Entity#kill(InternalCredential)}メソッドを
	 * クライアントから呼び出せないようにするためのヘルパークラス。
	 * 
	 * @version $Id$
	 * @author daisuke
	 */
	public static class InternalCredential {
		
		InternalCredential() {
		}
		
	}
	
}
