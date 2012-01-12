/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
package org.jiemamy.serializer;

import java.io.InputStream;
import java.io.OutputStream;

import org.jiemamy.FacetProvider;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyFacet;

/**
 * シリアライザインターフェイス。
 * 
 * <p>シリアライザの実装クラスはステートレスでなければならない。</p>
 * 
 * @author daisuke
 */
public interface JiemamySerializer {
	
	/** インデント整形を行うためのXSLT */
	String XSLT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" //
			+ "<xsl:stylesheet version=\"1.0\""
			+ " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\""
			+ " xmlns:xalan=\"http://xml.apache.org/xslt\">"
			+ "<xsl:output method=\"xml\" encoding=\"UTF-8\" indent=\"yes\" xalan:indent-amount=\"2\"/>"
			+ "<xsl:template match=\"/\">" //
			+ "<xsl:copy-of select=\".\"/>" //
			+ "</xsl:template>" //
			+ "</xsl:stylesheet>";
	
	/** ファイル拡張子 */
	String FILE_EXT = "jiemamy";
	
	
	/**
	 * {@link InputStream} から {@link JiemamyContext} にデシリアライズする。
	 * 
	 * @param in デシリアライズするInputStream
	 * @param facetProviders デシリアライズした{@link JiemamyContext}がサポートすべき {@link JiemamyFacet} のプロバイダ
	 * @return デシリアライズした {@link JiemamyContext}
	 * @throws SerializationException デシリアライズに失敗した時
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	JiemamyContext deserialize(InputStream in, FacetProvider... facetProviders) throws SerializationException;
	
	/**
	 * {@link JiemamyContext} を {@link OutputStream} にシリアライズする。
	 * 
	 * @param context シリアライズする {@link JiemamyContext}
	 * @param out シリアライズ結果を出力するstream
	 * @throws SerializationException シリアライズに失敗した時
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	void serialize(JiemamyContext context, OutputStream out) throws SerializationException;
	
}
