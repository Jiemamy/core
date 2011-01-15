/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.model.table;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.KeyConstraintModel;
import org.jiemamy.model.constraint.NotNullConstraintModel;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * リレーショナルデータベースにおける「テーブル」を表すモデルインターフェイス。
 * 
 * @author daisuke
 */
public interface TableModel extends DatabaseObjectModel {
	
	TableModel clone();
	
	/**
	 * このテーブルに属する {@link KeyConstraintModel}の中から、指定した {@code foreignKey} が参照するキー制約を取得する。
	 * 
	 * @param foreignKey 対象外部キー
	 * @return この属性が所属するテーブル. どのテーブルにも所属していない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	KeyConstraintModel findReferencedKeyConstraint(ForeignKeyConstraintModel foreignKey);
	
	/**
	 * このテーブルのカラムのうち、{@code reference}で示したカラムを返す。
	 * 
	 * @param reference カラム参照
	 * @return カラム
	 * @throws ColumnNotFoundException カラムが見つからなかった場合
	 */
	ColumnModel getColumn(EntityRef<? extends ColumnModel> reference);
	
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
	 * 属性のリストを取得する。
	 * 
	 * @return 属性のリスト
	 * @since 0.2
	 */
	Set<? extends ConstraintModel> getConstraints();
	
	/**
	 * 属性のリストを取得する。
	 * 
	 * @param <T> 属性の型
	 * @param clazz 属性の型
	 * @return 属性のリスト
	 * @since 0.2
	 */
	<T extends ConstraintModel>Collection<T> getConstraints(Class<T> clazz);
	
	/**
	 * このテーブルの外部キー制約の集合を返す。
	 * 
	 * @return このテーブルの外部キー制約の集合
	 */
	Collection<? extends ForeignKeyConstraintModel> getForeignKeyConstraintModels();
	
	/**
	 * このテーブルのキー制約の集合を返す。
	 * 
	 * @return このテーブルのキー制約の集合
	 */
	Collection<? extends KeyConstraintModel> getKeyConstraintModels();
	
	/**
	 * 指定したカラム参照に対する NOT NULL 制約を取得する。
	 * 
	 * @param reference カラム参照
	 * @return NOT NULL制約。無い場合は{@code null}
	 */
	NotNullConstraintModel getNotNullConstraintFor(EntityRef<? extends ColumnModel> reference);
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	<T>T getParam(TableParameterKey<T> key);
	
	/**
	 * このモデルが持つ全パラメータを取得する。
	 * 
	 * @return カラムが持つ全パラメータ
	 */
	ParameterMap getParams();
	
	/**
	 * このテーブルの主キー制約を取得する。
	 * 
	 * @return 主キー制約。無い場合は {@code null}
	 */
	PrimaryKeyConstraintModel getPrimaryKey();
	
	/**
	 * 指定したカラムがこのテーブルにおいて NOT NULL 制約を受けているかどうか調べる。
	 * 
	 * <p>但し、指定したカラムがこのテーブルのカラムでない場合は常に {@code false} を返すので注意すること。</p>
	 * 
	 * @param ref カラム参照
	 * @return 制約を受けている場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isNotNullColumn(EntityRef<? extends ColumnModel> ref);
	
	/**
	 * 指定したカラムがこのテーブルの主キーカラムを構成しているかどうか調べる。
	 * 
	 * <p>但し、指定したカラムがこのテーブルのカラムでない場合は常に {@code false} を返すので注意すること。</p>
	 * 
	 * @param ref カラム参照
	 * @return このテーブルの主キーカラムを構成している場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isPrimaryKeyColumn(EntityRef<? extends ColumnModel> ref);
	
	EntityRef<? extends TableModel> toReference();
	
}
