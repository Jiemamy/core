/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/01/29
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

import javax.xml.namespace.QName;

/**
 * Jiemamyで使用するXML完全修飾名を表すインターフェイス。
 * 
 * @author daisuke
 */
public interface JiemamyQName {
	
	/**
	 * XMLノード完全修飾名を取得する。
	 * 
	 * @return XMLノード完全修飾名
	 * @since 0.2
	 */
	QName getQName();
	
	/**
	 * 完全修飾名文字列を取得する。
	 * 
	 * <pre>
	 * prefix=empty, localPart="foo" = "foo"
	 * prefix=empty, localPart="bar" = "bar"
	 * prefix="foo", localPart="foo" = "foo:foo"
	 * prefix="bar", localPart="foo" = "bar:foo"
	 * </pre>
	 * 
	 * @return 完全修飾名文字列
	 * @since 0.2
	 */
	String getQNameString();
}
