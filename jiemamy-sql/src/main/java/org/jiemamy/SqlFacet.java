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

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.EmitConfig;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.model.script.AroundScriptModel;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.transaction.Command;
import org.jiemamy.xml.JiemamyNamespace;
import org.jiemamy.xml.SqlNamespace;

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
	
	private static Logger logger = LoggerFactory.getLogger(SqlFacet.class);
	
	private final JiemamyContext context;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 */
	private SqlFacet(JiemamyContext context) {
		Validate.notNull(context);
		this.context = context;
	}
	
	public void delete(final EntityRef<? extends AroundScriptModel> ref) {
		scripts.delete(ref);
		context.getEventBroker().fireCommandProcessed(new Command() {
			
			public void execute() {
				// TODO Auto-generated method stub
				
			}
			
			public Command getNegateCommand() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public EntityRef<?> getTarget() {
				return ref;
			}
		});
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
	
	/**
	 * TODO for daisuke
	 * 
	 * @param ref
	 * @return
	 */
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
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public Collection<? extends AroundScriptModel> getAroundScripts() {
		return scripts.getEntitiesAsSet();
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return SqlNamespace.values();
	}
	
	public Iterable<? extends SerializationWorker<?>> getSerializationWorkers(SerializationDirector director) {
		Validate.notNull(director);
		return Lists.newArrayList();
	}
	
	public <T2 extends Entity>T2 resolve(EntityRef<T2> ref) {
		return scripts.resolve(ref);
	}
	
	public Entity resolve(UUID id) {
		return scripts.resolve(id);
	}
	
	/**
	 * スクリプトを保存する。
	 * 
	 * @param script スクリプト
	 */
	public void store(final AroundScriptModel script) {
		Validate.notNull(script);
		Validate.notNull(script.getId());
//		Validate.notNull(script.getTarget());
		scripts.store(script);
		context.getEventBroker().fireCommandProcessed(new Command() {
			
			public void execute() {
				
			}
			
			public Command getNegateCommand() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public EntityRef<?> getTarget() {
				return script.toReference();
			}
		});
	}
}
