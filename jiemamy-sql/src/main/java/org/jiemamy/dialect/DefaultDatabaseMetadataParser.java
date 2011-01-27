/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import java.sql.SQLException;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.utils.sql.metadata.TypeSafeDatabaseMetaData;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultDatabaseMetadataParser implements DatabaseMetadataParser {
	
	/** DBからエンティティ情報をインポートするビジター */
	private final DatabaseObjectImportVisitor doImportVisitor;
	
	/** DBから外部キー情報をインポートするビジター */
	private final ForeignKeyImportVisitor fkImportVisitor;
	

	/**
	 * インスタンスを生成する。
	 */
	public DefaultDatabaseMetadataParser() {
		this(new DefaultDatabaseObjectImportVisitor(), new DefaultForeignKeyImportVisitor());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param doImportVisitor
	 * @param fkImportVisitor
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultDatabaseMetadataParser(DatabaseObjectImportVisitor doImportVisitor,
			ForeignKeyImportVisitor fkImportVisitor) {
		Validate.notNull(doImportVisitor);
		Validate.notNull(fkImportVisitor);
		this.doImportVisitor = doImportVisitor;
		this.fkImportVisitor = fkImportVisitor;
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
		
		doImportVisitor.intialize(metaData, context, config);
		reader.readEnities(doImportVisitor);
		
		fkImportVisitor.initialize(context);
		reader.readRelations(fkImportVisitor);
		
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
