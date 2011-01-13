/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/03
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
package org.jiemamy.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.xml.namespace.NamespaceContext;

import org.junit.Test;

/**
 * {@link JiemamyNamespaceContext}のテストクラス。
 * 
 * @author daisuke
 */
public class JiemamyNamespaceContextTest {
	
	/**
	 * prefixからURIが、URIからprefixが相互変換できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_prefixからURIが_URIからprefixが相互変換できる() throws Exception {
		NamespaceContext ctx = new JiemamyNamespaceContext(CoreNamespace.values());
		assertThat(ctx.getNamespaceURI(""), is("http://jiemamy.org/xml/ns/core"));
		assertThat(ctx.getNamespaceURI("core"), is("http://jiemamy.org/xml/ns/core"));
		assertThat(ctx.getPrefix("http://jiemamy.org/xml/ns/core"), is(""));
	}
}
