/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/07
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
package org.jiemamy.model;

/**
 * {@link ValueObject}のインスタンスを生成するビルダー。
 * 
 * @version $Id$
 * @author daisuke
 * @param <T> ビルド対象のインスタンスの型
 * @param <S> このビルダークラスの型
 */
public abstract class ValueObjectBuilder<T extends ValueObject, S extends ValueObjectBuilder<T, S>> {
	
	/**
	 * 既に存在する{@link ValueObject}の内容で、このビルダーの状態を変更する。
	 * 
	 * <p>このメソッドを実行することでビルダーの内部状態が変更される。つまり状態が上書きされるため、次のコードのように<code>apply</code>前に行われた操作は意味を成さなくなる。</p>
	 * 
	 * <pre><code>VarioustBuilder<T,S> builder = new VariousBuilder<T,S>();
	 * T vo = new T();
	 * builder.setVarious(foo).appry(vo);</code></pre>
	 * 
	 * @param vo 状態を引用する{@link ValueObject}
	 * @return このビルダーインスタンス。
	 */
	public abstract S apply(T vo);
	
	/**
	 * ビルダの設定に基づいて{@link ValueObject}の新しいインスタンスを生成する。
	 * 
	 * @return {@link ValueObject}の新しいインスタンス
	 */
	public abstract T build();
	
	/**
	 * このビルダークラスのインスタンスを返す。
	 * 
	 * @return このビルダークラスのインスタンス。
	 */
	protected abstract S getThis();
	
}
