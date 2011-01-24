/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.dataset.DataSetModel;
import org.jiemamy.model.dataset.DefaultRecordModel;
import org.jiemamy.model.dataset.RecordModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.script.ScriptString;

/**
 * {@link DataSetModel}のユーティリティクラス。
 * 
 * @author daisuke
 */
public final class DataSetUtil {
	
	/**
	 * 1テーブル分のレコードデータをすべてCSV化し、ストリームにエクスポートする。
	 * 
	 * @param dataSetModel データセット
	 * @param tableModel 対象テーブル
	 * @param out 出力ストリーム
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void exportToCsv(DataSetModel dataSetModel, TableModel tableModel, OutputStream out)
			throws IOException {
		Validate.notNull(dataSetModel);
		Validate.notNull(tableModel);
		Validate.notNull(out);
		
		List<RecordModel> records = dataSetModel.getRecords().get(tableModel.toReference());
		
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new OutputStreamWriter(out));
			
			List<ColumnModel> columns = tableModel.getColumns();
			String[] line = new String[columns.size()];
			int i = 0;
			for (ColumnModel columnModel : columns) {
				line[i++] = columnModel.getName();
			}
			writer.writeNext(line);
			
			for (RecordModel recordModel : records) {
				line = new String[columns.size()];
				for (ColumnModel columnModel : columns) {
					int index = columns.indexOf(columnModel);
					line[index] = recordModel.getValues().get(columnModel.toReference()).getScript();
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
	 * @param dataSetModel データセット
	 * @param tableModel 対象テーブル
	 * @param in 入力ストリーム
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void importFromCsv(DataSetModel dataSetModel, TableModel tableModel, InputStream in)
			throws IOException {
		Validate.notNull(dataSetModel);
		Validate.notNull(tableModel);
		Validate.notNull(in);
		
		List<RecordModel> records = dataSetModel.getRecords().get(tableModel.toReference());
		records.clear();
		
		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(in));
			String[] line = reader.readNext();
			
			List<ColumnModel> columnModels = Lists.newArrayList();
			for (String columnName : line) {
				ColumnModel columnModel = tableModel.getColumn(columnName);
				columnModels.add(columnModel);
			}
			
			while ((line = reader.readNext()) != null) {
				Map<EntityRef<? extends ColumnModel>, ScriptString> values = Maps.newHashMap();
				for (int i = 0; i < line.length; i++) {
					values.put(columnModels.get(i).toReference(), new ScriptString(line[i]));
				}
				records.add(new DefaultRecordModel(values));
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	/**
	 * {@link DataSetModel}にレコードを設定する。
	 * 
	 * @param dataSetModel 対象{@link DataSetModel}
	 * @param tableModel 設定対象テーブル
	 * @param records レコード
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void setRecord(DataSetModel dataSetModel, TableModel tableModel, List<RecordModel> records) {
		Validate.notNull(dataSetModel);
		Validate.notNull(tableModel);
		Validate.notNull(records);
		dataSetModel.getRecords().put(tableModel.toReference(), records);
	}
	
	private DataSetUtil() {
	}
}
