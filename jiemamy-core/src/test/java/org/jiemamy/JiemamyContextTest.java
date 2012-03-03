/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.jiemamy.utils.RandomUtil.strNullable;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Iterables;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dialect.MockDialect;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.dataset.SimpleJmDataSetTest;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;
import org.jiemamy.model.table.JmTableTest;
import org.jiemamy.model.table.TableNotFoundException;
import org.jiemamy.model.table.TooManyTablesFoundException;
import org.jiemamy.model.view.JmViewTest;
import org.jiemamy.serializer.JiemamySerializer;
import org.jiemamy.serializer.stax.JiemamyStaxSerializer;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link JiemamyContext}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContextTest {
	
	private static final UUID TID1 = UUIDUtil.valueOfOrRandom("tid1");
	
	private static final UUID TID2 = UUIDUtil.valueOfOrRandom("tid2");
	
	private static final UUID TID3 = UUIDUtil.valueOfOrRandom("tid3");
	
	private static final UUID CID1 = UUIDUtil.valueOfOrRandom("cid1");
	
	private static final UUID CID2 = UUIDUtil.valueOfOrRandom("cid2");
	
	private static final UUID CID3 = UUIDUtil.valueOfOrRandom("cid3");
	
	
	/**
	 * 適当な {@link JiemamyContext} のインスタンスを作る。
	 * 
	 * @param providers contextに組み込む {@link JiemamyFacet}のプロバイダ
	 * @return {@link JiemamyContext}
	 */
	public static JiemamyContext random(FacetProvider... providers) {
		JiemamyContext context = new JiemamyContext(providers);
		
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDescription(strNullable());
		meta.setDialectClassName(bool() ? null : "org.jiemamy.dialect.GenericDialect");
		meta.setSchemaName(strNullable());
		context.setMetadata(meta);
		
		// tableの生成
		int size = integer(5) + 1;
		for (int i = 0; i < size; i++) {
			context.store(JmTableTest.random());
		}
		// viewの生成
		size = integer(5) + 1;
		for (int i = 0; i < size; i++) {
			context.store(JmViewTest.random());
		}
		
		// TODO domain, indexとかもstoreする
		
		// dateSetの生成
		size = integer(5) + 1;
		for (int i = 0; i < size; i++) {
			context.store(SimpleJmDataSetTest.random(context.getTables()));
		}
		
		return context;
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@AfterClass
	public static void tearDownClass() throws Exception {
		UUIDUtil.clear();
	}
	
	
	private JiemamyContext ctx1;
	
	private JiemamyContext ctx2;
	
	private JmTable t1a;
	
	private JmTable t1b;
	
	private JmTable t2;
	
	private JmTable t3;
	
	private JmColumn c1a;
	
	private JmColumn c1b;
	
	private JmColumn c2;
	
	private JmColumn c3;
	
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx1 = new JiemamyContext();
		ctx2 = new JiemamyContext();
		
		// FORMAT-OFF
		t1a = new JmTable(TID1); t1a.setName("A");
		t1b = new JmTable(TID1); t1b.setName("B");
		t2 = new JmTable(TID2);
		t3 = new JmTable(TID3);
		
		c1a = new JmColumn(CID1); c1a.setName("A");
		c1b = new JmColumn(CID1); c1b.setName("B");
		c2 = new JmColumn(CID2);
		c3 = new JmColumn(CID3);
		// FORMAT-ON
	}
	
	/**
	 * IDが違うテーブルを同じctxに追加できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test00_IDが違うテーブルを同じctxに追加できる() throws Exception {
		ctx1.store(t1a);
		ctx1.store(t3); // ID違うのでOK
		assertThat(((JmTable) ctx1.resolve(TID1)).getName(), is("A"));
		assertThat(((JmTable) ctx1.resolve(TID3)).getName(), is(nullValue()));
	}
	
	/**
	 * IDが同じテーブルを同じctxに追加すると置換更新となる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_IDが同じテーブルを同じctxに追加すると置換更新となる() throws Exception {
		ctx1.store(t1a);
		ctx1.store(t1b); // 等価なので置換
		assertThat(((JmTable) ctx1.resolve(TID1)).getName(), is("B"));
	}
	
	/**
	 * IDが同じであってもctxが違えば普通に別管理となる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_IDが同じであってもctxが違えば普通に別管理となる() throws Exception {
		ctx1.store(t1a);
		ctx2.store(t1b); // 等価だけどctxが違うので変化なし
		assertThat(((JmTable) ctx1.resolve(TID1)).getName(), is("A"));
		assertThat(((JmTable) ctx2.resolve(TID1)).getName(), is("B"));
	}
	
	/**
	 * 同一テーブルを同じctx内にaddする意味はあまりない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_同一テーブルを同じctx内にaddする意味はあまりない() throws Exception {
		ctx1.store(t1a);
		ctx1.store(t1a); // 同一なので置き換えるが無意味
		assertThat(((JmTable) ctx1.resolve(TID1)).getName(), is("A"));
	}
	
	/**
	 * 同一テーブルを2つの異なるctxにaddしてもお互い影響しない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_同一テーブルを2つの異なるctxにaddしてもお互い影響しない() throws Exception {
		ctx1.store(t1a);
		ctx2.store(t1a); // ctxは違えど同一なのでexception
		assertThat(((JmTable) ctx1.resolve(TID1)).getName(), is("A"));
		assertThat(((JmTable) ctx2.resolve(TID1)).getName(), is("A"));
		t1a.setName("A2");
		assertThat(((JmTable) ctx1.resolve(TID1)).getName(), is("A"));
		assertThat(((JmTable) ctx2.resolve(TID1)).getName(), is("A"));
		ctx1.store(t1a);
		assertThat(((JmTable) ctx1.resolve(TID1)).getName(), is("A2"));
		assertThat(((JmTable) ctx2.resolve(TID1)).getName(), is("A"));
	}
	
	/**
	 * removeしたテーブルを同じctxに追加できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_removeしたテーブルを同じctxに追加できる() throws Exception {
		ctx1.store(t1a);
		ctx1.deleteDbObject(t1a.toReference());
		ctx1.store(t1a);
	}
	
	/**
	 * もちろんremoveしたテーブルを他のctxに追加してもよい。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_もちろんremoveしたテーブルを他のctxに追加してもよい() throws Exception {
		ctx1.store(t1a);
		ctx1.deleteDbObject(t1a.toReference());
		ctx2.store(t1a);
	}
	
	/**
	 * ctx1で管理したのはt1bじゃなくてt1aだけどIDが同じなのでremoveできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_ctx1で管理したのはt1bじゃなくてt1aだけどIDが同じなのでremoveできる() throws Exception {
		ctx1.store(t1a);
		ctx1.deleteDbObject(t1b.toReference());
		try {
			ctx1.resolve(t1a.toReference());
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
	}
	
	/**
	 * 管理していないインスタンスをremoveできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityNotFoundException.class)
	public void test08_管理していないインスタンスをremoveできない() throws Exception {
		ctx1.deleteDbObject(t1a.toReference());
	}
	
	/**
	 * t1aを管理しているのはctx2じゃないので例外。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityNotFoundException.class)
	public void test09_t1aを管理しているのはctx2じゃないので例外() throws Exception {
		ctx1.store(t1a);
		ctx2.deleteDbObject(t1a.toReference());
	}
	
	/**
	 * IDが違うカラムを同じctxに追加できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test10_IDが違うカラムを同じctxに追加できる() throws Exception {
		t1a.store(c1a);
		t1a.store(c3); // ID違うのでOK
		assertThat(t1a.getColumn(c1a.toReference()).getName(), is("A"));
		assertThat(t1a.getColumn(c3.toReference()).getName(), is(nullValue()));
	}
	
	/**
	 * IDが同じカラムを同じテーブル内に置くことはできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11_IDが同じカラムを同じテーブル内に置くことはできない() throws Exception {
		t1a.store(c1a);
		t1a.store(c1b); // 等価なので置き換え
		assertThat(t1a.getColumn(c1a.toReference()).getName(), is("B"));
	}
	
	/**
	 * カラムのIDが同じであってもテーブルが違えば置くことができる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12_カラムのIDが同じであってもテーブルが違えば置くことができる() throws Exception {
		t1a.store(c1a);
		t2.store(c1b); // 等価だけどテーブルが違うのでOK
		assertThat(t1a.getColumn(c1a.toReference()).getName(), is("A"));
		assertThat(t2.getColumn(c1b.toReference()).getName(), is("B"));
	}
	
	/**
	 * カラムのIDが同じであってもテーブルのインスタンスが違えば置くことができる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12b_カラムのIDが同じであってもテーブルのインスタンスが違えば置くことができる() throws Exception {
		t1a.store(c1a);
		t1b.store(c1b); // 等価だけどテーブルが違うのでOK
		assertThat(t1a.getColumn(c1a.toReference()).getName(), is("A"));
		assertThat(t1b.getColumn(c1b.toReference()).getName(), is("B"));
	}
	
	/**
	 * カラムとIDのIDが同じであってもコンテキストが違えば置くことができる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12c_カラムとIDのIDが同じであってもコンテキストが違えば置くことができる() throws Exception {
		t1a.store(c1a);
		t1b.store(c1b);
		ctx1.store(t1a);
		ctx2.store(t1b); // コンテキストが違うからOK
		
		assertThat(ctx1.resolve(c1a.toReference()).getName(), is("A"));
		assertThat(ctx2.resolve(c1b.toReference()).getName(), is("B"));
	}
	
	/**
	 * カラムとIDのIDが同じセットを同じコンテキストに置こうとすると例外。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test12d_カラムとIDのIDが同じセットを同じコンテキストに置こうとすると例外() throws Exception {
		t1a.store(c1a);
		t2.store(c1b);
		ctx1.store(t1a);
		ctx1.store(t2); // 既に管理済みのt1aが持つc1aと同じIDを持つc1bをt2が持ってるからダメ
	}
	
	/**
	 * 同じIDのカラムを複数のテーブルに格納した場合、それぞれのテーブルが属するcontextが異なれば問題は起きない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12e_() throws Exception {
		t1a.store(c1a);
		t2.store(c1b);
		ctx1.store(t1a);
		ctx2.store(t2); // でもコンテキストが違えばOK
		
		assertThat(ctx1.resolve(c1a.toReference()).getName(), is("A"));
		assertThat(ctx2.resolve(c1b.toReference()).getName(), is("B"));
	}
	
	/**
	 * 同一カラムを同じテーブル内に置くことはできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test13_同一カラムを同じテーブル内に置くことはできない() throws Exception {
		t1a.store(c1a);
		t1a.store(c1a); // 同一なので置き換え
		
		assertThat(t1a.getColumn(c1a.toReference()).getName(), is("A"));
	}
	
	/**
	 * たとえテーブルが違っていても同一カラムだったら置けない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test14_たとえテーブルが違っていても同一カラムだったら置けない() throws Exception {
		t1a.store(c1a);
		t2.store(c1a); // テーブルは違えど同一なのでexception
		
		assertThat(t1a.getColumn(c1a.toReference()).getName(), is("A"));
		assertThat(t2.getColumn(c1a.toReference()).getName(), is("A"));
	}
	
	/**
	 * removeしたカラムを同じテーブルに追加できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test15_removeしたカラムを同じテーブルに追加できる() throws Exception {
		t1a.store(c1a);
		t1a.deleteColumn(c1a.toReference());
		t1a.store(c1a); // removeしたら再addできる
	}
	
	/**
	 * もちろんremoveしたカラムを他のテーブルに追加してもよい。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test16_もちろんremoveしたカラムを他のテーブルに追加してもよい() throws Exception {
		t1a.store(c1a);
		t1a.deleteColumn(c1a.toReference());
		t2.store(c1a); // removeしたら再addできる
	}
	
	/**
	 * t1aで管理したのはc1bじゃなくてc1だけどremoveできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test17_t1aで管理したのはc1bじゃなくてc1だけどremoveできる() throws Exception {
		t1a.store(c1a);
		t1a.deleteColumn(c1b.toReference());
	}
	
	/**
	 * 管理していないカラムをremoveできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityNotFoundException.class)
	public void test18_管理していないカラムをremoveできない() throws Exception {
		t1a.deleteColumn(c1a.toReference());
	}
	
	/**
	 * c1aを管理しているのはt2じゃないので例外。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityNotFoundException.class)
	public void test19_c1aを管理しているのはt2じゃないので例外() throws Exception {
		t1a.store(c1a);
		t2.deleteColumn(c1a.toReference());
	}
	
	/**
	 * カラムをテーブルにstoreしただけでは、contextからresolveはできない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test20a() throws Exception {
		t1a.store(c1a);
		t2.store(c2);
		t3.store(c3);
		
		try {
			ctx1.resolve(TID1);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(CID1);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(TID2);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(CID2);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(TID3);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(CID3);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test20b() {
		t1a.store(c1a);
		t2.store(c2);
		t3.store(c3);
		
		ctx1.store(t1a);
		
		assertThat(ctx1.resolve(TID1), is((Entity) t1a));
		assertThat(ctx1.resolve(CID1), is((Entity) c1a));
		
		try {
			ctx1.resolve(TID2);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(CID2);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(TID3);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(CID3);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test20c() {
		t1a.store(c1a);
		t2.store(c2);
		t3.store(c3);
		
		ctx1.store(t1a);
		ctx1.deleteDbObject(t1a.toReference());
		
		try {
			ctx1.resolve(TID1);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(CID1);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(TID2);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(CID2);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(TID3);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		try {
			ctx1.resolve(CID3);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test21() throws Exception {
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDialectClassName(MockDialect.class.getName());
		ctx1.setMetadata(meta);
		assertThat(ctx1.findDialect(), is(instanceOf(MockDialect.class)));
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test31_double_add() {
		JmTable table = spy(new JmTable());
		
		ctx1.store(table);
		ctx1.store(table);
		ctx2.store(table);
		ctx2.deleteDbObject(table.toReference());
		ctx1.deleteDbObject(table.toReference());
		
		try {
			ctx1.deleteDbObject(table.toReference());
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test32_double_add() {
		JmTable table1 = new JmTable(ctx1.toUUID("a"));
		JmTable table2 = new JmTable(ctx1.toUUID("a"));
		
		ctx1.store(table1);
		ctx1.store(table1);
		ctx1.store(table2);
		ctx2.store(table1);
		ctx2.store(table2);
		ctx2.deleteDbObject(table1.toReference());
		ctx1.deleteDbObject(table2.toReference());
		
		try {
			ctx1.deleteDbObject(table1.toReference());
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		try {
			ctx2.deleteDbObject(table2.toReference());
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test34_get() {
		UUID id = UUID.randomUUID();
		EntityRef<JmTable> tableRef = new EntityRef<JmTable>(id);
		
		try {
			ctx1.resolve(id);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		try {
			ctx1.resolve(tableRef);
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
		
		JmTable table = new JmTable(id);
		ctx1.store(table);
		
		assertThat(ctx1.resolve(id), is((Entity) table));
		assertThat(ctx1.resolve(tableRef), is((Entity) table));
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test37_query() {
		JmColumn b;
		JmColumn c;
		JmColumn d;
		JmColumn e;
		
		// FORMAT-OFF
		JmTable t1 = new JmTableBuilder("ONE")
				.with(new JmColumnBuilder("A").build())
				.with(b = new JmColumnBuilder("B").build())
				.with(JmPrimaryKeyConstraint.of(b))
				.build();
		JmTable t2 = new JmTableBuilder("TWO")
				.with(c = new JmColumnBuilder("C").build())
				.with(d = new JmColumnBuilder("D").build())
				.with(JmPrimaryKeyConstraint.of(d))
				.with(JmForeignKeyConstraint.of(c, b))
				.build();
		JmTable t3 = new JmTableBuilder("THREE")
				.with(e = new JmColumnBuilder("E").build())
				.with(new JmColumnBuilder("F").build())
				.with(JmForeignKeyConstraint.of(e, d))
				.build();
		
		ctx1.store(t1);
		ctx1.store(t2);
		ctx1.store(t3);
		
		assertThat(ctx1.findSubDbObjectsNonRecursive(t1).size(), is(1));
		assertThat(ctx1.findSubDbObjectsNonRecursive(t2).size(), is(1));
		assertThat(ctx1.findSubDbObjectsNonRecursive(t3).size(), is(0));
		assertThat(ctx1.findSubDbObjectsNonRecursive(t1), hasItem((DbObject) t2));
		assertThat(ctx1.findSubDbObjectsNonRecursive(t2), hasItem((DbObject) t3));
		
		assertThat(ctx1.findSubDbObjectsRecursive(t1).size(), is(2));
		assertThat(ctx1.findSubDbObjectsRecursive(t2).size(), is(1));
		assertThat(ctx1.findSubDbObjectsRecursive(t3).size(), is(0));
		assertThat(ctx1.findSubDbObjectsRecursive(t1), hasItems((DbObject) t2, (DbObject) t3));
		assertThat(ctx1.findSubDbObjectsRecursive(t2), hasItem((DbObject) t3));
		
		assertThat(ctx1.findSuperDbObjectsNonRecursive(t1).size(), is(0));
		assertThat(ctx1.findSuperDbObjectsNonRecursive(t2).size(), is(1));
		assertThat(ctx1.findSuperDbObjectsNonRecursive(t3).size(), is(1));
		assertThat(ctx1.findSuperDbObjectsNonRecursive(t2), hasItem((DbObject) t1));
		assertThat(ctx1.findSuperDbObjectsNonRecursive(t3), hasItem((DbObject) t2));
		
		assertThat(ctx1.findSuperDbObjectsRecursive(t1).size(), is(0));
		assertThat(ctx1.findSuperDbObjectsRecursive(t2).size(), is(1));
		assertThat(ctx1.findSuperDbObjectsRecursive(t3).size(), is(2));
		assertThat(ctx1.findSuperDbObjectsRecursive(t2), hasItem((DbObject) t1));
		assertThat(ctx1.findSuperDbObjectsRecursive(t3), hasItems((DbObject) t1, (DbObject) t2));
		// FORMAT-ON
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test38_() {
		JmColumn col1 = new JmColumnBuilder().name("KEY").build();
		JmColumn col2 = new JmColumnBuilder().name("VALUE").build();
		
		JmTable table = new JmTableBuilder().name("T_PROPERTY").build();
		table.store(col1);
		table.store(col2);
		List<EntityRef<? extends JmColumn>> pk = new ArrayList<EntityRef<? extends JmColumn>>();
		pk.add(col1.toReference());
		table.store(JmPrimaryKeyConstraint.of(pk));
		ctx1.store(table);
		
		assertThat(table.getColumns().size(), is(2));
		assertThat(table.getConstraints().size(), is(1));
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test39_() {
		JmColumn pkColumn;
		// FORMAT-OFF
		JmTable table = new JmTableBuilder().name("T_PROPERTY")
				.with(pkColumn = new JmColumnBuilder().name("KEY").build())
				.with(new JmColumnBuilder().name("VALUE").build())
				.with(JmPrimaryKeyConstraint.of(pkColumn))
				.build();
		// FORMAT-ON
		ctx1.store(table);
		
		assertThat(table.getColumns().size(), is(2));
		assertThat(table.getConstraints().size(), is(1));
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test40_() {
		JmColumn pkColumn;
		JmColumn fkColumn1;
		JmColumn fkColumn2;
		JmColumn refColumn;
		// FORMAT-OFF
		JmTable dept = new JmTableBuilder().name("T_DEPT")
				.with(pkColumn = refColumn = new JmColumnBuilder().name("ID").build())
				.with(new JmColumnBuilder().name("NAME").build())
				.with(new JmColumnBuilder().name("LOC").build())
				.with( JmPrimaryKeyConstraint.of(pkColumn))
				.build();
		JmTable emp = new JmTableBuilder().name("T_EMP")
				.with(pkColumn = new JmColumnBuilder().name("ID").build())
				.with(new JmColumnBuilder().name("NAME").build())
				.with(fkColumn1 = new JmColumnBuilder().name("DEPT_ID").build())
				.with(fkColumn2 = new JmColumnBuilder().name("MGR_ID").build())
				.with( JmPrimaryKeyConstraint.of(pkColumn))
				.with(JmForeignKeyConstraint.of(fkColumn1,refColumn))
				.with(JmForeignKeyConstraint.of(fkColumn2,pkColumn))
				.build();
		// FORMAT-ON
		ctx1.store(dept);
		ctx1.store(emp);
		
		assertThat(dept.getColumns().size(), is(3));
		assertThat(dept.getConstraints().size(), is(1));
		assertThat(emp.getColumns().size(), is(4));
		assertThat(emp.getConstraints().size(), is(3));
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test41() {
		JmTable t1 = new JmTable();
		t1.setName("TBL");
		ctx1.store(t1);
		JmTable t2 = new JmTable();
		t2.setName("TBL");
		ctx1.store(t2);
		
		try {
			ctx1.getTable("XXX");
			fail();
		} catch (TableNotFoundException e) {
			// success
		}
		
		try {
			ctx1.getTable("TBL");
			fail();
		} catch (TooManyTablesFoundException e) {
			Iterable<? extends JmTable> tables = e.getTables();
			assertThat(Iterables.size(tables), is(2));
			assertThat(Iterables.contains(tables, t1), is(true));
			assertThat(Iterables.contains(tables, t2), is(true));
		}
	}
	
	/**
	 * {@link JiemamyContext#isDebug()}, {@link JiemamyContext#setDebug(boolean)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test42_() throws Exception {
		assertThat(JiemamyContext.isDebug(), is(false));
		JiemamyContext.setDebug(true);
		assertThat(JiemamyContext.isDebug(), is(true));
		JiemamyContext.setDebug(false);
		assertThat(JiemamyContext.isDebug(), is(false));
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test43_toString() {
		assertThat(ctx1, hasToString(is("JiemamyContext@" + Integer.toHexString(System.identityHashCode(ctx1)))));
		assertThat(ctx2, hasToString(is("JiemamyContext@" + Integer.toHexString(System.identityHashCode(ctx2)))));
	}
	
	@Test
	@SuppressWarnings("javadoc")
	public void test44_uuid() {
		// UUID化できるStringを与えた場合 → fromString生成
		UUID uuid1 = ctx1.toUUID("ffffffff-ffff-ffff-ffff-ffffffffffff");
		assertThat(uuid1, is(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")));
		
		// UUID化できないStringを与えた場合 → randomUUID生成
		UUID uuid2 = ctx1.toUUID("foo");
		assertThat(uuid2.toString(), is(not("foo")));
		
		UUID foo1 = ctx1.toUUID("foo");
		UUID bar1 = ctx1.toUUID("bar");
		UUID baz1 = ctx1.toUUID("baz");
		UUID nul1 = ctx1.toUUID(null);
		UUID foo2 = ctx2.toUUID("foo");
		UUID bar2 = ctx2.toUUID("bar");
		UUID baz2 = ctx2.toUUID("baz");
		UUID nul2 = ctx2.toUUID(null);
		
		// 同じname（nullを含む）には同じUUIDが対応し続けること
		assertThat(ctx1.toUUID("foo"), is(equalTo(foo1)));
		assertThat(ctx1.toUUID("bar"), is(equalTo(bar1)));
		assertThat(ctx1.toUUID("baz"), is(equalTo(baz1)));
		assertThat(ctx1.toUUID(null), is(equalTo(nul1)));
		assertThat(ctx2.toUUID("foo"), is(equalTo(foo2)));
		assertThat(ctx2.toUUID("bar"), is(equalTo(bar2)));
		assertThat(ctx2.toUUID("baz"), is(equalTo(baz2)));
		assertThat(ctx2.toUUID(null), is(equalTo(nul2)));
		
		assertThat(ctx1.toUUID("foo"), is(not(equalTo(foo2))));
		assertThat(ctx1.toUUID("bar"), is(not(equalTo(bar2))));
		assertThat(ctx1.toUUID("baz"), is(not(equalTo(baz2))));
		assertThat(ctx1.toUUID(null), is(not(equalTo(nul2))));
	}
	
	/**
	 * {@link JiemamyContext#findSerializer()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test80_() throws Exception {
		JiemamySerializer serializer = JiemamyContext.findSerializer();
		assertThat(serializer, is(instanceOf(JiemamyStaxSerializer.class)));
		
//		String backup = JiemamyContext.getSerializerName();
		try {
			JiemamyContext.setSerializerName("xxx");
			JiemamyContext.findSerializer();
			fail();
		} catch (ServiceNotFoundException e) {
			// success
		} finally {
			JiemamyContext.setSerializerName(/*backup*/JiemamyStaxSerializer.class.getName());
		}
	}
}
