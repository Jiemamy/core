/*
 * Copyright 2010 Jiemamy Project and the others.
 * Created on 2010/12/08
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
import java.util.Collections;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.utils.MutationMonitor;

/**
 * {@link NodeModel}のデフォルト実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DefaultDatabaseObjectNodeModel extends DefaultNodeModel implements DatabaseObjectNodeModel {
	
	private final EntityRef<? extends DatabaseObjectModel> coreModelRef;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @param coreModelRef コアモデルへの参照
	 */
	public DefaultDatabaseObjectNodeModel(UUID id, EntityRef<? extends DatabaseObjectModel> coreModelRef) {
		super(id);
		this.coreModelRef = coreModelRef;
	}
	
	@Override
	public Collection<? extends ConnectionModel> breachEncapsulationOfSourceConnections() {
		return sourceConnections;
	}
	
	@Override
	public DefaultDatabaseObjectNodeModel clone() {
		DefaultDatabaseObjectNodeModel clone = (DefaultDatabaseObjectNodeModel) super.clone();
		clone.sourceConnections = CloneUtil.cloneEntityArrayList(sourceConnections);
		return clone;
	}
	
	@Override
	public void delete(EntityRef<? extends ConnectionModel> reference) {
		for (ConnectionModel connectionModel : Lists.newArrayList(sourceConnections)) {
			if (reference.isReferenceOf(connectionModel)) {
				sourceConnections.remove(connectionModel);
				return;
			}
		}
		throw new EntityNotFoundException("ref=" + reference);
	}
	
	public EntityRef<? extends DatabaseObjectModel> getCoreModelRef() {
		return coreModelRef;
	}
	
	@Override
	public Collection<? extends ConnectionModel> getSourceConnections() {
		return MutationMonitor.monitor(Lists.newArrayList(sourceConnections));
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return Lists.newArrayList(sourceConnections);
	}
	
	@Override
	public Collection<? extends ConnectionModel> getTargetConnections() {
		return Collections.emptyList(); // FIXME ロジックで算出しなければならない
	}
	
	@Override
	public void store(ConnectionModel connection) {
		Validate.notNull(connection);
		if (sourceConnections.contains(connection)) {
			sourceConnections.remove(connection);
		}
		sourceConnections.add(connection.clone());
	}
	
	@Override
	public EntityRef<? extends DefaultDatabaseObjectNodeModel> toReference() {
		return new DefaultEntityRef<DefaultDatabaseObjectNodeModel>(this);
	}
}
