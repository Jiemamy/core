/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/09/14
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
package org.jiemamy.script;

import java.util.Map;

/**
 * スクリプトの処理エンジンインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface ScriptEngine {
	
	/**
	 * 周辺スクリプトを実行し、結果を得る。
	 * 
	 * @param env スクリプト実行環境
	 * @param script 評価するスクリプト
	 * @return スクリプト評価結果
	 * @throws IllegalArgumentException 引数{@code script}に{@code null}を与えた場合
	 * @since 0.3
	 */
	String process(Map<String, Object> env, String script);
	
}
