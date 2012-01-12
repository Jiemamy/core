/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
import org.jiemamy.dddbase.OnMemoryEntityResolver;
import org.jiemamy.dddbase.OrderedOnMemoryRepository;
import org.jiemamy.dddbase.UUIDEntity;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.DiagramNotFoundException;
import org.jiemamy.model.JmDiagram;
import org.jiemamy.model.JmStickyNode;
import org.jiemamy.model.JmStickyNodeStaxHandler;
import org.jiemamy.model.SimpleDbObjectNode;
import org.jiemamy.model.SimpleDbObjectNodeStaxHandler;
import org.jiemamy.model.SimpleJmConnection;
import org.jiemamy.model.SimpleJmConnectionStaxHandler;
import org.jiemamy.model.SimpleJmDiagram;
import org.jiemamy.model.SimpleJmDiagramStaxHandler;
import org.jiemamy.model.TooManyDiagramsFoundException;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmColorStaxHandler;
import org.jiemamy.model.geometory.JmPoint;
import org.jiemamy.model.geometory.JmPointStaxHandler;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.geometory.JmRectangleStaxHandler;
import org.jiemamy.serializer.stax.StaxDirector;
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
	
	private OrderedOnMemoryRepository<JmDiagram, UUID> diagrams = new OrderedOnMemoryRepository<JmDiagram, UUID>();
	
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
	 * {@link JmDiagram}を削除する。
	 * 
	 * @param reference 削除する{@link JmDiagram}への参照
	 * @return 削除したモデル
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public JmDiagram deleteDiagram(UUIDEntityRef<? extends JmDiagram> reference) {
		Validate.notNull(reference);
		JmDiagram deleted = diagrams.delete(reference);
		logger.info("diagram deleted: " + deleted);
		context.getEventBroker().fireEvent(new StoredEvent<JmDiagram>(diagrams, deleted, null));
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
	public JmDiagram getDiagram(final String name) {
		Collection<JmDiagram> c = Collections2.filter(diagrams.getEntitiesAsList(), new Predicate<JmDiagram>() {
			
			public boolean apply(JmDiagram diagram) {
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
	public List<? extends JmDiagram> getDiagrams() {
		return diagrams.getEntitiesAsList();
	}
	
	public Set<? extends UUIDEntity> getEntities() {
		return diagrams.getEntitiesAsSet();
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return DiagramNamespace.values();
	}
	
	public OnMemoryEntityResolver<? extends UUIDEntity, UUID> getResolver() {
		return diagrams;
	}
	
	public URL getSchema() {
		return DiagramFacet.class.getResource("/jiemamy-diagram.xsd");
	}
	
	public void prepareStaxHandlers(StaxDirector director) {
		Validate.notNull(director);
		// FORMAT-OFF CHECKSTYLE:OFF
		director.addHandler(DiagramFacet.class, DiagramQName.DIAGRAMS, new DiagramFacetStaxHandler(director));
		director.addHandler(SimpleJmDiagram.class, DiagramQName.DIAGRAM, new SimpleJmDiagramStaxHandler(director));
		
		// THINK
		// Nodeモデルのハンドラは必ず最後にSimpleDbObjectNodeを登録すること
		// でないとHashマップのキーが上書きされてしまう。
		director.addHandler(JmStickyNode.class, DiagramQName.NODE, new JmStickyNodeStaxHandler(director));
		director.addHandler(SimpleDbObjectNode.class, DiagramQName.NODE, new SimpleDbObjectNodeStaxHandler(director));
		director.addHandler(SimpleJmConnection.class, DiagramQName.CONNECTION, new SimpleJmConnectionStaxHandler(director));
		director.addHandler(JmColor.class, DiagramQName.COLOR, new JmColorStaxHandler(director));
		director.addHandler(JmPoint.class, DiagramQName.BENDPOINT, new JmPointStaxHandler(director));
		director.addHandler(JmRectangle.class, DiagramQName.BOUNDARY, new JmRectangleStaxHandler(director));
		// CHECKSTYLE:ON FORMAT-ON
	}
	
	/**
	 * IDから、{@link Entity}を引き当てる。
	 * 
	 * @param id ENTITY ID
	 * @return {@link Entity}
	 * @throws EntityNotFoundException IDで示す{@link Entity}が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public Entity<UUID> resolve(UUID id) {
		Validate.notNull(id);
		return diagrams.resolve(id);
	}
	
	/**
	 * {@link UUIDEntityRef}から、{@link Entity}を引き当てる。
	 * 
	 * @param <T> {@link Entity}の型
	 * @param reference {@link UUIDEntityRef}
	 * @return {@link Entity}
	 * @throws EntityNotFoundException 参照で示す{@link Entity}が見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T extends UUIDEntity>T resolve(UUIDEntityRef<T> reference) {
		Validate.notNull(reference);
		return diagrams.resolve(reference);
	}
	
	/**
	 * {@link JmDiagram}を保存する。
	 * 
	 * @param diagram ダイアグラム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void store(JmDiagram diagram) {
		Validate.notNull(diagram);
		JmDiagram old = diagrams.store(diagram);
		if (old == null) {
			logger.debug(LogMarker.LIFECYCLE, "diagram stored: " + diagram);
		} else {
			logger.debug(LogMarker.LIFECYCLE, "diagram updated: (old) " + old);
			logger.debug(LogMarker.LIFECYCLE, "                 (new) " + diagram);
		}
		context.getEventBroker().fireEvent(new StoredEvent<JmDiagram>(diagrams, old, diagram));
	}
	
}
