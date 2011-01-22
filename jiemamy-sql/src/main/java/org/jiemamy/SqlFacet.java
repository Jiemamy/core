/*
 * Copyright 2010 Jiemamy Project and the others.
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

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryEntityResolver;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.EmitConfig;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.script.AroundScriptModel;
import org.jiemamy.model.script.DefaultAroundScriptModel;
import org.jiemamy.model.script.DefaultAroundScriptModelSerializationHandler;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.transaction.StoredEvent;
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
	
	/** プロバイダ */
	public static final FacetProvider PROVIDER = new FacetProvider() {
		
		public JiemamyFacet getFacet(JiemamyContext context) {
			return new SqlFacet(context);
		}
		
		public Class<? extends JiemamyFacet> getFacetType() {
			return SqlFacet.class;
		}
		
	};
	
	private OnMemoryRepository<AroundScriptModel> scripts = new OnMemoryRepository<AroundScriptModel>();
	
	private final JiemamyContext context;
	
	private static Logger logger = LoggerFactory.getLogger(SqlFacet.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 */
	private SqlFacet(JiemamyContext context) {
		Validate.notNull(context);
		this.context = context;
	}
	
	public void deleteScript(EntityRef<? extends AroundScriptModel> ref) {
		AroundScriptModel deleted = scripts.delete(ref);
		logger.info("script deleted: " + deleted);
		context.getEventBroker().fireEvent(new StoredEvent<AroundScriptModel>(scripts, deleted, null));
	}
	
	/**
	 * {@link JiemamyContext}からSQL文のリストを生成する。
	 * 
	 * @param dialect SQL方言 
	 * @param config 設定オブジェクト
	 * @return SQL文のリスト
	 * @throws UnsupportedOperationException SQL文の出力をサポートしていない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	public List<SqlStatement> emitStatements(Dialect dialect, EmitConfig config) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AroundScriptModel getAroundScriptFor(EntityRef<? extends DatabaseObjectModel> ref) {
		Validate.notNull(ref);
		for (AroundScriptModel aroundScriptModel : scripts.getEntitiesAsSet()) {
			if (ref.equals(aroundScriptModel.getCoreModelRef())) {
				return aroundScriptModel;
			}
		}
		throw new EntityNotFoundException("ref=" + ref);
	}
	
	/**
	 * このファセットが管理する全ての {@link AroundScriptModel} の集合を取得する。
	 * 
	 * @return {@link AroundScriptModel}の集合
	 */
	public Collection<? extends AroundScriptModel> getAroundScripts() {
		return scripts.getEntitiesAsSet();
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return SqlNamespace.values();
	}
	
	public OnMemoryEntityResolver<?> getResolver() {
		return scripts;
	}
	
	public void prepareSerializationHandlers(SerializationDirector director) {
		Validate.notNull(director);
		// FORMAT-OFF CHECKSTYLE:OFF
		director.addHandler(SqlFacet.class, SqlQName.SQLS, new SqlFacetSerializationHandler(director));
		director.addHandler(DefaultAroundScriptModel.class, SqlQName.AROUND_SCRIPT, new DefaultAroundScriptModelSerializationHandler(director));
		// CHECKSTYLE:ON FORMAT-ON
	}
	
	/**
	 * エンティティ参照から、実体を引き当てる。
	 * 
	 * @param <T> エンティティの型
	 * @param ref エンティティ参照
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	@Deprecated
	public <T extends Entity>T resolve(EntityRef<T> ref) {
		return scripts.resolve(ref);
	}
	
	/**
	 * エンティティIDから、実体を引き当てる。
	 * 
	 * @param id ENTITY ID
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	@Deprecated
	public Entity resolve(UUID id) {
		return scripts.resolve(id);
	}
	
	/**
	 * スクリプトを保存する。
	 * 
	 * @param script スクリプト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(AroundScriptModel script) {
		Validate.notNull(script);
		AroundScriptModel old = scripts.store(script);
		
		if (old == null) {
			logger.info("script stored: " + script);
		} else {
			logger.info("script updated: (old) " + old);
			logger.info("                (new) " + script);
		}
		context.getEventBroker().fireEvent(new StoredEvent<AroundScriptModel>(scripts, old, script));
	}
}
