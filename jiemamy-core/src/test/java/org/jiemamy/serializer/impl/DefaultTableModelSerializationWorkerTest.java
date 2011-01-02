/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/01
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
package org.jiemamy.serializer.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.dbo.DefaultTableModel;
import org.jiemamy.serializer.SerializationDirector;
import org.jiemamy.serializer.SerializationWorker;

/**
 * {@link DefaultTableModelSerializationWorker}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultTableModelSerializationWorkerTest extends SerializationWorkerTest<DefaultTableModel> {
	
	private static final UUID ID = UUID.randomUUID();
	

	@Override
	protected void assertDeserialized(DefaultTableModel deserialized) {
		assertThat(deserialized.getId(), is(ID));
		assertThat(deserialized.getName(), is("JIEMAMY"));
		assertThat(deserialized.getLogicalName(), is("地豆テスト"));
		assertThat(deserialized.getDescription(), is(""));
	}
	
	@Override
	protected void assertSerialized(String serialized) {
		// TODO
	}
	
	@Override
	protected DefaultTableModel createModel() {
		DefaultTableModel model = new DefaultTableModel(ID);
		model.setName("JIEMAMY");
		model.setLogicalName("地豆テスト");
		model.setDescription("");
//		model.addConstraint(new DefaultCheckConstraintModel(null, null, null, "A>B", null));
		return model;
	}
	
	@Override
	protected SerializationWorker<DefaultTableModel> createSerializationWorker(JiemamyContext context,
			SerializationDirector director) {
		return new DefaultTableModelSerializationWorker(context, director);
	}
}
