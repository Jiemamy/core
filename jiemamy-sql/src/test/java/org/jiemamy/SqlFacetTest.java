/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.model.script.JmAroundScript;
import org.jiemamy.model.script.Position;
import org.jiemamy.model.script.SimpleJmAroundScript;
import org.jiemamy.model.table.SimpleJmTable;
import org.jiemamy.script.PlainScriptEngine;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link SqlFacet}のテストクラス。
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
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		UUIDUtil.clear();
	}
	
	/**
	 * SqlFacetを与えたcontextの挙動確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_SqlFacetを与えたcontextの挙動確認() throws Exception {
		JiemamyContext context = new JiemamyContext(SqlFacet.PROVIDER);
		assertThat(context.hasFacet(SqlFacet.class), is(true));
		assertThat(context.getFacet(SqlFacet.class), is(notNullValue()));
	}
	
	/**
	 * テーブルにASを定義したら、getAroundScriptForで取得できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_テーブルにASを定義したら_getAroundScriptForで取得できる() throws Exception {
		SimpleJmTable table = new SimpleJmTable(UUIDUtil.valueOfOrRandom("a"));
		table.setName("T_FOO");
		context.store(table);
		
		SimpleJmTable table2 = new SimpleJmTable(UUIDUtil.valueOfOrRandom("b"));
		table2.setName("T_BAR");
		context.store(table2);
		
		SimpleJmAroundScript asm = new SimpleJmAroundScript(UUIDUtil.valueOfOrRandom("b"));
		asm.setScript(Position.BEGIN, "-- BEGIN", PlainScriptEngine.class);
		asm.setCoreModelRef(table.toReference());
		facet.store(asm);
		
		// SqlFacet#getAroundScriptFor(Entity) で取り出せる
		JmAroundScript asm2 = facet.getAroundScriptFor(table.toReference());
		assertThat(asm2, is(equalTo((JmAroundScript) asm)));
		assertThat(asm2, is(not(sameInstance((JmAroundScript) asm))));
		
		assertThat(facet.getAroundScriptFor(table2.toReference()), is(nullValue()));
		
		// SqlFacet#resolve(EntityRef) で取り出せる
		SimpleJmAroundScript asm3 = facet.resolve(asm.toReference());
		assertThat(asm3, is(equalTo((JmAroundScript) asm)));
		assertThat(asm3, is(not(sameInstance((JmAroundScript) asm))));
		assertThat(asm3, is(not(sameInstance(asm2))));
		
		// SqlFacet#resolve(UUID) で取り出せる
		Entity asm4 = facet.resolve(asm.getId());
		assertThat(asm4, is(equalTo((Entity) asm)));
		assertThat(asm4, is(not(sameInstance((Entity) asm))));
		
		assertThat(facet.getAroundScripts().size(), is(1));
		facet.deleteScript(asm.toReference());
		assertThat(facet.getAroundScripts().size(), is(0));
	}
}
