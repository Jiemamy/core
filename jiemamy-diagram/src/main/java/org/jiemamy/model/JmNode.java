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

import java.util.UUID;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmRectangle;

/**
 * ダイアグラム上のノードを表すインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public abstract class JmNode extends AbstractEntity {
	
	private JmRectangle boundary;
	
	private JmColor color;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public JmNode(UUID id) {
		super(id);
	}
	
	@Override
	public JmNode clone() {
		JmNode clone = (JmNode) super.clone();
		return clone;
	}
	
	/**
	 * ノードのレイアウト情報を取得する。
	 * 
	 * @return ノードのレイアウト情報. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public JmRectangle getBoundary() {
		return boundary;
	}
	
	/**
	 * ノードの色情報を取得する。
	 * 
	 * @return ノードの色情報. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public JmColor getColor() {
		return color;
	}
	
	/**
	 * ノードの位置を設定する。
	 * 
	 * @param boundary 位置
	 */
	public void setBoundary(JmRectangle boundary) {
		this.boundary = boundary;
	}
	
	/**
	 * ノードの色を設定する。
	 * 
	 * @param color 色
	 */
	public void setColor(JmColor color) {
		this.color = color;
	}
	
	@Override
	public EntityRef<? extends JmNode> toReference() {
		return new EntityRef<JmNode>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{" + boundary + ", " + color + "}";
	}
}
