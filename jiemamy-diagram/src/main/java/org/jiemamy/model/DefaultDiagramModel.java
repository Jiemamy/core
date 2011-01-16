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
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;

/**
 * {@link DiagramModel}のデフォルト実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public final class DefaultDiagramModel extends AbstractEntity implements DiagramModel {
	
	private String name;
	
	private Level level = Level.ATTRTYPE;
	
	private Mode mode = Mode.PHYSICAL;
	
	private OnMemoryRepository<NodeModel> nodes = new OnMemoryRepository<NodeModel>();
	
	private OnMemoryRepository<ConnectionModel> connections = new OnMemoryRepository<ConnectionModel>();
	
	private int index = -1;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultDiagramModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultDiagramModel clone() {
		DefaultDiagramModel clone = (DefaultDiagramModel) super.clone();
		clone.nodes = nodes.clone();
		return clone;
	}
	
	/**
	 * {@link ConnectionModel}を削除する。
	 * 
	 * @param reference 削除する{@link ConnectionModel}への参照
	 * @throws EntityNotFoundException このダイアグラムが指定したノードを管理していない場合
	 */
	public void deleteConnection(EntityRef<? extends ConnectionModel> reference) {
		connections.delete(reference);
	}
	
	/**
	 * {@link NodeModel}を削除する。
	 * 
	 * @param reference 削除する{@link NodeModel}への参照
	 * @throws EntityNotFoundException このダイアグラムが指定したノードを管理していない場合
	 */
	public void deleteNode(EntityRef<? extends NodeModel> reference) {
		nodes.delete(reference);
	}
	
	public ConnectionModel getConnectionFor(EntityRef<? extends ForeignKeyConstraintModel> ref) {
		Validate.notNull(ref);
		for (ConnectionModel connectionModel : connections.getEntitiesAsSet()) {
			if (ref.equals(connectionModel.getCoreModelRef())) {
				return connectionModel;
			}
		}
		throw new EntityNotFoundException("ref=" + ref);
	}
	
	public Collection<? extends ConnectionModel> getConnections() {
		return connections.getEntitiesAsSet();
	}
	
	public int getIndex() {
		return index;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public String getName() {
		return name;
	}
	
	public NodeModel getNodeFor(EntityRef<? extends DatabaseObjectModel> ref) {
		Validate.notNull(ref);
		for (NodeModel node : nodes.getEntitiesAsSet()) {
			if (node instanceof DatabaseObjectNodeModel) {
				DatabaseObjectNodeModel databaseObjectNodeModel = (DatabaseObjectNodeModel) node;
				if (ref.equals(databaseObjectNodeModel.getCoreModelRef())) {
					return node;
				}
			}
		}
		throw new EntityNotFoundException("ref=" + ref);
	}
	
	public Collection<? extends NodeModel> getNodes() {
		return nodes.getEntitiesAsSet();
	}
	
	public Collection<? extends ConnectionModel> getSourceConnectionsFor(EntityRef<? extends NodeModel> ref) {
		Validate.notNull(ref);
		Validate.isTrue(nodes.contains(ref));
		Collection<ConnectionModel> result = Lists.newArrayList();
		for (ConnectionModel connectionModel : getConnections()) {
			if (connectionModel.getSource().equals(ref)) {
				result.add(connectionModel);
			}
		}
		return result;
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return Lists.newArrayList(Iterables.concat(getNodes(), getConnections()));
	}
	
	public Collection<? extends ConnectionModel> getTargetConnections(EntityRef<? extends NodeModel> ref) {
		Validate.notNull(ref);
		Validate.isTrue(nodes.contains(ref));
		Collection<ConnectionModel> result = Lists.newArrayList();
		for (ConnectionModel connectionModel : getConnections()) {
			if (connectionModel.getTarget().equals(ref)) {
				result.add(connectionModel);
			}
		}
		return result;
	}
	
	/**
	 * エンティティ参照から、{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子エンティティも含む。</p>
	 * 
	 * @param <T> エンティティの型
	 * @param ref エンティティ参照
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 */
	public <T extends Entity>T resolve(EntityRef<T> ref) {
		return nodes.resolve(ref);
	}
	
	/**
	 * 指定したIDを持つ{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子エンティティも含む。</p>
	 * 
	 * @param id ENTITY ID
	 * @return 見つかった{@link Entity}
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 * @since 1.0.0
	 */
	public Entity resolve(UUID id) {
		return nodes.resolve(id);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * 表示レベルを設定する。
	 * 
	 * @param level 表示レベル
	 */
	public void setLevel(Level level) {
		this.level = level;
	}
	
	/**
	 * 表示モードを設定する。
	 * 
	 * @param mode 表示モード
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	/**
	 * ダイアグラム名を設定する。
	 * 
	 * @param name ダイアグラム名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * {@link ConnectionModel}を保存する。
	 * 
	 * @param connectionModel {@link ConnectionModel}
	 */
	public void store(ConnectionModel connectionModel) {
		connections.store(connectionModel);
	}
	
	/**
	 * {@link NodeModel}を保存する。
	 * 
	 * @param nodeModel {@link NodeModel}
	 */
	public void store(NodeModel nodeModel) {
		nodes.store(nodeModel);
	}
	
	public EntityRef<? extends DefaultDiagramModel> toReference() {
		return new DefaultEntityRef<DefaultDiagramModel>(this);
	}
	
	@Override
	public String toString() {
		return "Diagram[" + index + ":" + name + "]";
	}
}
