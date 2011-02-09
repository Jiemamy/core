/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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

import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.JmColumnBuilder;
import org.jiemamy.model.constraint.SimpleJmPrimaryKeyConstraint;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.table.JmTableBuilder;
import org.jiemamy.model.table.SimpleJmTable;

/**
 * {@link SimpleJmDomain}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmDomainTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		JiemamyContext context = new JiemamyContext();
		
		SimpleDataType integer = new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.INTEGER));
		SimpleDataType varchar32 = new SimpleDataType(new SimpleRawTypeDescriptor(RawTypeCategory.VARCHAR));
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		SimpleJmDomain domain = new SimpleJmDomain();
		domain.setName("NAME");
		domain.setDataType(varchar32);
		context.store(domain);
		
		SimpleDataType domainType = new SimpleDataType(domain.asType());
		
		// FORMAT-OFF
		SimpleJmTable table = new JmTableBuilder("FOO")
				.with(new JmColumnBuilder("ID").type(integer).build())
				.with(new JmColumnBuilder("FOONAME").type(domainType).build()).build();
		// FORMAT-ON
		table.store(SimpleJmPrimaryKeyConstraint.of(table.getColumn("ID")));
		
		context.store(table);
		
		// UNDONE 続きをなんか書かないと。
//		JiemamyContext.findSerializer().serialize(context, System.out);
	}
	
}
