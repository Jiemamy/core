/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/27
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
package org.jiemamy.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.dataset.SimpleJmDataSet;
import org.jiemamy.model.dataset.SimpleJmRecord;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.script.ScriptString;
import org.jiemamy.utils.DbObjectDependencyCalculator;
import org.jiemamy.utils.sql.SqlExecutor;
import org.jiemamy.utils.sql.SqlExecutorHandler;
import org.jiemamy.utils.sql.metadata.TypeSafeDatabaseMetaData;

/**
 * {@link DatabaseMetadataParser}のデフォルト実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultDatabaseMetadataParser implements DatabaseMetadataParser {
	
	/** DBから{@link DbObject}情報をインポートするビジター */
	private final DbObjectImportVisitor dbObjectImportVisitor;
	
	/** DBから外部キー情報をインポートするビジター */
	private final ForeignKeyImportVisitor foreignKeyImportVisitor;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param dialect {@link Dialect}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultDatabaseMetadataParser(Dialect dialect) {
		this(new DefaultDbObjectImportVisitor(dialect), new DefaultForeignKeyImportVisitor(dialect));
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param dbObjectImportVisitor {@link DbObjectImportVisitor}
	 * @param foreignKeyImportVisitor {@link ForeignKeyImportVisitor}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected DefaultDatabaseMetadataParser(DbObjectImportVisitor dbObjectImportVisitor,
			ForeignKeyImportVisitor foreignKeyImportVisitor) {
		Validate.notNull(dbObjectImportVisitor);
		Validate.notNull(foreignKeyImportVisitor);
		this.dbObjectImportVisitor = dbObjectImportVisitor;
		this.foreignKeyImportVisitor = foreignKeyImportVisitor;
	}
	
	public void parseMetadata(JiemamyContext context, DatabaseMetaData meta, ParseMetadataConfig config)
			throws SQLException {
		Validate.notNull(context);
		Validate.notNull(meta);
		Validate.notNull(config);
		
		Connection connection = meta.getConnection();
		TypeSafeDatabaseMetaData metaData = new TypeSafeDatabaseMetaData(meta);
		DatabaseReader reader = new DatabaseReader(this, config, meta);
		
		setUpRead(connection);
		
		dbObjectImportVisitor.intialize(metaData, context, config);
		reader.readEnities(dbObjectImportVisitor);
		
		foreignKeyImportVisitor.initialize(context);
		reader.readRelations(foreignKeyImportVisitor);
		
		if (config.isImportDataSet()) {
			List<DbObject> dbObjects = DbObjectDependencyCalculator.getSortedEntityList(context);
			List<JmTable> tables = Lists.newArrayList(Iterables.filter(dbObjects, JmTable.class));
			
			final SimpleJmDataSet dataSet = new SimpleJmDataSet();
			dataSet.setName("imported " + new Date().toString());
			SqlExecutor ex = new SqlExecutor(connection);
			for (final JmTable table : tables) {
				ex.execute("SELECT * FROM " + table.getName() + ";", new SqlExecutorHandler() {
					
					public void handleResultSet(String sql, ResultSet rs) throws SQLException {
						while (rs.next()) {
							Map<EntityRef<? extends JmColumn>, ScriptString> map = Maps.newHashMap();
							for (JmColumn col : table.getColumns()) {
								String data = rs.getString(col.getName());
								map.put(col.toReference(), new ScriptString(data));
							}
							dataSet.addRecord(table.toReference(), new SimpleJmRecord(map));
						}
					}
					
					public void handleUpdateCount(String sql, int count) {
					}
				});
			}
			context.store(dataSet);
		}
		
		tearDownRead(connection);
	}
	
	/**
	 * DBメタデータの読み出し前に実行するロジックを実行する。
	 * 
	 * <p>必要に応じて適宜オーバーライドすること。</p>
	 * 
	 * @param connection コネクション
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected void setUpRead(Connection connection) {
	}
	
	/**
	 * DBメタデータの読み出し後に実行するロジックを実行する。
	 * 
	 * <p>必要に応じて適宜オーバーライドすること。</p>
	 * 
	 * @param connection コネクション
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected void tearDownRead(Connection connection) {
	}
	
}
