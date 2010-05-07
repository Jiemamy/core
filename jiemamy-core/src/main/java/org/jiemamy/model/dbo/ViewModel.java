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

/**
 * リレーショナルデータベースにおける「ビュー」を表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface ViewModel extends DatabaseObjectModel {
	
	/**
	 * VIEW定義SELECT文を取得する。
	 * 
	 * @return VIEW定義SELECT文. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	String getDefinition();
	
}