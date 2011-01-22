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

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OrderedEntity;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;

/**
 * 一枚のER図を表すモデルインターフェイス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public interface DiagramModel extends OrderedEntity {
	
	DiagramModel clone();
	
	/**
	 * このダイアグラムにおける、指定した {@link ForeignKeyConstraintModel} の写像となる {@link ConnectionModel} を取得する。
	 * 
	 * @param ref {@link ForeignKeyConstraintModel}の参照
	 * @return 写像となる {@link ConnectionModel}、存在しない場合は{@code null} 
	 */
	ConnectionModel getConnectionFor(EntityRef<? extends ForeignKeyConstraintModel> ref);
	
	/**
	 * このダイアグラムが持つ{@link ConnectionModel}の集合を取得する。
	 * 
	 * @return {@link NodeModel}の集合
	 */
	Collection<? extends ConnectionModel> getConnections();
	
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
	 * このダイアグラムにおける、指定した {@link DatabaseObjectModel} の写像となる {@link NodeModel} を取得する。
	 * 
	 * @param ref {@link DatabaseObjectModel}の参照
	 * @return 写像となる {@link NodeModel}、存在しない場合は{@code null} 
	 */
	NodeModel getNodeFor(EntityRef<? extends DatabaseObjectModel> ref);
	
	/**
	 * このダイアグラムが持つ{@link NodeModel}の集合を取得する。
	 * 
	 * @return {@link NodeModel}の集合
	 */
	Collection<? extends NodeModel> getNodes();
	
	/**
	 * 指定したノードを接続元（起点）とするコネクションの集合を取得する。
	 * 
	 * <p>返される{@link Collection}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @param ref ノード参照
	 * @return コネクションの集合
	 * @throws IllegalArgumentException 指定したノードが、このダイアグラムに属していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	Collection<? extends ConnectionModel> getSourceConnectionsFor(EntityRef<? extends NodeModel> ref);
	
	/**
	 * 指定したノードを接続先（終点）とするコネクションの集合を取得する。
	 * 
	 * <p>返される{@link Collection}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @param ref ノード参照
	 * @return コネクションの集合
	 * @throws IllegalArgumentException 指定したノードが、このダイアグラムに属していない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	Collection<? extends ConnectionModel> getTargetConnections(EntityRef<? extends NodeModel> ref);
	
	EntityRef<? extends DiagramModel> toReference();
}
