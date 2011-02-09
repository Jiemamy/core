/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/09/17
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
package org.jiemamy.composer;

/**
 * インポートする際に必要となる設定情報を供給するインターフェイス。
 * 
 * <p>{@link Importer}実装者は、 {@link Importer}の実装と共に、このインターフェイスを拡張し、{@link Importer}の
 * 動作に必要なパラメータを取得するメソッドを定義する。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public interface ImportConfig {
	
}
