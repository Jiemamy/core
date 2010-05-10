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

import org.jiemamy.model.CompositEntity;
import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.index.IndexModel;

/**
 * リレーショナルデータベースにおける「テーブル」を表すモデルインターフェイス。
 * 
 * @author daisuke
 */
public interface TableModel extends DatabaseObjectModel, CompositEntity {
	
	public void addIndex(IndexModel index);
	
	public void removeIndex(IndexModel index);
	
	/**
	 * テーブルに属性を追加する。
	 * 
	 * @param attribute 属性
	 */
	void addAttribute(AttributeModel attribute);
	
	/**
	 * テーブルにカラムを追加する。
	 * 
	 * @param column カラム
	 * @throws EntityLifecycleException {@code column}のライフサイクルがaliveの場合
	 */
	void addColumn(ColumnModel column);
	
	/**
	 * 属性のリストを取得する。
	 * 
	 * @return 属性のリスト
	 * @since 0.2
	 */
	List<AttributeModel> getAttributes();
	
	/**
	 * このテーブルのカラムのうち、{@code name}で示した名前を持つカラムを返す。
	 * 
	 * @param name カラム名
	 * @return カラム
	 * @throws ColumnNotFoundException カラムが見つからなかった場合
	 */
	ColumnModel getColumn(String name);
	
	/**
	 * このテーブルのカラムの {@link List} を返す。
	 * 
	 * @return このテーブルのカラムの {@link List}
	 */
	List<ColumnModel> getColumns();
	
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
	
	EntityRef<TableModel> getReference();
	
	/**
	 * テーブルから属性を削除する。
	 * 
	 * @param attribute 属性
	 */
	void removeAttribute(AttributeModel attribute);
	
	/**
	 * テーブルからカラムを削除する。
	 * 
	 * @param column カラム
	 * @throws EntityLifecycleException {@code column}のライフサイクルがaliveではない場合
	 */
	void removeColumn(ColumnModel column);
}
