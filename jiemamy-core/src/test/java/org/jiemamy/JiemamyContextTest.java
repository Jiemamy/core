/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/04
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.utils.UUIDUtil;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContextTest {
	
	JiemamyContext ctx1;
	
	JiemamyContext ctx2;
	
	TableModel t0a1;
	
	TableModel t0a2;
	
	TableModel t0b1;
	
	TableModel t0b2;
	
	DefaultTableModel t1;
	
	DefaultTableModel t2;
	
	ColumnModel c1a1;
	
	ColumnModel c1a2;
	
	ColumnModel c1b1;
	
	ColumnModel c1b2;
	
	ColumnModel c2;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx1 = new JiemamyContext();
		ctx2 = new JiemamyContext();
		
		t0a1 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("tid0"));
		t0a2 = t0a1;
		t0b1 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("tid0"));
		t0b2 = t0b1;
		t1 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("tid1"));
		t2 = new DefaultTableModel(UUIDUtil.valueOfOrRandom("tid2"));
		
		c1a1 = new DefaultColumnModel(UUIDUtil.valueOfOrRandom("cid1"));
		c1a2 = c1a1;
		c1b1 = new DefaultColumnModel(UUIDUtil.valueOfOrRandom("cid1"));
		c1b2 = c1b1;
		c2 = new DefaultColumnModel(UUIDUtil.valueOfOrRandom("cid2"));
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		ctx1.dispose();
		ctx2.dispose();
	}
	
	@Test
	public void test00_IDが違うテーブルを同じctxに追加できる() {
		ctx1.getCore().add(t0a1);
		ctx1.getCore().add(t2); // ID違うのでOK
		
		assertThat(JiemamyContext.managedEntityCount(), is(2));
	}
	
	@Test(expected = EntityLifecycleException.class)
	public void test01_IDが同じテーブルを同じctx内に置くことはできない() {
		ctx1.getCore().add(t0a1);
		ctx1.getCore().add(t0b1); // equalsなのでexception
	}
	
	@Test
	public void test02_IDが同じであってもctxが違えば置くことができる() {
		ctx1.getCore().add(t0a1);
		ctx2.getCore().add(t0b1); // equalsだけどctxが違うのでOK
		
		assertThat(JiemamyContext.managedEntityCount(), is(2));
	}
	
	@Test(expected = EntityLifecycleException.class)
	public void test03_たとえctxが違っていても同じインスタンスだったら置けない() {
		ctx1.getCore().add(t0a1);
		ctx2.getCore().add(t0a2); // ctxは違えど==なのでexception
	}
	
	@Test
	public void test10_IDが違うカラムを同じテーブルに追加できる() throws Exception {
		t1.addColumn(c1a1);
		t1.addColumn(c2); // ID違うのでOK
		
		assertThat(JiemamyContext.managedEntityCount(), is(0));
	}
	
	@Test(expected = EntityLifecycleException.class)
	public void test11_IDが同じカラムを同じテーブル内に置くことはできない() {
		t1.addColumn(c1a1);
		t1.addColumn(c1b1); // equalsなのでexception
	}
	
	@Test
	public void test12_IDが同じであってもctxが違えば置くことができる() {
		t1.addColumn(c1a1);
		t2.addColumn(c1b1); // equalsなのでexceptionかと思いきや
		ctx1.getCore().add(t1);
		ctx2.getCore().add(t2); // equalsだけどctxが違うのでOK
		
		// TODO
//		assertThat(JiemamyContext.managedEntityCount(), is(4));
	}
	
	@Ignore("カラムを持ったテーブルをctxにaddした時、現状ctxに管理下に入るのはtableのみ")
	@Test(expected = EntityLifecycleException.class)
	public void test12b_IDが同じカラムはテーブルが違ってもctxが同じであれば置くことができない() {
		t1.addColumn(c1a1);
		t2.addColumn(c1b1); // equalsなのでexceptionかな
		ctx1.getCore().add(t1);
		ctx1.getCore().add(t2); // やっぱり
		
		// THINK tableをctxにaddした時、columnも管理下に入るべきなのか、columnをAPIユーザの手で管理下に入れるべきなのか
	}
	
	@Test(expected = EntityLifecycleException.class)
	public void test13_そもそも同じインスタンスだったらctxと関係なくテーブルに置けない() {
		t1.addColumn(c1a1);
		t2.addColumn(c1a2); // ==なのでexception
		
		// この先にはたどり着けない
//		ctx1.getCore().add(t0);
//		ctx2.getCore().add(t2);
	}
	
	@Test
	public void test20_テーブルにカラムを追加してもctxに管理されないが_ctxにaddした途端にtableもろともcolumnも管理される() throws Exception {
		t1.addColumn(c1a1);
		t1.addColumn(c2); // ID違うのでOK
		
		// TODO
//		assertThat(JiemamyContext.managedEntityCount(), is(0));
//		ctx1.getCore().add(t1);
//		assertThat(JiemamyContext.managedEntityCount(), is(3));
	}
	
	@Test
	public void test21_test20を別の角度から順を追って確認() {
		assertThat(JiemamyContext.managedEntityCount(), is(0));
		t1.addColumn(c1a1);
		t2.addColumn(c1b1);
		assertThat(JiemamyContext.managedEntityCount(), is(0));
		ctx1.getCore().add(t1);
		
		// TODO
//		assertThat(JiemamyContext.managedEntityCount(), is(2));
//		ctx2.getCore().add(t2);
//		assertThat(JiemamyContext.managedEntityCount(), is(4));
	}
	
}
