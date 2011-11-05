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

import java.util.UUID;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultUUIDEntityRef;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmRectangle;

/**
 * {@link JmNode}のデフォルト実装クラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public abstract class SimpleJmNode extends AbstractEntity<UUID> implements JmNode {
	
	private JmRectangle boundary;
	
	private JmColor color;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public SimpleJmNode(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmNode clone() {
		SimpleJmNode clone = (SimpleJmNode) super.clone();
		return clone;
	}
	
	public JmRectangle getBoundary() {
		return boundary;
	}
	
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
	public UUIDEntityRef<? extends SimpleJmNode> toReference() {
		return new DefaultUUIDEntityRef<SimpleJmNode>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{" + boundary + ", " + color + "}";
	}
}
