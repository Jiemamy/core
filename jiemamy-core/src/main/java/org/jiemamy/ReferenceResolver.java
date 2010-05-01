package org.jiemamy;
/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/20
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


import org.jiemamy.model.ElementReference;
import org.jiemamy.model.JiemamyElement;

/**
 * 参照オブジェクト({@link ElementReference})から実体({@link JiemamyElement})を解決する、参照リゾルバクラス。
 * 
 * @see ElementReference
 * @since 0.2
 * @author daisuke
 */
public interface ReferenceResolver {
	
	/**
	 * 解決のための情報を記録する。
	 * 
	 * @param model 記録する対象モデル
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	void add(JiemamyElement model);
	
	/**
	 * モデルの参照オブジェクトからモデルの実体を解決できるかどうか調べる。
	 * 
	 * @param reference モデルの参照オブジェクト
	 * @return 解決できる場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.2
	 */
	boolean canResolve(ElementReference<?> reference);
	
	/**
	 * モデルの参照オブジェクトからモデルの実体を解決する。
	 * 
	 * @param <T> モデルの実態の型
	 * @param reference モデルの参照オブジェクト
	 * @return モデルの実体
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws ReferenceResolveException 参照オブジェクトからモデルの実体が解決できなかった場合
	 * @since 0.2
	 */
	<T extends JiemamyElement>T resolve(ElementReference<T> reference);
}
