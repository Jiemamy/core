/*
 * Copyright 2010 Jiemamy Project and the others.
 * Created on 2010/12/07
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
import org.jiemamy.model.DefaultDiagramModelSerializationWorker;
import org.jiemamy.model.DefaultNodeModelSerializationWorker;
import org.jiemamy.model.DiagramModel;
import org.jiemamy.model.geometory.JmColorSerializationWorker;
import org.jiemamy.model.geometory.JmRectangleSerializationWorker;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationWorker;
import org.jiemamy.transaction.Command;
import org.jiemamy.xml.DiagramNamespace;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * ER図表現ファセット。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DiagramFacet implements JiemamyFacet {
	
	/** プロバイダ */
	public static final FacetProvider PROVIDER = new FacetProvider() {
		
		public JiemamyFacet getFacet(JiemamyContext context) {
			return new DiagramFacet(context);
		}
		
		public Class<? extends JiemamyFacet> getFacetType() {
			return DiagramFacet.class;
		}
		
	};
	
	OnMemoryRepository<DiagramModel> repos = new OnMemoryRepository<DiagramModel>();
	
	private static Logger logger = LoggerFactory.getLogger(DiagramFacet.class);
	
	private final JiemamyContext context;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	private DiagramFacet(JiemamyContext context) {
		Validate.notNull(context);
		this.context = context;
	}
	
	public void delete(final EntityRef<? extends DiagramModel> ref) {
		repos.delete(ref);
		context.getEventBroker().fireCommandProcessed(new Command() {
			
			public void execute() {
				
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
	
	public List<? extends DiagramModel> getDiagrams() {
		return repos.getEntitiesAsList();
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return DiagramNamespace.values();
	}
	
	public Iterable<? extends SerializationWorker<?>> getSerializationWorkers(SerializationDirector director) {
		Validate.notNull(director);
		Collection<SerializationWorker<?>> result = Lists.newArrayList();
		result.add(new DiagramFacetSerializationWorker(context, director));
		result.add(new DefaultDiagramModelSerializationWorker(context, director));
		result.add(new DefaultNodeModelSerializationWorker(context, director));
		// TODO ...
		result.add(new JmColorSerializationWorker(context, director));
		result.add(new JmRectangleSerializationWorker(context, director));
		return result;
	}
	
	/**
	 * エンティティ参照から、実体を引き当てる。
	 * 
	 * @param <T> エンティティの型
	 * @param ref エンティティ参照
	 * @return 実体
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 */
	public <T extends Entity>T resolve(EntityRef<T> ref) {
		return repos.resolve(ref);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param id ENTITY ID
	 * @return 
	 */
	public Entity resolve(UUID id) {
		return repos.resolve(id);
	}
	
	/**
	 * {@link DiagramModel}を保存する。
	 * 
	 * @param diagram ダイアグラム
	 */
	public void store(final DiagramModel diagram) {
		repos.store(diagram);
		context.getEventBroker().fireCommandProcessed(new Command() {
			
			public void execute() {
				// TODO Auto-generated method stub
				
			}
			
			public Command getNegateCommand() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public EntityRef<?> getTarget() {
				return diagram.toReference();
			}
		});
	}
}
