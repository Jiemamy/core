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

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmPoint;

/**
 * {@link ConnectionModel}のデフォルト実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public final class DefaultConnectionModel extends AbstractEntity implements ConnectionModel {
	
	private List<JmPoint> bendpoints = Lists.newArrayList();
	
	private JmColor color;
	
	private EntityRef<? extends NodeModel> source;
	
	private EntityRef<? extends NodeModel> target;
	
	private final EntityRef<? extends ForeignKeyConstraintModel> coreModelRef;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @param coreModelRef CORE側の実体となる外部キーへの参照
	 */
	public DefaultConnectionModel(UUID id, EntityRef<? extends ForeignKeyConstraintModel> coreModelRef) {
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
	public DefaultConnectionModel clone() {
		DefaultConnectionModel clone = (DefaultConnectionModel) super.clone();
		clone.bendpoints = CloneUtil.cloneValueArrayList(bendpoints);
		return clone;
	}
	
	public List<JmPoint> getBendpoints() {
		return MutationMonitor.monitor(Lists.newArrayList(bendpoints));
	}
	
	public JmColor getColor() {
		return color;
	}
	
	public EntityRef<? extends ForeignKeyConstraintModel> getCoreModelRef() {
		return coreModelRef;
	}
	
	public EntityRef<? extends NodeModel> getSource() {
		return source;
	}
	
	public EntityRef<? extends NodeModel> getTarget() {
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
	public void setSource(EntityRef<? extends NodeModel> source) {
		Validate.notNull(source);
		this.source = source;
	}
	
	/**
	 * 接続先ノードを設定する。
	 * 
	 * @param target 接続先ノード
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setTarget(EntityRef<? extends NodeModel> target) {
		Validate.notNull(target);
		this.target = target;
	}
	
	public EntityRef<? extends DefaultConnectionModel> toReference() {
		return new DefaultEntityRef<DefaultConnectionModel>(this);
	}
}
