/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/07/12
 *
 * Jiemamy.
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
package org.jiemamy.composer;

/**
 * {@link Exporter}実行時に問題が発生した場合にスローされる例外。
 * 
 * @author daisuke
 * @since 0.3
 */
@SuppressWarnings("serial")
public class ExportException extends Exception {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param cause 起因例外
	 * @since 0.3
	 */
	public ExportException(Exception cause) {
		super(cause);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ
	 * @since 0.3
	 */
	public ExportException(String message) {
		super(message);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ
	 * @param cause 起因例外
	 * @since 0.3
	 */
	public ExportException(String message, Exception cause) {
		super(message, cause);
	}
	
}
