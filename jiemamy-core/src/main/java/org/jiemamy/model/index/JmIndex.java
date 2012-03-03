/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.model.index;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.DbObject;

/**
 * インデックスモデル。
 * 
 * @author daisuke
 */
public final class JmIndex extends DbObject {
	
	/** ユニークインデックスか否か */
	private boolean unique;
	
	/** インデックスカラムのリスト */
	private List<JmIndexColumn> indexColumns = Lists.newArrayList();
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public JmIndex() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public JmIndex(UUID id) {
		super(id);
	}
	
	/**
	 * インデックスカラムを追加する。
	 * 
	 * @param indexColumn インデックスカラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void addIndexColumn(JmIndexColumn indexColumn) {
		Validate.notNull(indexColumn);
		indexColumns.add(indexColumn);
	}
	
	@Override
	public JmIndex clone() {
		JmIndex clone = (JmIndex) super.clone();
		clone.indexColumns = CloneUtil.cloneValueArrayList(indexColumns);
		return clone;
	}
	
	/**
	 * インデックスカラムのリストを取得する。
	 * 
	 * @return インデックスカラムのリスト
	 * @since 0.3
	 */
	public List<JmIndexColumn> getIndexColumns() {
		assert indexColumns != null;
		return MutationMonitor.monitor(Lists.newArrayList(indexColumns));
	}
	
	public boolean isUnique() {
		return unique;
	}
	
	/**
	 * インデックスカラムを削除する。
	 * 
	 * @param indexColumn インデックスカラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void reomveIndexColumn(JmIndexColumn indexColumn) {
		Validate.notNull(indexColumn);
		indexColumns.remove(indexColumn);
	}
	
	/**
	 * ユニークインデックスか否かを設定する
	 * 
	 * @param unique ユニークインデックスの場合は{@code true}、そうでない場合は{@code false}
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	@Override
	public EntityRef<? extends JmIndex> toReference() {
		return new EntityRef<JmIndex>(this);
	}
}
