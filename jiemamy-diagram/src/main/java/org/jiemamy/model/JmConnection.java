/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/02/28
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

import java.util.List;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmPoint;

/**
 * コネクションのビジュアル情報インターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public interface JmConnection extends Entity {
	
	JmConnection clone();
	
	/**
	 * ベンドポイントのリストを取得する。
	 * 
	 * <p>リストの順序は、コネクションのsource側からtargetに向かう順序である。</p>
	 * 
	 * <p>ベンドポイントが1つも設定されていない場合は、空のリストを返す。</p>
	 * 
	 * @return ベンドポイントのリスト
	 * @since 0.3
	 */
	List<JmPoint> getBendpoints();
	
	/**
	 * コネクションの色情報を取得する。
	 * 
	 * @return コネクションの色情報. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	JmColor getColor();
	
	/**
	 * コアモデルへの参照を取得する。
	 * 
	 * @return コアモデルへの参照
	 * @since 0.3
	 */
	EntityRef<? extends JmForeignKeyConstraint> getCoreModelRef();
	
	/**
	 * 接続元ノードを取得する。
	 * 
	 * @return 接続元ノード
	 * @throws ModelConsistencyException モデルの不整合により、接続元ノードが不明な場合
	 * @since 0.3
	 */
	EntityRef<? extends JmNode> getSource();
	
	/**
	 * 接続先ノードを取得する。 
	 * 
	 * @return 接続先ノード
	 * @throws ModelConsistencyException モデルの不整合により、接続先ノードが不明な場合
	 * @since 0.3
	 */
	EntityRef<? extends JmNode> getTarget();
	
	/**
	 * 自分同士を繋ぐコネクションであるかどうかを調べる。
	 * 
	 * @return 自己コネクションである場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalStateException sourceまたはtargetが{@code null}の場合
	 * @since 0.3
	 */
	boolean isSelfConnection();
	
	EntityRef<? extends JmConnection> toReference();
}
