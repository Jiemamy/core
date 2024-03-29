/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/12/03
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

import java.net.URL;
import java.util.Set;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.OnMemoryEntityResolver;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax.StaxHandler;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * Jiemamyに対する拡張を表すインターフェイス。
 * 
 * <p>一般的に、{@link JiemamyContext}のみを引数にとるpublicなコンストラクタが必要となる。</p>
 * 
 * @version $Id$
 * @author daisuke
 */
public interface JiemamyFacet {
	
	/**
	 * このファセットが管理保持する {@link Entity} の集合を取得する。
	 * 
	 * @return {@link Entity}の集合
	 */
	Set<? extends Entity> getEntities();
	
	/**
	 * このファセットが利用する全ての名前空間を取得する。
	 * 
	 * @return 利用する全ての名前空間
	 */
	JiemamyNamespace[] getNamespaces();
	
	/**
	 * このファセットが管理保持する {@link Entity} のリゾルバを取得する。
	 * 
	 * @return リゾルバ
	 */
	OnMemoryEntityResolver<? extends Entity> getResolver();
	
	/**
	 * このファセットが定義するXMLスキーマのロケーションを返す。
	 * 
	 * @return XMLスキーマのロケーション
	 */
	URL getSchema();
	
	/**
	 * {@link StaxDirector}に対して各種 {@link StaxHandler} を設定する。
	 * 
	 * <p>このメソッドはAPIユーザが呼び出すことを想定していない。</p>
	 * 
	 * @param staxDirector {@link StaxDirector}
	 */
	void prepareStaxHandlers(StaxDirector staxDirector);
}
