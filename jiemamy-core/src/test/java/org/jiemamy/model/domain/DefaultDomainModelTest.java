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

import java.util.UUID;

import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.column.Column;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultDataType;
import org.jiemamy.model.datatype.DefaultTypeReference;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.Table;

/**
 * {@link DefaultDomainModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultDomainModelTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		JiemamyContext context = new JiemamyContext();
		
		DefaultDataType integer = new DefaultDataType(new DefaultTypeReference(DataTypeCategory.INTEGER));
		DefaultDataType varchar32 = new DefaultDataType(new DefaultTypeReference(DataTypeCategory.VARCHAR));
		varchar32.putParam(TypeParameterKey.SIZE, 32);
		
		DefaultDomainModel domain = new DefaultDomainModel(UUID.randomUUID());
		domain.setName("NAME");
		domain.setDataType(varchar32);
		context.store(domain);
		
		DefaultDataType domainType = new DefaultDataType(domain.asType());
		
		// FORMAT-OFF
		DefaultTableModel table = new Table("FOO")
				.with(new Column("ID").type(integer).build())
				.with(new Column("FOONAME").type(domainType).build()).build();
		// FORMAT-ON
		table.store(DefaultPrimaryKeyConstraintModel.of(table.getColumn("ID")));
		
		context.store(table);
		
		// UNDONE 続きをなんか書かないと。
//		JiemamyContext.findSerializer().serialize(context, System.out);
	}
	
}
