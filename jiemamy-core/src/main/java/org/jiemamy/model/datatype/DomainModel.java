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
package org.jiemamy.model.datatype;

import org.jiemamy.model.JiemamyElement;
import org.jiemamy.model.attribute.constraint.ColumnCheckConstraint;
import org.jiemamy.model.attribute.constraint.NotNullConstraint;

/**
 * ドメインを表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface DomainModel extends JiemamyElement, DataTypeMold<DomainRef>, DataTypeHolder<BuiltinDataType> {
	
	/**
	 * チェック制約を取得する。
	 * 
	 * @return　チェック制約. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	ColumnCheckConstraint getCheckConstraint();
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getDescription();
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getLogicalName();
	
	/**
	 * ドメイン名を取得する。
	 * 
	 * @return ドメイン名. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getName();
	
	/**
	 * NOT　NULL制約を取得する。
	 * 
	 * @return　NOT　NULL制約. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	NotNullConstraint getNotNullConstraint();
}
