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

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.OnMemoryEntityResolver;
import org.jiemamy.dddbase.OrderedOnMemoryRepository;
import org.jiemamy.model.DefaultConnectionModel;
import org.jiemamy.model.DefaultConnectionModelSerializationHandler;
import org.jiemamy.model.DefaultDatabaseObjectNodeModel;
import org.jiemamy.model.DefaultDatabaseObjectNodeModelSerializationHandler;
import org.jiemamy.model.DefaultDiagramModel;
import org.jiemamy.model.DefaultDiagramModelSerializationHandler;
import org.jiemamy.model.DiagramModel;
import org.jiemamy.model.DiagramNotFoundException;
import org.jiemamy.model.StickyNodeModel;
import org.jiemamy.model.StickyNodeModelSerializationHandler;
import org.jiemamy.model.TooManyDiagramsFoundException;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmColorSerializationHandler;
import org.jiemamy.model.geometory.JmPoint;
import org.jiemamy.model.geometory.JmPointSerializationHandler;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.geometory.JmRectangleSerializationHandler;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.transaction.StoredEvent;
import org.jiemamy.utils.LogMarker;
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
	
	private OrderedOnMemoryRepository<DiagramModel> diagrams = new OrderedOnMemoryRepository<DiagramModel>();
	
	private final JiemamyContext context;
	
	private static Logger logger = LoggerFactory.getLogger(DiagramFacet.class);
	

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
	
	/**
	 * {@link DiagramModel}を削除する。
	 * 
	 * @param reference 削除する{@link DiagramModel}への参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * TODO rename to deleteDiagram
	 */
	public DiagramModel delete(EntityRef<? extends DiagramModel> reference) {
		Validate.notNull(reference);
		DiagramModel deleted = diagrams.delete(reference);
		logger.info("diagram deleted: " + deleted);
		context.getEventBroker().fireEvent(new StoredEvent<DiagramModel>(diagrams, deleted, null));
		return deleted;
	}
	
	/**
	 * このファセットのダイアグラムのうち、{@code name}で示した名前を持つダイアグラムを返す。
	 * 
	 * @param name ダイアグラム名
	 * @return ダイアグラム
	 * @throws DiagramNotFoundException ダイアグラムが見つからなかった場合
	 * @throws TooManyDiagramsFoundException 同名のダイアグラムが複数見つかった場合
	 */
	public DiagramModel getDiagram(final String name) {
		Collection<DiagramModel> c = Collections2.filter(diagrams.getEntitiesAsList(), new Predicate<DiagramModel>() {
			
			public boolean apply(DiagramModel diagram) {
				return StringUtils.equals(diagram.getName(), name);
			}
		});
		
		try {
			return Iterables.getOnlyElement(c);
		} catch (NoSuchElementException e) {
			throw new DiagramNotFoundException("name=" + name);
		} catch (IllegalArgumentException e) {
			throw new TooManyDiagramsFoundException(c);
		}
	}
	
	/**
	 * このファセットが持つダイアグラムの {@link List} を返す。
	 * 
	 * @return このファセットが持つダイアグラムの {@link List}
	 */
	public List<? extends DiagramModel> getDiagrams() {
		return diagrams.getEntitiesAsList();
	}
	
	public Set<? extends Entity> getEntities() {
		return diagrams.getEntitiesAsSet();
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return DiagramNamespace.values();
	}
	
	public OnMemoryEntityResolver<?> getResolver() {
		return diagrams;
	}
	
	public URL getSchema() {
		return DiagramFacet.class.getResource("/jiemamy-diagram.xsd");
	}
	
	public void prepareSerializationHandlers(SerializationDirector director) {
		Validate.notNull(director);
		// FORMAT-OFF CHECKSTYLE:OFF
		director.addHandler(DiagramFacet.class, DiagramQName.DIAGRAMS, new DiagramFacetSerializationHandler(director));
		director.addHandler(DefaultDiagramModel.class, DiagramQName.DIAGRAM, new DefaultDiagramModelSerializationHandler(director));
		director.addHandler(DefaultDatabaseObjectNodeModel.class, DiagramQName.NODE, new DefaultDatabaseObjectNodeModelSerializationHandler(director));
		director.addHandler(StickyNodeModel.class, DiagramQName.NODE, new StickyNodeModelSerializationHandler(director));
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
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示すエンティティが見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	@Deprecated
	public <T extends Entity>T resolve(EntityRef<T> ref) {
		Validate.notNull(ref);
		return diagrams.resolve(ref);
	}
	
	/**
	 * エンティティIDから、実体を引き当てる。
	 * 
	 * @param id ENTITY ID
	 * @return {@link Entity}
	 * @throws EntityNotFoundException IDで示すエンティティが見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	@Deprecated
	public Entity resolve(UUID id) {
		Validate.notNull(id);
		return diagrams.resolve(id);
	}
	
	/**
	 * {@link DiagramModel}を保存する。
	 * 
	 * @param diagram ダイアグラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(DiagramModel diagram) {
		Validate.notNull(diagram);
		DiagramModel old = diagrams.store(diagram);
		if (old == null) {
			logger.info(LogMarker.LIFECYCLE, "diagram stored: " + diagram);
		} else {
			logger.info(LogMarker.LIFECYCLE, "diagram updated: (old) " + old);
			logger.info(LogMarker.LIFECYCLE, "                 (new) " + diagram);
		}
		context.getEventBroker().fireEvent(new StoredEvent<DiagramModel>(diagrams, old, diagram));
	}
}
