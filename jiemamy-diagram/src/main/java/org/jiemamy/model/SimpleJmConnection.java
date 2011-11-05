/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import org.jiemamy.dddbase.DefaultUUIDEntityRef;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmPoint;

/**
 * {@link JmConnection}のデフォルト実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public final class SimpleJmConnection extends AbstractEntity<UUID> implements JmConnection {
	
	private List<JmPoint> bendpoints = Lists.newArrayList();
	
	private JmColor color;
	
	private UUIDEntityRef<? extends JmNode> source;
	
	private UUIDEntityRef<? extends JmNode> target;
	
	private final UUIDEntityRef<? extends JmForeignKeyConstraint> coreModelRef;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @param coreModelRef CORE側の実体となる外部キーへの参照
	 */
	public SimpleJmConnection(UUID id, UUIDEntityRef<? extends JmForeignKeyConstraint> coreModelRef) {
		super(id);
		Validate.notNull(coreModelRef);
		this.coreModelRef = coreModelRef;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 * 
	 * @param coreModelRef CORE側の実体となる外部キーへの参照
	 */
	public SimpleJmConnection(UUIDEntityRef<? extends JmForeignKeyConstraint> coreModelRef) {
		this(UUID.randomUUID(), coreModelRef);
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
	public SimpleJmConnection clone() {
		SimpleJmConnection clone = (SimpleJmConnection) super.clone();
		clone.bendpoints = CloneUtil.cloneValueArrayList(bendpoints);
		return clone;
	}
	
	public List<JmPoint> getBendpoints() {
		return MutationMonitor.monitor(Lists.newArrayList(bendpoints));
	}
	
	public JmColor getColor() {
		return color;
	}
	
	public UUIDEntityRef<? extends JmForeignKeyConstraint> getCoreModelRef() {
		return coreModelRef;
	}
	
	public UUIDEntityRef<? extends JmNode> getSource() {
		return source;
	}
	
	public UUIDEntityRef<? extends JmNode> getTarget() {
		return target;
	}
	
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
	public void setSource(UUIDEntityRef<? extends JmNode> source) {
		Validate.notNull(source);
		this.source = source;
	}
	
	/**
	 * 接続先ノードを設定する。
	 * 
	 * @param target 接続先ノード
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setTarget(UUIDEntityRef<? extends JmNode> target) {
		Validate.notNull(target);
		this.target = target;
	}
	
	@Override
	public UUIDEntityRef<? extends SimpleJmConnection> toReference() {
		return new DefaultUUIDEntityRef<SimpleJmConnection>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{bendpoints=" + bendpoints + ", core=" + coreModelRef + "}";
	}
}
