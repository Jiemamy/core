/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
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
public final class JmConnection extends AbstractEntity {
	
	private List<JmPoint> bendpoints = Lists.newArrayList();
	
	private JmColor color;
	
	private EntityRef<? extends JmNode> source;
	
	private EntityRef<? extends JmNode> target;
	
	private final EntityRef<? extends JmForeignKeyConstraint> coreModelRef;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 * 
	 * @param coreModelRef CORE側の実体となる外部キーへの参照
	 */
	public JmConnection(EntityRef<? extends JmForeignKeyConstraint> coreModelRef) {
		this(UUID.randomUUID(), coreModelRef);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @param coreModelRef CORE側の実体となる外部キーへの参照
	 */
	public JmConnection(UUID id, EntityRef<? extends JmForeignKeyConstraint> coreModelRef) {
		super(id);
		Validate.notNull(coreModelRef);
		this.coreModelRef = coreModelRef;
	}
	
	/**
	 * ベンドポイントのリストを取得する。
	 * 
	 * <p>このメソッドはフィールドとして保持する可変オブジェクトを直接返すことによって、
	 * 内部表現を暴露していることに注意すること。</p>
	 * 
	 * @return ベンドポイントのリスト
	 * @see #getBendpoints()
	 */
	public List<JmPoint> breachEncapsulationOfBendpoints() {
		return bendpoints;
	}
	
	@Override
	public JmConnection clone() {
		JmConnection clone = (JmConnection) super.clone();
		clone.bendpoints = CloneUtil.cloneValueArrayList(bendpoints);
		return clone;
	}
	
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
	public List<JmPoint> getBendpoints() {
		return MutationMonitor.monitor(Lists.newArrayList(bendpoints));
	}
	
	/**
	 * コネクションの色情報を取得する。
	 * 
	 * @return コネクションの色情報. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public JmColor getColor() {
		return color;
	}
	
	/**
	 * コアモデルへの参照を取得する。
	 * 
	 * @return コアモデルへの参照
	 * @since 0.3
	 */
	public EntityRef<? extends JmForeignKeyConstraint> getCoreModelRef() {
		return coreModelRef;
	}
	
	/**
	 * 接続元ノードを取得する。
	 * 
	 * @return 接続元ノード
	 * @throws ModelConsistencyException モデルの不整合により、接続元ノードが不明な場合
	 * @since 0.3
	 */
	public EntityRef<? extends JmNode> getSource() {
		return source;
	}
	
	/**
	 * 接続先ノードを取得する。 
	 * 
	 * @return 接続先ノード
	 * @throws ModelConsistencyException モデルの不整合により、接続先ノードが不明な場合
	 * @since 0.3
	 */
	public EntityRef<? extends JmNode> getTarget() {
		return target;
	}
	
	/**
	 * 自分同士を繋ぐコネクションであるかどうかを調べる。
	 * 
	 * @return 自己コネクションである場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalStateException sourceまたはtargetが{@code null}の場合
	 * @since 0.3
	 */
	public boolean isSelfConnection() {
		if (getSource() == null || getTarget() == null) {
			throw new IllegalStateException();
		}
		return getSource().equals(getTarget());
	}
	
	/**
	 * コネクションの色を設定する。
	 * 
	 * @param color 色
	 */
	public void setColor(JmColor color) {
		this.color = color;
	}
	
	/**
	 * 接続元ノードを設定する。
	 * 
	 * @param source 接続元ノード
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setSource(EntityRef<? extends JmNode> source) {
		Validate.notNull(source);
		this.source = source;
	}
	
	/**
	 * 接続先ノードを設定する。
	 * 
	 * @param target 接続先ノード
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setTarget(EntityRef<? extends JmNode> target) {
		Validate.notNull(target);
		this.target = target;
	}
	
	@Override
	public EntityRef<? extends JmConnection> toReference() {
		return new EntityRef<JmConnection>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{bendpoints=" + bendpoints + ", core=" + coreModelRef + "}";
	}
}
