/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/02/16
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
package org.jiemamy.utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.dataset.JmRecord;
import org.jiemamy.model.dataset.SimpleJmDataSet;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.serializer.JiemamySerializer;

/**
 * {@link DataSetUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DataSetUtilTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_core195() throws Exception {
		JiemamySerializer serializer = JiemamyContext.findSerializer();
		InputStream inJiemamy = null;
		InputStream inCsv = null;
		try {
			inJiemamy = DataSetUtilTest.class.getResourceAsStream("/org/jiemamy/utils/core195.jiemamy");
			inCsv = DataSetUtilTest.class.getResourceAsStream("/org/jiemamy/utils/core195.csv");
			JiemamyContext context = serializer.deserialize(inJiemamy);
			SimpleJmDataSet dataSet = (SimpleJmDataSet) context.getDataSets().get(0);
			JmTable table = context.getTable("TABLE_1");
			DataSetUtil.importFromCsv(dataSet, table, inCsv);
			context.store(dataSet);
			
			JmRecord record = dataSet.getRecord(table.toReference()).get(0);
			assertThat(record.getValues().get(table.getColumn("a").toReference()).getScript(), is("a"));
			assertThat(record.getValues().containsKey(table.getColumn("b").toReference()), is(false));
		} finally {
			IOUtils.closeQuietly(inJiemamy);
			IOUtils.closeQuietly(inCsv);
		}
	}
	
	/**
	 * Core-196に対応する。インポートするCSVの1行目がカラム名にあっていない時に
	 * 例外が出ないようにしたい
	 * 
	 * TODO for s.tonouchi
	 * 
	 * @throws Exception 想定外の例外
	 */
	@Test
	public void test_core196() throws Exception {
		JiemamySerializer serializer = JiemamyContext.findSerializer();
		InputStream inJiemamy = null;
		InputStream inCsv = null;
		JiemamyContext context = null;
		try {
			inJiemamy = DataSetUtilTest.class.getResourceAsStream("/org/jiemamy/utils/core196.jiemamy");
			inCsv = DataSetUtilTest.class.getResourceAsStream("/org/jiemamy/utils/core196.csv");
			context = serializer.deserialize(inJiemamy);
			SimpleJmDataSet dataSet = (SimpleJmDataSet) context.getDataSets().get(0);
			JmTable table = context.getTable("TABLE_1");
			DataSetUtil.importFromCsv(dataSet, table, inCsv);
			fail("このメソッドは無事に通過してはいけない");
			
		} catch (IOException ex) {
			
		} finally {
			IOUtils.closeQuietly(inJiemamy);
			IOUtils.closeQuietly(inCsv);
		}
	}
	
}
