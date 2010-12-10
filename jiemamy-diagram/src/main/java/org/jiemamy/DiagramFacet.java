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
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import org.jiemamy.model.ConnectionModel;
import org.jiemamy.model.DiagramModel;
import org.jiemamy.model.EntityNotFoundException;
import org.jiemamy.model.NodeModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.dbo.DatabaseObjectModel;
import org.jiemamy.utils.collection.CollectionsUtil;
import org.jiemamy.xml.DiagramNamespace;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public class DiagramFacet implements JiemamyFacet {
	
	public static final FacetProvider PROVIDER = new FacetProvider() {
		
		public JiemamyFacet getFacet(JiemamyContext context) {
			return new DiagramFacet(context);
		}
		
		public Class<? extends JiemamyFacet> getFacetType() {
			return DiagramFacet.class;
		}
		
	};
	
	private final JiemamyContext context;
	
	private Map<DatabaseObjectModel, NodeModel> nodeMap = CollectionsUtil.newHashMap();
	
	private Map<ForeignKeyConstraintModel, ConnectionModel> connectionMap = CollectionsUtil.newHashMap();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DiagramFacet(JiemamyContext context) {
		Validate.notNull(context);
		this.context = context;
	}
	
	public void addDiagram(DiagramModel diagram) {
		context.add(diagram);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * <p>返される{@link List}は他に影響を及ぼさない独立したインスタンスである。</p>
	 * 
	 * @return
	 * @since TODO
	 */
	public List<? extends DiagramModel> getDiagrams() {
		return CollectionsUtil.newArrayList(getEntities(DiagramModel.class));
	}
	
	public <T extends IdentifiableEntity>Set<T> getEntities(Class<T> clazz) {
		Validate.notNull(clazz);
		Set<T> s = CollectionsUtil.newHashSet();
		for (IdentifiableEntity e : context.map.values()) {
			if (clazz.isInstance(e)) {
				s.add(clazz.cast(e));
			}
		}
		return s;
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return DiagramNamespace.values();
	}
	
	public NodeModel getNode(DatabaseObjectModel dbo, DiagramModel diagram) {
		for (NodeModel node : diagram.getNodes()) {
			if (node.getCoreModelRef().isReferenceOf(dbo)) {
				return node;
			}
		}
		throw new EntityNotFoundException();
	}
	
	public ConnectionModel getNode(ForeignKeyConstraintModel fk, DiagramModel diagram) {
		for (NodeModel node : diagram.getNodes()) {
			for (ConnectionModel connection : node.getSourceConnections()) {
				// TODO
//				if (connection.getCoreModelRef().isReferenceOf(fk)) {
//					return connection;
//				}
			}
		}
		return null;
	}
	
	public void removeDiagram(EntityRef<DiagramModel> ref) {
		context.remove(ref);
	}
}
