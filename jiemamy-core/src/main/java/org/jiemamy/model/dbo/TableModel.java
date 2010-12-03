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

import java.util.Collection;
import java.util.List;

import org.jiemamy.EntityRef;
import org.jiemamy.JiemamyCore;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ConstraintModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;

/**
 * リレーショナルデータベースにおける「テーブル」を表すモデルインターフェイス。
 * 
 * @author daisuke
 */
public interface TableModel extends DatabaseObjectModel {
	
	/**
	 * このテーブルに属する {@link KeyConstraintModel}の中から、指定した {@code foreignKey} が参照するキー制約を取得する。
	 * 
	 * @param foreignKey 対象外部キー
	 * @return この属性が所属するテーブル. どのテーブルにも所属していない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	KeyConstraintModel findReferencedKeyConstraint(ForeignKeyConstraintModel foreignKey);
	
	/**
	 * このテーブルのカラムのうち、{@code name}で示した名前を持つカラムを返す。
	 * 
	 * @param name カラム名
	 * @return カラム
	 * @throws ColumnNotFoundException カラムが見つからなかった場合
	 */
	EntityRef<ColumnModel> getColumn(String name, JiemamyCore core);
	
	/**
	 * このテーブルのカラムの {@link List} を返す。
	 * 
	 * @return このテーブルのカラムの {@link List}
	 */
	List<EntityRef<ColumnModel>> getColumns();
	
	/**
	 * 属性のリストを取得する。
	 * 
	 * @return 属性のリスト
	 * @since 0.2
	 */
	List<ConstraintModel> getConstraints();
	
	/**
	 * このテーブルの外部キー制約の集合を返す。
	 * 
	 * @return このテーブルの外部キー制約の集合
	 */
	Collection<ForeignKeyConstraintModel> getForeignKeyConstraintModels();
	
	/**
	 * このテーブルのキー制約の集合を返す。
	 * 
	 * @return このテーブルのキー制約の集合
	 */
	Collection<KeyConstraintModel> getKeyConstraintModels();
	
	EntityRef<TableModel> getReference();
	
}
