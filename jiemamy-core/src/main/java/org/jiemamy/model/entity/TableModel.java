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
package org.jiemamy.model.entity;

import java.util.List;

import org.jiemamy.exception.ElementNotFoundException;
import org.jiemamy.exception.TooManyElementsException;
import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.ForeignKey;
import org.jiemamy.model.attribute.constraint.PrimaryKey;
import org.jiemamy.model.index.IndexModel;

/**
 * リレーショナルデータベースにおける「テーブル」を表すモデルインターフェイス。
 * 
 * @author daisuke
 */
public interface TableModel extends EntityModel {
	
	/**
	 * 指定した型を持つ属性を取得する。
	 * 
	 * @param <T> 属性の型
	 * @param clazz 属性の型
	 * @return 属性
	 * @throws TooManyElementsException 複数の属性が見つかった場合
	 * @throws ElementNotFoundException 属性が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	<T extends AttributeModel>T findAttribute(Class<T> clazz);
	
	/**
	 * 指定した型を持つ属性を取得する。
	 * 
	 * @param <T> 属性の型
	 * @param clazz 属性の型
	 * @param columnAttr カラム属性も検索対象とする場合は{@code true}、そうでない場合は{@code false}
	 * @return 属性
	 * @throws TooManyElementsException 複数の属性が見つかった場合
	 * @throws ElementNotFoundException 属性が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	<T extends AttributeModel>T findAttribute(Class<T> clazz, boolean columnAttr);
	
	/**
	 * 指定した型を持つ属性のリストを取得する。
	 * 
	 * <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @param <T> 属性の型
	 * @param clazz 検索する型
	 * @return 属性のリスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	<T extends AttributeModel>List<T> findAttributes(Class<T> clazz);
	
	/**
	 * 指定した型を持つ属性のリストを取得する。
	 * 
	 * <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @param <T> 属性の型
	 * @param clazz 検索する型
	 * @param columnAttr カラム属性も検索対象とする場合は{@code true}、そうでない場合は{@code false}
	 * @return 属性のリスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	<T extends AttributeModel>List<T> findAttributes(Class<T> clazz, boolean columnAttr);
	
	/**
	 * 指定した名前を持つカラムを取得する。
	 * 
	 * @param columnName カラム名
	 * @return 見つかったカラム
	 * @throws TooManyElementsException 同名のカラムが複数見つかった場合
	 * @throws ElementNotFoundException カラムが見つからなかった場合 
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	ColumnModel findColumn(String columnName);
	
	/**
	 * カラムのリストを取得する。
	 * 
	 * <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return カラムのリスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	List<ColumnModel> findColumns();
	
	/**
	 * 外部キー制約のリストを取得する。
	 * 
	 * <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return 外部キー制約のリスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	List<ForeignKey> findForeignKeys();
	
	/**
	 * このテーブルの主キーを取得する。
	 * 
	 * <p>テーブルに対して設定された主キーだけではなく、カラムに対して設定された主キーも検索対象となる。</p>
	 * 
	 * @return 主キー
	 * @throws TooManyElementsException 複数の主キーが見つかった場合
	 * @throws ElementNotFoundException 主キーが存在しない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws UnsupportedOperationException 実装がこのメソッドをサポートしない場合
	 */
	PrimaryKey findPrimaryKey();
	
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
}
