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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.DefaultDiagramModel;
import org.jiemamy.model.DefaultDatabaseObjectNodeModel;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.utils.UUIDUtil;

/**
 * TODO for daisuke
 * 
 * @since TODO for daisuke
 * @version $Id$
 * @author daisuke
 */
public class StoryTest {
	
	private JiemamyContext ctx;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx = new JiemamyContext(DiagramFacet.PROVIDER);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		DefaultTableModel table = new DefaultTableModel(UUIDUtil.valueOfOrRandom("table"));
		DefaultDatabaseObjectNodeModel node = new DefaultDatabaseObjectNodeModel(UUIDUtil.valueOfOrRandom("node"), table.toReference());
		DefaultDatabaseObjectNodeModel node2 = new DefaultDatabaseObjectNodeModel(UUIDUtil.valueOfOrRandom("node2"), table.toReference());
		DefaultDiagramModel diagram = new DefaultDiagramModel(UUIDUtil.valueOfOrRandom("diagram"));
		DefaultDiagramModel diagram2 = new DefaultDiagramModel(UUIDUtil.valueOfOrRandom("diagram2"));
		
		node.setBoundary(new JmRectangle(0, 0));
		diagram.setName("diagram-1");
		diagram.store(node);
		
		DiagramFacet diagramFacet = ctx.getFacet(DiagramFacet.class);
		
		ctx.store(table);
		diagramFacet.store(diagram);
		
		assertThat(diagramFacet.getDiagrams().size(), is(1));
		// TODO
//		assertThat(diagramFacet.getNode(table, diagram), is((NodeModel) node));
//		
//		try {
//			diagramFacet.getNode(table, diagram2);
//			fail();
//		} catch (EntityNotFoundException e) {
//			// success
//		}
//		
//		node2.setBoundary(new JmRectangle(10, 10));
//		diagram2.setName("diagram-2");
//		diagram2.putNode(node2);
//		diagramFacet.addDiagram(diagram2);
//		
//		assertThat(diagramFacet.getDiagrams().size(), is(2));
//		assertThat(diagramFacet.getNode(table, diagram).getBoundary(), is(new JmRectangle(0, 0)));
//		assertThat(diagramFacet.getNode(table, diagram2).getBoundary(), is(new JmRectangle(10, 10)));
	}
	
}
