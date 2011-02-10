/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/07/26
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
package org.jiemamy.composer.importer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.JiemamyContext;
import org.jiemamy.composer.AbstractImporter;
import org.jiemamy.composer.ImportException;
import org.jiemamy.dialect.DatabaseMetadataParser;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.utils.sql.DriverNotFoundException;
import org.jiemamy.utils.sql.DriverUtil;

/**
 * データベースからモデルを生成するインポータ。
 * 
 * @author daisuke
 */
public class DbImporter extends AbstractImporter<DbImportConfig> {
	
	/** ConfigKey: SQL方言 (Dialect) */
	public static final String DIALECT = "dialect";
	
	/** ConfigKey: データベース名 (String) */
	public static final String DATABASE_NAME = "databaseName";
	
	/** ConfigKey: ドライバJARファイルのURL (URL[]) */
	public static final String DRIVER_JAR_PATHS = "driverJarPaths";
	
	/** ConfigKey: ドライバの完全修飾クラス名 (String) */
	public static final String DRIVER_CLASS_NAME = "driverClassName";
	
	/** ConfigKey: DB接続URI (String) */
	public static final String URI = "uri";
	
	/** ConfigKey: DB接続スキーマ名 (String) */
	public static final String SCHEMA = "schema";
	
	/** ConfigKey: DB接続username (String) */
	public static final String USERNAME = "username";
	
	/** ConfigKey: DB接続password (String) */
	public static final String PASSWORD = "password";
	
	/** ConfigKey: importするエンティティ名 (String[]) */
	public static final String SELECTED_ENTITIES = "selectedEntities";
	
	/** ConfigKey: importするエンティティの種類 (String[]) */
	public static final String ENTITY_TYPES = "entityTypes";
	

	public String getName() {
		return "Database Importer (core)";
	}
	
	public boolean importModel(JiemamyContext context, DbImportConfig config) throws ImportException {
		Validate.notNull(context);
		Validate.notNull(config);
		Validate.notNull(config.getUri());
		
		Properties props = new Properties();
		props.setProperty("user", config.getUsername());
		props.setProperty("password", config.getPassword());
		
		Connection connection = null;
		try {
			URL[] paths = config.getDriverJarPaths();
			String className = config.getDriverClassName();
			Driver driver = DriverUtil.getDriverInstance(paths, className);
			connection = driver.connect(config.getUri(), props);
			DatabaseMetaData meta = connection.getMetaData();
			
			Dialect dialect = config.getDialect();
			DatabaseMetadataParser parser = dialect.getDatabaseMetadataParser();
			parser.parseMetadata(context, meta, config);
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
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	protected DbImportConfig newSimpleConfigInstance() {
		return new SimpleDbImportConfig();
	}
	
}
