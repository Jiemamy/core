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
import org.jiemamy.dddbase.DefaultUUIDEntityRef;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.dddbase.utils.CloneUtil;
import org.jiemamy.dddbase.utils.MutationMonitor;
import org.jiemamy.model.DbObject;
import org.jiemamy.script.ScriptEngine;
import org.jiemamy.script.ScriptException;
import org.jiemamy.script.ScriptString;

/**
 * {@link JmAroundScript}の実装クラス。
 * 
 * @author daisuke
 */
public final class SimpleJmAroundScript extends AbstractEntity<UUID> implements JmAroundScript {
	
	private Map<Position, ScriptString> scripts = Maps.newHashMap();
	
	private UUIDEntityRef<? extends DbObject> coreModelRef;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public SimpleJmAroundScript() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public SimpleJmAroundScript(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmAroundScript clone() {
		SimpleJmAroundScript clone = (SimpleJmAroundScript) super.clone();
		clone.scripts = CloneUtil.cloneValueHashMap(scripts);
		return clone;
	}
	
	public UUIDEntityRef<? extends DbObject> getCoreModelRef() {
		return coreModelRef;
	}
	
	public String getScript(Position position) {
		Validate.notNull(position);
		return scripts.get(position).getScript();
	}
	
	public ScriptEngine getScriptEngine(Position position) throws ClassNotFoundException {
		Validate.notNull(position);
		ServiceLocator sl = JiemamyContext.getServiceLocator();
		return sl.getService(ScriptEngine.class, getScriptEngineClassName(position));
	}
	
	public String getScriptEngineClassName(Position position) {
		Validate.notNull(position);
		return scripts.get(position).getScriptEngineClassName();
	}
	
	/**
	 * 挿入位置とスクリプト文字列の{@link Map}を返す。
	 * 
	 * @return 挿入位置とスクリプト文字列の{@link Map}
	 */
	public Map<Position, ScriptString> getScriptStrings() {
		return MutationMonitor.monitor(Maps.newHashMap(scripts));
	}
	
	public String process(JiemamyContext context, Position position, Object target) throws ClassNotFoundException,
			ScriptException {
		Validate.notNull(context);
		Validate.notNull(position);
		Validate.notNull(target);
		
		Map<String, Object> env = Maps.newHashMap();
		env.put("target", target);
		// TODO env-objectを整備
		return scripts.get(position).process(env);
	}
	
	/**
	 * 設定対象{@link DbObject}の参照を設定する。
	 * 
	 * @param coreModelRef 設定対象{@link DbObject}の参照
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setCoreModelRef(UUIDEntityRef<? extends DbObject> coreModelRef) {
		Validate.notNull(coreModelRef);
		this.coreModelRef = coreModelRef;
	}
	
	/**
	 * 挿入位置に対してスクリプトを設定する。
	 * 
	 * @param position スクリプト挿入位置
	 * @param script スクリプト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
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
	
	@Override
	public UUIDEntityRef<? extends SimpleJmAroundScript> toReference() {
		return new DefaultUUIDEntityRef<SimpleJmAroundScript>(this);
	}
	
	@Override
	public String toString() {
		return "Script[" + scripts + "]";
	}
}
