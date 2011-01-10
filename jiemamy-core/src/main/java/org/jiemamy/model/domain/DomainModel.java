/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.domain;

import java.util.Collection;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.datatype.TypeVariant;

/**
 * ドメインを表すモデルインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface DomainModel extends DatabaseObjectModel {
	
	/**
	 * ドメインを指す型記述子を取得する。
	 * 
	 * @return ドメインを指す型記述子
	 * @since 0.3
	 */
	TypeVariant asType();
	
	DomainModel clone();
	
	/**
	 * チェック制約を取得する。
	 * 
	 * @return　チェック制約. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	Collection<? extends CheckConstraintModel> getCheckConstraints();
	
	/**
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	TypeVariant getDataType();
	
	/**
	 * NOT　NULL制約を取得する。
	 * 
	 * @return　NOT　NULL制約. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	boolean isNotNull();
	
	EntityRef<? extends DomainModel> toReference();
}