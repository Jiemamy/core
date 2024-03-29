/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2008/06/19
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
package org.jiemamy.composer;

import java.util.Map;

import org.jiemamy.JiemamyContext;

/**
 * 外部リソースからJiemamyモデルに入力を行うインターフェイス。
 * 
 * @param <T> インポート作業に必要な設定情報を保持する型
 * @since 0.3
 * @author daisuke
 */
public interface Importer<T extends ImportConfig> {
	
	/**
	 * Importerの名称を取得する。
	 * 
	 * @return Importerの名称
	 * @since 0.3
	 */
	String getName();
	
	/**
	 * 外部リソースからモデルにデータをインポートする。
	 * 
	 * <p>通常は {@link #importModel(JiemamyContext, ImportConfig)}を利用すべきだが、
	 * {@link Importer}の実装が不明な抽象度でimport処理を起動するために設けたメソッドである。</p>
	 * 
	 * @param context インポート対象コンテキスト
	 * @param configMap インポートのコンテキスト情報
	 * @return インポートが正常に完了した場合は{@code true}、設定等により入力が行われなかった場合は{@code false}
	 * @throws ImportException インポートに失敗した時
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 必須config情報が{@code null}の場合
	 * @since 0.3
	 */
	boolean importModel(JiemamyContext context, Map<String, Object> configMap) throws ImportException;
	
	/**
	 * 外部リソースからモデルにデータをインポートする。
	 * 
	 * @param context インポート対象コンテキスト
	 * @param config インポートのコンテキスト情報
	 * @return インポートが正常に完了した場合は{@code true}、設定等により入力が行われなかった場合は{@code false}
	 * @throws ImportException インポートに失敗した時
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 必須config情報が{@code null}の場合
	 * @since 0.3
	 */
	boolean importModel(JiemamyContext context, T config) throws ImportException;
	
}
