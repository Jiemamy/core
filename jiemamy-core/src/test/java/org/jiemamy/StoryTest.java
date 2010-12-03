/*
 * Created on 2010/11/28
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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.model.attribute.Column;
import org.jiemamy.model.attribute.ColumnModel;
import org.jiemamy.model.attribute.constraint.DefaultPrimaryKeyConstraintModelBuilder;
import org.jiemamy.model.dbo.Table;
import org.jiemamy.model.dbo.TableModel;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class StoryTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void story1() throws Exception {
		JiemamyContext ctx = new JiemamyContext();
		JiemamyCore core = ctx.getCore();
		
		// FORMAT-OFF
		ColumnModel pk;
		TableModel dept = new Table("T_DEPT").with(
			pk = new Column("ID").whoseTypeIs(Dummy.TYPE).build(),
			new Column("NAME").whoseTypeIs(Dummy.TYPE).build(),
			new Column("LOC").whoseTypeIs(Dummy.TYPE).build()
		).with(new DefaultPrimaryKeyConstraintModelBuilder().addKeyColumn(pk).build()).build();
		
		core.add(dept);
		
		//FORMAT-ON
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void story2() throws Exception {
		JiemamyContext ctx1 = new JiemamyContext();
		JiemamyContext ctx2 = new JiemamyContext();
		
		TableModel table = mock(TableModel.class);
		ctx1.getCore().add(table);
		try {
			ctx2.getCore().add(table);
			fail();
		} catch (EntityLifecycleException e) {
			// success
		}
	}
}
