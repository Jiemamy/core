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
package org.jiemamy.model.dbo;

import org.jiemamy.model.attribute.constraint.ColumnCheckConstraintModel;
import org.jiemamy.model.attribute.constraint.NotNullConstraintModel;
import org.jiemamy.model.datatype.DataType;

/**
 * ドメインを表すモデルインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface DomainModel extends DatabaseObjectModel {
	
	/**
	 * チェック制約を取得する。
	 * 
	 * @return　チェック制約. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	ColumnCheckConstraintModel getCheckConstraint();
	
	/**
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	DataType getDataType();
	
	/**
	 * NOT　NULL制約を取得する。
	 * 
	 * @return　NOT　NULL制約. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	NotNullConstraintModel getNotNullConstraint();
}
