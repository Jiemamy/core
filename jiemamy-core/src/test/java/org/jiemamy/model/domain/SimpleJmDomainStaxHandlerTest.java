/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/03/01
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
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.CharEncoding;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.constraint.SimpleJmCheckConstraint;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableBuilder;
import org.jiemamy.serializer.stax.JiemamyStaxSerializer;

/**
 * {@link SimpleJmDomainStaxHandler}のテストクラス。
 * 
 * @version $Id$
 * @author yamkazu
 */
public class SimpleJmDomainStaxHandlerTest {
	
//	private static Logger logger = LoggerFactory.getLogger(SimpleJmDomainStaxHandlerTest.class);
	
	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	/**
	 * シリアライズしてデシリアライズするテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_簡単なDomainを1つシリアライズして内容を確認する() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SimpleDataType varchar32 = new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.VARCHAR));
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		JiemamyContext context = new JiemamyContext();
		
		// ドメインの作成
		SimpleJmDomain domain = new SimpleJmDomain();
		domain.setName("NAME");
		domain.setLogicalName("名前");
		domain.setDescription("名前のドメイン");
		domain.setDataType(varchar32);
		domain.setNotNull(true);
		domain.addCheckConstraint(SimpleJmCheckConstraint.of("check", "name"));
		domain.putParam(TypeParameterKey.SIZE, 10);
		context.store(domain);
		
		// ドメインモデルを持つカラムとそのテーブルを作成
		SimpleDataType hasDomainDataType = new SimpleDataType(domain.asType(context));
		// FORMAT-OFF
		JmTable t1 = new JmTableBuilder().name("ONE")
				.with(new JmColumnBuilder().name("A").type(hasDomainDataType).build())
				.build();
		// FORMAT-ON
		context.store(t1);
		
		JiemamyStaxSerializer serializer = new JiemamyStaxSerializer();
		serializer.serialize(context, baos);
		String first = baos.toString(CharEncoding.UTF_8);
		
		// そのXMLをデシリアライズしてみる
		ByteArrayInputStream bais = new ByteArrayInputStream(first.getBytes(CharEncoding.UTF_8));
		JiemamyContext deserialized = serializer.deserialize(bais);
		assertThat(deserialized, is(notNullValue()));
		
		// デシリアライズされた新しいcontextを再びシリアライズしてみる(2)
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		serializer.serialize(deserialized, baos2);
		String second = baos2.toString(CharEncoding.UTF_8);
		
//		logger.info("actual={}", first);
//		logger.info("expect={}", second);
		
		assertThat(first, is(second));
	}
	
}
