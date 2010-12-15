/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/02
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
package org.jiemamy.model.dbo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.StringWriter;
import java.util.Collection;
import java.util.UUID;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;

import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.attribute.Column;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.DefaultColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.KeyConstraintModel;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultTypeVariant;
import org.jiemamy.utils.UUIDUtil;

/**
 * {@link DefaultTableModel}のテストクラス。
 * 
 * @since 0.3
 * @version $Id$
 * @author daisuke
 */
public class DefaultTableModelTest {
	
	private JiemamyContext ctx;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		ctx = new JiemamyContext();
	}
	
	/**
	 * オブジェクト生成テスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_create() throws Exception {
		try {
			@SuppressWarnings("unused")
			DefaultTableModel model = new DefaultTableModel(null);
		} catch (IllegalArgumentException e) {
			// success
		}
		
		UUID id = UUID.randomUUID();
		DefaultTableModel t = new DefaultTableModel(id);
		t.setName("FOO");
		
		assertThat(t.getId(), is(id));
		assertThat(t.getName(), is("FOO"));
		
		t.setName("BAR");
		
		assertThat(t.getId(), is(id));
		assertThat(t.getName(), is("BAR"));
	}
	
	/**
	 * {@link DefaultTableModel#equals(Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_equals() throws Exception {
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		DefaultTableModel live1 = new DefaultTableModel(uuid1);
		DefaultTableModel live2 = new DefaultTableModel(uuid1);
		DefaultTableModel live3 = new DefaultTableModel(uuid2);
		
		assertThat(live1.equals(live1), is(true));
		assertThat(live1.equals(live2), is(true));
		assertThat(live1.equals(live3), is(false));
		
		assertThat(live2.equals(live1), is(true));
		assertThat(live2.equals(live2), is(true));
		assertThat(live2.equals(live3), is(false));
		
		assertThat(live3.equals(live1), is(false));
		assertThat(live3.equals(live2), is(false));
		assertThat(live3.equals(live3), is(true));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_findDeclaringTable() throws Exception {
		ColumnModel a;
		ColumnModel b;
		ColumnModel c;
		ColumnModel d;
		
		// FORMAT-OFF
		TableModel t1 = new Table().whoseNameIs("ONE")
				.with(a = new Column().whoseNameIs("A").build())
				.with(b = new Column().whoseNameIs("B").build())
				.build();
		TableModel t2 = new Table().whoseNameIs("TWO")
				.with(c = new Column().whoseNameIs("C").build())
				.with(d = new Column().whoseNameIs("D").build())
				.build();
		// FORMAT-ON
		
		ctx.store(t1);
		ctx.store(t2);
		
		Collection<TableModel> tables = ctx.getTables();
		
		assertThat(DefaultTableModel.findDeclaringTable(tables, a), is(t1));
		assertThat(DefaultTableModel.findDeclaringTable(tables, b), is(t1));
		assertThat(DefaultTableModel.findDeclaringTable(tables, c), is(t2));
		assertThat(DefaultTableModel.findDeclaringTable(tables, d), is(t2));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_column_lifecycle3() throws Exception {
		DefaultTableModel table1 = new Table().build();
		DefaultTableModel table2 = new Table().build();
		
		DefaultColumnModel column = new Column().build();
		
		table1.store(column);
		table1.delete(column.toReference());
		table2.store(column);
		table2.delete(column.toReference());
		
		table1.store(column);
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test10_getColumn() throws Exception {
		DefaultTableModel table = new Table("HOGE").build();
		ColumnModel foo = new Column("FOO").build();
		ColumnModel foo2 = new Column("FOO").build();
		ColumnModel bar = new Column("BAR").build();
		
		assertThat(table.getColumns().size(), is(0));
		
		table.store(foo);
		table.store(bar);
		assertThat(table.getColumns().size(), is(2));
		
		ctx.store(table);
		
		assertThat(table.getColumns().size(), is(2));
		assertThat(table.getColumn("FOO"), is(foo));
		assertThat(table.getColumn("BAR"), is(bar));
		
		table.delete(bar.toReference());
		
		assertThat(table.getColumns().size(), is(1));
		assertThat(table.getColumn("FOO"), is(foo));
		
		try {
			table.getColumn("BAR");
			fail();
		} catch (ColumnNotFoundException e) {
			// success
		}
		
		table.store(foo2);
		try {
			table.getColumn("FOO");
			fail();
		} catch (TooManyColumnsFoundException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11() throws Exception {
		ColumnModel b;
		ColumnModel c;
		ColumnModel d;
		ColumnModel e;
		ForeignKeyConstraintModel fk12;
		ForeignKeyConstraintModel fk23;
		KeyConstraintModel pk1;
		KeyConstraintModel pk2;
		
		// FORMAT-OFF
		TableModel t1 = new Table("ONE")
				.with(new Column("A").build())
				.with(b = new Column("B").build())
				.with(pk1 = DefaultPrimaryKeyConstraintModel.of(b))
				.build();
		TableModel t2 = new Table("TWO")
				.with(c = new Column("C").build())
				.with(d = new Column("D").build())
				.with(pk2 = DefaultPrimaryKeyConstraintModel.of(d))
				.with(fk12 = DefaultForeignKeyConstraintModel.of(c, b))
				.build();
		TableModel t3 = new Table("THREE")
				.with(e = new Column("E").build())
				.with(new Column("F").build())
				.with(fk23 = DefaultForeignKeyConstraintModel.of(e, d))
				.build();
		
		ctx.store(t1);
		ctx.store(t2);
		ctx.store(t3);
		
		assertThat(DefaultTableModel.findReferencedDatabaseObject(ctx.getDatabaseObjects(), fk12), is((DatabaseObjectModel) t1));
		assertThat(DefaultTableModel.findReferencedDatabaseObject(ctx.getDatabaseObjects(), fk23), is((DatabaseObjectModel) t2));
		assertThat(DefaultTableModel.findReferencedKeyConstraint(ctx.getDatabaseObjects(), fk12), is(pk1));
		assertThat(DefaultTableModel.findReferencedKeyConstraint(ctx.getDatabaseObjects(), fk23), is(pk2));
		// FORMAT-ON
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12_clone() throws Exception {
		DefaultTableModel table = new DefaultTableModel(UUIDUtil.valueOfOrRandom("a"));
		table.setName("name1");
		
		DefaultTableModel clone = table.clone();
		
		assertThat(clone.getName(), is("name1"));
		assertThat(clone.getColumns().size(), is(0));
		
		table.setName("name2");
		assertThat(table.getName(), is("name2"));
		assertThat(clone.getName(), is("name1"));
		
		clone.setName("name3");
		assertThat(table.getName(), is("name2"));
		assertThat(clone.getName(), is("name3"));
		
		clone.store(new DefaultColumnModel(UUIDUtil.valueOfOrRandom("b")));
		assertThat(table.getColumns().size(), is(0));
		assertThat(clone.getColumns().size(), is(1));
		
		table.store(new DefaultColumnModel(UUIDUtil.valueOfOrRandom("c")));
		assertThat(table.getColumns().size(), is(1));
		assertThat(clone.getColumns().size(), is(1));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test99() throws Exception {
		DefaultTableModel table = new DefaultTableModel(UUIDUtil.valueOfOrRandom("tab1"));
		table.setName("name1");
		table.setLogicalName("logicalname1");
		table.setDescription("description1");
		
		DefaultColumnModel col1 = new DefaultColumnModel(UUIDUtil.valueOfOrRandom("col11"));
		col1.setName("FOO");
		col1.setDataType(DefaultTypeVariant.of(DataTypeCategory.VARCHAR));
		col1.setDefaultValue("default");
		table.store(col1);
		
		DefaultColumnModel col2 = new DefaultColumnModel(UUIDUtil.valueOfOrRandom("col12"));
		col2.setName("BAR");
		col2.setDataType(DefaultTypeVariant.of(DataTypeCategory.NUMERIC));
		col2.setDefaultValue("3.2");
		table.store(col2);
		
		ctx.store(table);
		
		XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
		StringWriter stringWriter = new StringWriter();
		XMLEventWriter writer = outFactory.createXMLEventWriter(stringWriter);
		writer = new IndentingXMLEventWriter(writer);
		table.getWriter(ctx).writeTo(writer);
		writer.flush();
		
		System.out.println(stringWriter.toString());
		
		writer.close();
	}
}
