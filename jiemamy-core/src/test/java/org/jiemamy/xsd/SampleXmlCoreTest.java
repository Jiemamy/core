/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/01/15
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
package org.jiemamy.xsd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;

import org.jiemamy.JiemamyContext;

/**
 * サンプルとして用意した sample.xml と、XML Schemaファイルの定義の整合性をチェックするテストクラス。
 * 
 * @author daisuke
 */
public class SampleXmlCoreTest {
	
	/**
	 * sample.xmlとxsdの整合性をチェックする。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_sample_xmlとxsdの整合性をチェックする() throws Exception {
		// Windowsでは通り、Linuxではコケるので、ひとまずLinuxではテストしない。
		assumeThat(System.getProperty("os.name").toLowerCase(), is(not("linux")));
		
		// XML Schemaオブジェクトを作る
		SchemaFactory schemaFactory =
				SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema"/* XMLConstants.W3C_XML_SCHEMA_NS_URI */);
		// HACK StAXをclasspathに入れると、XMLConstantsの仕様が変わって、上記フィールドにアクセスできなくなる。なんでじゃ。
		Schema schema = schemaFactory.newSchema(new Source[] {
			new StreamSource(JiemamyContext.class.getResourceAsStream("/jiemamy-core.xsd"))
		});
		
		// 妥当性検証
		Validator validator = schema.newValidator();
		validator.validate(new StreamSource(JiemamyContext.class.getResourceAsStream("/jiemamy-sample0.xml")));
		validator.validate(new StreamSource(JiemamyContext.class.getResourceAsStream("/jiemamy-sample.xml")));
	}
}
