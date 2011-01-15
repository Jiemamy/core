/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.model.constraint;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;

/**
 * 制約を表すモデルのインターフェイス。
 * 
 * @author daisuke
 */
public interface ConstraintModel extends Entity {
	
	ConstraintModel clone();
	
	/**
	 * 遅延評価可能性モデルを取得する。
	 * 
	 * @return 遅延評価可能性モデル. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	DeferrabilityModel getDeferrability();
	
	/**
	 * 説明を取得する。
	 * 
	 * @return 説明。未設定の場合は{@code null}
	 */
	String getDescription();
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名。未設定の場合は{@code null}
	 */
	String getLogicalName();
	
	/**
	 * 物理名を取得する。
	 * 
	 * @return 物理名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getName();
	
	EntityRef<? extends ConstraintModel> toReference();
	
}
