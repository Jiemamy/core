/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/08
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
package org.jiemamy;

import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryEntityResolver;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.script.JmAroundScript;
import org.jiemamy.model.script.SimpleJmAroundScript;
import org.jiemamy.model.script.SimpleJmAroundScriptStaxHandler;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.transaction.StoredEvent;
import org.jiemamy.utils.LogMarker;
import org.jiemamy.xml.JiemamyNamespace;
import org.jiemamy.xml.SqlNamespace;
import org.jiemamy.xml.SqlQName;

/**
 * SQL表現ファセット。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class SqlFacet implements JiemamyFacet {
	
	private static Logger logger = LoggerFactory.getLogger(SqlFacet.class);
	
	/** プロバイダ */
	public static final FacetProvider PROVIDER = new FacetProvider() {
		
		public JiemamyFacet getFacet(JiemamyContext context) {
			return new SqlFacet(context);
		}
		
		public Class<? extends JiemamyFacet> getFacetType() {
			return SqlFacet.class;
		}
		
	};
	
	private final JiemamyContext context;
	
	private OnMemoryRepository<JmAroundScript> scripts = new OnMemoryRepository<JmAroundScript>();
	
	private JmAroundScript universalAroundScript;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 */
	private SqlFacet(JiemamyContext context) {
		Validate.notNull(context);
		this.context = context;
	}
	
	/**
	 * {@link JmAroundScript}を削除する。
	 * 
	 * @param reference 削除する{@link JmAroundScript}への参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmAroundScript deleteScript(EntityRef<? extends JmAroundScript> reference) {
		JmAroundScript deleted = scripts.delete(reference);
		logger.info("script deleted: " + deleted);
		context.getEventBroker().fireEvent(new StoredEvent<JmAroundScript>(scripts, deleted, null));
		return deleted;
	}
	
	/**
	 * このSQLにおける、指定した {@link DbObject} の写像となる {@link JmAroundScript} を取得する。
	 * 
	 * @param reference {@link DbObject}の参照
	 * @return 写像となる {@link JmAroundScript}、存在しない場合は{@code null}
	 */
	public JmAroundScript getAroundScriptFor(EntityRef<? extends DbObject> reference) {
		Validate.notNull(reference);
		for (JmAroundScript aroundScript : scripts.getEntitiesAsSet()) {
			if (reference.equals(aroundScript.getCoreModelRef())) {
				return aroundScript;
			}
		}
		return null;
//		throw new EntityNotFoundException("ref=" + reference);
	}
	
	/**
	 * このファセットが管理する全ての {@link JmAroundScript} の集合を取得する。
	 * 
	 * @return {@link JmAroundScript}の集合
	 */
	public Collection<? extends JmAroundScript> getAroundScripts() {
		return scripts.getEntitiesAsSet();
	}
	
	public Set<? extends Entity> getEntities() {
		return scripts.getEntitiesAsSet();
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return SqlNamespace.values();
	}
	
	public OnMemoryEntityResolver<?> getResolver() {
		return scripts;
	}
	
	public URL getSchema() {
		return SqlFacet.class.getResource("/jiemamy-sql.xsd");
	}
	
	/**
	 * SQL出力全体に適用する{@link JmAroundScript}を取得する。
	 * 
	 * @return SQL出力全体に適用する{@link JmAroundScript}
	 */
	public JmAroundScript getUniversalAroundScript() {
		if (universalAroundScript == null) {
			return null;
		}
		return universalAroundScript.clone();
	}
	
	public void prepareStaxHandlers(StaxDirector director) {
		Validate.notNull(director);
		// FORMAT-OFF CHECKSTYLE:OFF
		director.addHandler(SqlFacet.class, SqlQName.SQLS, new SqlFacetStaxHandler(director));
		director.addHandler(SimpleJmAroundScript.class, SqlQName.AROUND_SCRIPT, new SimpleJmAroundScriptStaxHandler(director));
		// CHECKSTYLE:ON FORMAT-ON
	}
	
	/**
	 * {@link EntityRef}から、{@link Entity}を引き当てる。
	 * 
	 * @param <T> {@link Entity}の型
	 * @param reference {@link EntityRef}
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T extends Entity>T resolve(EntityRef<T> reference) {
		return scripts.resolve(reference);
	}
	
	/**
	 * IDから、{@link Entity}を引き当てる。
	 * 
	 * @param id ENTITY ID
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Entity resolve(UUID id) {
		return scripts.resolve(id);
	}
	
	/**
	 * SQL出力全体に適用する{@link JmAroundScript}を設定する。
	 * 
	 * @param universalAroundScript SQL出力全体に適用する{@link JmAroundScript}
	 */
	public void setUniversalAroundScript(JmAroundScript universalAroundScript) {
		if (universalAroundScript == null) {
			this.universalAroundScript = null;
		} else {
			this.universalAroundScript = universalAroundScript.clone();
		}
	}
	
	/**
	 * スクリプトを保存する。
	 * 
	 * @param script スクリプト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(JmAroundScript script) {
		Validate.notNull(script);
		JmAroundScript old = scripts.store(script);
		
		if (old == null) {
			logger.debug(LogMarker.LIFECYCLE, "script stored: " + script);
		} else {
			logger.debug(LogMarker.LIFECYCLE, "script updated: (old) " + old);
			logger.debug(LogMarker.LIFECYCLE, "                (new) " + script);
		}
		context.getEventBroker().fireEvent(new StoredEvent<JmAroundScript>(scripts, old, script));
	}
}
