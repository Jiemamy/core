/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/24
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
package org.jiemamy.dialect;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.SqlFacet;
import org.jiemamy.composer.exporter.DefaultSqlExportConfig;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.Column;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.column.DefaultColumnModel;
import org.jiemamy.model.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultNotNullConstraintModel;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.dataset.DefaultDataSetModel;
import org.jiemamy.model.dataset.DefaultRecordModel;
import org.jiemamy.model.dataset.RecordModel;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultDataType;
import org.jiemamy.model.datatype.DefaultTypeReference;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.datatype.TypeReference;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.Table;
import org.jiemamy.model.view.DefaultViewModel;
import org.jiemamy.script.ScriptString;

/**
 * {@link DefaultSqlEmitter}のテスト。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultSqlEmitterTest {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultSqlEmitterTest.class);
	
	private static final TypeReference INTEGER = new DefaultTypeReference(DataTypeCategory.INTEGER);
	
	private static final TypeReference VARCHAR = new DefaultTypeReference(DataTypeCategory.VARCHAR);
	
	private static final TypeReference TIMESTAMP = new DefaultTypeReference(DataTypeCategory.TIMESTAMP);
	
	private DefaultSqlEmitter emitter;
	
	private DefaultSqlExportConfig config;
	
	private JiemamyContext context;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		emitter = new DefaultSqlEmitter(new GenericDialect());
		
		config = new DefaultSqlExportConfig();
		config.setDataSetIndex(-1);
		config.setEmitCreateSchema(true);
		config.setEmitDropStatements(true);
		
		context = new JiemamyContext(SqlFacet.PROVIDER);
	}
	
	/**
	 * 空のcontextをemitしても文は生成されない。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_空のcontextをemitしても文は生成されない() throws Exception {
		List<SqlStatement> statements = emitter.emit(context, config);
		assertThat(statements.size(), is(0));
	}
	
	/**
	 * 単純なテーブルを1つemitして確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_単純なテーブルを1つemitして確認() throws Exception {
		DefaultDataType varchar32 = new DefaultDataType(VARCHAR);
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		// FORMAT-OFF
		DefaultTableModel table = new Table("T_FOO")
				.with(new Column("HOGE").type(new DefaultDataType(INTEGER)).build())
				.with(new Column("FUGA").type(varchar32).build())
				.with(new Column("PIYO").type(new DefaultDataType(TIMESTAMP)).build())
				.build();
		// FORMAT-ON
		context.store(table);
		
		List<SqlStatement> statements = emitter.emit(context, config);
		for (SqlStatement statement : statements) {
			logger.info(statement.toString());
		}
		assertThat(statements.size(), is(2));
		assertThat(statements.get(0).toString(), is("DROP TABLE T_FOO;"));
		assertThat(statements.get(1).toString(),
				is("CREATE TABLE T_FOO(HOGE INTEGER, FUGA VARCHAR(32), PIYO TIMESTAMP);"));
	}
	
	/**
	 * 単純なテーブルを1つemitして確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_単純なテーブルを1つemitして確認() throws Exception {
		DefaultDataType varchar32 = new DefaultDataType(VARCHAR);
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		// FORMAT-OFF
		DefaultTableModel table = new Table("T_FOO")
				.with(new Column("HOGE").type(new DefaultDataType(INTEGER)).build())
				.with(new Column("FUGA").type(varchar32).build())
				.with(new Column("PIYO").type(new DefaultDataType(TIMESTAMP)).build())
				.build();
		// FORMAT-ON
		context.store(table);
		
		config.setEmitDropStatements(false);
		
		List<SqlStatement> statements = emitter.emit(context, config);
		for (SqlStatement statement : statements) {
			logger.info(statement.toString());
		}
		assertThat(statements.size(), is(1));
		assertThat(statements.get(0).toString(),
				is("CREATE TABLE T_FOO(HOGE INTEGER, FUGA VARCHAR(32), PIYO TIMESTAMP);"));
	}
	
	/**
	 * 制約付きテーブルを2つemitして確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_制約付きテーブルを2つemitして確認() throws Exception {
		DefaultDataType varchar32 = new DefaultDataType(VARCHAR);
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		DefaultDataType varchar16 = new DefaultDataType(VARCHAR);
		varchar16.putParam(TypeParameterKey.SIZE, 16);
		
		// FORMAT-OFF
		DefaultColumnModel deptId = new Column("ID").type(new DefaultDataType(INTEGER)).build();
		DefaultColumnModel deptName = new Column("NAME").type(varchar32).build();
		DefaultTableModel dept = new Table("DEPT")
				.with(deptId)
				.with(deptName)
				.with(new Column("LOC").type(varchar16).build())
				.with(DefaultPrimaryKeyConstraintModel.of(deptId))
				.with(DefaultNotNullConstraintModel.of(deptName))
				.build();
		
		DefaultColumnModel empId = new Column("ID").type(new DefaultDataType(INTEGER)).build();
		DefaultColumnModel empName = new Column("NAME").type(varchar32).build();
		DefaultColumnModel empDeptId = new Column("DEPT_ID").type(new DefaultDataType(INTEGER)).build();
		DefaultColumnModel empMgrId = new Column("MGR_ID").type(new DefaultDataType(INTEGER)).build();
		DefaultTableModel emp = new Table("EMP")
				.with(empId)
				.with(empName)
				.with(empDeptId)
				.with(empMgrId)
				.with(DefaultPrimaryKeyConstraintModel.of(empId))
				.with(DefaultNotNullConstraintModel.of(empName))
				.with(DefaultForeignKeyConstraintModel.of(empDeptId, deptId))
				.with(DefaultForeignKeyConstraintModel.of(empMgrId, empId))
				.build();
		// FORMAT-ON
		context.store(dept);
		context.store(emp);
		
		List<SqlStatement> statements = emitter.emit(context, config);
		for (SqlStatement statement : statements) {
			logger.info(statement.toString());
		}
		assertThat(statements.size(), is(4));
		
		// FORMAT-OFF
		assertThat(statements.get(0).toString(), is("DROP TABLE EMP;"));
		assertThat(statements.get(1).toString(), is("DROP TABLE DEPT;"));
		assertThat(statements.get(2).toString(), is("CREATE TABLE DEPT"
				+ "(ID INTEGER, NAME VARCHAR(32)NOT NULL, LOC VARCHAR(16), "
				+ "PRIMARY KEY(ID));"));
		
		// statement:3 は、FKのIDに依存してFKの出力順序が変わるので、以下のどちらでもOK
		Collection<Matcher<? extends String>> m = Lists.newArrayListWithCapacity(2);
		m.add(equalTo("CREATE TABLE EMP"
				+ "(ID INTEGER, NAME VARCHAR(32)NOT NULL, DEPT_ID INTEGER, MGR_ID INTEGER, "
				+ "PRIMARY KEY(ID), "
				+ "FOREIGN KEY(MGR_ID)REFERENCES EMP(ID), "
				+ "FOREIGN KEY(DEPT_ID)REFERENCES DEPT(ID)"
				+ ");"));
		m.add(equalTo("CREATE TABLE EMP"
				+ "(ID INTEGER, NAME VARCHAR(32)NOT NULL, DEPT_ID INTEGER, MGR_ID INTEGER, "
				+ "PRIMARY KEY(ID), "
				+ "FOREIGN KEY(DEPT_ID)REFERENCES DEPT(ID), "
				+ "FOREIGN KEY(MGR_ID)REFERENCES EMP(ID)"
				+ ");"));
		assertThat(statements.get(3).toString(), is(anyOf(m)));
		// FORMAT-ON
	}
	
	/**
	 * テーブルとビューemitして確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_テーブルとビューemitして確認() throws Exception {
		DefaultDataType varchar32 = new DefaultDataType(VARCHAR);
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		// FORMAT-OFF
		DefaultTableModel table = new Table("T_FOO")
				.with(new Column("HOGE").type(new DefaultDataType(INTEGER)).build())
				.with(new Column("FUGA").type(varchar32).build())
				.with(new Column("PIYO").type(new DefaultDataType(TIMESTAMP)).build())
				.build();
		// FORMAT-ON
		context.store(table);
		
		DefaultViewModel view = new DefaultViewModel(UUID.randomUUID());
		view.setName("V_BAR");
		view.setDefinition("SELECT * FROM T_FOO WHERE HOGE > 0");
		context.store(view);
		
		List<SqlStatement> statements = emitter.emit(context, config);
		for (SqlStatement statement : statements) {
			logger.info(statement.toString());
		}
		
		assertThat(statements.size(), is(4));
		assertThat(statements.get(0).toString(), is("DROP VIEW V_BAR;"));
		assertThat(statements.get(1).toString(), is("DROP TABLE T_FOO;"));
		assertThat(statements.get(2).toString(),
				is("CREATE TABLE T_FOO(HOGE INTEGER, FUGA VARCHAR(32), PIYO TIMESTAMP);"));
		assertThat(statements.get(3).toString(), is("CREATE VIEW V_BAR AS SELECT * FROM T_FOO WHERE HOGE > 0;"));
	}
	
	/**
	 * DataSetをemitして確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_DataSetをemitして確認() throws Exception {
		DefaultDataType varchar32 = new DefaultDataType(VARCHAR);
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		// FORMAT-OFF
		ColumnModel colFoo = new Column("FOO").type(new DefaultDataType(INTEGER)).build();
		ColumnModel colBar = new Column("BAR").type(varchar32).build();
		ColumnModel colBaz = new Column("BAZ").type(new DefaultDataType(TIMESTAMP)).build();
		DefaultTableModel table = new Table("T_HOGE")
				.with(colFoo)
				.with(colBar)
				.with(colBaz)
				.with(DefaultPrimaryKeyConstraintModel.of(colFoo))
				.build();
		// FORMAT-ON
		context.store(table);
		
		DefaultDataSetModel dataSet1 = new DefaultDataSetModel(UUID.randomUUID());
		dataSet1.setName("type-1");
		List<RecordModel> records1 = Lists.newArrayList();
		Map<EntityRef<? extends ColumnModel>, ScriptString> values1 = Maps.newHashMap();
		values1.put(colFoo.toReference(), new ScriptString("1"));
		values1.put(colBar.toReference(), new ScriptString("one"));
		values1.put(colBaz.toReference(), new ScriptString("2011-01-25 09:22:01"));
		records1.add(new DefaultRecordModel(values1));
		values1.put(colFoo.toReference(), new ScriptString("2"));
		values1.put(colBar.toReference(), new ScriptString("two"));
		values1.put(colBaz.toReference(), new ScriptString("2011-01-25 09:34:05"));
		records1.add(new DefaultRecordModel(values1));
		dataSet1.putRecord(table.toReference(), records1);
		context.store(dataSet1);
		
		DefaultDataSetModel dataSet2 = new DefaultDataSetModel(UUID.randomUUID());
		dataSet2.setName("type-2");
		List<RecordModel> records2 = Lists.newArrayList();
		Map<EntityRef<? extends ColumnModel>, ScriptString> values2 = Maps.newHashMap();
		values2.put(colFoo.toReference(), new ScriptString("3"));
		values2.put(colBar.toReference(), new ScriptString("three"));
		values2.put(colBaz.toReference(), new ScriptString("2011-02-17 09:34:40"));
		records2.add(new DefaultRecordModel(values2));
		values2.put(colFoo.toReference(), new ScriptString("4"));
		values2.put(colBar.toReference(), new ScriptString("four"));
		values2.put(colBaz.toReference(), new ScriptString("2011-02-17 10:00:00"));
		records2.add(new DefaultRecordModel(values2));
		values2.put(colFoo.toReference(), new ScriptString("5"));
		values2.put(colBar.toReference(), new ScriptString("five"));
		values2.put(colBaz.toReference(), new ScriptString("2011-02-17 10:55:59"));
		records2.add(new DefaultRecordModel(values2));
		dataSet2.putRecord(table.toReference(), records2);
		context.store(dataSet2);
		
		config.setEmitDropStatements(false);
		config.setDataSetIndex(0);
		
		List<SqlStatement> statements1 = emitter.emit(context, config);
		for (SqlStatement statement : statements1) {
			logger.info(statement.toString());
		}
		
		assertThat(statements1.size(), is(5));
		assertThat(statements1.get(0).toString(),
				is("CREATE TABLE T_HOGE(FOO INTEGER, BAR VARCHAR(32), BAZ TIMESTAMP, PRIMARY KEY(FOO));"));
		assertThat(statements1.get(1).toString(), is("BEGIN;"));
		assertThat(statements1.get(2).toString(),
				is("INSERT INTO T_HOGE(FOO, BAR, BAZ)VALUES(1, 'one', TIMESTAMP '2011-01-25 09:22:01');"));
		assertThat(statements1.get(3).toString(),
				is("INSERT INTO T_HOGE(FOO, BAR, BAZ)VALUES(2, 'two', TIMESTAMP '2011-01-25 09:34:05');"));
		assertThat(statements1.get(4).toString(), is("COMMIT;"));
		
		config.setEmitDropStatements(true);
		config.setDataSetIndex(1);
		
		List<SqlStatement> statements2 = emitter.emit(context, config);
		for (SqlStatement statement : statements2) {
			logger.info(statement.toString());
		}
		
		assertThat(statements2.size(), is(7));
		assertThat(statements2.get(0).toString(), is("DROP TABLE T_HOGE;"));
		assertThat(statements2.get(1).toString(),
				is("CREATE TABLE T_HOGE(FOO INTEGER, BAR VARCHAR(32), BAZ TIMESTAMP, PRIMARY KEY(FOO));"));
		assertThat(statements2.get(2).toString(), is("BEGIN;"));
		assertThat(statements2.get(3).toString(),
				is("INSERT INTO T_HOGE(FOO, BAR, BAZ)VALUES(3, 'three', TIMESTAMP '2011-02-17 09:34:40');"));
		assertThat(statements2.get(4).toString(),
				is("INSERT INTO T_HOGE(FOO, BAR, BAZ)VALUES(4, 'four', TIMESTAMP '2011-02-17 10:00:00');"));
		assertThat(statements2.get(5).toString(),
				is("INSERT INTO T_HOGE(FOO, BAR, BAZ)VALUES(5, 'five', TIMESTAMP '2011-02-17 10:55:59');"));
		assertThat(statements2.get(6).toString(), is("COMMIT;"));
	}
}
