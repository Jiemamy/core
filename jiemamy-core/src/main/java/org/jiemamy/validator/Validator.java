/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/21
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
package org.jiemamy.validator;

import java.util.Collection;

import org.jiemamy.JiemamyContext;

/**
 * モデルの妥当性を検査するバリデータのインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface Validator {
	
	/**
	 * モデルのバリデーションを行う。
	 * 
	 * @param context バリデーション対象コンテキスト
	 * @return バリデーションの結果、問題がなかった場合は、空の{@link Collection}を返す。
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	Collection<? extends Problem> validate(JiemamyContext context);
	
}
