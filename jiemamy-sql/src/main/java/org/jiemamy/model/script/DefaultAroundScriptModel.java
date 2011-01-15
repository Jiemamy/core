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
package org.jiemamy.model.script;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.ServiceLocator;
import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.script.ScriptEngine;
import org.jiemamy.script.ScriptString;

/**
 * {@link AroundScriptModel}の実装クラス。
 * 
 * @author daisuke
 */
public final class DefaultAroundScriptModel extends AbstractEntity implements AroundScriptModel {
	
	private Map<Position, ScriptString> scripts = Maps.newHashMap();
	
	private EntityRef<? extends DatabaseObjectModel> coreModelRef;
	

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
		return clone;
	}
	
	public EntityRef<? extends DatabaseObjectModel> getCoreModelRef() {
		return coreModelRef;
	}
	
	public String getScript(Position position) {
		return scripts.get(position).getScript();
	}
	
	public ScriptEngine getScriptEngine(JiemamyContext context, Position position) throws ClassNotFoundException {
		ServiceLocator sl = context.getServiceLocator();
		return sl.getService(ScriptEngine.class, getScriptEngineClassName(position));
	}
	
	public String getScriptEngineClassName(Position position) {
		return scripts.get(position).getScriptEngineClassName();
	}
	
	public Map<Position, ScriptString> getScriptStrings() {
		return MutationMonitor.monitor(Maps.newHashMap(scripts));
	}
	
	public String process(JiemamyContext context, Position position, DatabaseObjectModel target)
			throws ClassNotFoundException {
		Validate.notNull(context);
		Validate.notNull(position);
		Validate.notNull(target);
		
		Map<String, Object> env = Maps.newHashMap();
		env.put("target", target);
		return scripts.get(position).process(context, env);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param coreModelRef
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setCoreModelRef(EntityRef<? extends DatabaseObjectModel> coreModelRef) {
		Validate.notNull(coreModelRef);
		this.coreModelRef = coreModelRef;
	}
	
	public void setScript(Position position, String script) {
		Validate.notNull(position);
		Validate.notNull(script);
		scripts.put(position, new ScriptString(script));
	}
	
	/**
	 * スクリプトを設定する。
	 * 
	 * @param position スクリプト挿入位置
	 * @param script スクリプト
	 * @param scriptEngineClass エンジンクラス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	public void setScript(Position position, String script, Class<? extends ScriptEngine> scriptEngineClass) {
		Validate.notNull(position);
		Validate.notNull(script);
		scripts.put(position, new ScriptString(script, scriptEngineClass));
	}
	
	/**
	 * スクリプトを設定する。
	 * 
	 * @param position スクリプト挿入位置
	 * @param script スクリプト
	 * @param scriptEngineClassName エンジンクラス名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	public void setScript(Position position, String script, String scriptEngineClassName) {
		Validate.notNull(position);
		Validate.notNull(script);
		scripts.put(position, new ScriptString(script, scriptEngineClassName));
	}
	
	public EntityRef<DefaultAroundScriptModel> toReference() {
		return new DefaultEntityRef<DefaultAroundScriptModel>(this);
	}
	
	@Override
	public String toString() {
		return "Script[" + scripts + "]";
	}
}
