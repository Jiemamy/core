/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/09/18
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
package org.jiemamy.model.attribute.constraint;

/**
 * チェック制約を表すモデルインターフェイス。
 * 
 * @author daisuke
 */
public interface CheckConstraintModel extends ValueConstraintModel {
	
	/**
	 * CHECK制約定義式を取得する。
	 * 
	 * @return CHECK制約定義式. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getExpression();
	
}