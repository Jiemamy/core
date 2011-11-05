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
package org.jiemamy.model.dataset;

import java.util.Map;
import java.util.Map.Entry;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.dddbase.ValueObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.script.ScriptString;

/**
 * DB上のデータの各レコード（Row）を表すモデルインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface JmRecord extends ValueObject {
	
	/**
	 * カラムに対応するデータを取得する。
	 * 
	 * <p>返される{@link Map}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * <p>データは「存在する場合」と「NULLである場合」と「出力されない場合」があり得る。
	 * 「出力されない場合」とは、例えば "FOO" テーブルに BAR, BAZ, QUX カラムが存在する場合に、
	 * {@code INSERT INTO FOO (BAZ, QUX) VALUES('QUUX', 'COURGE');}
	 * というINSERT文が発行される場合である。</p>
	 * 
	 * <p>{@link Map} は、この3種類のステートを保持可能である。{@code null}が設定されたkeyも未設定のkeyも、
	 * どちらも{@code null}を返すが、{@link Map#containsKey(Object)}メソッドで、両者を判別可能である。
	 * 詳しくは{@link Map#get(Object)}のjavadocを参照のこと。</p>
	 * 
	 * <table border>
	 *   <tr>
	 *     <th>containsKey(columnRef)</th>
	 *     <th>get(columnRef)</th>
	 *     <th>INSERT文</th>
	 *   </tr>
	 *   <tr>
	 *     <td>true</td>
	 *     <td>not null</td>
	 *     <td>このカラムのデータはget(columnRef)の結果となる</td>
	 *   </tr>
	 *   <tr>
	 *     <td>true</td>
	 *     <td>null</td>
	 *     <td>このカラムのデータはNULLとなる</td>
	 *   </tr>
	 *   <tr>
	 *     <td>false</td>
	 *     <td>null</td>
	 *     <td>このカラムのデータは出力されない</td>
	 *   </tr>
	 * </table>
	 * 
	 * @return カラムに対応するデータ
	 * @since 0.3
	 */
	Map<UUIDEntityRef<? extends JmColumn>, ScriptString> getValues();
	
	/**
	 * 1レコード分の各カラムとそのカラムに対する値のセットを、テーブルに定義したカラム順に取得するための {@link Iterable} インスタンスを取得する。
	 * 
	 * @param context コンテキスト
	 * @param tableRef テーブル参照
	 * @return {@link Iterable}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	Iterable<Entry<UUIDEntityRef<? extends JmColumn>, ScriptString>> toIterable(JiemamyContext context,
			UUIDEntityRef<? extends JmTable> tableRef);
}
