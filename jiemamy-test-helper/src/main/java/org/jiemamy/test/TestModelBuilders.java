/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/04/04
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
package org.jiemamy.test;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;

import org.jiemamy.JiemamyContext;
import org.jiemamy.SqlFacet;

/**
 * テスト用モデルの構築を行うビルダを供給するプロバイダ。
 * 
 * @author daisuke
 */
public enum TestModelBuilders {
	
	/**  */
	EMP_DEPT(new CoreTestModelBuilder(newJiemamy()), getXml("/sample.jiemamy")),

	/**  */
	ORDER(new CoreTestModelBuilder2(newJiemamy()), getXml("/sample.jiemamy"));
	
	private static String getXml(String path) {
		InputStream in = TestModelBuilders.class.getResourceAsStream(path);
		String xml = null;
		try {
			xml = IOUtils.toString(in, CharEncoding.UTF_8);
		} catch (Exception e) {
//			throw new Error("リソースにアクセスできない", e);
		}
		return xml;
	}
	
	private static JiemamyContext newJiemamy() {
		return new JiemamyContext(SqlFacet.PROVIDER);
	}
	

	private final TestModelBuilder builder;
	
	private final String xml;
	

	TestModelBuilders(TestModelBuilder builder, String xml) {
		this.builder = builder;
		this.xml = xml;
	}
	
	/**
	 * モデルをビルドし、Jiemamyコンテキストを取得する。
	 * 
	 * @return Jiemamyコンテキスト
	 */
	public JiemamyContext getBuiltModel() {
		return builder.build();
	}
	
	/**
	 * モデルをビルドし、Jiemamyコンテキストを取得する。
	 * 
	 * @param dialectClassName SQL方言クラス名
	 * @return Jiemamyコンテキスト
	 */
	public JiemamyContext getBuiltModel(String dialectClassName) {
		return builder.build(dialectClassName);
	}
	
	/**
	 * XML文字列を取得する。
	 * 
	 * @return XML文字列
	 */
	public String getXml() {
		return xml;
	}
	
}
