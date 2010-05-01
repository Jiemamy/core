/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/04/15
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

import org.jiemamy.model.ElementReference;

/**
 * 参照オブジェクトからモデルの実体が解決できなかった場合。
 * 
 * @since 0.2
 * @author daisuke
 */
@SuppressWarnings("serial")
public class ReferenceResolveException extends RuntimeException {
	
	/** 解決できなかった参照オブジェクト */
	private final ElementReference<?> ref;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param ref 解決できなかった参照オブジェクト
	 */
	public ReferenceResolveException(ElementReference<?> ref) {
		this.ref = ref;
	}
	
	/**
	 * 解決できなかった参照オブジェクトを取得する。
	 * 
	 * @return 解決できなかった参照オブジェクト
	 * @since 0.2
	 */
	public ElementReference<?> getId() {
		return ref;
	}
}
