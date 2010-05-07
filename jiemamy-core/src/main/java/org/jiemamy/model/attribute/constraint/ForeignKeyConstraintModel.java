/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

import java.util.List;

import org.jiemamy.model.EntityRef;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * リレーショナルデータベースにおける「外部キー」を表すモデルインターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface ForeignKeyConstraintModel extends KeyConstraintModel {
	
	/**
	 * 参照列に挿入された値は、被参照テーブルと被参照列の値に対して、指定した照合型で照会される。
	 * 
	 * <p>照合型には3種類があり、デフォルトはSIMPLE照合型。</p>
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
	 * <p>このメソッドは、インスタンスの持つフィールドをそのまま返す。返される{@link List}を直接操作することで、
	 * このオブジェクトのフィールドとして保持される{@link List}を変更することができる。</p>
	 * 
	 * @return 参照カラムのリスト
	 * @since 0.2
	 */
	List<EntityRef<ColumnModel>> getReferenceColumns();
}
