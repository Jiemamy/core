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
	 * インスタンスを生成する。
	 */
	public ValueObjectBuilder() {
		// noop
	}
	
	/**
	 * ビルド対象の{@link ValueObject}からこのビルダーを生成するコピーコンストラクタ。
	 * 
	 * @param base コピー元の{@link ValueObject}
	 */
	public ValueObjectBuilder(T base) {
		// noop
	}
	
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
