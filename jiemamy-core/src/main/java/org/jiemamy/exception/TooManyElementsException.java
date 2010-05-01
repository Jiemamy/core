/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/06/22
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
package org.jiemamy.exception;

import java.util.Collection;
import java.util.List;

/**
 * 問い合わせの結果、同名の要素が複数存在する時スローされる例外。
 * 
 * @since 0.2
 * @author daisuke
 */
@SuppressWarnings("serial")
public class TooManyElementsException extends JiemamyRuntimeException {
	
	/** 見つかった同名要素のリスト */
	private Collection<?> elements;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param <T> 条件とした型
	 * @param parent 検索対象親モデル
	 * @param clazz 条件クラス
	 * @param elements 見つかった同条件要素のリスト
	 * @since 0.2
	 */
	public <T> TooManyElementsException(Object parent, Class<T> clazz, List<T> elements) {
		super(clazz.getName() + " exists two or more in " + parent + ".");
		this.elements = elements;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param parent 検索対象親モデル
	 * @param name 名前
	 * @param elements 見つかった同名要素のリスト
	 * @since 0.2
	 */
	public TooManyElementsException(Object parent, String name, Collection<?> elements) {
		super(name + " exists two or more in " + parent + ".");
		this.elements = elements;
	}
	
	/**
	 * 見つかった同名要素のリストを取得する。
	 * 
	 * @return 見つかった同名要素のリスト
	 * @since 0.2
	 */
	public Collection<?> getElements() {
		return elements;
	}
	
}
