/*
   * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/04/04
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.composer.ImportException;
import org.jiemamy.composer.importer.DatabaseImportConfig;
import org.jiemamy.composer.importer.DatabaseImporter;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.index.IndexModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.model.view.ViewModel;
import org.jiemamy.utils.collection.ListUtil;
import org.jiemamy.utils.sql.DriverNotFoundException;
import org.jiemamy.utils.sql.DriverUtil;
import org.jiemamy.utils.sql.SqlExecutor;

/**
 * データベースのエンティティなどを全て削除するクリーナークラス。
 * 
 * @author daisuke
 */
public class DatabaseCleaner {
	
	private static Logger logger = LoggerFactory.getLogger(DatabaseCleaner.class);
	

	/**
	 * データベースのエンティティなどを全て削除する。
	 * 
	 * @param config DB接続情報
	 * @throws ImportException DB情報の読み込みに失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void clean(DatabaseImportConfig config) throws ImportException {
		logger.info("clean: " + config.getUri());
		JiemamyContext context = new JiemamyContext();
		
		DatabaseImporter databaseImporter = new DatabaseImporter();
		databaseImporter.importModel(context, config);
		
		List<DatabaseObjectModel> sortedDomList = EntityDependencyCalculator.getSortedEntityList(context);
		ListUtil.reverse(sortedDomList);
		
		Connection connection = null;
		try {
			Properties props = new Properties();
			props.setProperty("user", config.getUsername());
			props.setProperty("password", config.getPassword());
			
			URL[] paths = config.getDriverJarPaths();
			String className = config.getDriverClassName();
			
			connection = connect(config, props, paths, className);
			SqlExecutor sqlExecuter = getExecutor(connection);
			
			ContextMetadata metadata = context.getMetadata();
			if (metadata != null && StringUtils.isEmpty(metadata.getSchemaName()) == false) {
				sqlExecuter.execute(String.format("DROP SCHEMA \"%s\";", metadata.getSchemaName()));
			}
			
			for (DatabaseObjectModel dom : sortedDomList) {
				String type = null;
				if (dom instanceof TableModel) {
					type = "TABLE";
				} else if (dom instanceof ViewModel) {
					type = "VIEW";
				} else if (dom instanceof IndexModel) {
					type = "INDEX";
				}
				if (type != null) {
					sqlExecuter.execute(String.format("DROP %s %s;", type, dom.getName()));
				} else {
					logger.warn("unsupported database object: " + dom.getClass().getName());
				}
			}
		} catch (DriverNotFoundException e) {
			throw new ImportException(e);
		} catch (InstantiationException e) {
			throw new ImportException(e);
		} catch (IllegalAccessException e) {
			throw new ImportException(e);
		} catch (IOException e) {
			throw new ImportException(e);
		} catch (SQLException e) {
			throw new ImportException(e);
		} catch (Exception e) {
			throw new ImportException(e);
		} finally {
			DbUtils.closeQuietly(connection);
		}
	}
	
	Connection connect(DatabaseImportConfig config, Properties props, URL[] paths, String className)
			throws InstantiationException, IllegalAccessException, DriverNotFoundException, IOException, SQLException {
		Driver driver = DriverUtil.getDriverInstance(paths, className);
		return driver.connect(config.getUri(), props);
	}
	
	SqlExecutor getExecutor(Connection connection) {
		return new SqlExecutor(connection);
	}
}
