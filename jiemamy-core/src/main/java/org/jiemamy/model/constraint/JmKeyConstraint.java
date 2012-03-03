/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/28
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
package org.jiemamy.model.constraint;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.column.JmColumn;

/**
 * キー制約を表すインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @author daisuke
 */
public abstract class JmKeyConstraint extends JmConstraint {
	
	/** キー制約を構成するカラムのリスト */
	private List<EntityRef<? extends JmColumn>> keyColumns = Lists.newArrayList();
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public JmKeyConstraint(UUID id) {
		super(id);
	}
	
	/**
	 * キーカラムを追加する。
	 * 
	 * @param keyColumn キーカラム参照
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void addKeyColumn(EntityRef<? extends JmColumn> keyColumn) {
		Validate.notNull(keyColumn);
		keyColumns.add(keyColumn);
	}
	
	/**
	 * キーカラムを全削除する。
	 */
	public void clearKeyColumns() {
		keyColumns.clear();
	}
	
	@Override
	public JmKeyConstraint clone() {
		JmKeyConstraint clone = (JmKeyConstraint) super.clone();
		clone.keyColumns = CloneUtil.cloneValueArrayList(keyColumns);
		return clone;
	}
	
	/**
	 * キーを構成するカラムのリストを取得する。
	 * 
	 * <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return キーを構成するカラムのリスト
	 * @since 0.3
	 */
	public List<EntityRef<? extends JmColumn>> getKeyColumns() {
		return MutationMonitor.monitor(Lists.newArrayList(keyColumns));
	}
	
	/**
	 * キーカラムを削除する。
	 * 
	 * <p>指定したキーカラムが見つからなかった場合は、何もしない。</p>
	 * 
	 * @param keyColumn キーカラム参照
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeKeyColumn(EntityRef<? extends JmColumn> keyColumn) {
		Validate.notNull(keyColumn);
		keyColumns.remove(keyColumn);
	}
	
	@Override
	public EntityRef<? extends JmKeyConstraint> toReference() {
		return new EntityRef<JmKeyConstraint>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(sb.length() - 1, ", key=" + keyColumns);
		return sb.toString();
	}
}
