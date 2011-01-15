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
	
	private final String script;
	
	private final String scriptEngineClassName;
	
	private ScriptEngine scriptEngine;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param script
	 */
	public ScriptString(String script) {
		this(script, DEFAULT_ENGINE);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param script
	 * @param scriptEngineClassName
	 */
	public ScriptString(String script, Class<? extends ScriptEngine> scriptEngineClass) {
		this(script, scriptEngineClass.getName());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param script
	 * @param scriptEngineClassName
	 */
	public ScriptString(String script, String scriptEngineClassName) {
		Validate.notNull(script);
		Validate.notNull(scriptEngineClassName);
		this.script = script;
		this.scriptEngineClassName = scriptEngineClassName;
	}
	
	public String getScript() {
		return script;
	}
	
	public String getScriptEngineClassName() {
		return scriptEngineClassName;
	}
	
	public String process(JiemamyContext context, Map<String, Object> env) throws ClassNotFoundException {
		setContext(context);
		return process(env);
	}
	
	public String process(Map<String, Object> env) {
		if (scriptEngine == null) {
			throw new IllegalStateException();
		}
		return scriptEngine.process(env, script);
	}
	
	public void setContext(JiemamyContext context) throws ClassNotFoundException {
		ServiceLocator serviceLocator = context.getServiceLocator();
		scriptEngine = serviceLocator.getService(ScriptEngine.class, scriptEngineClassName);
	}
	
}
