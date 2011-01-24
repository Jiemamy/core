/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/11/10
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
package org.jiemamy.serializer;

/**
 * シリアライズ・デシリアライズに失敗した時にスローされる。
 * 
 * @author daisuke
 */
@SuppressWarnings("serial")
public class SerializationException extends Exception {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @since 0.2
	 */
	public SerializationException() {
		super();
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ
	 * @since 0.2
	 */
	public SerializationException(String message) {
		super(message);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ
	 * @param cause 起因例外
	 * @since 0.2
	 */
	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param cause 起因例外
	 * @since 0.2
	 */
	public SerializationException(Throwable cause) {
		super(cause);
	}
	
}
