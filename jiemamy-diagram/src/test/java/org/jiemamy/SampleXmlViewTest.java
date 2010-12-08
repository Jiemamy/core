/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;

/**
 * サンプルとして用意した sample.xml と、XML Schemaファイルの定義の整合性をチェックするテストクラス。
 * 
 * @author daisuke
 */
public class SampleXmlViewTest {
	
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
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new Source[] {
			new StreamSource(SampleXmlViewTest.class.getResourceAsStream("/jiemamy-core.xsd")),
			new StreamSource(SampleXmlViewTest.class.getResourceAsStream("/jiemamy-view.xsd")),
		});
		
		// 妥当性検証
		Validator validator = schema.newValidator();
		validator.validate(new StreamSource(SampleXmlViewTest.class.getResourceAsStream("/sample.xml")));
	}
}
