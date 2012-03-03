/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/22
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.io.IOExceptionWithCause;
import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.dataset.JmDataSet;
import org.jiemamy.model.dataset.JmRecord;
import org.jiemamy.model.dataset.SimpleJmDataSet;
import org.jiemamy.model.dataset.SimpleJmRecord;
import org.jiemamy.model.table.ColumnNotFoundException;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.script.ScriptString;

/**
 * {@link JmDataSet}のユーティリティクラス。
 * 
 * @author daisuke
 */
public final class DataSetUtil {
	
	/**
	 * 1テーブル分のレコードデータをすべてCSV化し、ストリームにエクスポートする。
	 * 
	 * @param dataSet データセット
	 * @param table 対象テーブル
	 * @param out 出力ストリーム
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void exportToCsv(JmDataSet dataSet, JmTable table, OutputStream out) throws IOException {
		Validate.notNull(dataSet);
		Validate.notNull(table);
		Validate.notNull(out);
		
		List<JmRecord> records = dataSet.getRecords().get(table.toReference());
		
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new OutputStreamWriter(out));
			
			List<JmColumn> columns = table.getColumns();
			String[] line = new String[columns.size()];
			int i = 0;
			for (JmColumn column : columns) {
				line[i++] = column.getName();
			}
			writer.writeNext(line);
			
			for (JmRecord record : records) {
				line = new String[columns.size()];
				for (JmColumn column : columns) {
					int index = columns.indexOf(column);
					line[index] = record.getValues().get(column.toReference()).getScript();
				}
				writer.writeNext(line);
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	/**
	 * ストリームからCSVを読み出し、1テーブル分のレコードデータとしてすべてインポートする。
	 * 
	 * <p>既にセットされているレコードはすべてクリアされる。</p>
	 * 
	 * @param dataSet データセット
	 * @param table 対象テーブル
	 * @param in 入力ストリーム
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws ColumnNotFoundException CSVの1行目で指定したカラム名が見つからない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void importFromCsv(SimpleJmDataSet dataSet, JmTable table, InputStream in) throws IOException {
		Validate.notNull(dataSet);
		Validate.notNull(table);
		Validate.notNull(in);
		
		List<JmRecord> records = Lists.newArrayList();
		
		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(in));
			String[] columnNames = reader.readNext();
			
			List<JmColumn> columns = Lists.newArrayList();
			for (String columnName : columnNames) {
				JmColumn column = table.getColumn(columnName);
				columns.add(column);
			}
			
			String[] dataElements;
			while ((dataElements = reader.readNext()) != null) {
				Map<EntityRef<? extends JmColumn>, ScriptString> values = Maps.newHashMap();
				for (int i = 0; i < Math.min(columns.size(), dataElements.length); i++) {
					values.put(columns.get(i).toReference(), new ScriptString(dataElements[i]));
				}
				records.add(new SimpleJmRecord(values));
			}
		} catch (ColumnNotFoundException ex) {
			throw new IOExceptionWithCause("CSVファイル不正", ex);
		} catch (IndexOutOfBoundsException ex) {
			throw new IOExceptionWithCause("CSVファイル不正", ex);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
		dataSet.putRecord(table.toReference(), records);
	}
	
	/**
	 * {@link JmDataSet}にレコードを設定する。
	 * 
	 * @param dataSet 対象{@link JmDataSet}
	 * @param table 設定対象テーブル
	 * @param records レコード
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void setRecord(JmDataSet dataSet, JmTable table, List<JmRecord> records) {
		Validate.notNull(dataSet);
		Validate.notNull(table);
		Validate.notNull(records);
		dataSet.getRecords().put(table.toReference(), records);
	}
	
	private DataSetUtil() {
	}
}
