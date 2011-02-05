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

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.EntityResolver;
import org.jiemamy.dddbase.OrderedEntity;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;

/**
 * 一枚のER図を表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public interface JmDiagram extends OrderedEntity, EntityResolver {
	
	JmDiagram clone();
	
	/**
	 * このダイアグラムにおける、指定した {@link JmForeignKeyConstraint} の写像となる {@link JmConnection} を取得する。
	 * 
	 * @param reference {@link JmForeignKeyConstraint}の参照
	 * @return 写像となる {@link JmConnection}、存在しない場合は{@code null} 
	 */
	JmConnection getConnectionFor(EntityRef<? extends JmForeignKeyConstraint> reference);
	
	/**
	 * このダイアグラムが持つ{@link JmConnection}の集合を取得する。
	 * 
	 * @return {@link JmNode}の集合
	 */
	Collection<? extends JmConnection> getConnections();
	
	/**
	 * 表示レベルを取得する。
	 * 
	 * @return 表示レベル. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	Level getLevel();
	
	/**
	 * 物理/論理モードを取得する。
	 * 
	 * @return 物理/論理モード. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	Mode getMode();
	
	/**
	 * ダイアグラム名を取得する。
	 * 
	 * @return ダイアグラム名
	 */
	String getName();
	
	/**
	 * このダイアグラムにおける、指定した {@link DbObject} の写像となる {@link JmNode} を取得する。
	 * 
	 * @param reference {@link DbObject}の参照
	 * @return 写像となる {@link JmNode}、存在しない場合は{@code null} 
	 */
	JmNode getNodeFor(EntityRef<? extends DbObject> reference);
	
	/**
	 * このダイアグラムが持つ{@link JmNode}の集合を取得する。
	 * 
	 * @return {@link JmNode}の集合
	 */
	Collection<? extends JmNode> getNodes();
	
	/**
	 * 指定したノードを接続元（起点）とするコネクションの集合を取得する。
	 * 
	 * <p>返される{@link Collection}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @param nodeRef ノード参照
	 * @return コネクションの集合
	 * @throws IllegalArgumentException 指定したノードが、このダイアグラムに属していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	Collection<? extends JmConnection> getSourceConnectionsFor(EntityRef<? extends JmNode> nodeRef);
	
	/**
	 * 指定したノードを接続先（終点）とするコネクションの集合を取得する。
	 * 
	 * <p>返される{@link Collection}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @param nodeRef ノード参照
	 * @return コネクションの集合
	 * @throws IllegalArgumentException 指定したノードが、このダイアグラムに属していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	Collection<? extends JmConnection> getTargetConnectionsFor(EntityRef<? extends JmNode> nodeRef);
	
	<E extends Entity>E resolve(EntityRef<E> reference);
	
	Entity resolve(UUID id);
	
	EntityRef<? extends JmDiagram> toReference();
}
