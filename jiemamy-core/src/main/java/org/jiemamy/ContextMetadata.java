/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/25
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

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public interface ContextMetadata extends Cloneable {
	
	ContextMetadata clone();
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文
	 */
	String getDescription();
	
	/**
	 * SQL方言IDを取得する。
	 * 
	 * @return SQL方言ID
	 */
	String getDialectClassName();
	
	/**
	 * スキーマ名を取得する。
	 * 
	 * @return スキーマ名
	 */
	String getSchemaName();
	
}