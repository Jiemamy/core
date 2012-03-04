/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import org.jiemamy.model.DbObjectNode;
import org.jiemamy.model.JmDiagram;
import org.jiemamy.model.JmNode;
import org.jiemamy.model.geometory.JmRectangle;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.utils.UUIDUtil;

@SuppressWarnings("javadoc")
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
	
	@Test
	public void testname() {
		JmTable table = new JmTable(UUIDUtil.valueOfOrRandom("table"));
		DbObjectNode node = new DbObjectNode(UUIDUtil.valueOfOrRandom("node"), table.toReference());
		DbObjectNode node2 = new DbObjectNode(UUIDUtil.valueOfOrRandom("node2"), table.toReference());
		JmDiagram diagram = new JmDiagram(UUIDUtil.valueOfOrRandom("diagram"));
		JmDiagram diagram2 = new JmDiagram(UUIDUtil.valueOfOrRandom("diagram2"));
		
		node.setBoundary(new JmRectangle(0, 0));
		diagram.setName("diagram-1");
		diagram.store(node);
		
		DiagramFacet diagramFacet = ctx.getFacet(DiagramFacet.class);
		
		ctx.add(table);
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
