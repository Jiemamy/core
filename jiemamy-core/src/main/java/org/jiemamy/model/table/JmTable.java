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
import java.util.UUID;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.EntityResolver;
import org.jiemamy.dddbase.HierarchicalEntity;
import org.jiemamy.dddbase.UUIDEntity;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.constraint.JmConstraint;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmKeyConstraint;
import org.jiemamy.model.constraint.JmNotNullConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.parameter.ParameterMap;

/**
 * リレーショナルデータベースにおける「テーブル」を表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @author daisuke
 */
public interface JmTable extends DbObject, HierarchicalEntity<UUID>, EntityResolver<UUID> {
	
	JmTable clone();
	
	/**
	 * このテーブルに属する{@link JmKeyConstraint}の中から、指定した{@code foreignKey}が参照するキー制約を取得する。
	 * 
	 * @param foreignKey 対象外部キー
	 * @return 指定した{@code foreignKey}が参照するキー制約、見つからない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	JmKeyConstraint findReferencedKeyConstraint(JmForeignKeyConstraint foreignKey);
	
	/**
	 * このテーブルのカラムのうち、{@code name}で示した名前を持つカラムを返す。
	 * 
	 * @param name カラム名
	 * @return カラム
	 * @throws ColumnNotFoundException カラムが見つからなかった場合
	 */
	JmColumn getColumn(String name);
	
	/**
	 * このテーブルのカラムのうち、{@code reference}で示したカラムを返す。
	 * 
	 * @param reference カラム参照
	 * @return カラム
	 * @throws ColumnNotFoundException カラムが見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	JmColumn getColumn(UUIDEntityRef<? extends JmColumn> reference);
	
	/**
	 * このテーブルが持つカラムの {@link List} を返す。
	 * 
	 * @return このテーブルが持つカラムの {@link List}
	 */
	List<JmColumn> getColumns();
	
	/**
	 * このテーブルが持つカラムのうち、指定した型を持つカラムのリストを返す。
	 * 
	 * @param <T> 型
	 * @param clazz 型
	 * @return 指定した型を持つカラムのリスト
	 */
	<T extends JmColumn>List<T> getColumns(Class<T> clazz);
	
	/**
	 * このテーブルが持つ制約の集合を取得する。
	 * 
	 * @return このテーブルが持つ制約の集合
	 * @since 0.3
	 */
	Set<? extends JmConstraint> getConstraints();
	
	/**
	 * このテーブルが持つ制約のうち、指定した型を持つものの集合を取得する。
	 * 
	 * @param <T> 制約の型
	 * @param clazz 制約の型
	 * @return 制約の集合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	<T extends JmConstraint>Collection<T> getConstraints(Class<T> clazz);
	
	/**
	 * このテーブルの外部キー制約の集合を返す。
	 * 
	 * @return このテーブルの外部キー制約の集合
	 */
	Collection<? extends JmForeignKeyConstraint> getForeignKeyConstraints();
	
	/**
	 * このテーブルのキー制約の集合を返す。
	 * 
	 * @return このテーブルのキー制約の集合
	 */
	Collection<? extends JmKeyConstraint> getKeyConstraints();
	
	/**
	 * 指定したカラム参照に対する NOT NULL 制約を取得する。
	 * 
	 * @param reference カラム参照
	 * @return NOT NULL制約。無い場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	JmNotNullConstraint getNotNullConstraintFor(UUIDEntityRef<? extends JmColumn> reference);
	
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
	JmPrimaryKeyConstraint getPrimaryKey();
	
	/**
	 * 指定したカラムがこのテーブルにおいて NOT NULL 制約を受けているかどうか調べる。
	 * 
	 * <p>但し、指定したカラムがこのテーブルのカラムでない場合は常に {@code false} を返すので注意すること。</p>
	 * 
	 * @param reference カラム参照
	 * @return 制約を受けている場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isNotNullColumn(UUIDEntityRef<? extends JmColumn> reference);
	
	/**
	 * 指定したカラムがこのテーブルの主キーカラムを構成しているかどうか調べる。
	 * 
	 * <p>但し、指定したカラムがこのテーブルのカラムでない場合は常に {@code false} を返すので注意すること。</p>
	 * 
	 * @param reference カラム参照
	 * @return このテーブルの主キーカラムを構成している場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isPrimaryKeyColumn(UUIDEntityRef<? extends JmColumn> reference);
	
	<E extends Entity<UUID>>E resolve(EntityRef<E, UUID> ref);
	
	UUIDEntity resolve(UUID id);
	
	UUIDEntityRef<? extends JmTable> toReference();
	
}
