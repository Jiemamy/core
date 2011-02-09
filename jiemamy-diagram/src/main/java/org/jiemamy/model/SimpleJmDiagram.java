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
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.AbstractOrderedEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryCompositeEntityResolver;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.geometory.JmPoint;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.utils.LogMarker;

/**
 * {@link JmDiagram}のデフォルト実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmDiagram extends AbstractOrderedEntity implements JmDiagram {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJmDiagram.class);
	
	private static final int SELF_CONNECTION_OFFSET = 50;
	
	private String name;
	
	private Level level = Level.ATTRTYPE;
	
	private Mode mode = Mode.PHYSICAL;
	
	private OnMemoryRepository<JmNode> nodes = new OnMemoryRepository<JmNode>();
	
	private OnMemoryRepository<JmConnection> connections = new OnMemoryRepository<JmConnection>();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public SimpleJmDiagram() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public SimpleJmDiagram(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmDiagram clone() {
		SimpleJmDiagram clone = (SimpleJmDiagram) super.clone();
		clone.nodes = nodes.clone();
		clone.connections = connections.clone();
		return clone;
	}
	
	public boolean contains(EntityRef<?> reference) {
		Validate.notNull(reference);
		return contains(reference.getReferentId());
	}
	
	public boolean contains(UUID id) {
		return new OnMemoryCompositeEntityResolver(nodes, connections).contains(id);
	}
	
	/**
	 * {@link JmConnection}を削除する。
	 * 
	 * @param reference 削除する{@link JmConnection}への参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException このダイアグラムが指定したノードを管理していない場合
	 */
	public JmConnection deleteConnection(EntityRef<? extends JmConnection> reference) {
		JmConnection deleted = connections.delete(reference);
		logger.info("connection deleted: " + deleted);
		return deleted;
	}
	
	/**
	 * {@link JmNode}を削除する。
	 * 
	 * @param reference 削除する{@link JmNode}への参照
	 * @return 削除したモデル
	 * @throws ModelConsistencyException ノードに関連付くコネクションがある場合
	 * @throws EntityNotFoundException このダイアグラムが指定したノードを管理していない場合
	 */
	public JmNode deleteNode(EntityRef<? extends JmNode> reference) {
		Collection<? extends JmConnection> sourceConnections = getSourceConnectionsFor(reference);
		if (sourceConnections.size() > 0) {
			throw new ModelConsistencyException("node has source connections: " + sourceConnections);
		}
		Collection<? extends JmConnection> targetConnections = getTargetConnectionsFor(reference);
		if (targetConnections.size() > 0) {
			throw new ModelConsistencyException("node has target connections: " + targetConnections);
		}
		JmNode deleted = nodes.delete(reference);
		logger.info("node deleted: " + deleted);
		return deleted;
	}
	
	public JmConnection getConnectionFor(EntityRef<? extends JmForeignKeyConstraint> reference) {
		Validate.notNull(reference);
		for (JmConnection connection : connections.getEntitiesAsSet()) {
			if (reference.equals(connection.getCoreModelRef())) {
				return connection;
			}
		}
		return null;
//		throw new EntityNotFoundException("ref=" + reference);
	}
	
	public Collection<? extends JmConnection> getConnections() {
		return connections.getEntitiesAsSet();
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
	
	public JmNode getNodeFor(EntityRef<? extends DbObject> reference) {
		Validate.notNull(reference);
		for (JmNode node : nodes.getEntitiesAsSet()) {
			if (node instanceof DbObjectNode) {
				DbObjectNode dbObjectNode = (DbObjectNode) node;
				if (reference.equals(dbObjectNode.getCoreModelRef())) {
					return node;
				}
			}
		}
		return null;
//		throw new EntityNotFoundException("ref=" + reference);
	}
	
	public Collection<? extends JmNode> getNodes() {
		return nodes.getEntitiesAsSet();
	}
	
	public Collection<? extends JmConnection> getSourceConnectionsFor(EntityRef<? extends JmNode> reference) {
		Validate.notNull(reference);
		Validate.isTrue(nodes.contains(reference));
		Collection<JmConnection> result = Lists.newArrayList();
		for (JmConnection connection : getConnections()) {
			if (connection.getSource().equals(reference)) {
				result.add(connection);
			}
		}
		return result;
	}
	
	@Override
	public Collection<? extends Entity> getSubEntities() {
		return Lists.newArrayList(Iterables.concat(getNodes(), getConnections()));
	}
	
	public Collection<? extends JmConnection> getTargetConnectionsFor(EntityRef<? extends JmNode> reference) {
		Validate.notNull(reference);
		Validate.isTrue(nodes.contains(reference));
		Collection<JmConnection> result = Lists.newArrayList();
		for (JmConnection connection : getConnections()) {
			if (connection.getTarget().equals(reference)) {
				result.add(connection);
			}
		}
		return result;
	}
	
	/**
	 * {@link EntityRef}から、{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子{@link Entity}も含む。</p>
	 * 
	 * @param <T> {@link Entity}の型
	 * @param reference {@link EntityRef}
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 */
	public <T extends Entity>T resolve(EntityRef<T> reference) {
		return new OnMemoryCompositeEntityResolver(nodes, connections).resolve(reference);
	}
	
	/**
	 * 指定したIDを持つ{@link Entity}を引き当てる。
	 * 
	 * <p>リポジトリは、この実体のクローンを返す。従って、取得した {@link Entity}に対して
	 * ミューテーションを起こしても、ストアした実体には影響を及ぼさない。</p>
	 * 
	 * <p>検索対象は子{@link Entity}も含む。</p>
	 * 
	 * @param id ENTITY ID
	 * @return 見つかった{@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @since 1.0.0
	 */
	public Entity resolve(UUID id) {
		return new OnMemoryCompositeEntityResolver(nodes, connections).resolve(id);
	}
	
	/**
	 * 表示レベルを設定する。
	 * 
	 * @param level 表示レベル
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setLevel(Level level) {
		Validate.notNull(level);
		this.level = level;
	}
	
	/**
	 * 表示モードを設定する。
	 * 
	 * @param mode 表示モード
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setMode(Mode mode) {
		Validate.notNull(mode);
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
	 * {@link JmConnection}を保存する。
	 * 
	 * <p>{@link JmConnection}が自己結合コネクションであり、bendpointが1つも無い場合は、
	 * 自動敵にbendpointを2つ作製する。</p>
	 * 
	 * @param connection {@link JmConnection}
	 * @throws ModelConsistencyException ダイアグラムがコネクションのsourceとtargetを保持していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(JmConnection connection) {
		Validate.notNull(connection);
		if (nodes.contains(connection.getSource()) == false) {
			throw new ModelConsistencyException("connection's source node is unknown: " + connection.getSource());
		}
		if (nodes.contains(connection.getTarget()) == false) {
			throw new ModelConsistencyException("connection's target node is unknown: " + connection.getTarget());
		}
		if (connection.isSelfConnection() && connection.getBendpoints().isEmpty()
				&& connection instanceof SimpleJmConnection) {
			SimpleJmConnection simpleJmConnection = (SimpleJmConnection) connection;
			JmRectangle boundary = nodes.resolve(connection.getSource()).getBoundary();
			List<JmPoint> points = simpleJmConnection.breachEncapsulationOfBendpoints();
			points.add(new JmPoint(boundary.x - SELF_CONNECTION_OFFSET, boundary.y - 0));
			points.add(new JmPoint(boundary.x - 0, boundary.y - SELF_CONNECTION_OFFSET));
		}
		JmConnection old = connections.store(connection);
		if (old == null) {
			logger.debug(LogMarker.LIFECYCLE, "connection stored: " + connection);
		} else {
			logger.debug(LogMarker.LIFECYCLE, "connection updated: (old)" + old);
			logger.debug(LogMarker.LIFECYCLE, "                    (new)" + connection);
		}
	}
	
	/**
	 * {@link JmNode}を保存する。
	 * 
	 * @param node {@link JmNode}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(JmNode node) {
		Validate.notNull(node);
		JmNode old = nodes.store(node);
		if (old == null) {
			logger.debug(LogMarker.LIFECYCLE, "node stored: " + node);
		} else {
			logger.debug(LogMarker.LIFECYCLE, "node updated: (old)" + old);
			logger.debug(LogMarker.LIFECYCLE, "              (new)" + node);
		}
	}
	
	@Override
	public EntityRef<? extends SimpleJmDiagram> toReference() {
		return new DefaultEntityRef<SimpleJmDiagram>(this);
	}
	
	@Override
	public String toString() {
		return "Diagram[" + name + "]";
	}
}
