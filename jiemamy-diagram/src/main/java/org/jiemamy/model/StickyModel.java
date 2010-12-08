/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jiemamy.Entity;
import org.jiemamy.EntityRef;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmRectangle;

/**
 * ダイアグラムにおける「付箋（注釈）」を表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public class StickyModel extends AbstractEntityModel implements NodeModel {
	
	/** 内容文 */
	private String contents;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public StickyModel(UUID id) {
		super(id);
	}
	
	@Override
	public StickyModel clone() {
		return (StickyModel) super.clone();
	}
	
	public int compareTo(NodeModel o) {
		if (o == null) {
			return -1;
		}
		if (getClass() != o.getClass()) {
			return this.getClass().getName().compareTo(o.getClass().getName());
		}
		return getId().compareTo(((StickyModel) o).getId());
	}
	
	public JmRectangle getBoundary() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public JmColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 内容文を取得する。
	 * 
	 * @return 内容文. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	public String getContents() {
		return contents;
	}
	
	public EntityRef<? extends DatabaseObjectModel> getCoreModelRef() {
		return new EntityRef<DatabaseObjectModel>() {
			
			public UUID getReferentId() {
				return null;
			}
			
			public boolean isReferenceOf(Entity target) {
				return false;
			}
		};
	}
	
	public List<? extends ConnectionModel> getSourceConnections() {
		// 現状で、Stickyはコネクションを持たない
		return Collections.emptyList();
	}
	
	public List<? extends ConnectionModel> getTargetConnections() {
		// 現状で、Stickyはコネクションを持たない
		return Collections.emptyList();
	}
	
	/**
	* 内容文を設定する。
	* 
	 * <p>未設定とする場合は{@code null}を与えるが、モデルとしては無効となる。</p>
	 * 
	* @param contents 内容文
	* @since 0.2
	*/
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public EntityRef<StickyModel> toReference() {
		return new DefaultEntityRef<StickyModel>(this);
	}
}
