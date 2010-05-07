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

import java.util.List;

import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.index.IndexModel;

/**
 * リレーショナルデータベースにおける「テーブル」を表すモデルインターフェイス。
 * 
 * @author daisuke
 */
public interface TableModel extends DatabaseObjectModel {
	
	/**
	 * 属性のリストを取得する。
	 * 
	 * <p>このメソッドは、インスタンスの持つフィールドをそのまま返す。返される{@link List}を直接操作することで、
	 * このオブジェクトのフィールドとして保持される{@link List}を変更することができる。</p>
	 * 
	 * @return 属性のリスト
	 * @since 0.2
	 */
	List<AttributeModel> getAttributes();
	
	/**
	 * インデックスのリストを取得する。
	 * 
	 * <p>このメソッドは、インスタンスの持つフィールドをそのまま返す。返される{@link List}を直接操作することで、
	 * このオブジェクトのフィールドとして保持される{@link List}を変更することができる。</p>
	 * 
	 * @return インデックスのリスト
	 * @since 0.2
	 */
	List<IndexModel> getIndexes();
	
	TableRef getReference();
}
