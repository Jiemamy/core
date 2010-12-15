/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.script.AroundScriptModel;
import org.jiemamy.model.script.DefaultAroundScriptModel;
import org.jiemamy.model.script.PlainScriptEngine;
import org.jiemamy.model.script.Position;
import org.jiemamy.utils.UUIDUtil;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class SqlFacetTest {
	
	private JiemamyContext context;
	
	private SqlFacet facet;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		context = new JiemamyContext(SqlFacet.PROVIDER);
		facet = context.getFacet(SqlFacet.class);
		
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_テーブルにASを定義したら_getAroundScriptForで取得できる() throws Exception {
		DefaultTableModel table = new DefaultTableModel(UUIDUtil.valueOfOrRandom("a"));
		table.setName("T_FOO");
		context.store(table);
		
		DefaultTableModel table2 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("b"));
		table2.setName("T_BAR");
		context.store(table2);
		
		DefaultAroundScriptModel asm = new DefaultAroundScriptModel(UUIDUtil.valueOfOrRandom("b"));
		asm.setScriptEngineClass(Position.BEGIN, PlainScriptEngine.class);
		asm.setScript(Position.BEGIN, "-- BEGIN");
		asm.setTarget(table.toReference());
		facet.store(asm);
		
		// SqlFacet#getAroundScriptFor(Entity) で取り出せる
		AroundScriptModel asm2 = facet.getAroundScriptFor(table.toReference());
		assertThat(asm2, is(equalTo((AroundScriptModel) asm)));
		assertThat(asm2, is(not(sameInstance((AroundScriptModel) asm))));
		
		try {
			facet.getAroundScriptFor(table2.toReference());
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		// SqlFacet#resolve(EntityRef) で取り出せる
		DefaultAroundScriptModel asm3 = facet.resolve(asm.toReference());
		assertThat(asm3, is(equalTo((AroundScriptModel) asm)));
		assertThat(asm3, is(not(sameInstance((AroundScriptModel) asm))));
		assertThat(asm3, is(not(sameInstance(asm2))));
		
		// SqlFacet#resolve(UUID) で取り出せる
		Entity asm4 = facet.resolve(asm.getId());
		assertThat(asm4, is(equalTo((Entity) asm)));
		assertThat(asm4, is(not(sameInstance((Entity) asm))));
		
		assertThat(facet.getAroundScripts().size(), is(1));
		facet.delete(asm.toReference());
		assertThat(facet.getAroundScripts().size(), is(0));
	}
}
