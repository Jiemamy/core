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

import java.net.URI;

/**
 * Jiemamyで使用するXML名前空間を表すインターフェイス。
 * 
 * @author daisuke
 */
public interface JiemamyNamespace {
	
	/**
	 * 名前空間URLを取得する。
	 * 
	 * @return 名前空間URI
	 * @since 0.3
	 */
	URI getNamespaceURI();
	
	/**
	 * 名前空間prefixを取得する。
	 * 
	 * @return 名前空間prefix
	 * @since 0.3
	 */
	String getPrefix();
	
	/**
	 * XMLスキーマリソースが存在するURLを取得する。
	 * 
	 * @return XMLスキーマリソースが存在するURL. may be null.
	 * @since 0.3
	 */
	String getXmlSchemaLocation();
}
