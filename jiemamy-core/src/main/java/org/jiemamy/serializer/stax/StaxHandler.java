/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/05
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
package org.jiemamy.serializer.stax;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.serializer.SerializationException;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * 各モデルをシリアライズ/デシリアライズするハンドラの抽象クラス。
 * 
 * @param <T> 処理対象モデルの型 
 * @version $Id$
 * @author daisuke
 */
public abstract class StaxHandler<T> {
	
	private final StaxDirector director;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 子要素のハンドリングを別のハンドラに委譲するためのディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public StaxHandler(StaxDirector director) {
		Validate.notNull(director);
		this.director = director;
	}
	
	/**
	 * デシリアライズする。
	 * 
	 * @param dctx デシリアライズコンテキスト
	 * @return デシリアライズの結果モデル
	 * @throws SerializationException デシリアライズに失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public abstract T handleDeserialization(DeserializationContext dctx) throws SerializationException;
	
	/**
	 * シリアライズする。
	 * 
	 * @param model シリアライズ対象モデル
	 * @param sctx シリアライズコンテキスト
	 * @throws SerializationException デシリアライズに失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public abstract void handleSerialization(T model, SerializationContext sctx) throws SerializationException;
	
	/**
	 * ディレクタを取得する。
	 * 
	 * @return {@link StaxDirector}
	 */
	protected StaxDirector getDirector() {
		return director;
	}
	
	/**
	 * XMLのxsi:schemaLocation属性に設定する名前空間スキーマ定義文字列を生成する。
	 * 
	 * @param namespaces XML名前空間の配列
	 * @return 名前空間スキーマ定義文字列
	 */
	protected String getSchemaLocationDefinition(JiemamyNamespace[] namespaces) {
		StringBuilder sb = new StringBuilder();
		for (JiemamyNamespace namespace : namespaces) {
			String ns = namespace.getNamespaceURI().toString();
			String loc = namespace.getXmlSchemaLocation();
			if (StringUtils.isEmpty(ns) || StringUtils.isEmpty(loc)) {
				continue;
			}
			sb.append(" ").append(ns).append(" ").append(loc);
		}
		// 先頭の余計な空白1つを除去
		return sb.deleteCharAt(0).toString();
	}
}
