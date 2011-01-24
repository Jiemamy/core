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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.SqlFacet;
import org.jiemamy.composer.exporter.DefaultSqlExportConfig;
import org.jiemamy.model.column.Column;
import org.jiemamy.model.column.DefaultColumnModel;
import org.jiemamy.model.constraint.DefaultNotNullConstraintModel;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.ForeignKey;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultTypeReference;
import org.jiemamy.model.datatype.DefaultTypeVariant;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.datatype.TypeReference;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.Table;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultSqlEmitterTest {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultSqlEmitterTest.class);
	
	private static final TypeReference INTEGER = new DefaultTypeReference(DataTypeCategory.INTEGER);
	
	private static final TypeReference VARCHAR = new DefaultTypeReference(DataTypeCategory.VARCHAR);
	
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
		emitter = new DefaultSqlEmitter();
		
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
		DefaultTypeVariant varchar32 = new DefaultTypeVariant(VARCHAR);
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		// FORMAT-OFF
		DefaultTableModel table = new Table("T_FOO")
				.with(new Column("HOGE").whoseTypeIs(new DefaultTypeVariant(INTEGER)).build())
				.with(new Column("FUGA").whoseTypeIs(varchar32).build())
				.with(new Column("PIYO").whoseTypeIs(new DefaultTypeVariant(INTEGER)).build())
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
				is("CREATE TABLE T_FOO(HOGE INTEGER, FUGA VARCHAR(32), PIYO INTEGER);"));
	}
	
	/**
	 * 制約付きテーブルを2つemitして確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_制約付きテーブルを2つemitして確認() throws Exception {
		DefaultTypeVariant varchar32 = new DefaultTypeVariant(VARCHAR);
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		DefaultTypeVariant varchar16 = new DefaultTypeVariant(VARCHAR);
		varchar16.putParam(TypeParameterKey.SIZE, 16);
		
		// FORMAT-OFF
		DefaultColumnModel deptId = new Column("ID").whoseTypeIs(new DefaultTypeVariant(INTEGER)).build();
		DefaultColumnModel deptName = new Column("NAME").whoseTypeIs(varchar32).build();
		DefaultTableModel dept = new Table("DEPT")
				.with(deptId)
				.with(deptName)
				.with(new Column("LOC").whoseTypeIs(varchar16).build())
				.with(DefaultPrimaryKeyConstraintModel.of(deptId))
				.with(DefaultNotNullConstraintModel.of(deptName))
				.build();
		
		DefaultColumnModel empId = new Column("ID").whoseTypeIs(new DefaultTypeVariant(INTEGER)).build();
		DefaultColumnModel empName = new Column("NAME").whoseTypeIs(varchar32).build();
		DefaultColumnModel empDeptId = new Column("DEPT_ID").whoseTypeIs(new DefaultTypeVariant(INTEGER)).build();
		DefaultTableModel emp = new Table("EMP")
				.with(empId)
				.with(empName)
				.with(empDeptId)
				.with(DefaultPrimaryKeyConstraintModel.of(empId))
				.with(DefaultNotNullConstraintModel.of(empName))
				.with(new ForeignKey().referencing(empDeptId, deptId).build())
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
		assertThat(statements.get(3).toString(), is("CREATE TABLE EMP"
				+ "(ID INTEGER, NAME VARCHAR(32)NOT NULL, DEPT_ID INTEGER, "
				+ "PRIMARY KEY(ID), "
				+ "FOREIGN KEY(DEPT_ID)REFERENCES DEPT(ID));"));
		// FORMAT-ON
	}
}
