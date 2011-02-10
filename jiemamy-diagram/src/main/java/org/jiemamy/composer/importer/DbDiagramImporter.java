/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/02/10
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
package org.jiemamy.composer.importer;

import java.util.Collection;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.DiagramFacet;
import org.jiemamy.JiemamyContext;
import org.jiemamy.composer.ImportException;
import org.jiemamy.composer.Importer;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.JmNode;
import org.jiemamy.model.SimpleDbObjectNode;
import org.jiemamy.model.SimpleJmConnection;
import org.jiemamy.model.SimpleJmDiagram;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.view.JmView;
import org.jiemamy.transaction.StoredEvent;
import org.jiemamy.transaction.StoredEventListener;

/**
 * {@link DbImporter}の処理と共に、NodeとConnectionを自動生成する{@link Importer}実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DbDiagramImporter extends DbImporter {
	
	@Override
	public String getName() {
		return "Database Importer";
	}
	
	@Override
	public boolean importModel(JiemamyContext context, DbImportConfig config) throws ImportException {
		StoredEventListenerImpl listener = new StoredEventListenerImpl(context);
		
		boolean importCompleted;
		try {
			context.getEventBroker().addListener(listener);
			importCompleted = super.importModel(context, config);
		} finally {
			context.getEventBroker().removeListener(listener);
		}
		listener.close();
		
		context.getFacet(DiagramFacet.class).store(listener.getDiagram());
		
		return importCompleted;
	}
	

	private static class StoredEventListenerImpl implements StoredEventListener {
		
		private final JiemamyContext context;
		
		private final SimpleJmDiagram diagram;
		
		private int loc = 100;
		
		private Collection<JmForeignKeyConstraint> fks = Lists.newArrayList();
		

		StoredEventListenerImpl(JiemamyContext context) {
			Validate.notNull(context);
			this.context = context;
			DiagramFacet facet = context.getFacet(DiagramFacet.class);
			if (facet.getDiagrams().isEmpty()) {
				diagram = new SimpleJmDiagram();
			} else {
				diagram = (SimpleJmDiagram) facet.getDiagrams().get(0);
			}
		}
		
		public void handleStoredEvent(StoredEvent<?> event) {
			if (event.getBefore() != null || event.getAfter() == null) {
				return;
			}
			Object object = event.getAfter();
			if (object instanceof JmTable || object instanceof JmView) {
				DbObject dbObject = (DbObject) object;
				SimpleDbObjectNode node = new SimpleDbObjectNode(dbObject.toReference());
				node.setBoundary(new JmRectangle(loc, loc));
				loc++;
				diagram.store(node);
				if (dbObject instanceof JmTable) {
					JmTable table = (JmTable) dbObject;
					fks.addAll(table.getForeignKeyConstraints());
				}
			}
		}
		
		void close() {
			for (JmForeignKeyConstraint fk : fks) {
				SimpleJmConnection conn = new SimpleJmConnection(fk.toReference());
				JmTable declaringTable = fk.findDeclaringTable(context.getTables());
				JmNode source = diagram.getNodeFor(declaringTable.toReference());
				conn.setSource(source.toReference());
				
				JmTable referenceTable = fk.findReferenceTable(context.getTables());
				JmNode target = diagram.getNodeFor(referenceTable.toReference());
				conn.setTarget(target.toReference());
				
				diagram.store(conn);
			}
		}
		
		SimpleJmDiagram getDiagram() {
			return diagram;
		}
	}
	
}
