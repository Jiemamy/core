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
 * Jiemamyモデルから外部リソースに出力を行うインターフェイス。
 * 
 * @param <T> エクスポート作業に必要な設定情報を提供する型
 * @since 0.3
 * @author daisuke
 */
public interface Exporter<T extends ExportConfig> {
	
	/**
	 * モデルを外部リソースにエクスポートする。
	 * 
	 * <p>ファイル出力であれば、ファイルに保存する所までの責務を負う。</p>
	 * 
	 * <p>通常は {@link #exportModel(JiemamyContext, ExportConfig)}を利用すべきだが、
	 * {@link Exporter}の実装が不明な抽象度でexport処理を起動するために設けたメソッドである。</p>
	 * 
	 * @param context エクスポート対象コンテキスト
	 * @param configMap エクスポートの設定情報
	 * @return エクスポートが正常に完了した場合は{@code true}、設定等により出力が行われなかった場合は{@code false}
	 * @throws ExportException エクスポートに失敗した時
	 * @throws ClassCastException {@code configMap}の値が想定外である場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 必須config情報に対して{@code null}を返した場合
	 * @since 0.3
	 */
	boolean exportModel(JiemamyContext context, Map<String, Object> configMap) throws ExportException;
	
	/**
	 * モデルを外部リソースにエクスポートする。
	 * 
	 * <p>ファイル出力であれば、ファイルに保存する所までの責務を負う。</p>
	 * 
	 * @param context エクスポート対象コンテキスト
	 * @param config エクスポートの設定情報
	 * @return エクスポートが正常に完了した場合は{@code true}、設定等により出力が行われなかった場合は{@code false}
	 * @throws ExportException エクスポートに失敗した時
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException configが満たすべき条件を満たさなかった場合
	 * @since 0.3
	 */
	boolean exportModel(JiemamyContext context, T config) throws ExportException;
	
	/**
	 * Exporterの名称を取得する。
	 * 
	 * @return Exporterの名称
	 * @since 0.3
	 */
	String getName();
	
}
