/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/06/09
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

/**
 * Jiemamyからスローされる非チェック例外の基底クラス。
 * 
 * <p>Jiemamy自身のコードは、基本的にこのクラス以外を基底クラスに持つ非チェック例外を生成し、スローしてはならない。
 * 必ずこのクラスを拡張した例外を投げる。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
@SuppressWarnings("serial")
public abstract class JiemamyRuntimeException extends RuntimeException {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @since 0.2
	 */
	public JiemamyRuntimeException() {
		super();
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ
	 * @since 0.2
	 */
	public JiemamyRuntimeException(String message) {
		super(message);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ
	 * @param cause 起因例外
	 * @since 0.2
	 */
	public JiemamyRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param cause 起因例外
	 * @since 0.2
	 */
	public JiemamyRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
