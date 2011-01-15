/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy.model.dataset;

import static org.hamcrest.Matchers.is;
import static org.jiemamy.utils.RandomUtil.bool;
import static org.jiemamy.utils.RandomUtil.meta;
import static org.jiemamy.utils.RandomUtil.strNullable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import org.junit.Test;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.script.ScriptString;

/**
 * {@link DefaultRecordModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultRecordModelTest {
	
	/**
	 * 適当な {@link DefaultRecordModel} のインスタンスを作る。
	 * 
	 * @param columns 対象カラムのリスト
	 * @return {@link DefaultRecordModel}
	 */
	public static DefaultRecordModel random(List<ColumnModel> columns) {
		Map<EntityRef<? extends ColumnModel>, ScriptString> values = Maps.newHashMap();
		
		for (ColumnModel columnModel : columns) {
			if (bool()) {
				
				values.put(columnModel.toReference(), new ScriptString(strNullable(), meta()));
			}
		}
		
		return new DefaultRecordModel(values);
	}
	
	/**
	 * mapがしっかりcloneされてるかチェック。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void test01_mapがしっかりcloneされてるかチェック() throws Exception {
		Map<EntityRef<? extends ColumnModel>, ScriptString> map = Maps.newHashMap();
		map.put(mock(EntityRef.class), new ScriptString("a"));
		map.put(mock(EntityRef.class), new ScriptString("b"));
		map.put(mock(EntityRef.class), new ScriptString("c"));
		DefaultRecordModel model1 = new DefaultRecordModel(map);
		
		assertThat(model1.getValues().size(), is(3));
		
		map.put(mock(EntityRef.class), new ScriptString("d")); // model1に影響しないはず
		model1.getValues().put(mock(EntityRef.class), new ScriptString("e")); // これもclone返ししているので影響しないはず
		
		assertThat(model1.getValues().size(), is(3));
		assertThat(model1.getValues().containsValue(new ScriptString("a")), is(true));
		assertThat(model1.getValues().containsValue(new ScriptString("b")), is(true));
		assertThat(model1.getValues().containsValue(new ScriptString("c")), is(true));
		assertThat(model1.getValues().containsValue(new ScriptString("d")), is(false));
	}
	
	/**
	 * {@link DefaultRecordModel#equals(Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void test02_equals() throws Exception {
		Map<EntityRef<? extends ColumnModel>, ScriptString> map = Maps.newHashMap();
		map.put(mock(EntityRef.class), new ScriptString("a"));
		map.put(mock(EntityRef.class), new ScriptString("b"));
		map.put(mock(EntityRef.class), new ScriptString("c"));
		DefaultRecordModel model1 = new DefaultRecordModel(map);
		DefaultRecordModel model2 = new DefaultRecordModel(map);
		
		map.put(mock(EntityRef.class), new ScriptString("d"));
		
		DefaultRecordModel model3 = new DefaultRecordModel(map);
		
		assertThat(model1.equals(model1), is(true));
		assertThat(model1.equals(model2), is(true));
		assertThat(model1.equals(model3), is(false));
		assertThat(model2.equals(model1), is(true));
		assertThat(model2.equals(model2), is(true));
		assertThat(model2.equals(model3), is(false));
		assertThat(model3.equals(model1), is(false));
		assertThat(model3.equals(model2), is(false));
		assertThat(model3.equals(model3), is(true));
		
		assertThat(model1.equals(new Object()), is(false));
		assertThat(model1.equals(null), is(false));
	}
}
