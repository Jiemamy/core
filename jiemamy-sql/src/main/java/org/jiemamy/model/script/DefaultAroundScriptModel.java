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
import java.util.UUID;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.JiemamyContext;
import org.jiemamy.ServiceLocator;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.AbstractJiemamyEntity;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.utils.MutationMonitor;

/**
 * {@link AroundScriptModel}の実装クラス。
 * 
 * @author daisuke
 */
public class DefaultAroundScriptModel extends AbstractJiemamyEntity implements AroundScriptModel {
	
	private Map<Position, String> scripts = new HashMap<Position, String>();
	
	private Map<Position, String> scriptEngineClassNames = new HashMap<Position, String>();
	
	private static final String DEFAULT_ENGINE = PlainScriptEngine.class.getName();
	
	private EntityRef<? extends DatabaseObjectModel> target;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultAroundScriptModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultAroundScriptModel clone() {
		DefaultAroundScriptModel clone = (DefaultAroundScriptModel) super.clone();
		clone.scripts = Maps.newHashMap(scripts);
		clone.scriptEngineClassNames = Maps.newHashMap(scriptEngineClassNames);
		return clone;
	}
	
	public EntityRef<? extends DatabaseObjectModel> getCoreModelRef() {
		return target;
	}
	
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
		return MutationMonitor.monitor(Maps.newHashMap(scriptEngineClassNames));
	}
	
	/**
	 * スクリプトの{@link Map}を取得する。
	 * 
	 * @return スクリプトの{@link Map}
	 */
	public Map<Position, String> getScripts() {
		return MutationMonitor.monitor(Maps.newHashMap(scripts));
	}
	
	public String process(JiemamyContext context, Position position, DatabaseObjectModel target)
			throws ClassNotFoundException {
		Validate.notNull(context);
		Validate.notNull(position);
		Validate.notNull(target);
		return process(context.getServiceLocator(), position, target);
	}
	
	public String process(ServiceLocator serviceLocator, Position position, DatabaseObjectModel target)
			throws ClassNotFoundException {
		Validate.notNull(serviceLocator);
		Validate.notNull(position);
		Validate.notNull(target);
		ScriptEngine engine = serviceLocator.getService(ScriptEngine.class, getScriptEngineClassName(position));
		return engine.process(target, getScript(position));
	}
	
	/**
	 * スクリプトを設定する。
	 * 
	 * @param position スクリプト挿入位置
	 * @param script スクリプト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	public void setScript(Position position, String script) {
		Validate.notNull(position);
		Validate.notNull(script);
		scripts.put(position, script);
	}
	
	public void setScriptEngineClass(Position position, Class<? extends ScriptEngine> scriptEngineClass) {
		Validate.notNull(position);
		Validate.notNull(scriptEngineClass);
		setScript(position, scriptEngineClass.getName());
	}
	
	/**
	 * スクリプトエンジンのクラス名を設定する。
	 * 
	 * @param position スクリプト挿入位置
	 * @param scriptEngineClassName スクリプトエンジンのクラス名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	public void setScriptEngineClassName(Position position, String scriptEngineClassName) {
		Validate.notNull(position);
		Validate.notNull(scriptEngineClassName);
		if (DEFAULT_ENGINE.equals(scriptEngineClassName)) {
			scriptEngineClassNames.remove(position);
		} else {
			scriptEngineClassNames.put(position, scriptEngineClassName);
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param target
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setTarget(EntityRef<? extends DatabaseObjectModel> target) {
		Validate.notNull(target);
		this.target = target;
	}
	
	public EntityRef<DefaultAroundScriptModel> toReference() {
		return new DefaultEntityRef<DefaultAroundScriptModel>(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
