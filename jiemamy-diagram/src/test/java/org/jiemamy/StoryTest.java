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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.JmNode;
import org.jiemamy.model.SimpleDbObjectNode;
import org.jiemamy.model.SimpleJmDiagram;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.table.SimpleJmTable;
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
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		UUIDUtil.clear();
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		SimpleJmTable table = new SimpleJmTable(UUIDUtil.valueOfOrRandom("table"));
		SimpleDbObjectNode node = new SimpleDbObjectNode(UUIDUtil.valueOfOrRandom("node"), table.toReference());
		SimpleDbObjectNode node2 = new SimpleDbObjectNode(UUIDUtil.valueOfOrRandom("node2"), table.toReference());
		SimpleJmDiagram diagram = new SimpleJmDiagram(UUIDUtil.valueOfOrRandom("diagram"));
		SimpleJmDiagram diagram2 = new SimpleJmDiagram(UUIDUtil.valueOfOrRandom("diagram2"));
		
		node.setBoundary(new JmRectangle(0, 0));
		diagram.setName("diagram-1");
		diagram.store(node);
		
		DiagramFacet diagramFacet = ctx.getFacet(DiagramFacet.class);
		
		ctx.store(table);
		diagramFacet.store(diagram);
		
		assertThat(diagramFacet.getDiagrams().size(), is(1));
		assertThat(diagramFacet.getDiagram("diagram-1").getNodeFor(table.toReference()), is((JmNode) node));
		
		assertThat(diagram2.getNodeFor(table.toReference()), is(nullValue()));
		
		node2.setBoundary(new JmRectangle(10, 10));
		diagram2.setName("diagram-2");
		diagram2.store(node2);
		diagramFacet.store(diagram2);
		
		assertThat(diagramFacet.getDiagrams().size(), is(2));
		assertThat(diagramFacet.getDiagram("diagram-1").getNodeFor(table.toReference()).getBoundary(),
				is(new JmRectangle(0, 0)));
		assertThat(diagramFacet.getDiagram("diagram-2").getNodeFor(table.toReference()).getBoundary(),
				is(new JmRectangle(10, 10)));
	}
}
