/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/15
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

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.ServiceLocator;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public final class ScriptString {
	
	private static final String DEFAULT_ENGINE = PlainScriptEngine.class.getName();
	
	/** スクリプト文字列 */
	private final String script;
	
	private final String scriptEngineClassName;
	
	private ScriptEngine scriptEngine;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param script スクリプト文字列
	 */
	public ScriptString(String script) {
		this(script, DEFAULT_ENGINE);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param script スクリプト文字列
	 * @param scriptEngineClass {@link ScriptEngine}の実装クラス
	 */
	public ScriptString(String script, Class<? extends ScriptEngine> scriptEngineClass) {
		this(script, scriptEngineClass.getName());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param script スクリプト文字列
	 * @param scriptEngineClassName {@link ScriptEngine}の実装クラス名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ScriptString(String script, String scriptEngineClassName) {
		Validate.notNull(script);
		Validate.notNull(scriptEngineClassName);
		this.script = script;
		this.scriptEngineClassName = scriptEngineClassName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ScriptString other = (ScriptString) obj;
		if (script == null) {
			if (other.script != null) {
				return false;
			}
		} else if (!script.equals(other.script)) {
			return false;
		}
		if (scriptEngineClassName == null) {
			if (other.scriptEngineClassName != null) {
				return false;
			}
		} else if (!scriptEngineClassName.equals(other.scriptEngineClassName)) {
			return false;
		}
		return true;
	}
	
	/**
	 * スクリプト文字列を取得する。
	 * 
	 * @return スクリプト文字列
	 */
	public String getScript() {
		return script;
	}
	
	/**
	 * {@link ScriptEngine}の実装クラス名を取得する。
	 * 
	 * @return {@link ScriptEngine}の実装クラス名
	 */
	public String getScriptEngineClassName() {
		return scriptEngineClassName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((script == null) ? 0 : script.hashCode());
		result = prime * result + ((scriptEngineClassName == null) ? 0 : scriptEngineClassName.hashCode());
		return result;
	}
	
	/**
	 * スクリプトを実行して、結果を得る。
	 * 
	 * @param env 環境オブジェクトの{@link Map}
	 * @return 処理結果
	 * @throws ClassNotFoundException {@link #scriptEngineClassName}に対応するエンジンが存在しない場合
	 * @throws ClassCastException {@link #scriptEngineClassName}が {@link ScriptEngine} インターフェイスを持たない場合
	 */
	public String process(Map<String, Object> env) throws ClassNotFoundException {
		if (scriptEngine == null) {
			ServiceLocator serviceLocator = JiemamyContext.getServiceLocator();
			scriptEngine = serviceLocator.getService(ScriptEngine.class, scriptEngineClassName);
		}
		return scriptEngine.process(env, script);
	}
	
	@Override
	public String toString() {
		return script;
	}
}
