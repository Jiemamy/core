/*
 * Copyright 2007-2009 MIYAMOTO Daisuke, jiemamy.org and the Others.
 * Created on 2009/01/02
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
package org.jiemamy.model;

import java.util.UUID;

/**
 * 
 * 全てのJiemamyモデル要素が持つインターフェイス。
 * 
 * <p>このインターフェイスは、モデル要素の定義declarationを表しており、この型の1つのインスタンスは
 * {@link RootModel}を起点とするモデル構造内の複数箇所から参照してはならない。（定義は一度だけ）</p>
 * TODO コメントを直す必要あり（RootModelは廃止なので）。
 * 
 * <p>各モデル要素の参照referenceは、{@link ElementReference}で定義されており、特定のモデル要素を
 * 定義ではなく参照したい場合はこちらの型を使う。</p>
 * 
 * <pre>
 * <b>1:</b> CREATE TABLE FOO (
 * <b>2:</b>   F_ONE INTEGER,
 * <b>3:</b>   F_TWO VARCHAR,
 * <b>4:</b>   PRIMARY KEY (F_ONE),
 * <b>5:</b>   FOREIGN KEY (F_TWO) REFERENCES BAR (B_ONE)
 * <b>6:</b> );
 * </pre>
 * 
 * 上記のSQLは以下のような構造となっている。
 * <ol>
 *   <li>FOO:Tableの定義</li>
 *   <li>F_ONE:Columnの定義, INTEGER型への参照</li>
 *   <li>F_TWO:Columnの定義, VARCHAR型への参照</li>
 *   <li>PrimaryKeyの定義, F_ONE:Columnへの参照</li>
 *   <li>ForeignKeyの定義, F_TWO:Columnへの参照, BAR:Tableへの参照, B_ONE:Columnへの参照</li>
 * </ol>
 * 
 * @see ElementReference
 * @since 0.2
 * @author daisuke
 */
public interface JiemamyElement {
	
	/**
	 * モデルを取得する。
	 * 
	 * <p>IDは、オブジェクトの生成時に指定または自動生成され、このオブジェクトのライフサイクル（生成から削除まで）を通して
	 * 一貫していなければならない。</p>
	 * 
	 * @return モデルID
	 * @since 0.2
	 */
	UUID getId();
}
