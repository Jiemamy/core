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

import java.util.Collection;

import org.jiemamy.IdentifiableEntity;
import org.jiemamy.EntityRef;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public interface DiagramModel extends IdentifiableEntity {
	
	DiagramModel clone();
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 * @since TODO
	 */
	Collection<? extends ConnectionModel> getConnections();
	
	/**
	 * 表示レベルを取得する。
	 * 
	 * @return 表示レベル. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	Level getLevel();
	
	/**
	 * 物理/論理モードを取得する。
	 * 
	 * @return 物理/論理モード. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	Mode getMode();
	
	String getName();
	
	Collection<? extends NodeModel> getNodes();
	
	EntityRef<? extends DiagramModel> toReference();
}
