/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.model.script;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.ServiceLocator;
import org.jiemamy.model.dbo.DatabaseObjectModel;

/**
 * {@link AroundScript}の実装クラス。
 * 
 * @author daisuke
 */
public class DefaultAroundScript implements AroundScript {
	
	private Map<Position, String> scripts = new HashMap<Position, String>();
	
	private Map<Position, String> scriptEngineClassNames = new HashMap<Position, String>();
	
	private static final String DEFAULT_ENGINE = PlainScriptEngine.class.getName();
	

	public String getScript(Position position) {
		return scripts.get(position);
	}
	
	public String getScriptEngineClassName(Position position) {
		String scriptEngineClassName = scriptEngineClassNames.get(position);
		if (scriptEngineClassName == null) {
			return DEFAULT_ENGINE;
		}
		return scriptEngineClassName;
	}
	
	/**
	 * エンジンの{@link Map}を取得する。 
	 * 
	 * @return エンジンの{@link Map}
	 */
	public Map<Position, String> getScriptEngineClassNames() {
		return scriptEngineClassNames;
	}
	
	/**
	 * スクリプトの{@link Map}を取得する。
	 * 
	 * @return スクリプトの{@link Map}
	 */
	public Map<Position, String> getScripts() {
		return scripts;
	}
	
	public String process(ServiceLocator serviceLocator, Position position, DatabaseObjectModel target)
			throws ClassNotFoundException {
		ScriptEngine engine;
		engine = serviceLocator.getService(ScriptEngine.class, getScriptEngineClassName(position));
		return engine.process(target, getScript(position));
	}
	
	public void setScript(Position position, String script) {
		scripts.put(position, script);
	}
	
	public void setScriptEngineClassName(Position position, String scriptEngine) {
		if (DEFAULT_ENGINE.equals(scriptEngine)) {
			scriptEngineClassNames.remove(position);
		} else {
			scriptEngineClassNames.put(position, scriptEngine);
		}
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
