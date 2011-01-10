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

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.model.DefaultConnectionModel;
import org.jiemamy.model.DefaultConnectionModelSerializationHandler;
import org.jiemamy.model.DefaultDiagramModel;
import org.jiemamy.model.DefaultDiagramModelSerializationHandler;
import org.jiemamy.model.DefaultNodeModel;
import org.jiemamy.model.DefaultNodeModelSerializationHandler;
import org.jiemamy.model.DiagramModel;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmColorSerializationHandler;
import org.jiemamy.model.geometory.JmPoint;
import org.jiemamy.model.geometory.JmPointSerializationHandler;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.geometory.JmRectangleSerializationHandler;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.transaction.Command;
import org.jiemamy.xml.DiagramNamespace;
import org.jiemamy.xml.DiagramQName;
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
		context.getEventBroker().fireCommandProcessed(new Command() { // FIXME コマンド使ってない
				
					public void execute() {
					}
					
					public Command getNegateCommand() {
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
	
	public void prepareSerializationHandlers(SerializationDirector director) {
		Validate.notNull(director);
		// FORMAT-OFF CHECKSTYLE:OFF
		director.addHandler(DiagramFacet.class, DiagramQName.DIAGRAMS, new DiagramFacetSerializationHandler(director));
		director.addHandler(DefaultDiagramModel.class, DiagramQName.DIAGRAM, new DefaultDiagramModelSerializationHandler(director));
		director.addHandler(DefaultNodeModel.class, DiagramQName.NODE, new DefaultNodeModelSerializationHandler(director));
		director.addHandler(DefaultConnectionModel.class, DiagramQName.CONNECTION, new DefaultConnectionModelSerializationHandler(director));
		// TODO sticky handler
		director.addHandler(JmColor.class, DiagramQName.COLOR, new JmColorSerializationHandler(director));
		director.addHandler(JmPoint.class, DiagramQName.BENDPOINT, new JmPointSerializationHandler(director));
		director.addHandler(JmRectangle.class, DiagramQName.BOUNDARY, new JmRectangleSerializationHandler(director));
		// CHECKSTYLE:ON FORMAT-ON
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
		context.getEventBroker().fireCommandProcessed(new Command() { // FIXME コマンド使ってない
				
					public void execute() {
					}
					
					public Command getNegateCommand() {
						return null;
					}
					
					public EntityRef<?> getTarget() {
						return diagram.toReference();
					}
				});
	}
}
