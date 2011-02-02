/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.model.constraint;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.ModelConsistencyException;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.table.TableModel;

/**
 * リレーショナルデータベースにおける「外部キー」を表すモデルインターフェイス。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public interface ForeignKeyConstraintModel extends KeyConstraintModel {
	
	ForeignKeyConstraintModel clone();
	
	/**
	 * {@code databaseObjects}の中から、指定した外部キーが参照するキー制約を取得する。
	 * 
	 * @param databaseObjects 対象{@link DatabaseObjectModel}
	 * @return 指定した外部キーが参照するキー. 該当するキーが存在しなかった場合、{@code null}
	 * @throws ModelConsistencyException 指定した外部キーが参照カラムを持っていない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	KeyConstraintModel findReferencedKeyConstraint(Collection<? extends DatabaseObjectModel> databaseObjects);
	
	/**
	 * TODO for daisuke
	 * 
	 * @param tables
	 * @return
	 */
	TableModel findReferenceTable(Set<TableModel> tables);
	
	/**
	 * マッチ型を取得する。
	 * 
	 * @return マッチ型. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	MatchType getMatchType();
	
	/**
	 * 削除時アクションを取得する。
	 * 
	 * @return 削除時アクション. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	ReferentialAction getOnDelete();
	
	/**
	 * 更新時アクションを取得する。
	 * 
	 * @return 更新時アクション. 未設定の場合は{@code null}
	 * @since 0.2
	 */
	ReferentialAction getOnUpdate();
	
	/**
	 * 参照カラムのリストを取得する。
	 * 
	 * <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return 参照カラムのリスト
	 * @since 0.2
	 */
	List<EntityRef<? extends ColumnModel>> getReferenceColumns();
	
	/**
	 * この外部キーが自己参照外部キーであるかどうかを調べる。
	 * 
	 * @param context コンテキスト
	 * @return 自己参照外部キーである場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	boolean isSelfReference(JiemamyContext context);
	
	EntityRef<? extends ForeignKeyConstraintModel> toReference();
	

	/**
	 * 参照列に挿入された値は、被参照テーブルと被参照列の値に対して、指定した照合型で照会される。
	 * 
	 * <p>照合型には3種類があり、デフォルトは{@link #SIMPLE}照合型。</p>
	 * 
	 * @since 0.2
	 * @author daisuke
	 */
	public enum MatchType {
		
		/** 外部キーの他の部分がNULLでない限り、外部キーの一部をNULLとなることを許可する。 */
		SIMPLE,

		/** 全ての外部キー列がNULLとなる場合を除き、複数列外部キーのある列がNULLとなることを許可しない。 */
		FULL,

		/** まだ実装されていない。 */
		PARTIAL;
	}
	
	/**
	 * 連鎖参照整合性制約の設定。
	 * 
	 * @author daisuke
	 */
	public enum ReferentialAction {
		
		/** 連鎖的に修正する */
		CASCADE,

		/** NULLを設定する */
		SET_NULL,

		/** デフォルト値を設定する */
		SET_DEFAULT,

		/** 削除/変更を許可しない */
		RESTRICT,

		/** 特に何も行わない */
		NO_ACTION;
	}
}
