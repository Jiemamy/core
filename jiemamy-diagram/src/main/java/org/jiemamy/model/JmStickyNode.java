/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/09/17
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

import java.util.UUID;

import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;

/**
 * ダイアグラムにおける「付箋（注釈）」を表すモデルインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public final class JmStickyNode extends SimpleJmNode {
	
	/** 内容文 */
	private String contents;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public JmStickyNode() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public JmStickyNode(UUID id) {
		super(id);
	}
	
	@Override
	public JmStickyNode clone() {
		JmStickyNode clone = (JmStickyNode) super.clone();
		return clone;
	}
	
	/**
	 * 内容文を取得する。
	 * 
	 * @return 内容文. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getContents() {
		return contents;
	}
	
	/**
	* 内容文を設定する。
	* 
	 * <p>未設定とする場合は{@code null}を与えるが、モデルとしては無効となる。</p>
	 * 
	* @param contents 内容文
	* @since 0.3
	*/
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	@Override
	public EntityRef<? extends JmStickyNode> toReference() {
		return new DefaultEntityRef<JmStickyNode>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(sb.length() - 1, "," + contents);
		return sb.toString();
	}
}
