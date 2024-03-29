/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/02/04
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
package org.jiemamy.model.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;

/**
 * {@link JmDomain}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmDomainTest {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(JmDomainTest.class);
	
	
	/**
	 * CORE-203実証テスト
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_core203() throws Exception {
		JiemamyContext context = new JiemamyContext();
		
		JmDomain domain = new JmDomain();
		domain.setName("NAME");
		SimpleDataType t = SimpleDataType.of(RawTypeCategory.VARCHAR);
		t.putParam(TypeParameterKey.SIZE, 32);
		domain.setDataType(t);
		context.add(domain);
		
		// FORMAT-OFF
		JmTable table = new JmTableBuilder("HOGE")
			.with(new JmColumnBuilder("FOO").type(new SimpleDataType(domain.asType(context))).build())
			.build();
		// FORMAT-ON
		context.add(table);
		
		DataType type = table.getColumn("FOO").getDataType();
		assertThat(type.getRawTypeDescriptor().getCategory(), is(RawTypeCategory.VARCHAR));
		assertThat(type.getParam(TypeParameterKey.SIZE), is(32));
		
	}
	
	/**
	 * {@link JmDomain}を定義し、それをデータ型として使って見る。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_() throws Exception {
		JiemamyContext context = new JiemamyContext();
		
		SimpleDataType integer = new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.INTEGER));
		SimpleDataType varchar32 = new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.VARCHAR));
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		JmDomain domain = new JmDomain();
		domain.setName("NAME");
		domain.setDataType(varchar32);
		context.add(domain);
		
		SimpleDataType domainType = new SimpleDataType(domain.asType(context));
		
		// FORMAT-OFF
		JmTable table = new JmTableBuilder("FOO")
				.with(new JmColumnBuilder("ID").type(integer).build())
				.with(new JmColumnBuilder("FOONAME").type(domainType).build()).build();
		// FORMAT-ON
		table.add(JmPrimaryKeyConstraint.of(table.getColumn("ID")));
		
		context.add(table);
		
		// UNDONE 続きをなんか書かないと。
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		JiemamyContext.findSerializer().serialize(context, baos);
//		logger.info(baos.toString());
	}
	
}
