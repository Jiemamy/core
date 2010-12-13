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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.model.EntityNotFoundException;
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
	
	private static final UUID TID1 = UUIDUtil.valueOfOrRandom("tid1");
	
	private static final UUID TID2 = UUIDUtil.valueOfOrRandom("tid2");
	
	private static final UUID TID3 = UUIDUtil.valueOfOrRandom("tid3");
	
	private static final UUID CID1 = UUIDUtil.valueOfOrRandom("cid1");
	
	private static final UUID CID2 = UUIDUtil.valueOfOrRandom("cid2");
	
	private static final UUID CID3 = UUIDUtil.valueOfOrRandom("cid3");
	
	private JiemamyContext ctx1;
	
	private JiemamyContext ctx2;
	
	private DefaultTableModel t1a;
	
	private DefaultTableModel t1b;
	
	private DefaultTableModel t2;
	
	private DefaultTableModel t3;
	
	private DefaultColumnModel c1a;
	
	private DefaultColumnModel c1b;
	
	private DefaultColumnModel c2;
	
	private DefaultColumnModel c3;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx1 = new JiemamyContext();
		ctx2 = new JiemamyContext();
		
		t1a = new DefaultTableModel(TID1);
		t1a.setName("A");
		t1b = new DefaultTableModel(TID1);
		t1b.setName("B");
		t2 = new DefaultTableModel(TID2);
		t3 = new DefaultTableModel(TID3);
		
		c1a = new DefaultColumnModel(CID1);
		c1a.setName("A");
		c1b = new DefaultColumnModel(CID1);
		c1b.setName("B");
		c2 = new DefaultColumnModel(CID2);
		c3 = new DefaultColumnModel(CID3);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test00_IDが違うテーブルを同じctxに追加できる() throws Exception {
		ctx1.store(t1a);
		ctx1.store(t3); // ID違うのでOK
		assertThat(((TableModel) ctx1.resolve(TID1)).getName(), is("A"));
		assertThat(((TableModel) ctx1.resolve(TID3)).getName(), is(nullValue()));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_IDが同じテーブルを同じctxに追加すると置換更新となる() throws Exception {
		ctx1.store(t1a);
		ctx1.store(t1b); // 等価なので置換
		assertThat(((TableModel) ctx1.resolve(TID1)).getName(), is("B"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_IDが同じであってもctxが違えば普通に別管理となる() throws Exception {
		ctx1.store(t1a);
		ctx2.store(t1b); // 等価だけどctxが違うので変化なし
		assertThat(((TableModel) ctx1.resolve(TID1)).getName(), is("A"));
		assertThat(((TableModel) ctx2.resolve(TID1)).getName(), is("B"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_同一テーブルを同じctx内にaddする意味はあまりない() throws Exception {
		ctx1.store(t1a);
		ctx1.store(t1a); // 同一なので置き換えるが無意味
		assertThat(((TableModel) ctx1.resolve(TID1)).getName(), is("A"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_同一テーブルを2つの異なるctxにaddしてもお互い影響しない() throws Exception {
		ctx1.store(t1a);
		ctx2.store(t1a); // ctxは違えど同一なのでexception
		assertThat(((TableModel) ctx1.resolve(TID1)).getName(), is("A"));
		assertThat(((TableModel) ctx2.resolve(TID1)).getName(), is("A"));
		t1a.setName("A2");
		assertThat(((TableModel) ctx1.resolve(TID1)).getName(), is("A"));
		assertThat(((TableModel) ctx2.resolve(TID1)).getName(), is("A"));
		ctx1.store(t1a);
		assertThat(((TableModel) ctx1.resolve(TID1)).getName(), is("A2"));
		assertThat(((TableModel) ctx2.resolve(TID1)).getName(), is("A"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_removeしたテーブルを同じctxに追加できる() throws Exception {
		ctx1.store(t1a);
		ctx1.delete(t1a.toReference());
		ctx1.store(t1a);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_もちろんremoveしたテーブルを他のctxに追加してもよい() throws Exception {
		ctx1.store(t1a);
		ctx1.delete(t1a.toReference());
		ctx2.store(t1a);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_ctx1で管理したのはt1bじゃなくてt1aだけどIDが同じなのでremoveできる() throws Exception {
		ctx1.store(t1a);
		ctx1.delete(t1b.toReference());
		try {
			ctx1.resolve(t1a.toReference());
			fail();
		} catch (EntityNotFoundException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityNotFoundException.class)
	public void test08_管理していないインスタンスをremoveできない() throws Exception {
		ctx1.delete(t1a.toReference());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityNotFoundException.class)
	public void test09_t1aを管理しているのはctx2じゃないので例外() throws Exception {
		ctx1.store(t1a);
		ctx2.delete(t1a.toReference());
	}
	
	/**
	 * TODO for daisuke
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
	 * TODO for daisuke
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
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12_IDが同じであってもテーブルが違えば置くことができる() throws Exception {
		t1a.store(c1a);
		t2.store(c1b); // 等価だけどテーブルが違うのでOK
		assertThat(t1a.getColumn(c1a.toReference()).getName(), is("A"));
		assertThat(t2.getColumn(c1b.toReference()).getName(), is("B"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12b_() throws Exception {
		t1a.store(c1a);
		t1b.store(c1b); // 等価だけどテーブルが違うのでOK
		assertThat(t1a.getColumn(c1a.toReference()).getName(), is("A"));
		assertThat(t1b.getColumn(c1b.toReference()).getName(), is("B"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12c_() throws Exception {
		t1a.store(c1a);
		t1b.store(c1b);
		ctx1.store(t1a);
		ctx2.store(t1b); // コンテキストが違うからOK
		
		assertThat(ctx1.resolve(c1a.toReference()).getName(), is("A"));
		assertThat(ctx2.resolve(c1b.toReference()).getName(), is("B"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test12d_() throws Exception {
		t1a.store(c1a);
		t2.store(c1b);
		ctx1.store(t1a);
		ctx1.store(t2); // 既に管理済みのt1aが持つc1aと同じIDを持つc1bをt2が持ってるからダメ
	}
	
	/**
	 * TODO for daisuke
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
	 * TODO for daisuke
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
	 * TODO for daisuke
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
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test15_removeしたカラムを同じテーブルに追加できる() throws Exception {
		t1a.store(c1a);
		t1a.delete(c1a.toReference());
		t1a.store(c1a); // removeしたら再addできる
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test16_もちろんremoveしたカラムを他のテーブルに追加してもよい() throws Exception {
		t1a.store(c1a);
		t1a.delete(c1a.toReference());
		t2.store(c1a); // removeしたら再addできる
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test17_t1aで管理したのはc1bじゃなくてc1だけどremoveできる() throws Exception {
		t1a.store(c1a);
		t1a.delete(c1b.toReference());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityNotFoundException.class)
	public void test18_管理していないカラムをremoveできない() throws Exception {
		t1a.delete(c1a.toReference());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test(expected = EntityNotFoundException.class)
	public void test19_c1aを管理しているのはt2じゃないので例外() throws Exception {
		t1a.store(c1a);
		t2.delete(c1a.toReference());
	}
	
	/**
	 * TODO for daisuke
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
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test20b() throws Exception {
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
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test20c() throws Exception {
		t1a.store(c1a);
		t2.store(c2);
		t3.store(c3);
		
		ctx1.store(t1a);
		ctx1.delete(t1a.toReference());
		
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
}
