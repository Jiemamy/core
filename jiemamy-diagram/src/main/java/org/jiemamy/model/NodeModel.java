/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/02/04
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

import java.util.Collection;

import org.jiemamy.IdentifiableEntity;
import org.jiemamy.EntityRef;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmRectangle;

/**
 * ノードのビジュアル情報インターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface NodeModel extends IdentifiableEntity {
	
	NodeModel clone();
	
	/**
	 * ノードのレイアウト情報を取得する。
	 * 
	 * @return ノードのレイアウト情報. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	JmRectangle getBoundary();
	
	/**
	 * ノードの色情報を取得する。
	 * 
	 * @return ノードの色情報. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	JmColor getColor();
	
	/**
	 * TODO for daisuke
	 * 
	 * @return 
	 * @since TODO
	 */
	EntityRef<? extends DatabaseObjectModel> getCoreModelRef();
	
	/**
	 * このモデルを接続元とするコネクションの集合を取得する。
	 * 
	 * <p>返される{@link Collection}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return コネクションの集合
	 * @since 0.2
	 */
	Collection<? extends ConnectionModel> getSourceConnections();
	
	/**
	 * このモデルを接続先とするコネクションの集合を取得する。
	 * 
	 * <p>返される{@link Collection}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return コネクションの集合
	 * @since 0.2
	 */
	Collection<? extends ConnectionModel> getTargetConnections();
	
	EntityRef<? extends NodeModel> toReference();
}
